package com.hyeongsh.feb.service;

import com.hyeongsh.feb.domain.Extension;
import com.hyeongsh.feb.dto.*;
import com.hyeongsh.feb.enums.ExtensionType;
import com.hyeongsh.feb.exception.*;
import com.hyeongsh.feb.repository.FebRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FebService {

    private final FebRepository febRepository;

    public List<FixedExtensionResponse> getFixedExtensions() {
        List<Extension> extensionsByExtensionType = febRepository.getExtensionsByExtensionType(ExtensionType.FIXED);
        return extensionsByExtensionType.stream()
                .map(extension -> new FixedExtensionResponse(extension.getName(), extension.isBlocked()))
                .toList();
    }

    @Transactional
    public FixedExtensionResponse updateFixedExtension(FixedExtensionUpdateRequest request) {
        String extensionLowerCase = extensionToLowerCase(request.getExtension());
        Extension extension = febRepository.getExtensionByName(extensionLowerCase)
                .orElseThrow(() -> new InvalidExtensionException(String.format(ErrorMessages.INVALID_EXTENSION, request.getExtension())));
        if (ExtensionType.FIXED != extension.getExtensionType()) {
            throw new NotFixedException(String.format(ErrorMessages.NOT_FIXED, request.getExtension()));
        }
        extension.block(request.isBlock());
        return new FixedExtensionResponse(extension.getName(), extension.isBlocked());
    }

    public List<CustomExtensionResponse> getCustomExtensions() {
        List<Extension> extensionsByExtensionType = febRepository.getExtensionsByExtensionType(ExtensionType.CUSTOM);
        return extensionsByExtensionType.stream().map(extension -> new CustomExtensionResponse(extension.getName())).toList();
    }

    public CustomExtensionResponse addCustomExtension(CustomExtensionCreateRequest request) {
        String extensionLowerCase = extensionToLowerCase(request.getExtension());
        Optional<Extension> extensionByName = febRepository.getExtensionByName(extensionLowerCase);
        if (extensionByName.isPresent()) {
            if (ExtensionType.FIXED == extensionByName.get().getExtensionType()) {
                throw new ExtensionAlreadyInFixedException(String.format(ErrorMessages.EXTENSION_ALREADY_IN_FIXED, request.getExtension()));
            } else {
                throw new AlreadyBlockedException(String.format(ErrorMessages.ALREADY_BLOCKED, request.getExtension()));
            }
        }
        if (febRepository.getExtensionsByExtensionType(ExtensionType.CUSTOM).size() == 200) {
            throw new LimitExceedException(ErrorMessages.LIMIT_EXCEED);
        }
        Extension extension = new Extension(extensionLowerCase, ExtensionType.CUSTOM, true);
        Extension save = febRepository.save(extension);
        return new CustomExtensionResponse(save.getName());
    }

    public void removeCustomExtension(String extensionName) {
        String extensionLowerCase = extensionToLowerCase(extensionName);
        Extension extension = febRepository.getExtensionByName(extensionLowerCase)
                .orElseThrow(() -> new InvalidExtensionException(String.format(ErrorMessages.INVALID_EXTENSION, extensionName)));
        if (ExtensionType.FIXED == extension.getExtensionType()) {
            throw new InvalidExtensionException(String.format(ErrorMessages.INVALID_EXTENSION, extensionName));
        }
        febRepository.delete(extension);
    }

    private String extensionToLowerCase(String name) {
        if (name == null) {
            throw new InvalidExtensionException(String.format(ErrorMessages.INVALID_EXTENSION, "null"));
        } else if (name.isBlank() || name.length() > 20) {
            throw new InvalidExtensionException(String.format(ErrorMessages.INVALID_EXTENSION, name));
        }
        for (char c : name.toCharArray()) {
            if (!Character.isDigit(c) && !Character.isAlphabetic(c)) {
                throw new InvalidExtensionException(String.format(ErrorMessages.INVALID_EXTENSION, name));
            }
        }
        return name.toLowerCase();
    }
}

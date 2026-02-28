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
        if (request.getExtension() == null) {
            throw new InvalidExtensionException(String.format(ErrorMessages.INVALID_EXTENSION, "null"));
        }
        String extensionLowerCase = request.getExtension().toLowerCase();
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
        if (request.getExtension() == null) {
            throw new InvalidExtensionException(String.format(ErrorMessages.INVALID_EXTENSION, "null"));
        }
        String extensionLowerCase = request.getExtension().toLowerCase();
        if (extensionLowerCase.isBlank() || extensionLowerCase.length() > 20) {
            throw new InvalidExtensionException(String.format(ErrorMessages.INVALID_EXTENSION, request.getExtension()));
        }
        Optional<Extension> extensionByName = febRepository.getExtensionByName(extensionLowerCase);
        if (extensionByName.isPresent()) {
            if (ExtensionType.FIXED == extensionByName.get().getExtensionType()) {
                throw new ExtensionAlreadyInFixedException(String.format(ErrorMessages.EXTENSION_ALREADY_IN_FIXED, request.getExtension()));
            } else {
                throw new AlreadyBlockedException(String.format(ErrorMessages.ALREADY_BLOCKED, request.getExtension()));
            }
        }
        Extension extension = new Extension(extensionLowerCase, ExtensionType.CUSTOM, true);
        Extension save = febRepository.save(extension);
        return new CustomExtensionResponse(save.getName());
    }

    public void removeCustomExtension(String extensionName) {
        if (extensionName == null) {
            throw new InvalidExtensionException(String.format(ErrorMessages.INVALID_EXTENSION, "null"));
        }
        String extensionLowerCase = extensionName.toLowerCase();
        Extension extension = febRepository.getExtensionByName(extensionLowerCase)
                .orElseThrow(() -> new InvalidExtensionException(String.format(ErrorMessages.INVALID_EXTENSION, extensionName)));
        if (ExtensionType.FIXED == extension.getExtensionType()) {
            throw new InvalidExtensionException(String.format(ErrorMessages.INVALID_EXTENSION, extensionName));
        }
        febRepository.delete(extension);
    }

}

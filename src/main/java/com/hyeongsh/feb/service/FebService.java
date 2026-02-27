package com.hyeongsh.feb.service;

import com.hyeongsh.feb.dto.*;
import com.hyeongsh.feb.exception.*;
import com.hyeongsh.feb.repository.FebRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FebService {

    private final Map<String, Boolean> fixedExtensions;
    private final Set<String> customExtensions;

    public FebService() {
        this.fixedExtensions = new HashMap<>();
        fixedExtensions.put("bat", false);
        fixedExtensions.put("cmd", false);
        fixedExtensions.put("com", false);
        fixedExtensions.put("cpl", false);
        fixedExtensions.put("exe", false);
        fixedExtensions.put("scr", false);
        fixedExtensions.put("js", false);
        this.customExtensions = new HashSet<>();
    }

    public List<FixedExtensionResponse> getFixedExtensions() {
        return fixedExtensions
                .entrySet()
                .stream()
                .map(o -> new FixedExtensionResponse(o.getKey(), o.getValue()))
                .toList();
    }

    public FixedExtensionResponse updateFixedExtension(FixedExtensionUpdateRequest request) {
        if (!fixedExtensions.containsKey(request.getExtension())) {
            throw new ExtensionNotFoundException(String.format(ErrorMessages.EXTENSION_NOT_FOUND, request.getExtension()));
        }
        fixedExtensions.put(request.getExtension(), request.isBlock());
        return FixedExtensionResponse.builder()
                .extension(request.getExtension())
                .blocked(request.isBlock())
                .build();
    }

    public List<CustomExtensionResponse> getCustomExtensions() {
        return customExtensions.stream().map(CustomExtensionResponse::new).toList();
    }

    public CustomExtensionResponse addCustomExtension(CustomExtensionCreateRequest request) {
        String extension = request.getExtension();
        if (extension == null || extension.isBlank() || extension.length() > 20) {
            throw new InvalidExtensionException(String.format(ErrorMessages.INVALID_EXTENSION, extension));
        }
        String extensionLowerCase = extension.toLowerCase();
        if (fixedExtensions.containsKey(extensionLowerCase)) {
            throw new ExtensionAlreadyInFixedException(String.format(ErrorMessages.EXTENSION_ALREADY_IN_FIXED, extension));
        } else if (customExtensions.contains(extensionLowerCase)) {
            throw new AlreadyBlockedException(String.format(ErrorMessages.ALREADY_BLOCKED, extension));
        }
        customExtensions.add(extensionLowerCase);
        return new CustomExtensionResponse(extensionLowerCase);
    }

    public void removeCustomExtension(CustomExtensionDeleteRequest request) {
        String extensionLowerCase = request.getExtension().toLowerCase();
        if (!customExtensions.contains(extensionLowerCase)) {
            throw new ExtensionNotFoundException(String.format(ErrorMessages.EXTENSION_NOT_FOUND, request.getExtension()));
        }
        customExtensions.remove(extensionLowerCase);
    }

}

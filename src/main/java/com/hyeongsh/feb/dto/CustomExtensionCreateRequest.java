package com.hyeongsh.feb.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CustomExtensionCreateRequest {
    private String extension;

    public CustomExtensionCreateRequest(String extension) {
        this.extension = extension;
    }
}

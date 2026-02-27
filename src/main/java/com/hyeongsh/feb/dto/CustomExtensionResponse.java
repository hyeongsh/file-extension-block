package com.hyeongsh.feb.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CustomExtensionResponse {
    private String extension;

    public CustomExtensionResponse(String extension) {
        this.extension = extension;
    }
}

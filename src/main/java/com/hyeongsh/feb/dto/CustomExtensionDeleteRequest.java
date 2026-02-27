package com.hyeongsh.feb.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CustomExtensionDeleteRequest {
    private String extension;

    public CustomExtensionDeleteRequest(String extension) {
        this.extension = extension;
    }
}

package com.hyeongsh.feb.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FixedExtensionResponse {
    private String extension;
    private Boolean blocked;

    @Builder
    public FixedExtensionResponse(String extension, Boolean blocked) {
        this.extension = extension;
        this.blocked = blocked;
    }
}

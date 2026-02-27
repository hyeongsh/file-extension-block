package com.hyeongsh.feb.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FixedExtensionUpdateRequest {
    private String extension;
    private boolean block;

    @Builder
    public FixedExtensionUpdateRequest(String extension, boolean block) {
        this.extension = extension;
        this.block = block;
    }
}

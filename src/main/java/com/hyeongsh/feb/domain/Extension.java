package com.hyeongsh.feb.domain;

import com.hyeongsh.feb.enums.ExtensionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "extensions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Extension {

    @Id
    private String extension;

    @Enumerated(EnumType.STRING)
    @Column(name = "extension_type", nullable = false)
    private ExtensionType extensionType;

    @Column(nullable = false)
    private boolean blocked;

    @Builder
    public Extension(String extension, ExtensionType extensionType, boolean blocked) {
        this.extension = extension;
        this.extensionType = extensionType;
        this.blocked = blocked;
    }

    public void block() {
        blocked = true;
    }

    public void unBlock() {
        blocked = false;
    }

}

package com.hyeongsh.feb.repository;

import com.hyeongsh.feb.domain.Extension;
import com.hyeongsh.feb.enums.ExtensionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FebRepository extends JpaRepository<Extension, String> {
    List<Extension> getExtensionsByExtensionType(ExtensionType extensionType);

    Optional<Extension> getExtensionByName(String name);
}

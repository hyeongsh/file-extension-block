package com.hyeongsh.feb.repository;

import com.hyeongsh.feb.domain.Extension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FebRepository extends JpaRepository<Extension, String> {
}

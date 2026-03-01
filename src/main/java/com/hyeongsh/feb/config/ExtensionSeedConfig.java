package com.hyeongsh.feb.config;

import com.hyeongsh.feb.domain.Extension;
import com.hyeongsh.feb.enums.ExtensionType;
import com.hyeongsh.feb.repository.FebRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
public class ExtensionSeedConfig {

    @Bean
    @Transactional
    public ApplicationRunner extensionSeeder(FebRepository febRepository) {
        return args -> {
            List<String> fixed = List.of("bat", "cmd", "com", "cpl", "exe", "scr", "js");

            for (String f : fixed) {
                if (!febRepository.existsById(f)) {
                    febRepository.save(new Extension(f, ExtensionType.FIXED, false));
                }
            }
        };
    }
}

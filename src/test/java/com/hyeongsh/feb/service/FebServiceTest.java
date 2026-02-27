package com.hyeongsh.feb.service;

import com.hyeongsh.feb.domain.Extension;
import com.hyeongsh.feb.dto.*;
import com.hyeongsh.feb.enums.ExtensionType;
import com.hyeongsh.feb.exception.AlreadyBlockedException;
import com.hyeongsh.feb.exception.ExtensionAlreadyInFixedException;
import com.hyeongsh.feb.exception.InvalidExtensionException;
import com.hyeongsh.feb.repository.FebRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FebServiceTest {

    @Autowired
    private FebService febService;

    @Autowired
    private FebRepository febRepository;

    @BeforeEach
    void beforeEach() {
        febRepository.deleteAll();
        febRepository.save(new Extension("bat", ExtensionType.FIXED, false));
        febRepository.save(new Extension("cmd", ExtensionType.FIXED, false));
        febRepository.save(new Extension("com", ExtensionType.FIXED, false));
        febRepository.save(new Extension("cpl", ExtensionType.FIXED, false));
        febRepository.save(new Extension("exe", ExtensionType.FIXED, false));
        febRepository.save(new Extension("scr", ExtensionType.FIXED, false));
        febRepository.save(new Extension("js", ExtensionType.FIXED, false));
    }

    @Test
    void updateFixedExtension() {
        FixedExtensionUpdateRequest fixedExtensionPatchRequest = FixedExtensionUpdateRequest.builder()
                .extension("com")
                .block(true)
                .build();
        FixedExtensionResponse fixedExtensionResponse = febService.updateFixedExtension(fixedExtensionPatchRequest);
        assertEquals(true, fixedExtensionResponse.getBlocked());
        fixedExtensionPatchRequest.setExtension("abc");
        // 고정 확장자가 아닌 확장자 변경할 경우 예외 발생
        assertThrows(InvalidExtensionException.class, () -> febService.updateFixedExtension(fixedExtensionPatchRequest));
    }

    @Test
    void addCustomExtension_failTest() {
        CustomExtensionCreateRequest customExtensionCreateRequest1 = new CustomExtensionCreateRequest("");
        CustomExtensionCreateRequest customExtensionCreateRequest2 = new CustomExtensionCreateRequest("com");
        CustomExtensionCreateRequest customExtensionCreateRequest3 = new CustomExtensionCreateRequest("ajksjdkdlsjsksldjafkdalfdajfdask");
        CustomExtensionCreateRequest customExtensionCreateRequest4 = new CustomExtensionCreateRequest("ab");
        CustomExtensionCreateRequest customExtensionCreateRequest5 = new CustomExtensionCreateRequest("AB");

        // 빈 문자열일 경우 예외 발생
        assertThrows(InvalidExtensionException.class, () -> febService.addCustomExtension(customExtensionCreateRequest1));
        // 고정 확장자의 경우 예외 발생
        assertThrows(ExtensionAlreadyInFixedException.class, () -> febService.addCustomExtension(customExtensionCreateRequest2));
        // 20자 넘을 경우 예외 발생
        assertThrows(InvalidExtensionException.class, () -> febService.addCustomExtension(customExtensionCreateRequest3));
        febService.addCustomExtension(customExtensionCreateRequest4);
        // 같은 값 추가할 경우 예외 발생 (대문자 포함)
        assertThrows(AlreadyBlockedException.class, () -> febService.addCustomExtension(customExtensionCreateRequest5));
    }

    @Test
    void removeCustomExtension() {
        CustomExtensionCreateRequest customExtensionCreateRequest1 = new CustomExtensionCreateRequest("ab");
        CustomExtensionCreateRequest customExtensionCreateRequest2 = new CustomExtensionCreateRequest("abc");
        CustomExtensionCreateRequest customExtensionCreateRequest3 = new CustomExtensionCreateRequest("abcd");

        CustomExtensionDeleteRequest customExtensionDeleteRequest1 = new CustomExtensionDeleteRequest("abcd");
        CustomExtensionDeleteRequest customExtensionDeleteRequest2 = new CustomExtensionDeleteRequest("abcde");
        febService.addCustomExtension(customExtensionCreateRequest1);
        febService.addCustomExtension(customExtensionCreateRequest2);
        febService.addCustomExtension(customExtensionCreateRequest3);

        febService.removeCustomExtension(customExtensionDeleteRequest1);
        assertEquals(2, febService.getCustomExtensions().size());
        // 존재하지 않는 확장자 삭제할 경우 예외 발생
        assertThrows(InvalidExtensionException.class, () -> febService.removeCustomExtension(customExtensionDeleteRequest2));
    }
}
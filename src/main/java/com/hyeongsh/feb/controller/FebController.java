package com.hyeongsh.feb.controller;

import com.hyeongsh.feb.dto.*;
import com.hyeongsh.feb.service.FebService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FebController {

    private final FebService febService;

    @GetMapping("/fixed")
    public ResponseEntity<List<FixedExtensionResponse>> getFixedExtensions() {
        return ResponseEntity.ok(febService.getFixedExtensions());
    }

    @PatchMapping("/fixed")
    public ResponseEntity<FixedExtensionResponse> setFixedExtension(@RequestBody FixedExtensionUpdateRequest request) {
        return ResponseEntity.ok(febService.updateFixedExtension(request));
    }

    @GetMapping("/custom")
    public ResponseEntity<List<CustomExtensionResponse>> getCustomExtensions() {
        return ResponseEntity.ok(febService.getCustomExtensions());
    }

    @PostMapping("/custom")
    public ResponseEntity<CustomExtensionResponse> addCustomExtension(@RequestBody CustomExtensionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(febService.addCustomExtension(request));
    }

    @DeleteMapping("/custom")
    public ResponseEntity<?> deleteCustomExtension(@RequestParam("extension") String extension) {
        febService.removeCustomExtension(extension);
        return ResponseEntity.noContent().build();
    }
}

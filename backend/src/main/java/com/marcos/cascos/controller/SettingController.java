package com.marcos.cascos.controller;

import com.marcos.cascos.dto.SettingCreateRequest;
import com.marcos.cascos.dto.SettingResponse;
import com.marcos.cascos.dto.SettingUpdateRequest;
import com.marcos.cascos.service.SettingService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
public class SettingController {
    private final SettingService service;

    public SettingController(SettingService service) {
        this.service = service;
    }

    @GetMapping
    public List<SettingResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{key}")
    public SettingResponse findByKey(@PathVariable String key) {
        return service.findByKey(key);
    }

    @PostMapping
    public ResponseEntity<SettingResponse> create(@Valid @RequestBody SettingCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{key}")
    public SettingResponse update(@PathVariable String key, @Valid @RequestBody SettingUpdateRequest request) {
        return service.update(key, request);
    }
}

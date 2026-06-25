package com.marcos.cascos.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.marcos.cascos.dto.SettingCreateRequest;
import com.marcos.cascos.dto.SettingUpdateRequest;
import com.marcos.cascos.entity.Setting;
import com.marcos.cascos.exception.ResourceNotFoundException;
import com.marcos.cascos.mapper.SettingMapper;
import com.marcos.cascos.repository.SettingRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettingServiceImplTest {
    @Mock
    private SettingRepository repository;

    private SettingServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SettingServiceImpl(repository, new SettingMapper());
    }

    @Test
    void createsSetting() {
        SettingCreateRequest request = new SettingCreateRequest();
        request.setSettingKey("currency");
        request.setSettingValue("EUR");

        when(repository.existsBySettingKey("currency")).thenReturn(false);
        when(repository.save(org.mockito.ArgumentMatchers.any(Setting.class))).thenAnswer(invocation -> {
            Setting saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        assertEquals("EUR", service.create(request).getSettingValue());
    }

    @Test
    void updatesSetting() {
        Setting setting = new Setting();
        setting.setSettingKey("currency");
        setting.setSettingValue("EUR");
        SettingUpdateRequest request = new SettingUpdateRequest();
        request.setSettingValue("USD");

        when(repository.findBySettingKey("currency")).thenReturn(Optional.of(setting));
        when(repository.save(setting)).thenReturn(setting);

        assertEquals("USD", service.update("currency", request).getSettingValue());
    }

    @Test
    void throwsWhenSettingMissing() {
        when(repository.findBySettingKey("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findByKey("missing"));
    }
}

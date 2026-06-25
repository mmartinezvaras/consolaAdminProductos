package com.marcos.cascos.mapper;

import com.marcos.cascos.dto.SettingCreateRequest;
import com.marcos.cascos.dto.SettingResponse;
import com.marcos.cascos.entity.Setting;
import org.springframework.stereotype.Component;

@Component
public class SettingMapper {
    public Setting toEntity(SettingCreateRequest request) {
        Setting setting = new Setting();
        setting.setSettingKey(request.getSettingKey());
        setting.setSettingValue(request.getSettingValue());
        return setting;
    }

    public SettingResponse toResponse(Setting setting) {
        SettingResponse response = new SettingResponse();
        response.setId(setting.getId());
        response.setSettingKey(setting.getSettingKey());
        response.setSettingValue(setting.getSettingValue());
        response.setCreatedAt(setting.getCreatedAt());
        response.setUpdatedAt(setting.getUpdatedAt());
        return response;
    }
}

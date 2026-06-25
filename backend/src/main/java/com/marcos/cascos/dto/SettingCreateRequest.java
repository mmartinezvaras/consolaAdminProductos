package com.marcos.cascos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SettingCreateRequest extends SettingUpdateRequest {
    @NotBlank
    @Size(max = 255)
    private String settingKey;

    public String getSettingKey() { return settingKey; }
    public void setSettingKey(String settingKey) { this.settingKey = settingKey; }
}

package com.marcos.cascos.dto;

import jakarta.validation.constraints.Size;

public class SettingUpdateRequest {
    @Size(max = 5000)
    private String settingValue;

    public String getSettingValue() { return settingValue; }
    public void setSettingValue(String settingValue) { this.settingValue = settingValue; }
}

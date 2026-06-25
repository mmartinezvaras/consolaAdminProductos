package com.marcos.cascos.service;

import com.marcos.cascos.dto.SettingCreateRequest;
import com.marcos.cascos.dto.SettingResponse;
import com.marcos.cascos.dto.SettingUpdateRequest;
import java.util.List;

public interface SettingService {
    List<SettingResponse> findAll();
    SettingResponse findByKey(String key);
    SettingResponse create(SettingCreateRequest request);
    SettingResponse update(String key, SettingUpdateRequest request);
}

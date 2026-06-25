package com.marcos.cascos.service.impl;

import com.marcos.cascos.dto.SettingCreateRequest;
import com.marcos.cascos.dto.SettingResponse;
import com.marcos.cascos.dto.SettingUpdateRequest;
import com.marcos.cascos.entity.Setting;
import com.marcos.cascos.exception.ConflictException;
import com.marcos.cascos.exception.ResourceNotFoundException;
import com.marcos.cascos.mapper.SettingMapper;
import com.marcos.cascos.repository.SettingRepository;
import com.marcos.cascos.service.SettingService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SettingServiceImpl implements SettingService {
    private final SettingRepository repository;
    private final SettingMapper mapper;

    public SettingServiceImpl(SettingRepository repository, SettingMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SettingResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SettingResponse findByKey(String key) {
        return mapper.toResponse(getByKey(key));
    }

    @Override
    public SettingResponse create(SettingCreateRequest request) {
        if (repository.existsBySettingKey(request.getSettingKey())) {
            throw new ConflictException("Setting key already exists");
        }
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public SettingResponse update(String key, SettingUpdateRequest request) {
        Setting setting = getByKey(key);
        setting.setSettingValue(request.getSettingValue());
        return mapper.toResponse(repository.save(setting));
    }

    private Setting getByKey(String key) {
        return repository.findBySettingKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Setting not found: " + key));
    }
}

package com.marcos.cascos.repository;

import com.marcos.cascos.entity.Setting;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting> findBySettingKey(String settingKey);
    boolean existsBySettingKey(String settingKey);
}

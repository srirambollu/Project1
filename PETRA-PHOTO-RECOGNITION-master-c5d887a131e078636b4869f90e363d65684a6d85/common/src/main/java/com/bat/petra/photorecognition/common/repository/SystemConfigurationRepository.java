package com.bat.petra.photorecognition.common.repository;

import com.bat.petra.photorecognition.common.model.SystemConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConfigurationRepository extends JpaRepository<SystemConfiguration, Long> {

    @Query("SELECT c.value FROM SystemConfiguration c WHERE c.key = ?1")
    String getValueByKey(String key);

    SystemConfiguration findByKey(String key);
}

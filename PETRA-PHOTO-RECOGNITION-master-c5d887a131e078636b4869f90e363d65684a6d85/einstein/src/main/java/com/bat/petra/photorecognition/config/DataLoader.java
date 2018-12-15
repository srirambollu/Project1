package com.bat.petra.photorecognition.config;

import com.bat.petra.photorecognition.common.configuration.ApplicationProperty;
import com.bat.petra.photorecognition.common.model.SystemConfiguration;
import com.bat.petra.photorecognition.common.repository.SalesforceConfigurationRepository;
import com.bat.petra.photorecognition.common.repository.SystemConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Autowired
    private SystemConfigurationRepository systemConfigurationRepository;

    @Autowired
    private SalesforceConfigurationRepository salesforceConfigurationRepository;

    public void run() {

        System.out.println("DataLoader run");

        systemConfigurationRepository.deleteAll();
        systemConfigurationRepository.save(new SystemConfiguration(ApplicationProperty.EINSTEIN_VISION_ACCESS_TOKEN, null));
        systemConfigurationRepository.save(new SystemConfiguration(ApplicationProperty.EINSTEIN_VISION_REFRESH_TOKEN, null));
        systemConfigurationRepository.save(new SystemConfiguration(ApplicationProperty.EINSTEIN_VISION_ACCESS_TOKEN_EXPIRES_IN, null));
    }
}
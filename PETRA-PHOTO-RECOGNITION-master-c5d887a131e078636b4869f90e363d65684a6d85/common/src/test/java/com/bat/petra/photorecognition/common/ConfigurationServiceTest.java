package com.bat.petra.photorecognition.common;

import com.bat.petra.photorecognition.common.configuration.ApplicationProperty;
import com.bat.petra.photorecognition.common.model.SalesforceConfiguration;
import com.bat.petra.photorecognition.common.repository.SalesforceConfigurationRepository;
import com.bat.petra.photorecognition.common.repository.SystemConfigurationRepository;
import com.bat.petra.photorecognition.common.service.ConfigurationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationServiceTest {

    @InjectMocks
    private ConfigurationService configurationService;
    @Mock
    private SystemConfigurationRepository systemConfigurationRepository;
    @Mock
    private SalesforceConfigurationRepository salesforceConfigurationRepository;

    @Test
    public void getSystemValue_einsteinToken() {
        when(systemConfigurationRepository.getValueByKey(anyString()))
                .thenReturn(ApplicationProperty.EINSTEIN_VISION_ACCESS_TOKEN);

        assertEquals(ApplicationProperty.EINSTEIN_VISION_ACCESS_TOKEN, configurationService.getEinsteinUrl());
    }

    @Test
    public void getSalesforceValue_einsteinUrlFound() {
        when(salesforceConfigurationRepository.findByCodeAndIsActiveTrue(anyString()))
                .thenReturn(new SalesforceConfiguration(){
                    {
                        setValue(ApplicationProperty.EINSTEIN_VISION_URL);
                    }
                });
        assertEquals(ApplicationProperty.EINSTEIN_VISION_URL, configurationService.getEinsteinUrl());
    }

    @Test
    public void getSalesforceValue_einsteinUrlNotFound() {
        when(salesforceConfigurationRepository.findByCodeAndIsActiveTrue(anyString()))
                .thenReturn(null);

        when(systemConfigurationRepository.getValueByKey(anyString()))
                .thenReturn("NOT_FOUND");

        assertEquals("NOT_FOUND", configurationService.getEinsteinUrl());
    }
}

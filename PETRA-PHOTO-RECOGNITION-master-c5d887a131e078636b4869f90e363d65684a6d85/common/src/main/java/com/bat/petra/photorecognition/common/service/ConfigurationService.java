package com.bat.petra.photorecognition.common.service;

import com.bat.petra.photorecognition.common.configuration.ApplicationProperty;
import com.bat.petra.photorecognition.common.model.SalesforceConfiguration;
import com.bat.petra.photorecognition.common.model.SystemConfiguration;
import com.bat.petra.photorecognition.common.repository.SalesforceConfigurationRepository;
import com.bat.petra.photorecognition.common.repository.SystemConfigurationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigurationService {

    static Logger LOGGER = LogManager.getLogger(ConfigurationService.class);

    @Autowired
    private SystemConfigurationRepository systemConfigurationRepository;

    @Autowired
    private SalesforceConfigurationRepository salesforceConfigurationRepository;


    public String getEinsteinUrl() {
        return getSalesforceValueByCode(ApplicationProperty.EINSTEIN_VISION_URL);
    }

    public String getEinsteinAccountId() {
        return getSalesforceValueByCode(ApplicationProperty.EINSTEIN_VISION_ACCOUNT_ID);
    }

    public String getEinsteinPrivateKey() {
        return getSalesforceValueByCode(ApplicationProperty.EINSTEIN_VISION_PRIVATE_KEY);
    }

    public String getEinsteinAccessToken() {
        return getSystemValueByKey(ApplicationProperty.EINSTEIN_VISION_ACCESS_TOKEN);
    }

    public String getEinsteinRefreshToken() {
        return getSystemValueByKey(ApplicationProperty.EINSTEIN_VISION_REFRESH_TOKEN);
    }

    public String getEinsteinAccessTokenExpiresIn() {
        return getSystemValueByKey(ApplicationProperty.EINSTEIN_VISION_ACCESS_TOKEN_EXPIRES_IN);
    }

    public String getEinsteinTimeDifferenceLimit() {
        return getSalesforceValueByCode(ApplicationProperty.EINSTEIN_VISION_TIME_DIFFERENCE_LIMIT);
    }

    public String getAzureContainerName() {
        return getSalesforceValueByCode(ApplicationProperty.AZURE_CONTAINER_NAME);
    }

    public String getAzureConnectionString() {
        return getSalesforceValueByCode(ApplicationProperty.AZURE_CONNECTION_STRING);
    }

    public String getTimeValidationEnabledFlag() {
        return getSalesforceValueByCode(ApplicationProperty.EINSTEIN_VISION_VALIDATION_TIME_DIFFERENCE_ENABLED);
    }

    public String getFaceValidationEnabledFlag() {
        return getSalesforceValueByCode(ApplicationProperty.EINSTEIN_VISION_VALIDATION_FACE_DETECTION_ENABLED);
    }

    public String getEinsteinVisionModelId() {
        return getSalesforceValueByCode(ApplicationProperty.EINSTEIN_VISION_MODEL_ID);
    }

    public String getMaxSizeLimit() {
        return getSalesforceValueByCode(ApplicationProperty.EINSTEIN_VISION_FILE_SIZE_LIMIT_MAX);
    }

    public String getMinSizeLimit() {
        return getSalesforceValueByCode(ApplicationProperty.EINSTEIN_VISION_FILE_SIZE_LIMIT_MIN);
    }

    public String getFaceRecogntionUrl() {
        return getSalesforceValueByCode(ApplicationProperty.EINSTEIN_VISION_FACE_DETECTION_SERVICE_URL);
    }

    public List<SalesforceConfiguration> getAzureIntegrationConfigurations() {
        return salesforceConfigurationRepository.findByRecordTypeNameAndIsActiveTrue(ApplicationProperty.AZURE_INTEGRATION);
    }

    private String getSalesforceValueByCode(String key) {

        SalesforceConfiguration conf = salesforceConfigurationRepository.findByCodeAndIsActiveTrue(key);

        if (Optional.ofNullable(conf).isPresent()) {
            return conf.getValue();
        }

        return systemConfigurationRepository.getValueByKey(key);
    }

    private String getSystemValueByKey(String key) {
        return systemConfigurationRepository.getValueByKey(key);
    }

    public void saveTokens(String accessToken, String refreshToken, String expiresIn) {

        saveSystemConfiguration(ApplicationProperty.EINSTEIN_VISION_ACCESS_TOKEN, accessToken);
        saveSystemConfiguration(ApplicationProperty.EINSTEIN_VISION_REFRESH_TOKEN, refreshToken);
        saveSystemConfiguration(ApplicationProperty.EINSTEIN_VISION_ACCESS_TOKEN_EXPIRES_IN, expiresIn);
    }

    public void saveAccessToken(String accessToken, String expiresIn) {

        saveSystemConfiguration(ApplicationProperty.EINSTEIN_VISION_ACCESS_TOKEN, accessToken);
        saveSystemConfiguration(ApplicationProperty.EINSTEIN_VISION_ACCESS_TOKEN_EXPIRES_IN, expiresIn);
    }

    public void saveSystemConfiguration(String key, String value) {

        LOGGER.debug("saveSystemConfiguration " + key + " " + value);
        SystemConfiguration config = systemConfigurationRepository.findByKey(key);
        config.setValue(value);
        systemConfigurationRepository.save(config);
    }


}

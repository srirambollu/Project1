package com.bat.petra.photorecognition.einstein.service;

import com.bat.petra.photorecognition.common.service.ConfigurationService;
import com.bat.petra.photorecognition.einstein.client.EinsteinClient;
import com.bat.petra.photorecognition.einstein.model.TokenResult;
import com.bat.petra.photorecognition.einstein.service.impl.EinsteinServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class EinsteinServiceTest {

    @TestConfiguration
    static class EinsteinServiceTestContextConfiguration {

        @Bean
        public EinsteinService einsteinService() {
            return new EinsteinServiceImpl();
        }
    }

    @Autowired
    private EinsteinService einsteinService;

    @MockBean
    private EinsteinClient einsteinClient;

    @MockBean
    private AssertionGeneratorService assertionGeneratorService;

    @MockBean
    private ConfigurationService configService;

    @Test
    public void getOffllineAccessToken() throws IOException {

        TokenResult result = new TokenResult();
        result.setAccessToken("TOKEN");
        Mockito.when(configService.getEinsteinUrl()).thenReturn("url");
        Mockito.when(configService.getEinsteinAccountId()).thenReturn("accountId");
        Mockito.when(configService.getEinsteinPrivateKey()).thenReturn("privateKey");
        Mockito.when(assertionGeneratorService.generateJWTAssertion(
                "url",
                "accountId",
                "privateKey",
                10)
        ).thenReturn("asseration");

        Mockito.when(einsteinClient.accessTokenRequest(
                "url",
                "asseration",
                true)
        ).thenReturn(result);

        String accessToken = einsteinService.getOfflineAccessToken();

        assertEquals("TOKEN", accessToken);
    }
}
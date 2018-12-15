package com.bat.petra.photorecognition.einstein.service;

import com.bat.petra.photorecognition.config.DataLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class EinsteinInitializer implements ApplicationRunner {

    static Logger logger = LogManager.getLogger(EinsteinInitializer.class);

    @Autowired
    private EinsteinService einsteinService;

    @Autowired
    DataLoader dataLoader;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        logger.info("Getting refresh token from Einstein");
        dataLoader.run();
        einsteinService.getOfflineAccessToken();
    }
}
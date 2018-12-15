package com.bat.petra.photorecognition.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class FileUtils {

    static Logger LOGGER = LogManager.getLogger(FileUtils.class);

    public static File writeFile(String fileName, byte[] dataFile) throws IOException {

        File tempFile = File.createTempFile(fileName, "");
        LOGGER.debug("tempFile " + tempFile.getPath());

        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(dataFile);
        fos.close();
        return tempFile;
    }
}

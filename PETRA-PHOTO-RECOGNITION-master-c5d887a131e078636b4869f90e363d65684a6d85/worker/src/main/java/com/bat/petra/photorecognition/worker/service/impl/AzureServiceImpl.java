package com.bat.petra.photorecognition.worker.service.impl;

import com.bat.petra.photorecognition.common.exception.AzureBlobStorageServiceException;
import com.bat.petra.photorecognition.common.exception.IRProcessResultCodes;
import com.bat.petra.photorecognition.common.model.SalesforceConfiguration;
import com.bat.petra.photorecognition.common.repository.AzureIntegrationRepository;
import com.bat.petra.photorecognition.common.service.ConfigurationService;
import com.bat.petra.photorecognition.worker.service.AzureService;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AzureServiceImpl implements AzureService {

    static Logger LOGGER = LogManager.getLogger(AzureServiceImpl.class);

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private AzureIntegrationRepository azureIntegrationRepository;

    private Map<String, CloudStorageAccount> storageAccounts;

    public ByteArrayOutputStream downloadBlob(String urlToBlob) {
        try {

            CloudBlob blob = getBlobReference(urlToBlob);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            blob.download(output);
            return output;
        } catch (StorageException | URISyntaxException e) {

            LOGGER.error("Azure Blob Storage exception", e);
            throw new AzureBlobStorageServiceException(IRProcessResultCodes.AZURE_DOWNLOAD_ERROR, e.getMessage());
        }
    }

    @PostConstruct
    public void init() {
        storageAccounts = new HashMap<>();
        configurationService.getAzureIntegrationConfigurations()
                .stream()
                .filter(SalesforceConfiguration::getIsActive)
                .forEach(this::createStoreAccountEntry);
    }

    private CloudBlob getBlobReference(String blobUrl) throws StorageException, URISyntaxException {
        blobUrl = blobUrl.replace("https://", "");
        String[] parsedUrl = blobUrl.split("/");
        String domain = parsedUrl[0];
        String container = parsedUrl[1];
        StringBuilder pathToBlobBuilder = new StringBuilder();

        for(int i = 2; i < parsedUrl.length; i++) {
            pathToBlobBuilder.append(parsedUrl[i]).append("/");
        }

        String pathToBlob = pathToBlobBuilder.toString();
        pathToBlob = pathToBlob.substring(0, pathToBlob.length() - 1);

        CloudStorageAccount storageAccount = this.storageAccounts.get("https://" + domain);

        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        CloudBlobContainer blobContainer = blobClient.getContainerReference(container);
        return blobContainer.getBlockBlobReference(pathToBlob);
    }

    private void createStoreAccountEntry(SalesforceConfiguration salesforceConfiguration) {
        StringBuilder connectionStringBuffer = new StringBuilder();
        connectionStringBuffer
                .append("BlobEndpoint=")
                .append(salesforceConfiguration.getBlobEndpoint())
                .append(";")
                .append("QueueEndpoint=")
                .append(salesforceConfiguration.getQueueEndpoint())
                .append(";")
                .append("FileEndpoint=")
                .append(salesforceConfiguration.getFileEndpoint())
                .append(";")
                .append("TableEndpoint=")
                .append(salesforceConfiguration.getTableEndpoint())
                .append(";")
                .append("SharedAccessSignature=")
                .append(salesforceConfiguration.getSas());

        try {
            storageAccounts.put(salesforceConfiguration.getBlobEndpoint(), CloudStorageAccount.parse(connectionStringBuffer.toString()));
        } catch (URISyntaxException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}

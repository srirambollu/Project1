package com.bat.petra.photorecognition.common.repository;

import com.bat.petra.photorecognition.common.model.AzureIntegration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AzureIntegrationRepository extends JpaRepository<AzureIntegration, Integer> {
}

package com.bat.petra.photorecognition.common.repository;

import com.bat.petra.photorecognition.common.model.SalesforceConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesforceConfigurationRepository extends JpaRepository<SalesforceConfiguration, Integer> {

    SalesforceConfiguration findByCodeAndIsActiveTrue(String code);
    List<SalesforceConfiguration> findByRecordTypeNameAndIsActiveTrue(String recordTypeName);
}
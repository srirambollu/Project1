package com.bat.petra.photorecognition.common.repository;

import com.bat.petra.photorecognition.common.model.ImageBlobStorage;
import com.bat.petra.photorecognition.common.model.ProcessStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageBlobStorageRepository extends JpaRepository<ImageBlobStorage, Long> {

    ImageBlobStorage findByExternalId(String externalId);

    @Query("SELECT b FROM ImageBlobStorage b WHERE b.status=?1")
    List<ImageBlobStorage> findByStatus(ProcessStatus status);
}
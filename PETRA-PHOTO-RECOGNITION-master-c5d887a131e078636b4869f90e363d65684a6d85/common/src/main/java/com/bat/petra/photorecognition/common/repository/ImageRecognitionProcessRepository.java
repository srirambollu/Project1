package com.bat.petra.photorecognition.common.repository;

import com.bat.petra.photorecognition.common.model.ImageRecognitionProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ImageRecognitionProcessRepository extends JpaRepository<ImageRecognitionProcess, Integer> {

    @Query("SELECT p FROM ImageRecognitionProcess p WHERE p.status = 'NEW' ")
    List<ImageRecognitionProcess> findAllNew();

    ImageRecognitionProcess findBySfId(String sfId);
}

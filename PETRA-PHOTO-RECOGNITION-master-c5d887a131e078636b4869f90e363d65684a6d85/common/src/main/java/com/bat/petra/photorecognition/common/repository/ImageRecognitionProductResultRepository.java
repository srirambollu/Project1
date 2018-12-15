package com.bat.petra.photorecognition.common.repository;

import com.bat.petra.photorecognition.common.model.ImageRecognitionProductResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRecognitionProductResultRepository extends JpaRepository<ImageRecognitionProductResult, Integer> {
}

package com.bat.petra.photorecognition.common.repository;

import com.bat.petra.photorecognition.common.model.ImageRecognitionResultDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRecognitionResultDetailRepository extends JpaRepository<ImageRecognitionResultDetail, Integer> {
}

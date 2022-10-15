package com.mitocode.repo;

import com.mitocode.model.ConsultExam;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface IConsultExamRepo extends IGenericRepo<ConsultExam, Integer> {

//    @Transactional
    @Modifying
    @Query(value = "INSERT INTO consult_exam(consult_id, exam_id) VALUES (:consultId, :examId)", nativeQuery = true)
    Integer saveExam(@Param("consultId") Integer consultId, @Param("examId") Integer examId);

    @Query("FROM ConsultExam ce WHERE ce.consult.consultId = :consultId")
    List<ConsultExam> getExamsByConsultId(@Param("consultId") Integer consultId);
}

package com.mitocode.service;

import com.mitocode.model.Consult;
import com.mitocode.model.Exam;

import java.time.LocalDateTime;
import java.util.List;

public interface IConsultService extends ICRUD<Consult, Integer>{

    Consult saveTransactional(Consult consult, List<Exam> exams);
    List<Consult> search(String dni, String fullName);
    List<Consult> searchByDates(LocalDateTime date1, LocalDateTime date2);

}

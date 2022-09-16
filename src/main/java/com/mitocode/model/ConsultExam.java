package com.mitocode.model;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@IdClass(ConsultExamPK.class)
public class ConsultExam {

    @Id
    private Consult consult;

    @Id
    private Exam exam;

}

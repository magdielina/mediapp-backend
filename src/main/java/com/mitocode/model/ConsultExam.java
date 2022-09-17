package com.mitocode.model;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@IdClass(ConsultExamPK.class)
@Entity
public class ConsultExam {

    @Id
    private Consult consult;

    @Id
    private Exam exam;

}

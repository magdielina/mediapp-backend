package com.mitocode.model;


import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class ConsultExamPK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "consult_id")
    private Consult consult;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;
}

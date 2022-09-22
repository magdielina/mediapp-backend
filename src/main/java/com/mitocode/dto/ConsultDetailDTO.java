package com.mitocode.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mitocode.model.Consult;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsultDetailDTO {

    @EqualsAndHashCode.Include
    private Integer detailId;

    @JsonBackReference
    private ConsultDTO consult;

    @NotNull
    private String diagnosis;

    @NotNull
    private String treatment;
}

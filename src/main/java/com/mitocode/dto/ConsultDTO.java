package com.mitocode.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsultDTO {

    @EqualsAndHashCode.Include
    private Integer consultId;

    @NotNull
    private PatientDTO patient;

    @NotNull
    private MedicDTO medic;

    @NotNull
    private SpecialtyDTO speciality;

    @NotNull
    private String numConsult;

    @NotNull
    private LocalDateTime consultDate;

    @NotNull
    private List<ConsultDetailDTO> details;
}

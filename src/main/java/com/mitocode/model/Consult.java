package com.mitocode.model;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Consult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer consultId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false, foreignKey = @ForeignKey(name = "FK_CONSULT_PATIENT"))
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "medic_id", nullable = false, foreignKey = @ForeignKey(name = "FK_CONSULT_MEDIC"))
    private Medic medic;

    @ManyToOne
    @JoinColumn(name = "speciality_id", nullable = false, foreignKey = @ForeignKey(name = "FK_CONSULT_SPECIALITY"))
    private Speciality speciality;

    @Column(length = 3, nullable = false)
    private String numConsult;

    @Column(nullable = false)
    private LocalDateTime consultDate;
    // Spring Boot 1.5 -> pom.xml jsr310 data-types

    @OneToMany(mappedBy = "consult", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<ConsultDetail> details;

}
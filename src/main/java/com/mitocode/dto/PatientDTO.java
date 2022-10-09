package com.mitocode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PatientDTO {

    @EqualsAndHashCode.Include
    private Integer patientId;

    @NotNull
    @Size(min = 3, message = "{firstName.size}")
//    @JsonProperty(value = "nombre") // Cambia el parametro en el Json
    private String firstName;

    @NotEmpty
    @Size(min = 3, message = "{lastName.size}")
    private String lastName;

    private String dni;
    private String address;
    private String phone;

    @Email
    private String email;
}

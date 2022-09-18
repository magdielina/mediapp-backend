package com.mitocode.dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class MedicDTO {

    @EqualsAndHashCode.Include
    private Integer medicId;
    private String firstName;
    private String lastName;
    private String cmp;
    private String photoUrl;
}

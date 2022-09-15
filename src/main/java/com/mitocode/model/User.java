package com.mitocode.model;


import lombok.*;

import javax.persistence.*;

//@JacksonXmlRootElement
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "user_data")
public class User {

    @Id
    @EqualsAndHashCode.Include
    private Integer userId;

    @Column(nullable = false, length = 60, unique = true)
    private String username;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false)
    private boolean enabled;
}

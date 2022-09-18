package com.mitocode.model;


import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    @ManyToMany
    @JoinTable(name = "user_role",
                joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"),
                inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "roleId"))
    private List<Role> roles;
}

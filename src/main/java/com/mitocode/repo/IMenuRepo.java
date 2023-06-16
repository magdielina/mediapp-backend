package com.mitocode.repo;

import com.mitocode.model.Menu;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IMenuRepo extends IGenericRepo<Menu, Integer>{

    @Query(value= "SELECT m.* FROM menu_role mr \n" +
            "INNER JOIN user_role ur ON ur.role_id = mr.role_id \n" +
            "INNER JOIN menu m ON m.menu_id = mr.menu_id \n" +
            "INNER JOIN user_data u ON u.user_id = ur.user_id\n" +
            "WHERE u.username = :username", nativeQuery = true)
    List<Menu> getMenusByUsername(String username);

    //Para KeyCloak
    @Query(value="select mr.menu_id, r.role_id, r.name from menu_role mr inner join role r on mr.role_id = r.role_id order by mr.menu_id, role_id", nativeQuery = true)
    List<Object[]> getMenuRoles();
}

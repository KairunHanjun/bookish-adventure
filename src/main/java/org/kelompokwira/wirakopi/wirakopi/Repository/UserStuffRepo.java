package org.kelompokwira.wirakopi.wirakopi.Repository;

import org.kelompokwira.wirakopi.wirakopi.Entity.User;
import org.kelompokwira.wirakopi.wirakopi.Entity.UserStuff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface UserStuffRepo extends JpaRepository<UserStuff, Long>{
    List<UserStuff> findByUser(User user);
}

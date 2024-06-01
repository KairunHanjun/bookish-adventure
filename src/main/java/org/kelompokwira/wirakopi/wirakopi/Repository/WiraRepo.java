package org.kelompokwira.wirakopi.wirakopi.Repository;

import org.kelompokwira.wirakopi.wirakopi.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface WiraRepo extends JpaRepository<User, Long> {
    List<User> findByUsername(String username);
    List<User> findByEmail(String email);
} 

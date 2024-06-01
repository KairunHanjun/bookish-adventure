package org.kelompokwira.wirakopi.wirakopi.Repository;

import org.kelompokwira.wirakopi.wirakopi.Entity.UserAuthorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepo extends JpaRepository<UserAuthorities, Long>{
}
package org.kelompokwira.wirakopi.wirakopi.Entity;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "authorities"/*, uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})}*/)
public class UserAuthorities implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username")
    private User user;
    @NotNull
    String authority;
    @Override
    public String toString() {
        return "UserAuthorities [Id=" + Id + ", authority=" + authority + ", userEntityId=" + user.getUsername() + "]";
    }
}

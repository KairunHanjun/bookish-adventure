package org.kelompokwira.wirakopi.wirakopi.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "information")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID_INFO;
    @NotEmpty
    private String name;
    @NotEmpty
    private String no_telp;
    @OneToOne(mappedBy = "username")
    @JoinColumn(name = "user")
    private User user;
}

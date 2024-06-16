package org.kelompokwira.wirakopi.wirakopi.Entity;

import org.kelompokwira.wirakopi.wirakopi.JsonObject.ConvertGson;
import org.kelompokwira.wirakopi.wirakopi.JsonObject.JsonStuff;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Component
@Table(name = "User_Stuff")
public class UserStuff{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user", referencedColumnName = "id")
    private User user;
    @Convert(converter = ConvertGson.class)
    @Column(columnDefinition = "JSON")
    private JsonStuff jsonContent;
}
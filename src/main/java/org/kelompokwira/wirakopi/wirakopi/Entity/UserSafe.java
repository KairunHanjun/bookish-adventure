package org.kelompokwira.wirakopi.wirakopi.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSafe {
    public String Username = "";
    public String Email = "";
    public String password = "";
    public String RePassword = "";
}

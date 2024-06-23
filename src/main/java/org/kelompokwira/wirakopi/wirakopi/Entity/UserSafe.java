package org.kelompokwira.wirakopi.wirakopi.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSafe {
    public String Name = "";
    public String Telp = "";
    public String Username = "";
    public String Email = "";
    public String password = "";
    public String RePassword = "";

    public UserSafe(User user, boolean usePassword){
        this.Name = user.getName();
        this.Telp = user.getNo_telp();
        this.Username = user.getUsername();
        this.Email = user.getEmail();
        if(usePassword){
            this.password = user.getPassword();
            this.RePassword = user.getPassword();
        }
    }
}

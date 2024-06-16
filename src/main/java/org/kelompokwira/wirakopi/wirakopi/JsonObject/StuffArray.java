package org.kelompokwira.wirakopi.wirakopi.JsonObject;

import org.kelompokwira.wirakopi.wirakopi.Entity.Stuff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StuffArray {
    protected String DrinkName = "";
    protected int DrinkPrice = 0;
    protected int Amount = 0;

    public StuffArray(String product){
        if(product == null) return;
        this.Amount = 1;
        this.DrinkName = product;
        this.DrinkPrice = Stuff.getDrinkPrice(product);
    }
}

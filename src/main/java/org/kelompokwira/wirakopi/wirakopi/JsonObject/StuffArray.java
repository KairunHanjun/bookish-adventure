package org.kelompokwira.wirakopi.wirakopi.JsonObject;

import java.io.Serializable;

import org.kelompokwira.wirakopi.wirakopi.Entity.Stuff;
import org.kelompokwira.wirakopi.wirakopi.StaticMethod.StaticMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StuffArray implements Serializable{
    private String IDs = StaticMethod.generateRandomChars();
    private String DrinkName = "";
    private int DrinkPrice = 0;
    private int Amount = 0;
    private final String brand = "Sky's Coffee";
    private final String category = "Drink & Beverage";
    private final String merchant_name = "Sky's Coffee";

    public StuffArray(String product){
        if(product == null) return;
        this.Amount = 1;
        this.DrinkName = product;
        this.DrinkPrice = Stuff.getDrinkPrice(product)*1000;
    }

    public StuffArray(boolean addShippingCost){
        if(!addShippingCost) return;
        this.DrinkName = "Shipping Cost";
        this.DrinkPrice = 10000;
        this.Amount = 1;
    }
}

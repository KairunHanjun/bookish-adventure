package org.kelompokwira.wirakopi.wirakopi.Entity;

import java.util.HashMap;
import java.util.Map;

public class Stuff{
    public static enum DrinkNameEnum {
        Americano(25, "Americano"),
        Macchiato(30, "Macchiato"),
        Expresso(20, "Expresso"),
        Cappuccino(35, "Cappuccino"),
        IceVL(30, "Ice Vanila Latte"),
        IceHL(30, "Ice Hazelnut Latte"),
        Default(0, "");

        private int DrinkPrice = 0;
        private String DrinkCustomName = "";

        public final int getDrinkPrice(){
            return DrinkPrice;
        }

        public final String nama(){
            return DrinkCustomName;
        }

        DrinkNameEnum(int DrinkPrice) {
            this.DrinkPrice = DrinkPrice;
        }

        DrinkNameEnum(int i, String string) {
            this.DrinkPrice = i;
            this.DrinkCustomName = string; 
        }
    }

    public static DrinkNameEnum getDrinkNameEnum(String drinkName){
        switch (drinkName) {
            case "Americano":{
                return DrinkNameEnum.Americano;
            }
            case "Macchiato":{
                return DrinkNameEnum.Macchiato;
            }
            case "Expresso":{
                return DrinkNameEnum.Expresso;
            }
            case "Cappuccino":{
                return DrinkNameEnum.Cappuccino;
            }
            case "IceVL":{
                return DrinkNameEnum.IceVL;
            }
            case "IceHL":{
                return DrinkNameEnum.IceHL;
            }
            default:
                return DrinkNameEnum.Default;
        }
    }

    private final static HashMap<String, Integer> DrinkInfo = new HashMap<>(
        Map.of("Americano", 25, "Macchiato", 30, "Expresso", 20,
    "Cappuccino", 35, "IceVL", 30, "IceHL", 30));

    public static class DrinkInfoV2{
        private String DrinkName = "";
        private Integer DrinkPrice = 0;

        public DrinkInfoV2(String DrinkName, Integer DrinkPrice){
            this.DrinkName = DrinkName;
            this.DrinkPrice = DrinkPrice;
        }

        public DrinkInfoV2(){}

        public void setDrinkName(String DrinkName){
            this.DrinkName = DrinkName;
        }

        public void setDrinkPrice(Integer DrinkPrice){
            this.DrinkPrice = DrinkPrice;
        }

        public String getDrinkName(){
            return DrinkName;
        }

        public Integer getDrinkPrice(){
            return DrinkPrice;
        }
    }

    public static Integer getDrinkPrice(String drink){
        return (DrinkInfo.get(drink) == null ? 0 : DrinkInfo.get(drink));
    }

    public static String getDrinkName(String drink){
        return (DrinkInfo.get(drink) == null ? "" : drink);
    }

    public static DrinkInfoV2 getDrinkInfo(String keyValueDrinkName){
        String kv = (DrinkInfo.get(keyValueDrinkName) == null ? "" : keyValueDrinkName);
        Integer v = (!kv.isEmpty() ? DrinkInfo.get(keyValueDrinkName) : 0);
        return new DrinkInfoV2(keyValueDrinkName, v);
    }
    
}

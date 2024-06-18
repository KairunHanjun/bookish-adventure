package org.kelompokwira.wirakopi.wirakopi.HTMLJava;

import org.kelompokwira.wirakopi.wirakopi.Entity.Stuff;
import org.kelompokwira.wirakopi.wirakopi.Entity.Stuff.DrinkNameEnum;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

public class HTML {
    public static enum urlImage {
        Americano("../img/Gun.png"),
        Macchiato("../img/Mac.png"),
        Expresso("../img/Mac.png"),
        Cappuccino("../img/Cap.png"),
        IceVL("../img/IceVL.png"),
        IceHL("../img/IceHL.png"),
        Default("");
        
        private String Image;

        urlImage(String Image) {
           this.Image = Image;
        }

        public String getImage(){
            return this.Image == null ? "" : this.Image;
        }
    }
    
    public static urlImage getImage(String drinkName){
        switch (drinkName) {
            case "Americano":{
                return urlImage.Americano;
            }
            case "Macchiato":{
                return urlImage.Macchiato;
            }
            case "Expresso":{
                return urlImage.Expresso;
            }
            case "Cappuchino":{
                return urlImage.Cappuccino;
            }
            case "IceVL":{
                return urlImage.IceVL;
            }
            case "IceHL":{
                return urlImage.IceVL;
            }
            default:
                return urlImage.Default;
        }
    }

    public static String divItemsBuilder(urlImage image, DrinkNameEnum Drink, int Ammount){
        return "<div class=\"rounded-[10px] justify-start items-start gap-[294px] inline-flex\">\n"
        + "     <div class=\"flex-col justify-start items-start gap-2.5 inline-flex\">\n"
        + "         <div class=\"justify-start items-start gap-7 inline-flex\">\n"
        + "             <img class=\"w-[99.99px] h-[77.33px] rounded-[10px]\" src=\" "+ image.getImage() +" \" />\n"
        + "             <div class=\"w-[118.74px] h-[34.37px] text-neutral-600 text-lg font-semibold font-['Inter'] leading-normal\">"+ Drink.name() +"</div>\n"
        + "         </div>\n"
        + "         <div class=\"w-[82.81px] h-[34.37px] left-[128.12px] top-[37.24px] absolute text-zinc-400 text-[13px] font-bold font-['Inter'] leading-normal\">"+ Drink.getDrinkPrice() +"</div>\n"
        + "     </div>\n"
        + "     <div class=\"flex-col justify-start items-center gap-[17px] inline-flex\">\n"
        + "         <div class=\"h-[27.21px] text-neutral-600 text-base font-semibold font-['Inter']\">Quantity</div>\n"
        + "         <div class=\"h-auto text-zinc-400 gap-2 items-center flex flex-row text-base font-black font-['Inter']\">\n"
        + "             <form method=\"post\" th:action=\"@{/user/jemBelanda}\">\n"
        + "                 <input type=\"hidden\" th:name=\"action\" th:value=\"add\" />\n"
        + "                 <button type=\"submit\" class=\"rounded-md hover:bg-green-600 bg-orange-400 text-white w-7\">+</button>\n"
        + "             </form>\n"
        + "                 <p>"+ Ammount +"</p>\n"
        + "             <form method=\"post\" th:action=\"@{/user/jemBelanda}\">\n"
        + "                 <input type=\"hidden\" th:name=\"action\" th:value=\"sub\" />\n"
        + "                 <button type=\"submit\" class=\"rounded-md hover:bg-red-600 bg-orange-400 text-white w-7\">-</button>\n"
        + "             </form>\n"
        + "         </div>\n"
        + "     </div>\n"
        + "</div>\n\n";
    }

}

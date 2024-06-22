package org.kelompokwira.wirakopi.wirakopi.HTMLJava;
import java.text.DecimalFormat;

import org.kelompokwira.wirakopi.wirakopi.Entity.Stuff.DrinkNameEnum;


public class HTML {
    public static String formatRupiah(double number) {
        // UseDecimalFormat to format the number with commas and two decimal places
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String formattedNumber = formatter.format(number);

        // Prepend "Rp. " to the formatted number
        return "Rp. " + formattedNumber;
    }

    public static enum urlImage {
        Americano("../img/Gun.png"),
        Macchiato("../img/Mac.png"),
        Expresso("../img/Ekspresi.png"),
        Cappuccino("../img/betuah.png"),
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
            case "Cappuccino":{
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
        + "             <div class=\"flex flex-col\">\n"
        + "                 <div class=\"w-[118.74px] text-nowrap h-[34.37px] text-neutral-600 text-lg font-semibold font-['Inter'] leading-normal\">"+ Drink.nama() +"</div>\n"
        + "                 <div class=\"text-nowrap relative text-zinc-400 text-[13px] font-bold font-['Inter'] leading-normal\">"+ formatRupiah(((Ammount * Drink.getDrinkPrice()) * 1000)) +"</div>\n"
        + "             </div>\n"
        + "         </div>\n"
        + "     </div>\n"
        + "     <div class=\"flex-col justify-start items-center gap-[17px] inline-flex\">\n"
        + "         <div class=\"h-[27.21px] text-neutral-600 text-base font-semibold font-['Inter']\">Quantity</div>\n"
        + "         <div class=\"h-auto text-zinc-400 gap-2 flex flex-row font-black font-['Inter']\">\n"
        + "             <form method=\"get\" th:action=\"@{/user/jemBelanda}\">\n"
        + "                 <input type=\"hidden\" name=\"action\" value=\"add_"+ Drink.name() +"\" />\n"
        + "                 <button type=\"submit\" class=\"rounded-md hover:bg-green-600 bg-orange-400 text-white w-7\">+</button>\n"
        + "             </form>\n"
        + "                 <p>"+ Ammount +"</p>\n"
        + "             <form method=\"get\" th:action=\"@{/user/jemBelanda}\">\n"
        + "                 <input type=\"hidden\" name=\"action\" value=\"sub_"+ Drink.name() +"\" />\n"
        + "                 <button type=\"submit\" class=\"rounded-md hover:bg-red-600 bg-orange-400 text-white w-7\">-</button>\n"
        + "             </form>\n"
        + "         </div>\n"
        + "     </div>\n"
        + "</div>\n\n";
    }

}

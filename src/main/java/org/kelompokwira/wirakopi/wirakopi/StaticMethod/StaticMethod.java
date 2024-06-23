package org.kelompokwira.wirakopi.wirakopi.StaticMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.kelompokwira.wirakopi.wirakopi.Entity.User;
import org.kelompokwira.wirakopi.wirakopi.JsonObject.StuffArray;

import com.midtrans.Config;
import com.midtrans.ConfigBuilder;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransSnapApi;

public class StaticMethod {
    public static final String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    public static final int length = 5;

    public static String generateRandomChars (String candidateChars, int length) {
        StringBuilder sb = new StringBuilder ();
        Random random = new Random ();
        for (int i = 0; i < length; i ++) {
            sb.append (candidateChars.charAt (random.nextInt (candidateChars
                    .length ())));
        }

        return sb.toString ();
    }

    public static String generateRandomChars () {
        StringBuilder sb = new StringBuilder ();
        Random random = new Random ();
        for (int i = 0; i < length; i ++) {
            sb.append (candidateChars.charAt (random.nextInt (candidateChars
                    .length ())));
        }

        return sb.toString ();
    }

    public static StuffArray addItem(StuffArray stuff) {
        if (stuff == null) return new StuffArray();
        stuff.setAmount(stuff.getAmount()+1);
        return stuff; 
    }

    public static StuffArray subItem(StuffArray stuff) {
        if (stuff == null) return new StuffArray();
        stuff.setAmount(stuff.getAmount()-1);
        return stuff; 
    }

    public static MidtransSnapApi snapApi(String ClientKey, String ServerKey, boolean isProguction){
        ConfigBuilder config = Config.builder();
        config.setClientKey(ClientKey);
        config.setServerKey(ServerKey);
        config.setIsProduction(isProguction);
        return new ConfigFactory(config.build()).getSnapApi();
    }

    public static Map<String, Object> CreateMappingUser(User user, String Address){
        if(user == null) return new HashMap<>();
        Map<String, Object> billingAddres = new HashMap<>();
        billingAddres.put("first_name", user.getName());
        billingAddres.put("last_name", "");
        billingAddres.put("email", user.getEmail());
        billingAddres.put("phone", user.getNo_telp());
        billingAddres.put("address", Address);
        billingAddres.put("city", "");
        billingAddres.put("postal_code", "");
        billingAddres.put("country_code", "IDN");
        
        Map<String, Object> custDetail = new HashMap<>();
        custDetail.put("first_name", user.getName());
        custDetail.put("last_name", "");
        custDetail.put("email", user.getEmail());
        custDetail.put("phone", user.getNo_telp());
        custDetail.put("billing_address", billingAddres);

        return custDetail;
    }

}

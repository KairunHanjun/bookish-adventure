package org.kelompokwira.wirakopi.wirakopi.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonStuff implements Serializable{
    protected List<StuffArray> stuffArray = new ArrayList<>();

    public List<Map<String, String>> MappingStuffArray(){
        if(stuffArray.isEmpty()) return new ArrayList<>();
        List<Map<String, String>> items = new ArrayList<>();

        for (StuffArray stuff : stuffArray) {
            Map<String, String> item = new HashMap<>();
            item.put("id", stuff.getIDs());
            item.put("price", String.valueOf(stuff.getDrinkPrice()));
            item.put("quantity", String.valueOf(stuff.getAmount()));
            item.put("name", stuff.getDrinkName());
            item.put("brand", stuff.getBrand());
            item.put("category", stuff.getCategory());
            item.put("merchant_name", stuff.getMerchant_name());
            items.add(item);
        }

        return items;
    }

    public void addShippingCost(){
        for (StuffArray stuff : stuffArray) 
            if(stuff.getDrinkName().equals("Shipping Cost")) return;
        stuffArray.add(new StuffArray(true));
    }

    public String getGrossAmount(){
        int udin = 0;
        for (StuffArray stuff : stuffArray) 
            udin += stuff.getDrinkPrice()*stuff.getAmount();
        return String.valueOf(udin);
    }
}

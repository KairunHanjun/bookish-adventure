package org.kelompokwira.wirakopi.wirakopi.JsonObject;

import com.nimbusds.jose.shaded.gson.Gson;

import jakarta.persistence.AttributeConverter;

public class ConvertGson implements AttributeConverter<JsonStuff, String> {

    private final static Gson GSON = new Gson();
  
    @Override
    public String convertToDatabaseColumn(JsonStuff mjo) {
      return GSON.toJson(mjo);
    }
  
    @Override
    public JsonStuff convertToEntityAttribute(String dbData) {
      return GSON.fromJson(dbData, JsonStuff.class);
    }
}

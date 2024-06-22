package org.kelompokwira.wirakopi.wirakopi.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonStuff implements Serializable{
    protected List<StuffArray> stuffArray = new ArrayList<>();
}

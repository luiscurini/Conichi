package com.luisrubenrodriguez.conichi.Model.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GamingMonster on 05.05.2017.
 * Returns a List<String> from the JSON response with the next buses that will arrive to the station.
 */

public class NextBusesTypeAdapter implements JsonDeserializer<List<String>> {

    @Override
    public List<String> deserialize(
            JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        return extractStationBuses(json);
    }

    private List<String> extractStationBuses(JsonElement json) {
        JsonArray arr = json.getAsJsonObject()
                .getAsJsonArray("estimates");

        ArrayList<String> list = new ArrayList<>();

        for (JsonElement element : arr) {
            extractBuses(list, element);
        }

        return list;
    }

    private void extractBuses(ArrayList<String> list, JsonElement element) {
        JsonObject j = element.getAsJsonObject();

        String line = j.get("line").getAsString();

        Integer estimate = 0;

        if (!j.get("estimate").isJsonNull()) {
            estimate = j.get("estimate").getAsInt();
        }
        list.add("[" + line + "] " + estimate.toString() + "min");


    }


}

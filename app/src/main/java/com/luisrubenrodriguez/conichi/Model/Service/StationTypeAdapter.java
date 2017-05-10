package com.luisrubenrodriguez.conichi.Model.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.luisrubenrodriguez.conichi.Model.Station;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GamingMonster on 05.05.2017.
 * Creates a List<Station> from the JSON response.
 */

public class StationTypeAdapter implements JsonDeserializer<List<Station>> {

    @Override
    public List<Station> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return extractStations(json);
    }

    private ArrayList<Station> extractStations(JsonElement json) {
        JsonArray arr = json.getAsJsonObject().get("locations").getAsJsonArray();

        ArrayList<Station> list = new ArrayList<>();

        for (JsonElement element : arr) {
            extractStation(list, element);
        }
        return list;
    }

    private void extractStation(ArrayList<Station> list, JsonElement element) {
        JsonObject obj = element.getAsJsonObject();

        Long id = obj.get("id").getAsLong();
        String title = obj.get("title").getAsString();
        String subtitle = obj.get("subtitle").getAsString();
        Double lat = obj.get("lat").getAsDouble();
        Double lon = obj.get("lon").getAsDouble();

        list.add(new Station(id, title, subtitle, lat, lon, null));
    }
}

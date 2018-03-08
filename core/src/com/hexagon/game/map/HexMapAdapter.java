package com.hexagon.game.map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.hexagon.game.map.structures.IStructure;

import java.lang.reflect.Type;

/**
 * Created by Sven on 27.02.2018.
 */

public class HexMapAdapter implements JsonSerializer<IStructure>, JsonDeserializer<IStructure> {

    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE  = "INSTANCE";

    @Override
    public IStructure deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = prim.getAsString();

        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }
        return context.deserialize(jsonObject.get(INSTANCE), clazz);
    }

    @Override
    public JsonElement serialize(IStructure structure, Type type, JsonSerializationContext context) {
        JsonObject retValue = new JsonObject();
        String className = structure.getClass().getName();
        retValue.addProperty(CLASSNAME, className);
        JsonElement elem = context.serialize(structure);
        retValue.add(INSTANCE, elem);
        return retValue;
    }

}

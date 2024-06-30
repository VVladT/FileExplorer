package pe.edu.utp.aed.fileexplorer.util.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import xyz.cupscoffee.files.api.implementation.SimpleDisk;

import java.lang.reflect.Type;

public class SimpleDiskSerializer implements JsonSerializer<SimpleDisk> {
    @Override
    public JsonElement serialize(SimpleDisk src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", src.getName());
        jsonObject.add("rootFolder", context.serialize(src.getRootFolder()));
        jsonObject.addProperty("limitSize", src.getLimitSize());
        jsonObject.add("metadata", context.serialize(src.getMetadata()));
        return jsonObject;
    }
}

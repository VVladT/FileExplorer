package pe.edu.utp.aed.fileexplorer.util.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import xyz.cupscoffee.files.api.implementation.SimpleFile;

import java.lang.reflect.Type;
import java.util.Base64;

public class SimpleFileSerializer implements JsonSerializer<SimpleFile> {
    @Override
    public JsonElement serialize(SimpleFile src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", src.getName());
        jsonObject.addProperty("content", Base64.getEncoder().encodeToString(src.getContent().array())); // Serialize ByteBuffer as Base64 string
        jsonObject.addProperty("createdDateTime", src.getCreatedDateTime().toString());
        jsonObject.addProperty("lastModifiedDateTime", src.getLastModifiedDateTime().toString());
        jsonObject.addProperty("path", src.getPath().toString()); // Serialize Path as a string
        jsonObject.add("otherMeta", context.serialize(src.getOtherMeta()));
        return jsonObject;
    }
}

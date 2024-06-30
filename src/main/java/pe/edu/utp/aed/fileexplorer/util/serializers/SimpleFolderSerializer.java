package pe.edu.utp.aed.fileexplorer.util.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import xyz.cupscoffee.files.api.implementation.SimpleFolder;

import java.lang.reflect.Type;

public class SimpleFolderSerializer implements JsonSerializer<SimpleFolder> {
    @Override
    public JsonElement serialize(SimpleFolder src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", src.getName());
        jsonObject.add("files", context.serialize(src.getFiles()));
        jsonObject.add("folders", context.serialize(src.getFolders()));
        jsonObject.addProperty("createdDateTime", src.getCreatedDateTime().toString());
        jsonObject.addProperty("lastModifiedDateTime", src.getLastModifiedDateTime().toString());
        jsonObject.addProperty("path", src.getPath().toString()); // Serialize Path as a string
        jsonObject.add("otherMeta", context.serialize(src.getOtherMeta()));
        return jsonObject;
    }
}

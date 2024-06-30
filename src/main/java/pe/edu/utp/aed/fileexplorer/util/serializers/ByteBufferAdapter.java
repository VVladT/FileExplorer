package pe.edu.utp.aed.fileexplorer.util.serializers;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ByteBufferSerializer extends TypeAdapter<ByteBuffer> {
    @Override
    public ByteBuffer read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        byte[] bytes = Base64.getDecoder().decode(reader.nextString());
        return ByteBuffer.wrap(bytes);
    }

    @Override
    public void write(JsonWriter writer, ByteBuffer data) throws IOException {
        if (data == null) {
            writer.nullValue();
            return;
        }
        ByteBuffer base64Bytes = Base64.getEncoder().encode(data.duplicate());
        String base64String = StandardCharsets.ISO_8859_1.decode(base64Bytes).toString();
        writer.value(base64String);
    }
}

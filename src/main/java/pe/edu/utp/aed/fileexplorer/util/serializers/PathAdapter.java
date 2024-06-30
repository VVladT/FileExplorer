package pe.edu.utp.aed.fileexplorer.util.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathAdapter extends TypeAdapter<Path> {

    @Override
    public void write(JsonWriter out, Path value) throws IOException {
        out.value(value.toString()); // Escribir el Path como una cadena
    }

    @Override
    public Path read(JsonReader in) throws IOException {
        return Paths.get(in.nextString()); // Leer una cadena y convertirla de vuelta a Path
    }
}


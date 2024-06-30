package pe.edu.utp.aed.fileexplorer.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pe.edu.utp.aed.fileexplorer.util.serializers.*;
import xyz.cupscoffee.files.api.Disk;
import xyz.cupscoffee.files.api.File;
import xyz.cupscoffee.files.api.Folder;
import xyz.cupscoffee.files.api.SavStructure;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class SavWriter {
    private static final String HEADER = "VFileSystem";

    public static void writeSavToFile(SavStructure savStructure, String path) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Disk.class, new SimpleDiskSerializer())
                .registerTypeAdapter(File.class, new SimpleFileSerializer())
                .registerTypeAdapter(Folder.class, new SimpleFolderSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .registerTypeHierarchyAdapter(ByteBuffer.class, new ByteBufferAdapter())
                .registerTypeHierarchyAdapter(Path.class, new PathAdapter())
                .setPrettyPrinting().create();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(HEADER + "\n");
            String json = gson.toJson(savStructure);
            writer.write(json);
            JOptionPane.showMessageDialog(null, "Sav guardado exitosamente en " + path);
        }
    }

}

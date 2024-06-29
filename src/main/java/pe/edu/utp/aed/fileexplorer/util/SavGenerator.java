package pe.edu.utp.aed.fileexplorer.util;

import xyz.cupscoffee.files.api.Disk;
import xyz.cupscoffee.files.api.File;
import xyz.cupscoffee.files.api.Folder;
import xyz.cupscoffee.files.api.SavStructure;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;

public class SavGenerator {

    public static void writeSavToFile(SavStructure savStructure, String path) {
        //Metodo para escribir en un archivo plano
        String header = savStructure.getHeader();
        Disk[] disks = savStructure.getDisks(); //Folder, File
        String metadata = savStructure.getMetadata().get("quickAccess");

        StringBuilder sb = new StringBuilder();

        for (Disk disk : disks) {
            sb.append(diskToString(disk)).append("\n");
        }

        System.out.println(header + "\n" + sb + "\n" + metadata);
    }

    public static void readSavFromFile(SavStructure savStructure, String path) {
        //Recibe la ruta y la lee de un archivo plano
    }

    public static String diskToString(Disk disk) {
        return String.format("Disk|%s,%s,%d\n", disk.getName(),
                folderToString(disk.getRootFolder()), disk.getLimitSize());
    }

    public static String folderToString(Folder folder) {
        StringBuilder files = new StringBuilder();
        StringBuilder folders = new StringBuilder();

        for (File file : folder.getFiles()) {
            files.append(fileToString(file)).append(";");
        }

        for (Folder fold : folder.getFolders()) {
            folders.append(folderToString(fold)).append(";");
        }

        return String.format("Folder|%s,%s,%s,%s,%s,%s\n", folder.getName(), files, folders,
                folder.getCreatedDateTime().toString(), folder.getLastModifiedDateTime().toString(),
                folder.getPath().toString());
    }

    public static String fileToString(File file) {
        return String.format("File|%s,%s,%s,%s,%d,%s\n",file.getName(), base64Encode(file.getContent()),
                file.getCreatedDateTime().toString(), file.getLastModifiedDateTime().toString(), file.getSize(),
                file.getPath().toString());
    }

    private static String base64Encode(ByteBuffer buffer) {
        byte[] bytes;
        if (buffer.hasArray()) {
            bytes = buffer.array();
        } else {
            bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

}

package pe.edu.utp.aed.fileexplorer.util;

import pe.edu.utp.aed.fileexplorer.model.Element;
import pe.edu.utp.aed.fileexplorer.model.FileFolder;
import pe.edu.utp.aed.fileexplorer.model.TextFile;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileExporter {

    private int foldersCreated;
    private int filesCreated;

    public FileExporter() {
        this.foldersCreated = 0;
        this.filesCreated = 0;
    }

    public void exportFolder(FileFolder folder) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"), "Desktop"));

        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File destinationFolder = fileChooser.getSelectedFile();
            try {
                foldersCreated = 0;
                filesCreated = 0;
                exportFolderToDestination(folder, destinationFolder.getAbsolutePath());
                String message = "Carpeta exportada con éxito.\n\n" +
                        "Ruta: " + destinationFolder.getAbsolutePath() + "\n" +
                        "Nombre: " + folder.getName() + "\n\n" +
                        "Carpetas creadas: " + foldersCreated + "\n" +
                        "Archivos planos creados: " + filesCreated;
                JOptionPane.showMessageDialog(null, message);
                openDirectory(new File(destinationFolder, folder.getName()));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al exportar la carpeta: " + e.getMessage());
            }
        }
    }

    private void exportFolderToDestination(FileFolder folder, String destinationPath) throws IOException {
        String folderName = folder.getName().isEmpty() ? "Sin nombre" : folder.getName();
        File destinationFolder = new File(destinationPath, folderName);
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
            foldersCreated++;
        }

        for (Element child : folder.getChildren()) {
            String childDestinationPath = new File(destinationFolder, child.getName()).getPath();
            if (child instanceof FileFolder) {
                exportFolderToDestination((FileFolder) child, destinationFolder.getPath());
            } else if (child instanceof TextFile) {
                exportTextFileToDestination((TextFile) child, childDestinationPath + ".txt");
                filesCreated++;
            } else if (child instanceof Element) {
                Files.copy(new File(child.getPath()).toPath(), new File(childDestinationPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                filesCreated++;
            }
        }
    }

    private void exportTextFileToDestination(TextFile textFile, String destinationPath) throws IOException {
        File textFileDestination = new File(destinationPath);
        try (FileWriter writer = new FileWriter(textFileDestination)) {
            writer.write(textFile.getContent().toString());
        }
    }

    private void openDirectory(File directory) {
        try {
            Desktop.getDesktop().open(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
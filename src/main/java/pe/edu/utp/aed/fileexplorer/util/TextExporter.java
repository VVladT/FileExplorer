package pe.edu.utp.aed.fileexplorer.util;

import pe.edu.utp.aed.fileexplorer.model.TextFile;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TextExporter {

    public void exportTextFile(TextFile textFile) {

        String userHome = System.getProperty("user.home");
        File desktop = new File(userHome, "Desktop");

        JFileChooser fileChooser = new JFileChooser(desktop);
        fileChooser.setDialogTitle("Exportar como .txt");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));

        String defaultFileName = textFile.getName();
        if (!defaultFileName.toLowerCase().endsWith(".txt")) {
            defaultFileName += ".txt";
        }
        fileChooser.setSelectedFile(new File(desktop, defaultFileName));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {

            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".txt");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(textFile.getContent().toString());

                String message = String.format("Archivo exportado exitosamente.\n\nUbicación: %s\nNombre del archivo: %s\n",
                        fileToSave.getParent(), fileToSave.getName());
                JOptionPane.showMessageDialog(null, message, "Exportar", JOptionPane.INFORMATION_MESSAGE);

                if (Desktop.isDesktopSupported()) {
                    Desktop escritorio = Desktop.getDesktop();
                    if (escritorio.isSupported(Desktop.Action.OPEN)) {
                        escritorio.open(fileToSave);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se puede abrir el archivo automáticamente en este sistema.",
                                "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se puede abrir el archivo automáticamente en este sistema.",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                }

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al exportar el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }
}
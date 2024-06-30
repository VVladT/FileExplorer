package pe.edu.utp.aed.fileexplorer.util;

import pe.edu.utp.aed.fileexplorer.model.VirtualFileSystem;
import xyz.cupscoffee.files.api.SavStructure;
import xyz.cupscoffee.files.api.exception.InvalidFormatFileException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SaveAndLoadVFS {
    public static void saveVFS(VirtualFileSystem vfs) throws IOException {
        String userHome = System.getProperty("user.home");
        File desktop = new File(userHome, "Desktop");

        JFileChooser fileChooser = new JFileChooser(desktop);
        fileChooser.setDialogTitle("Guardar como .sav");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de guardad (.sav)", "sav"));

        String path = fileChooser.getSelectedFile().getAbsolutePath();
        if (!path.toLowerCase().endsWith(".sav")) {
            path += ".sav";
        }
        fileChooser.setSelectedFile(new File(desktop, path));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            SavStructure sav = SavStructureManager.parseSystemToSavStructure(vfs);
            SavWriter.writeSavToFile(sav, path);
        }
    }

    public static void loadVFS() throws FileNotFoundException, InvalidFormatFileException {
        VFileSystemDriver driver = new VFileSystemDriver();
        SavStructure sav;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo .sav");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de guardado (.sav)", "sav"));

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            FileInputStream fileInputStream = new FileInputStream(fileChooser.getSelectedFile());
            sav = driver.readSavFile(fileInputStream);
            SavStructureManager.parseSavStructureToSystem(sav);
        }
    }
}

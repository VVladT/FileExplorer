package pe.edu.utp.aed.fileexplorer.util;

import pe.edu.utp.aed.fileexplorer.model.*;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class FileSystemEditor {
    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9 _-]+$");

    public static VirtualDrive createNewDrive() {
        JTextField nameField = new JTextField();
        JTextField sizeField = new JTextField();
        JComboBox<FileSize.Unit> unitComboBox = FileSize.createUnitComboBox();

        String name = "Nuevo Disco";
        boolean processed = false;

        while (!processed) {
            Object[] message = {
                    "Nombre del disco:", nameField,
                    "Tamaño máximo:", sizeField, unitComboBox
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Nuevo disco",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    IconAdapter.getScaledIcon(40, 40, IconAdapter.DRIVE_ICON));

            if (option == JOptionPane.OK_OPTION) {
                if (isValidName(nameField.getText())) {
                    name = nameField.getText();
                }

                String sizeText = sizeField.getText();

                if (!sizeText.isEmpty()) {
                    FileSize.Unit unit = (FileSize.Unit) unitComboBox.getSelectedItem();
                    try {
                        double size = Double.parseDouble(sizeText);
                        long sizeInBytes = FileSize.convertToBytes(size, unit);
                        return new VirtualDrive(name, LocalDateTime.now(), sizeInBytes);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "El tamaño debe ser un número.");
                    }
                } else {
                    return new VirtualDrive(name, LocalDateTime.now(), VirtualDrive.DEFAULT_SIZE);
                }
            } else {
                return null;
            }
        }

        return new VirtualDrive(name, LocalDateTime.now(), VirtualDrive.DEFAULT_SIZE);
    }

    public static FileFolder createNewFolder() {
        JTextField nameField = new JTextField();
        Object[] message = {
                "Nombre de la carpeta:", nameField
        };

        String name = "Nueva Carpeta";

        int option = JOptionPane.showConfirmDialog(null, message, "Nueva carpeta",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                IconAdapter.getScaledIcon(40, 40, IconAdapter.EMPTY_FOLDER_ICON));
        if (option == JOptionPane.OK_OPTION) {
            if (isValidName(nameField.getText())) {
                name = nameField.getText();
            }
        } else {
            return null;
        }

        return new FileFolder(name, LocalDateTime.now());
    }

    public static TextFile createNewFile() {
        JTextField nameField = new JTextField();
        Object[] message = {
                "Nombre del archivo:", nameField
        };

        String name = "Nuevo Archivo";

        int option = JOptionPane.showConfirmDialog(null, message, "Nuevo archivo",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                IconAdapter.getScaledIcon(40, 40, IconAdapter.FILE_ICON));
        if (option == JOptionPane.OK_OPTION) {
            if (isValidName(nameField.getText())) {
                name = nameField.getText();
            }
        } else {
            return null;
        }

        return new TextFile(name, LocalDateTime.now());
    }

    public static String renameElement(Element element) {
        String oldName = element.getName();
        JTextField nameField = new JTextField(oldName);
        Object[] message = {
                "Nuevo nombre:", nameField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Renombrar elemento",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                IconAdapter.getScaledIcon(40, 40, element.getIcon()));
        if (option == JOptionPane.OK_OPTION) {
            if (isValidName(nameField.getText())) {
                return nameField.getText();
            }
        }

        return oldName;
    }

    private static boolean isValidName(String name) {
        return name != null && !name.isEmpty() && VALID_NAME_PATTERN.matcher(name).matches();
    }
}

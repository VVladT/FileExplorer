package pe.edu.utp.aed.fileexplorer.util;

import pe.edu.utp.aed.fileexplorer.model.TextFile;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TextEditor {

    public void openFile(TextFile textFile) {

        JFrame frame = new JFrame("Editor de texto - Archivo: " + textFile.getName());

        JTextArea textArea = new JTextArea(textFile.getContent().toString());
        textArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JLabel statusLabel = new JLabel("No hay cambios pendientes");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        textPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JLabel characterCountLabel = new JLabel("Caracteres: " + textArea.getText().length());
        characterCountLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCharacterCount();
                setStatusModified();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCharacterCount();
                setStatusModified();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCharacterCount();
                setStatusModified();
            }

            private void updateCharacterCount() {
                SwingUtilities.invokeLater(() -> {
                    characterCountLabel.setText("Caracteres: " + textArea.getText().length());
                });
            }

            private void setStatusModified() {
                statusLabel.setText("Texto modificado sin guardar");
            }
        });

        JButton saveButton = new JButton("Guardar");
        JButton exitButton = new JButton("Salir");
        JButton saveAndExitButton = new JButton("Guardar y Salir");
        JButton exitWithoutSaveButton = new JButton("Salir sin Guardar");
        JButton exportButton = new JButton("Exportar");

        saveButton.addActionListener(e -> {
            textFile.getContent().replace(0, textFile.getContent().length(), textArea.getText());
            textFile.updateSize();
            statusLabel.setText("No hay cambios pendientes");
            JOptionPane.showMessageDialog(frame, "El archivo se ha guardado correctamente.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
        });

        exitButton.addActionListener(e -> frame.dispose());

        saveAndExitButton.addActionListener(e -> {
            textFile.getContent().replace(0, textFile.getContent().length(), textArea.getText());
            textFile.updateSize();
            statusLabel.setText("No hay cambios pendientes");
            JOptionPane.showMessageDialog(frame, "El archivo se ha guardado correctamente.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        });

        exitWithoutSaveButton.addActionListener(e -> {
            frame.dispose();
        });

        exportButton.addActionListener(e -> {

            String userHome = System.getProperty("user.home");
            File desktop = new File(userHome, "Desktop");

            JFileChooser fileChooser = new JFileChooser(desktop);
            fileChooser.setDialogTitle("Exportar como .txt");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));

            String defaultFileName = textFile.getName();
            if (!defaultFileName.toLowerCase().endsWith(".txt")) {
                defaultFileName += ".txt";
            }
            fileChooser.setSelectedFile(new File(desktop, defaultFileName));

            int userSelection = fileChooser.showSaveDialog(frame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();

                if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                    fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".txt");
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                    writer.write(textArea.getText());

                    String message = String.format("Archivo exportado exitosamente.\n\nUbicación: %s\nNombre del archivo: %s\n",
                            fileToSave.getParent(), fileToSave.getName());
                    JOptionPane.showMessageDialog(frame, message, "Exportar", JOptionPane.INFORMATION_MESSAGE);

                    textFile.getContent().replace(0, textFile.getContent().length(), textArea.getText());
                    statusLabel.setText("No hay cambios pendientes");
                    JOptionPane.showMessageDialog(frame, "El archivo se ha guardado correctamente.", "Guardado", JOptionPane.INFORMATION_MESSAGE);

                    if (Desktop.isDesktopSupported()) {
                        Desktop escritorio = Desktop.getDesktop();
                        if (escritorio.isSupported(Desktop.Action.OPEN)) {
                            escritorio.open(fileToSave);
                        } else {
                            JOptionPane.showMessageDialog(frame, "No se puede abrir el archivo automáticamente en este sistema.",
                                    "Aviso", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "No se puede abrir el archivo automáticamente en este sistema.",
                                "Aviso", JOptionPane.WARNING_MESSAGE);
                    }

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error al exportar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(saveAndExitButton);
        buttonPanel.add(exitWithoutSaveButton);
        buttonPanel.add(exportButton);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(characterCountLabel, BorderLayout.EAST);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(textPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(statusPanel, BorderLayout.NORTH);

        frame.add(panel);
        frame.setSize(600, 400);
        frame.setMinimumSize(new Dimension(600, 400));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> {
            textArea.setCaretPosition(textArea.getDocument().getLength());
            textArea.requestFocusInWindow();
        });
    }
}
package pe.edu.utp.aed.fileexplorer.debug;

import pe.edu.utp.aed.fileexplorer.controller.ElementController;
import pe.edu.utp.aed.fileexplorer.model.*;

import java.time.LocalDateTime;

public class TestTree {
  /*  public static void main(String[] args) {
        VirtualFileSystem vfs = new VirtualFileSystem();

        VirtualDrive diskA = new VirtualDrive("A", LocalDateTime.now(), 2000);
        VirtualDrive diskB = new VirtualDrive("B", LocalDateTime.now(), 2000);

        vfs.addDrive(diskA);
        vfs.addDrive(diskB);

        Directory folder1 = new Folder("Folder 1", LocalDateTime.now());
        Directory folder2 = new Folder("Folder 2", LocalDateTime.now());
        Directory folder3 = new Folder("Folder 1", LocalDateTime.now());

        folder1.addChild(folder3);
        diskA.addChild(folder1);
        diskB.addChild(folder2);

        Element file1 = new File("oal.txt",LocalDateTime.now());
        file1.setSize(15);
        Element file2 = new File("oal2.txt",LocalDateTime.now());
        Element file3 = new File("oal3.txt",LocalDateTime.now());
        Directory anotherFolder = new Folder("Another Folder",LocalDateTime.now());
        Shortcut shortcut = new Shortcut(LocalDateTime.now(), diskA);

        folder1.addChild(file1);
        folder1.addChild(file2);

        folder2.addChild(file3);
        file3.setSize(10);
        folder2.addChild(anotherFolder);
        folder2.addChild(shortcut);

        System.out.println("\n PRUEBA DE RECORRIDO");

        for (Element disk : vfs.getDrives()) {
            recursive(disk, "-");
        }

        if (shortcut.getTarget().equals(vfs.getDrive("A"))) {
            System.out.println("Los accesos directos funcionan correctamente");
        }

        System.out.println("\n PRUEBA DE COMANDOS");

        ElementController ec = new ElementController(null);

        ec.copyElement(file3);
        ec.paste(folder1);
        ec.paste(folder1);
        file2.setSize(155);
        ec.paste(folder1);

        ec.cutElement(folder1, file2);
        ec.paste(anotherFolder);

        ec.deleteElement(folder2, shortcut);

        for (Element disk : vfs.getDrives()) {
            recursive(disk, "-");
        }

        System.out.println("\nPrueba de búsqueda");
        System.out.println(vfs.search("oal3.txt"));
        System.out.println("\nBúsqueda por ruta");
        System.out.println(vfs.getElementByPath("A/Folder 1/oal3.txt (1)"));
    } */

    public static void recursive(Element element, String indentation) {
        System.out.println(indentation + element.getName() + ": " + element.getType() +
                ":" + element.getSize());

        if (element.isDirectory()) {
            Directory dir = (Directory) element;
            for (Element els : dir.getChildren()) {
                recursive(els, indentation + "--");
            }
        }
    }
}

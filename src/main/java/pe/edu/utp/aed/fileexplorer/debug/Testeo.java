package pe.edu.utp.aed.fileexplorer.debug;

import pe.edu.utp.aed.fileexplorer.controller.ElementController;
import pe.edu.utp.aed.fileexplorer.model.*;
import pe.edu.utp.aed.fileexplorer.util.FileSize;
import pe.edu.utp.aed.fileexplorer.util.SavFileManager;
import pe.edu.utp.aed.fileexplorer.util.SavGenerator;
import pe.edu.utp.aed.fileexplorer.view.MainView;
import pe.edu.utp.aed.fileexplorer.view.events.KeyboardHandler;
import xyz.cupscoffee.files.api.SavStructure;

import java.time.LocalDateTime;

public class Testeo {
    public static void main(String[] args) {
        VirtualFileSystem vfs = new VirtualFileSystem();
        RootDirectory root = RootDirectory.getInstance();
        VirtualDrive mainDrive = new VirtualDrive("Disco", LocalDateTime.now(), FileSize.TB);

        root.addChild(mainDrive);

        Directory mainFolder = new FileFolder("Main Folder", LocalDateTime.now());

        FileFolder folder1 = new FileFolder("Folder 01", LocalDateTime.of(2000,3,12, 4,0));
        FileFolder folder2 = new FileFolder("Folder 02", LocalDateTime.now());
        FileFolder folder3 = new FileFolder("Folder 03", LocalDateTime.now());
        FileFolder folder4 = new FileFolder("Folder 04", LocalDateTime.now());
        FileFolder folder5 = new FileFolder("Folder 05", LocalDateTime.now());
        FileFolder folder6 = new FileFolder("Folder 06", LocalDateTime.of(2000,3,12, 4,0));
        FileFolder folder7 = new FileFolder("Folder 07", LocalDateTime.now());
        FileFolder folder8 = new FileFolder("Folder 08", LocalDateTime.now());
        FileFolder folder9 = new FileFolder("Folder 09", LocalDateTime.now());
        FileFolder folder10 = new FileFolder("Folder10", LocalDateTime.now());

        mainFolder.addChild(folder1);
        mainFolder.addChild(folder2);
        mainFolder.addChild(folder3);
        mainFolder.addChild(folder4);
        mainFolder.addChild(folder5);
        mainFolder.addChild(folder6);
        mainFolder.addChild(folder7);
        mainFolder.addChild(folder8);
        mainFolder.addChild(folder9);
        mainFolder.addChild(folder10);

        TextFile file = new TextFile("Texto", LocalDateTime.now());
        file.getContent().append("Hola amigos de youtube\n\n\nEn este video ...");
        file.updateSize();

        mainFolder.addChild(file);

        mainDrive.addChild(mainFolder);

        ElementController elementController = new ElementController(vfs);

        elementController.startApplication();

        elementController.addElementToQuickAccess(folder6);
        elementController.addElementToQuickAccess(folder8);

        MainView mv = new MainView(elementController);
        mv.setFocusable(true);
        mv.addKeyListener(new KeyboardHandler(elementController));

        SavStructure sav = SavFileManager.parseSystemToSavStructure(elementController.getVirtualFileSystem());
        SavGenerator.writeSavToFile(sav,"");
    }

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

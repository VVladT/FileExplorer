package pe.edu.utp.aed.fileexplorer.util;

import pe.edu.utp.aed.fileexplorer.model.*;
import xyz.cupscoffee.files.api.Disk;
import xyz.cupscoffee.files.api.File;
import xyz.cupscoffee.files.api.Folder;
import xyz.cupscoffee.files.api.SavStructure;
import xyz.cupscoffee.files.api.implementation.SimpleDisk;
import xyz.cupscoffee.files.api.implementation.SimpleFile;
import xyz.cupscoffee.files.api.implementation.SimpleFolder;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavStructureManager {

    public static SavStructure parseSystemToSavStructure(VirtualFileSystem vfs) {
        Disk[] disks = new SimpleDisk[vfs.getDrives().size()];
        List<Element> drives = vfs.getDrives();
        for (int i = 0; i < drives.size(); i++) {
            VirtualDrive vDrive = (VirtualDrive) drives.get(i);
            disks[i] = parseDisk(vDrive);
        }
        Map<String, String> quickAccessMetadata = parseQuickAccess(vfs.getQuickAccess());

        return new VFSSavStructure("VFileSystem", disks, quickAccessMetadata);
    }

    private static Disk parseDisk(VirtualDrive drive) {
        Folder root = parseFolder(drive);
        return new SimpleDisk(drive.getName(), root, drive.getTotalSpace(), new HashMap<>());
    }

    private static Folder parseFolder(Directory directory) {
        List<File> files = new ArrayList<>();
        List<Folder> folders = new ArrayList<>();
        if (!directory.isEmpty()) {
            separateFoldersAndFiles(directory, files, folders);
        }

        return new SimpleFolder(directory.getName(), files, folders, directory.getCreationDate(),
                directory.getModificationDate(), Paths.get(directory.getPath()), new HashMap<>());
    }

    private static File parseFile(TextFile textFile) {
        String str = textFile.getContent().toString();
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes(Charset.forName("UTF-8")));

        return new SimpleFile(textFile.getName(), buffer, textFile.getCreationDate(),
                textFile.getModificationDate(), Paths.get(textFile.getPath()), new HashMap<>());
    }

    private static void separateFoldersAndFiles(Directory directory, List<File> files, List<Folder> folders) {
        for (Element child : directory.getChildren()) {
            if (child.isDirectory()) {
                folders.add(parseFolder((Directory) child));
            } else {
                files.add(parseFile((TextFile) child));
            }
        }
    }

    private static Map<String, String> parseQuickAccess(QuickAccess quickAccess) {
        Map<String, String> metadata = new HashMap<>(1);
        StringBuilder sb = new StringBuilder();

        List<Element> elements = quickAccess.getElements();
        for (int i = 0, elementsSize = elements.size(); i < elementsSize; i++) {
            Element element = elements.get(i);
            sb.append(element.getPath());
            if (i < elementsSize - 1) {
                sb.append("\n");
            }
        }

        metadata.put("quickAccess", sb.toString());

        return metadata;
    }

    public static void parseSavStructureToSystem(SavStructure savStructure) {
        RootDirectory.clear();
        RootDirectory root = RootDirectory.getInstance();

        for (Disk disk : savStructure.getDisks()) {
            VirtualDrive vDrive = parseDiskToDrive(disk);
            root.addChild(vDrive);
        }
        QuickAccess quickAccess = parseQuickAccess(savStructure.getMetadata());

        new VirtualFileSystem(quickAccess);
    }

    private static VirtualDrive parseDiskToDrive(Disk disk) {
        Directory root = parseFolderToDirectory(disk.getRootFolder());
        return new VirtualDrive(root.getName(), root.getCreationDate(), root.getModificationDate(),
                root.getSize(), disk.getLimitSize(), root.getChildren());
    }

    private static Directory parseFolderToDirectory(Folder folder) {
        List<Element> children = new ArrayList<>();
        for (Folder subFolder : folder.getFolders()) {
            children.add(parseFolderToDirectory(subFolder));
        }
        for (File file : folder.getFiles()) {
            children.add(parseFileToTextFile(file));
        }
        return new FileFolder(folder.getName(), folder.getCreatedDateTime(), folder.getLastModifiedDateTime(),
                folder.getSize(), children);
    }

    private static TextFile parseFileToTextFile(File file) {
        ByteBuffer buffer = file.getContent();
        byte[] contentBytes = new byte[buffer.remaining()];
        buffer.get(contentBytes);
        String content = new String(contentBytes, Charset.forName("UTF-8"));

        return new TextFile(file.getName(), file.getCreatedDateTime(), file.getLastModifiedDateTime(),
                file.getSize(), content);
    }

    private static QuickAccess parseQuickAccess(Map<String, String> quickAccessMetadata) {
        List<Element> elements = new ArrayList<>();

        String paths = quickAccessMetadata.get("quickAccess");
        for (String path : paths.split("\n")) {
            if (!path.isEmpty()) {
                elements.add(VirtualFileSystem.getElementByPath(RootDirectory.getInstance(), path));
            }
        }

        return new QuickAccess(elements);
    }

    private static class VFSSavStructure implements SavStructure {
        private String header;
        private Disk[] disks;
        private Map<String, String> metadata;

        public VFSSavStructure(String header, Disk[] disks, Map<String, String> metadata) {
            this.header = header;
            this.disks = disks;
            this.metadata = metadata;
        }

        @Override
        public String getHeader() {
            return header;
        }

        @Override
        public Disk[] getDisks() {
            return disks;
        }

        @Override
        public Map<String, String> getMetadata() {
            return metadata;
        }
    }
}

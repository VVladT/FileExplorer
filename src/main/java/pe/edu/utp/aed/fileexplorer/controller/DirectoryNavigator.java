package pe.edu.utp.aed.fileexplorer.controller;

import pe.edu.utp.aed.fileexplorer.model.Directory;
import pe.edu.utp.aed.fileexplorer.util.datastructures.SimpleStack;

public class DirectoryNavigator {
    private SimpleStack<Directory> back;
    private SimpleStack<Directory> next;

    public DirectoryNavigator() {
        back = new SimpleStack<>();
        next = new SimpleStack<>();
    }

    public void addDirectoryToBack(Directory currentDir) {
        back.push(currentDir);
        next.clear();
    }

    public Directory back(Directory currentDir) {
        next.push(currentDir);
        return back.pop();
    }

    public Directory next(Directory currentDir) {
        back.push(currentDir);
        return next.pop();
    }

    public boolean hasBack() {
        return !back.isEmpty();
    }

    public boolean hasNext() {
        return !next.isEmpty();
    }

    public boolean contains(Directory directory) {
        return back.contains(directory) || next.contains(directory);
    }

    public void clear() {
        back.clear();
        next.clear();
    }
}

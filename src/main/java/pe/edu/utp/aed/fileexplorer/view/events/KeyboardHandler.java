package pe.edu.utp.aed.fileexplorer.view.events;

import pe.edu.utp.aed.fileexplorer.controller.ElementController;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardHandler extends KeyAdapter {
    private final ElementController controller;
    private boolean ctrlPressed;
    private boolean shiftPressed;

    public KeyboardHandler(ElementController controller) {
        this.controller = controller;
        ctrlPressed = false;
        shiftPressed = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_CONTROL:
                ctrlPressed = true;
                break;
            case KeyEvent.VK_SHIFT:
                shiftPressed = true;
                break;
            case KeyEvent.VK_DELETE:
                controller.deleteElement();
                break;
            case KeyEvent.VK_ENTER:
                controller.openElement();
                break;
        }

        if (ctrlPressed) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_X:
                    controller.cutElement();
                    break;
                case KeyEvent.VK_C:
                    controller.copyElement();
                    break;
                case KeyEvent.VK_V:
                    controller.pasteElement();
                    break;
            }

            if (shiftPressed) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_N:
                        controller.createNewFolder();
                        break;
                    case KeyEvent.VK_D:
                        controller.createNewDrive();
                        break;
                    case KeyEvent.VK_F:
                        controller.createNewFile();
                        break;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            ctrlPressed = false;
        }
    }
}

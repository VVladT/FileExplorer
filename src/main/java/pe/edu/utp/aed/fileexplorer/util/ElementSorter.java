package pe.edu.utp.aed.fileexplorer.util;

import pe.edu.utp.aed.fileexplorer.model.Element;
import pe.edu.utp.aed.fileexplorer.model.VirtualDrive;

import java.util.Comparator;
import java.util.List;

public class ElementSorter {

    public static void sortByName(List<Element> elements) {
        sortByName(elements, true);
    }

    public static void sortByCreationDate(List<Element> elements) {
        sortByCreationDate(elements, true);
    }

    public static void sortByModificationDate(List<Element> elements) {
        sortByModificationDate(elements, true);
    }

    public static void sortByType(List<Element> elements) {
        sortByType(elements, true);
    }

    public static void sortBySize(List<Element> elements) {
        sortBySize(elements, true);
    }

    public static void sortByAvailableSpace(List<VirtualDrive> drives) {
        sortByAvailableSpace(drives, true);
    }

    public static void sortByName(List<Element> elements, boolean asc) {
        if (asc) {
            elements.sort(Comparator.comparing(Element::getName));
        } else {
            elements.sort(Comparator.comparing(Element::getName).reversed());
        }
    }

    public static void sortByCreationDate(List<Element> elements, boolean asc) {
        if (asc) {
            elements.sort(Comparator.comparing(Element::getCreationDate));
        } else {
            elements.sort(Comparator.comparing(Element::getCreationDate).reversed());
        }
    }

    public static void sortByModificationDate(List<Element> elements, boolean asc) {
        if (asc) {
            elements.sort(Comparator.comparing(Element::getModificationDate));
        } else {
            elements.sort(Comparator.comparing(Element::getModificationDate).reversed());
        }
    }

    public static void sortByType(List<Element> elements, boolean asc) {
        if (asc) {
            elements.sort(Comparator.comparing(Element::getType));
        } else {
            elements.sort(Comparator.comparing(Element::getType).reversed());
        }
    }

    public static void sortBySize(List<Element> elements, boolean asc) {
        if (asc) {
            elements.sort(Comparator.comparing(Element::getSize));
        } else {
            elements.sort(Comparator.comparing(Element::getSize).reversed());
        }
    }

    public static void sortByAvailableSpace(List<VirtualDrive> drives, boolean asc) {
        if (asc) {
            drives.sort(Comparator.comparing(VirtualDrive::getAvailableSpace));
        } else {
            drives.sort(Comparator.comparing(VirtualDrive::getAvailableSpace).reversed());
        }
    }
}

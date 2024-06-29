package pe.edu.utp.aed.fileexplorer.util;

import javax.swing.*;
import java.text.DecimalFormat;

public class FileSize {
    public static final long BYTES = 8;
    public static final long KB = 1024 * BYTES;
    public static final long MB = 1024 * KB;
    public static final long GB = 1024 * MB;
    public static final long TB = 1024 * GB;

    public enum Unit {
        BYTES("Bytes", FileSize.BYTES),
        KB("KB", FileSize.KB),
        MB("MB", FileSize.MB),
        GB("GB", FileSize.GB),
        TB("TB", FileSize.TB);

        private final String name;
        private final long multiplier;

        Unit(String name, long multiplier) {
            this.name = name;
            this.multiplier = multiplier;
        }

        public long getMultiplier() {
            return multiplier;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static long convertToBytes(double size, Unit unit) {
        return (long) (size * unit.getMultiplier());
    }

    public static JComboBox<Unit> createUnitComboBox() {
        JComboBox<Unit> comboBox = new JComboBox<>(Unit.values());
        comboBox.setSelectedItem(Unit.GB);
        return comboBox;
    }

    public static String formatSize(long size) {
        if (size < KB) {
            return size + " " + Unit.BYTES;
        } else if (size < MB) {
            return format(size, KB, Unit.KB);
        } else if (size < GB) {
            return format(size, MB, Unit.MB);
        } else if (size < TB) {
            return format(size, GB, Unit.GB);
        } else {
            return format(size, TB, Unit.TB);
        }
    }

    private static String format(long size, long unitSize, Unit unit) {
        double result = (double) size / unitSize;
        DecimalFormat formatter = new DecimalFormat("#.##");
        return formatter.format(result) + " " + unit;
    }
}

package xyz.cupscoffee.files.api.implementation;

import xyz.cupscoffee.files.api.Disk;
import xyz.cupscoffee.files.api.SavStructure;

import java.util.Map;

public class VFSSavStructure implements SavStructure {
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

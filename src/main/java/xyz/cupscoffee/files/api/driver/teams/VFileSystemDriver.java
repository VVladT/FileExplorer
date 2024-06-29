package xyz.cupscoffee.files.api.driver.teams;

import xyz.cupscoffee.files.api.SavStructure;
import xyz.cupscoffee.files.api.driver.SavDriver;
import xyz.cupscoffee.files.api.exception.InvalidFormatFileException;

import java.io.FileInputStream;

public class VFileSystemDriver implements SavDriver {
    @Override
    public SavStructure readSavFile(FileInputStream fileInputStream) throws InvalidFormatFileException {

        return null;
    }
}

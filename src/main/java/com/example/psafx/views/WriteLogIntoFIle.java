package com.example.psafx.views;

import com.example.psafx.system.ComplexManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class WriteLogIntoFIle {

    public static void main(String[] args) {
        nOperationScript(100);
    }
    public static void beforeDenyScript(){
        ComplexManager complexManager = new ComplexManager(3, 3, 3);
        String message = complexManager.iterateString();

        try(FileWriter fileWriter = new FileWriter(new File("src/main/resources/log_file.txt"))) {
            while (!Objects.equals(message.split(" ")[0], "DENY")) {
                fileWriter.write(message);
                fileWriter.write("\n");
                message = complexManager.iterateString();
            }
        }
        catch (IOException ioException){

        }
    }

    public static void nOperationScript(int n){
        ComplexManager complexManager = new ComplexManager(3, 3, 3);
        String message = complexManager.iterateString();

        try(FileWriter fileWriter = new FileWriter(new File("src/main/resources/log_file.txt"))) {
            for (int i = 0; i < n; i++) {
                fileWriter.write(message);
                fileWriter.write("\n");
                message = complexManager.iterateString();
            }
        }
        catch (IOException ioException){

        }
    }
}

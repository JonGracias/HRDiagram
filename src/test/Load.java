package test;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Load extends JOptionPane {

    ArrayList<String> fileNames = new ArrayList<>();

    public void setAllFileNames(String fileName){
        File file = new File(fileName);
        String absolutePath = file.getAbsolutePath();
    }

    public StringBuilder read(String filename) throws IOException {
        StringBuilder result = new StringBuilder();
        // Filename for output
        File file = new File(filename);
        String absolutePath = file.getAbsolutePath();
        if (file.exists()) {
            Scanner fileIn = new Scanner(file);
            if (fileIn.hasNext())
                while (fileIn.hasNext()) {
                    result.append(fileIn.nextLine());
                    result.append("\n");
                }
            fileIn.close();
            return result;
        } else
            showMessageDialog(null, "Error File not found.");
        return result;
    }
}

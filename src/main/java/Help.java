package main.java;// **********************************************************************************
// Title: main
// Author: Jonathan Gracias Perez
// Course Section: CMIS201-ONL1 (Seidel) Fall 2021
// File: main.java
// Description: Splash Page Code.
// **********************************************************************************

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Help extends JOptionPane {
    private final String[] menu;
    protected File file = new File("src/main/resources/TextFile/EjnarHertzsprung.txt");
    protected File file1 = new File("src/main/resources/TextFile/HenryNorrisRussel.txt");
    protected File file2 = new File("src/main/resources/TextFile/howTo.txt");
    protected File file3 = new File("src/main/resources/TextFile/glossary.txt");
    protected File file4 = new File("src/main/resources/TextFile/hrhistory.txt");
    protected File file5 = new File("src/main/resources/TextFile/together.txt");



    public Help(String[] menu) throws IOException {
        this.menu = menu;
        getMenu();
    }

    public static StringBuilder read(String filename) throws IOException {
        StringBuilder result = new StringBuilder();
        // Filename for output
        File fileObj = new File(filename);
        if (fileObj.exists()) {
            Scanner fileIn = new Scanner(fileObj);
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

    public void getMenu() throws IOException {
        try {
            String selected = "";
            do {
                selected = (String) showInputDialog(null, toString(), "Welcome Message", JOptionPane.PLAIN_MESSAGE,
                        null, menu, menu[0]);
                switch (selected) {
                    case "How to":
                        showMessageDialog(null, read(file2.getAbsolutePath()));
                        break;
                    case "Spectral Class":
                        //option2
                        break;
                    case "Absolute Magnitude":
                        //Option3
                        break;
                    case "History":
                        new main.java.History(new String[]{"H-R Diagram History", "Ejnar Hertzsprung", "Henry Norris Russel", "H-R Diagram Comes together"});
                        break;
                    case "Glossary":
                        showMessageDialog(null, read(file3.getAbsolutePath()));
                        break;
                    case "H-R Diagram History":
                        showMessageDialog(null, read(file4.getAbsolutePath()));
                        break;
                    case "Ejnar Hertzsprung":
                        showMessageDialog(null, read(file.getAbsolutePath()));
                        break;
                    case "Henry Norris Russel":
                        showMessageDialog(null, read(file1.getAbsolutePath()));
                        break;
                    case "H-R Diagram Comes together":
                        showMessageDialog(null, read(file5.getAbsolutePath()));
                        break;
                }
            } while (!selected.equals(menu[menu.length - 1]));
        } catch (NullPointerException ignored) {

        }

    }

    //Help text
    public String toString() {
        // Write a well formatted, complete description of this midterm program that
        String output = "Title: HR Diagram\n";
        output += "Author: Jonathan Gracias Perez\n";
        output += "Course Section: CMIS201-ONL1 (Seidel) Fall 2021";
        output += "Assignment: Major Assignment\n";
        output += "******************************\n";
        output += "This is H-R diagram learning tool\n";
        output += "First add a new tab\n";
        output += "Second add spectral class and\n";
        output += "Absolute Magnitude\n";
        output += "Hit add star button\n";
        output += "More information is available below.\n";

        return output;

    }

}



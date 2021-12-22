package main.java;

import java.io.IOException;

public class History extends main.java.Help {

    public History(String[] menu) throws IOException {
        super(menu);
        getMenu();
    }

    public String toString() {
        // Write a well formatted, complete description of this midterm program that
        String output = "This section offers some\n";
        output += "historical information about the \n";
        output += "Hertzsprung-Russel Diagram";
        output += "Please select an option below\n";

        return output;

    }
}

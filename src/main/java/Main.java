package main.java;// **********************************************************************************
// Title: SplashPage
// Author: Jonathan Gracias Perez
// Course Section: CMIS201-ONL1 (Seidel) Fall 2021
// File: SplashPage.java
// Description: main().
// **********************************************************************************

import javax.swing.*;
import java.io.IOException;

// Main method
public class Main {
    //private static final MenuPanel menu = new MenuPanel();
    private static final main.java.MyFrame frame = new main.java.MyFrame();
    private static final JScrollPane scrollPane = frame.getScrollPane();


    public static void main(String[] args) throws IOException {


        // Adding everything to JFrame
        frame.setFrame(frame);
        frame.add(frame.menu);
        frame.add(frame.tabbedPane);
        frame.add(scrollPane);
        frame.add(frame.graph);
        frame.add(frame.spectral);
        frame.add(frame.magnitude);
        frame.add(frame.starGroup);
        scrollPane.setViewportView(frame.tabbedPane);

        frame.validate();
        frame.repaint();
        new main.java.Help(new String[]{"How to", "Spectral Class", "Absolute Magnitude", "History", "Glossary"});
    }


}
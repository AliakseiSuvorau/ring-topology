package org.example;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        RingProcessor processor = new RingProcessor(10, 3, new File("logs/logPath"));

        processor.startProcessing();
    }
}

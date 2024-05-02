package org.example;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class LoggingVisitor {
    private final Logger logger;
    long timeBegin = System.nanoTime();

    LoggingVisitor() {
        logger = Logger.getLogger("ringLogger");
    }

    public final void setLogFile(File logs) {
        try {
            Handler fileHandler = new FileHandler(logs.getAbsolutePath());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.out.println("Could not  open log file!");
        }
    }

    public final void logStartRing(int nodesAmount, int dataAmount, int coordinatorId) {

        logger.info("[" + (System.nanoTime() - timeBegin) +" ns]\t\tRing processor was created with " + nodesAmount + " nodes.\n" +
                "Information packages in each node: " + dataAmount + ".\n" +
                "Coordinator index: " + coordinatorId + ".\n");
    }

    public final void logPackageTransfer(int fromNodeId, int toNodeId, int dataPackageId, int destinationNodeId) {
        logger.info("[" + (System.nanoTime() - timeBegin) + " ns]\t\tPackage " + dataPackageId +
                " with destination node " + destinationNodeId +
                " was transferred from node " + fromNodeId +
                " to node " + toNodeId + ".\n");
    }

    public final void logAverageNetworkDelay(long avgDelay) {
        logger.info("\nAverage network delay time: " + avgDelay + ".\n");
    }

    public final void logAverageBufferDelay(long avgDelay) {
        logger.info("\nAverage buffer delay time: " + avgDelay + ".\n");
    }

    public final void logPackageCollection(int packageId) {
        logger.info("[" + (System.nanoTime() - timeBegin) + " ns]\t\tPackage " + packageId + " was collected.\n");
    }
}

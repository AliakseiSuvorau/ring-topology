package org.example;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class RingProcessor {
    private final int nodesAmount;
    private final int dataAmount;
    private static final LoggingVisitor loggingVisitor = new LoggingVisitor();
    private List<Node> nodeList;
    private int dataCreated = 0;
    private int coordinationNodeId;
    List<Long> timeList;
    private AtomicInteger finishedPackages = new AtomicInteger(0);

    RingProcessor(int nodesAmount, int dataAmount, File logs){
        this.nodesAmount = nodesAmount;
        this.dataAmount = dataAmount;
        timeList = new ArrayList<>();
        loggingVisitor.setLogFile(logs);
        init();
        loggingVisitor.logStartRing(nodesAmount, dataAmount, coordinationNodeId);
    }

    private long averageNetworkTime() {
        long totalTime = 0;
        for (Long time : timeList) {
            totalTime += time;
        }
        return totalTime / timeList.size();
    }

    private long averageBufferTime() {
        long totalTime = 0;
        for (DataPackage dataPackage : nodeList.get(coordinationNodeId).allData) {
            totalTime += dataPackage.getBufferTime();
        }
        return  totalTime / dataCreated;
    }

    private DataPackage createDataPackage(int nodeNum, int dataPackageNum) {
        int destinationNodeId = ThreadLocalRandom.current().nextInt(nodesAmount);
        String data = "Node: " + nodeNum + " , data package: " + dataPackageNum + "\n";
        ++dataCreated;
        return new DataPackage(destinationNodeId, data, dataCreated - 1);
    }

    private Node createNode(int index) {
        Node newNode = new Node(index, coordinationNodeId);
        for (int i = 0; i < dataAmount; ++i) {
            newNode.addData(createDataPackage(index, i));
        }
        newNode.setRingProcessor(this);
        return newNode;
    }

    private void init() {
        coordinationNodeId = ThreadLocalRandom.current().nextInt(nodesAmount);
        nodeList = new ArrayList<>();
        for (int i = 0; i < nodesAmount; ++i) {
            nodeList.add(createNode(i));
        }
        for (int i = 0; i < nodeList.size(); ++i) {
            nodeList.get(i).setNextNode(nodeList.get((i + 1) % nodesAmount));
            nodeList.get(i).setCoordinatorNode(nodeList.get(coordinationNodeId));
        }
    }

    public void startProcessing() {
        ExecutorService pool = Executors.newFixedThreadPool(nodesAmount);
        for (Node node : nodeList) {
            pool.execute(node);
        }
        pool.shutdown();
        handleProcessing();
    }

    public void handleProcessing() {
        while (finishedPackages.get() != dataCreated) {}
        for (Node node : nodeList) {
            node.isFinished = true;
        }
        loggingVisitor.logAverageNetworkDelay(averageNetworkTime());
        loggingVisitor.logAverageBufferDelay(averageBufferTime());
    }

    public final void addTime(long time) {
        timeList.add(time);
    }

    public LoggingVisitor getLogger() {
        return loggingVisitor;
    }

    public final void increaseFinishedPackages() {
        finishedPackages.incrementAndGet();
    }

    public final int getDataCreated() {
        return dataCreated;
    }

    public final int getFinishedPackages() {
        return finishedPackages.get();
    }
}

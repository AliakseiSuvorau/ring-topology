package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import static java.lang.Thread.sleep;

public class Node implements Runnable {
    private final int nodeId;
    private final int corId;
    private final ConcurrentLinkedQueue<DataPackage> bufferStack = new ConcurrentLinkedQueue<>();
    public List<DataPackage> allData = null;
    private Node nextNode = null;
    private Node coordinatorNode = null;
    private RingProcessor ringProcessor;
    Boolean isFinished = false;

    Node(int nodeId, int corId) {
        this.nodeId = nodeId;
        this.corId = corId;

        if (nodeId == corId)
            allData = new ArrayList<>();
    }

    public final void setRingProcessor(RingProcessor ringProcessor) {
        this.ringProcessor = ringProcessor;
    }

    public final void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    public final void setCoordinatorNode(Node coordinatorNode) {
        this.coordinatorNode = coordinatorNode;
    }

    public final int getId() {
        return nodeId;
    }

    public final void collectData(DataPackage dataPackage) {
        allData.add(dataPackage);
    }

    public final void addData(DataPackage dataPackage) {
        bufferStack.add(dataPackage);
    }

    private void sendToNextNode(DataPackage dataPackage) {
        nextNode.addData(dataPackage);
    }

    private void sendToCoordinationNode(DataPackage dataPackage) {
        long finishTime = System.nanoTime();
        ringProcessor.addTime(finishTime - dataPackage.getStartTime());
        coordinatorNode.collectData(dataPackage);
    }

    @Override
    public void run() {
        while (!isFinished) {
            DataPackage curPackage = bufferStack.poll();
            if (curPackage == null) {
                continue;
            }
            if (curPackage.getBufferPrevTime() != 0) {
                curPackage.addBufferTime();
            }
            try {
                sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("The thread was interrupted!");
            }
            if (curPackage.getDestinationNode() != nodeId) {
                long timeInBuffer = System.nanoTime();
                curPackage.setInBuffer(timeInBuffer);
                sendToNextNode(curPackage);
                ringProcessor.getLogger().logPackageTransfer(nodeId, nextNode.getId(), curPackage.getId(), curPackage.getDestinationNode());
            } else {
                sendToCoordinationNode(curPackage);
                ringProcessor.increaseFinishedPackages();
                ringProcessor.getLogger().logPackageTransfer(nodeId, corId, curPackage.getId(), curPackage.getDestinationNode());
                ringProcessor.getLogger().logPackageCollection(curPackage.getId());
            }
        }
    }
}


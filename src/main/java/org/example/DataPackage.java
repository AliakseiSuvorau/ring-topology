package org.example;

public class DataPackage {
    private final int destinationNode;
    private final int packageId;
    private final String data;
    private final long startTime;
    private long inBufferPrev = 0;
    private long totalBufferTime = 0;

    DataPackage(int destinationNode, String data, int packageId) {
        this.destinationNode = destinationNode;
        this.data = data;
        this.packageId = packageId;
        startTime = System.nanoTime();
    }

    public int getDestinationNode() {
        return destinationNode;
    }

    public final void setInBuffer(long time) {
        inBufferPrev = time;
    }

    public final void addBufferTime() {
        long curTime = System.nanoTime();
        totalBufferTime += (curTime - inBufferPrev);
    }

    public long getStartTime() {
        return startTime;
    }

    public String getData(){
        return data;
    }

    public final long getBufferTime() {
        return totalBufferTime;
    }

    public final int getId() {
        return packageId;
    }

    public final long getBufferPrevTime() {
        return inBufferPrev;
    }
}

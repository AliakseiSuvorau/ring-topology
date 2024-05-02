package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RingProcessorTest {

    @Test
    @Order(1)
    void simpleOneDataOneNodeTest() {
        int nodes = 1;
        int dataPerNode = 1;
        RingProcessor processor = new RingProcessor(nodes, dataPerNode, new File("logs/simpleOneDataOneNodeTest"));
        processor.startProcessing();

        assertEquals(processor.getDataCreated(), nodes * dataPerNode);  // Needed number of packages was created
        assertEquals(processor.getDataCreated(), processor.getFinishedPackages());  // All of them were precessed
    }

    @Test
    @Order(2)
    void simpleOneDataManyNodesTest() {
        int nodes = 100;
        int dataPerNode = 1;
        RingProcessor processor = new RingProcessor(nodes, dataPerNode, new File("logs/simpleOneDataManyNodesTest"));
        processor.startProcessing();

        assertEquals(processor.getDataCreated(), nodes * dataPerNode);
        assertEquals(processor.getDataCreated(), processor.getFinishedPackages());
    }

    @Test
    @Order(3)
    void simpleManyDataOneNodeTest() {
        int nodes = 1;
        int dataPerNode = 100;
        RingProcessor processor = new RingProcessor(nodes, dataPerNode, new File("logs/simpleManyDataOneNodeTest"));
        processor.startProcessing();

        assertEquals(processor.getDataCreated(), nodes * dataPerNode);
        assertEquals(processor.getDataCreated(), processor.getFinishedPackages());
    }

    @Test
    @Order(4)
    void simpleManyDataManyNodeTest() {
        int nodes = 20;
        int dataPerNode = 100;
        RingProcessor processor = new RingProcessor(nodes, dataPerNode, new File("logs/simpleManyDataManyNodeTest"));
        processor.startProcessing();

        assertEquals(processor.getDataCreated(), nodes * dataPerNode);
        assertEquals(processor.getDataCreated(), processor.getFinishedPackages());
    }

    @Test
    @Order(5)
    void stressTest() {
        int nodes = 100;
        int dataPerNode = 100;
        RingProcessor processor = new RingProcessor(nodes, dataPerNode, new File("logs/stressTest"));
        processor.startProcessing();

        assertEquals(processor.getDataCreated(), nodes * dataPerNode);
        assertEquals(processor.getDataCreated(), processor.getFinishedPackages());
    }
}
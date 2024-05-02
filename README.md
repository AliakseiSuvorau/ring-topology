## Task
The `Nodes` are put in a ring (meaning each of them can transfer data to the next one). Each Node is given a number of `DataPackages`. The aim is to move the packages around the cycle until the reach their destination node. After that the destination node transffers the data package to the coordination node, which puts all the data packges.

## Tests
Each tests checks how many data packages were created and that all of them were processed.

Approximate testing time: 30 seconds.

## Logs format
* Creating the ring
* [time in nanoseconds]   Action
* Average network delay
* Average buffer delay

#BloomReach Load Tests

To run locally: 
1. `gradle build`
2. `gradle gatlingRun` (to run all tests) OR `gradle gatlingRun-basic.testName` to run a specific test, i.e. `gradle gatlingRun-basic.PeakLoad`.

CI Configuration - PeakLoad
This test is built in CI with Brand and Target Environment parameters. 
The threads and throughput are determined by these parameters. To configure these
options or to add a new brand or environment, you can modify threadThroughputMap
# Todo 

Examples
- log request and response, 
    - for entry 
        - using CapturedInputAndOutputs(X)
        - using log method(X)
     - and internal
- log database contents
    - see hydra YatspecEventRecorder
    - log data stored in givens
    - log data stored after test has finished
    - log data stored during test (hard)
- log file contents
- log folder contents
- log all the logs
    - get from docker image when runnig AT during build
- log jms or objects calls

- Assert on log output
   - it is in captured Inputs and outputs
   - split into memory(map/list) or file 
        - application, access, audit
        - info, debug, warn, error
   - assert on contains
   - add note, not really a AT, as lots of unnecessary details. But if a user of app wants it, then can have assertion on it
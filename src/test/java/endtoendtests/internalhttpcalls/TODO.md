# TODO

- Use of wiremock
    - use to prime http call (x)
    - show wiremock primings
    - wiremock use files see nick's examples
    - xml response 
        - DoNotLoadExternalDtdDocumentBuilderFactory
    - replace sequence diagram generator with rule see hydra AcceptanceTest 265
- Sequence diagrams 
    - single call (X)
    - multiple calls
        - hyd to different services
            - http://dummy.restapiexample.com/
            - https://jsonplaceholder.typicode.com/
        - omar multiple call to same service but diff paths
- Asserting on request created
    - using captured inputs and outputs
    - using wiremock to verify http://wiremock.org/docs/verifying/
        - WiremockContainer
- interesting givens 
    - add important parts of response primed in wiremock
- Format response body depending on the content type
    - Need to check if done automatically
    - not done in sequence diagram or interesting givens (as indented from left)
- jzombie instead of wiremock

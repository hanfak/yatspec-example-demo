Yatspec can be used to turn unit tests into documentation tests, 
test where the output is written in non technical language 
and can use BDD style language.

Yatspec will output a html file, so that tests can be accessed via a browser

As it is a unit test, can use junit, mockito and assertj or similar

Due to the nature of the naming of the private methods used, can get non technical 
poeple to write them. Or have a parser to transform english into yatspec friendly
method names, althoug I believe this is not necessary.

These tests can also be thought of as unit acceptance tests,
 or business use case tests (in terms of clean architecture)
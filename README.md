lambda-dive
===========

This is a project containing test case for Lambda Expression, a new feature of Java SE 8.

Test cases are located under src/test directory.

git repository:  https://github.com/smilingjason/lambda-dive

Usage
-----
mvn test 
This cmd will run all test cases under src/test directory.

Stream, Laziness, Parralel 
--------------------------
See test case: src/test/java/demo/jason/lambda/StreamTest.java

Lambda Expression under the hood
--------------------------------
See the test case: src/test/java/demo/jason/invokedynamic/LambdaCallSiteTest.java

Hints
------
Use cmd javap -private -v classname to get more detail from the byte code.

Enjoy Java. :-)

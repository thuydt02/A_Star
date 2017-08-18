2017 by Thuy Do

File(s):
- A_Star.java
- Node.java
- org-apache-commons-lang.jar
- readme.txt
- passed_inp1.txt; out1.txt
- passed_inp2.txt; out2.txt
- passed_inp3.txt; out3.txt

Using javac and java to compile and run:

+ javac -cp org-apache-commons-lang.jar *.java
+ java -cp org-apache-commons-lang.jar:. A_Star <input file> 

Sample:
java -cp org-apache-commons-lang.jar:. A_Star passed_inp3.txt 

This is an implementation for Greedy Best First Search and A* algorithms
The 2 algorithm are to find solution path which is the moves from the Initial State to the Goal State
+ Initial State: Cursor at the first node of the graph(node 0)
+ Goal State: Cursor at the last node of graph (node n-1)

Output file:
+ Output The first line is a path from node 0 to the last node searched by Greedy Search.
+ The second line is a path from node 0 to the last node searched by A* Search
+ Output will be written to the pa1.out file.

passed_inp1.txt is a sample input file that the algorithms tested and resulted in the output file "out1.txt"
similarly, passed_inp2.txt is the sample input from textbook (Rumania Problem) => out2.txt
passed_inp3.txt is the sample with 1002 nodes and 5205 edges => out3.txt

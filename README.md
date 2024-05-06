# RepresentableGraphs


Word-representable graphs

A graph G = (V, E) is word-representable if there exists a word w over V such that x and y alternate in w if and only if (x,y) is in E. A comprehensive introduction to the theory of word-representable graphs is given in the book "Words and Graphs" by Sergey Kitaev and Vadim Lozin published by Springer. For a brief and clear introduction to the subjct, see "A Comprehensive Introduction to the Theory of Word-Representable Graphs" by Kitaev published in the Lecture Notes in Computer Science 10396:

http://personal.strath.ac.uk/sergey.kitaev/Papers/wrg-kitaev.pdf

Alternatelve, see the Wikipedia page giving basic information on the theory and its generalizations:

https://en.wikipedia.org/wiki/Word-representable_graph

A key result in the theory of word-representable graphs is the theorem stating that a graph is word-representable if and only if it admits a semi-transitive orientation.

This user-friendly program to work with word-representability of graphs was written around 2014-15, is based on the notion of a semi-transitive orientation. This program presents a graph in matrix form on the left, and in graphical form on the right. Edges are added/removed by clicking on the appropriate entry in the matrix. The program presents the following functionality:

Manipulating the graph

Controls for manipulating the graph are on the left-hand side panel. There is an "oriented/non-oriented" pair of radio buttons, which toggles which type of edge is placed on the graph. The up and down arrows decrease and increase the size of the matrix respectively (that is, removes the highest vertex, or adds a new one). The "clear" button completely removes all edges from the current graph.

Checking for word-representability

Controls for checking the word-representability of a graph are at the bottom of the window. The button "Is this orientation semi-transitive?" checks whether the given fully-oriented graph is semi-transitively oriented. The button "Is this graph word-representable?" checks whether a given (non-oriented) graph is word-representable, and if it is, it outputs a semi-transitive orientation of the graph, and a uniform word representing it. If you orient some (but not all) edges before clicking the button, these will be used as clues for what kind of orientation a semi-transitive orientation may be. So for example, if you orient one particular edge in one way, the program will not check any of the oriented graphs where that edge was oriented the other way, when searching for a semi-transitive orientation. The button "Get word representing this graph" will show a uniform word representing the current graph, if the graph is indeed word-representable, and will allow the user to save that word to a file.

Menu bar

File menu

New - loads a graph with no edges and the current number of vertices

Load - loads a saved graph

Save - saves the current graph

Quit - quits the application

Word menu

Load graph from word - accepts a word as input and loads a graph that the word represents.

Check if word represents this graph - accepts a word as input and checks whether that word represents the current graph.

Get uniform word from word - accepts a word as input and outputs a uniform word representing the same graph.



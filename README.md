# RepresentableGraphs

A graph $G = (V, E)$ is *word-representable* if there exists a word $w$ over $V$ such that $x$ and $y$ alternate in $w$ if and only if $(x,y) \in E$. For example, the graph $G$ below can be represented by the word $1 2 3 4 1 3 5$.

![graph G](https://github.com/julia-0/RepresentableGraphs/assets/88452917/ea405d87-6a1b-4758-ace9-396710feb479)

Any word-representable graph also has a $k$-uniform word that represents it for some $k$[^1], that is, a word where each letter appears exactly $k$ times. It is known that some graphs are word-representable and some are not; for example, the smallest non-word-representable graph is the wheel graph $W_5$ below:

![W_5](https://github.com/julia-0/RepresentableGraphs/assets/88452917/bc82fd46-8f3b-4ef1-9537-f2dbd616a908)

There has been much research into classifying graphs by word-representability and into investigating the word-representability or non-word-representability of entire graph classes; for example it is known that all [3-colourable](https://en.wikipedia.org/wiki/Graph_coloring) graphs are word-representable. For a brief and clear introduction to the theory of word-representable graphs, as well as their significance and relevance to other graph classes, see "A Comprehensive Introduction to the Theory of Word-Representable Graphs" by Sergey Kitaev published in the Lecture Notes in Computer Science 10396:

http://personal.strath.ac.uk/sergey.kitaev/Papers/wrg-kitaev.pdf [^2]

Or, see the Wikipedia page giving basic information on the theory and its generalizations:

https://en.wikipedia.org/wiki/Word-representable_graph

A key result in the theory of word-representable graphs is the theorem stating that a graph is word-representable if and only if it admits a semi-transitive orientation.[^3] *Semi-transitivity* is a generalization of transitivity in directed graphs: a digraph is called semi-transitive if it has no cycles and no shortcuts, where a "shortcut" is defined as a path $V_i \to \dots \to V_j$ with at least $4$ vertices which by itself is not transitive where $V_i \to V_j$ also exists (the "shortcutting" edge). A semi-transitive orientation of $G$ is given below.

![graph G st-oriented](https://github.com/julia-0/RepresentableGraphs/assets/88452917/393589cd-2248-4b92-8127-526223d4c1a2)

If an edge $1 \to 5$ existed, then there would be a shortcut $1 \to 2 \to 4 \to 5$ (if $5 \to 1$ existed it would be a cycle). 

This program to work with word-representability of graphs was written by me around 2014-15, and is based around semi-transitive orientations and looking for them in inputted graphs. This program presents a graph in adjacency matrix form on the left, and in graphical form on the right. Edges are added/removed by clicking on the appropriate entry in the matrix. 

![Screenshot - 090524 jar](https://github.com/julia-0/RepresentableGraphs/assets/88452917/2e3d2b5b-fa9b-4d1e-bd51-1813e1dd853c)
![Screenshot - 090524 - jar2](https://github.com/julia-0/RepresentableGraphs/assets/88452917/13046b9b-4fb2-4d8f-93b1-3861fbc4f459)


The program presents the following functionality:

# Manipulating the graph

Controls for manipulating the graph are on the left-hand side panel. There is an "oriented/non-oriented" pair of radio buttons, which toggles which type of edge is placed on the graph. The up and down arrows decrease and increase the size of the matrix respectively (that is, removes the highest vertex, or adds a new one). The "clear" button completely removes all edges from the current graph.

# Checking for word-representability

Controls for checking the word-representability of a graph are at the bottom of the window. The button "Is this orientation semi-transitive?" checks whether the given fully-oriented graph is semi-transitively oriented. The button "Is this graph word-representable?" checks whether a given (non-oriented) graph is word-representable, and if it is, it outputs a semi-transitive orientation of the graph, and a uniform word representing it. If you orient some (but not all) edges before clicking the button, these will be used as clues for what kind of orientation a semi-transitive orientation may be. So for example, if you orient one particular edge in one way, the program will not check any of the oriented graphs where that edge was oriented the other way, when searching for a semi-transitive orientation. The button "Get word representing this graph" will show a uniform word representing the current graph, if the graph is indeed word-representable, and will allow the user to save that word to a file.

# Menu bar

## File menu

### New - loads a graph with no edges and the current number of vertices

### Load - loads a saved graph

### Save - saves the current graph

### Quit - quits the application

## Word menu

### Load graph from word - accepts a word as input and loads a graph that the word represents.

### Check if word represents this graph - accepts a word as input and checks whether that word represents the current graph.

### Get uniform word from word - accepts a word as input and outputs a uniform word representing the same graph.


[^1]: Sergey Kitaev and Artem Pyatkin, *On representable graphs*, Journal of Automata, Languages and Combinatorics 13(1), 2008, pp.45--54
[^2]: Sergey Kitaev, *A Comprehensive Introduction to the Theory of Word-Representable Graphs*, In: Developments in Language Theory: 21st International Conference, DLT, Liege, Aug 7-11, 2017. Lecture Notes in Computer Science 10396, 2017 pp.36--67
[^3]: Magnus Halld&oacute;rsson, Sergey Kitaev and Artem Pyatkin, *Semi-transitive orientations and word-representable graphs*, Discrete Applied Mathematics 201, 2016, pp.164--171



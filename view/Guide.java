package view;

public interface Guide {

public static final String TEXT =

"Word-representable graphs\n" +
"\n" +
"This program presents a graph in matrix form on the left, and in graphical form on the right. Edges are added/removed by clicking on the appropriate entry in\n"+
"the matrix. The program presents the following functionality:\n" +
"\n" +
"Manipulating the graph\n" +
"Controls for manipulating the graph are on the left-hand side panel. There is an \"oriented/non-oriented\" pair of radio buttons, which toggles which type\n"+
"of edge is placed on the graph. The up and down arrows decrease and increase the size of the matrix respectively (that is, removes the highest vertex, or adds\n"+
"a new one). The \"clear\" button completely removes all edges from the current graph.\n" +
"\n" +
"Checking for word-representability\n" +
"Controls for checking the word-representability of a graph are at the bottom of the window. The button \"Is this orientation semi-transitive?\" checks\n"+
"whether the given fully-oriented graph is semi-transitively oriented. The button \"Is this graph word-representable?\" checks whether a given (non-oriented)\n"+
"graph is word-representable, and if it is, it outputs a semi-transitive orientation of the graph, and a uniform word representing it. If you orient some (but\n"+
"not all) edges before clicking the button, these will be used as clues for what kind of orientation a semi-transitive orientatio may be. So for example, if you\n"+
"orient one particular edge in one way, the program will not check any of the oriented graphs where that edge was oriented the other way, when searching for a\n"+
"semi-transitive orientation. The button \"Get word representing this graph\" will show a uniform word representing the current graph, if the graph is indeed\n"+
"word-representable, and will allow the user to save that word to a file.\n"+

"\n" +
"Menu bar\n" +
"\n" +
"File menu:\n" +
"New - loads a graph with no edges and the current number of vertices\n" +
"\n" +
"Load - loads a saved graph\n" +
"\n" +
"Save - saves the current graph\n" +
"\n" +
"Quit - quits the application\n" +
"\n" +
"Word menu:\n" +
"Load graph from word - accepts a word as input and loads a graph that the word represents.\n" +
"\n" +
"Check if word represents this graph - accepts a word as input and checks whether that word represents the current graph.\n" +
"\n"+
"Get uniform word from word - accepts a word as input and outputs a uniform word representing the same graph.\n"+
"\n" +
"More information can be found in \"Graphs and Words\" by Sergey Kitaev and Vadim Lozin, to be published by Springer.";

}

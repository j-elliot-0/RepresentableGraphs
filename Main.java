
//IMPORTS-----------------------------/
import graph.AdjacencyMatrix;

import view.AdjacencyMatrixView;

import javax.swing.SwingUtilities;

/***BEGIN CLASS Main.java*******************************************************
 *  The class that is run when the program starts. It simply creates a new
 *  adjacency matrix interface and starts the adjacency matrix view.
 *
 * @author julia
 *****************/public class Main implements Runnable {/*********************/


public static void main(String[] args)
{	SwingUtilities.invokeLater(new Main());
}


public void run()
{	new AdjacencyMatrixView(new AdjacencyMatrix(5));
}

/*****************/}/*************************************END CLASS Main.java***/

package controller;

import graph.Graph;
import graph.AdjacencyMatrix;

import view.AdjacencyMatrixView;
import word.GraphFromWord;
import word.Word;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;

/***BEGIN CLASS AdjacencyMatrixController.java**********************************
 * AdjacencyMatrix implementation of the main controller. Defines a few
 * primitive operations for Template Methods, for operations that aren't
 * possible without knowing a graph's internal representation as an adjacency
 * matrix, particularly with creating new graph instances.
 * (similar to factory methods)
 *
 * @author julia
 ******************/public class AdjacencyMatrixController/*********************/
						 extends MainController {

//FIELDS-------------------------------/
private AdjacencyMatrixView view;


//CONSTRUCTOR--------------------------/
public AdjacencyMatrixController (Graph g, AdjacencyMatrixView v)
{	super(g);
	view = v;
}


//METHODS------------------------------/
protected JFrame getFrame()
{	return view.getFrame();
}


protected void loadGraphFromFile (File file)
{	try {
		BufferedReader buffer =
			new BufferedReader(new FileReader(file));
		String line = buffer.readLine();
		AdjacencyMatrix newGraph = new AdjacencyMatrix(line.length());

		for (int r = 0; r < newGraph.length() ; r++) {
			for (int c = 0; c < newGraph.length(); c++)
				if (line.charAt(c) == '1')
					newGraph.set(r,c,true);
			line = buffer.readLine();
		}
	
		buffer.close();

		view.setGraph(newGraph);

	} catch (IOException e) {
		showFileErrorMessage(e);
	}
}


protected void loadGraphFromWord (Word word)
{	view.setGraph(GraphFromWord.graphFromWord(word));
}


/****************/}/*****************END CLASS AdjacencyMatrixController.java***/

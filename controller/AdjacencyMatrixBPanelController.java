package controller;

//IMPORTS------------------------------/
import graph.Graph;
import graph.AdjacencyMatrix;

import view.AdjacencyMatrixView;

import javax.swing.JFrame;


/***BEGIN CLASS AdjacencyMatrixBPanelController*********************************
 * An implementation of BottomPanel that defines a primitive operation for a
 * Factory method (actually a Template method) for checking
 * word-representability and instantiating a semi-transitive graph. Used with
 * AdjacencyMatrixView, which needs to know about the graph's representation as
 * an adjacency matrix.
 *
 * @author julia
 ****************/public class AdjacencyMatrixBPanelController/*****************/
					   extends BottomPanelController {


//FIELDS-------------------------------/
private AdjacencyMatrixView view;


//CONSTRUCTOR--------------------------/
public AdjacencyMatrixBPanelController(AdjacencyMatrix g, AdjacencyMatrixView v)
{	super(g);
	view = v;
}

//METHODS------------------------------/
protected JFrame getFrame()
{	return view.getFrame();
}


protected Graph checkWordRepresentability() throws Exception
{	//I know it's an adjacency matrix (see constructor) (bit of a cludge)
	AdjacencyMatrix res =
		(AdjacencyMatrix) model.admitsSemiTransitiveOrientation();

	//update view if indeed word-representable
	if (res != null)
		view.setGraph(res);

	return res;
}

/*****************/}/***************END CLASS AdjacencyMatrixBPanelController***/

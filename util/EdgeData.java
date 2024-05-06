package util;

import graph.Edge;

import java.awt.Point;

// Class containing edge data to be sent by the graph to observers
// Contains a Point describing the indices of the vertices, and
// an Edge object. That way observers can take the data they need.
public class EdgeData {

public final Edge edge;
public final Point indices;

public EdgeData	(Edge e, Point p)
{	edge = e;	indices = p;
}

}

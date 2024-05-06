package graph;

/***BEGIN CLASS Edge.java*******************************************************
 *  Simple class to conveniently talk about the two nodes of an edge as one
 *  object. Variable directed determines whether this is an oriented edge.
 * @author julia
 *****************/public class Edge {/*****************************************/

public Vertex from, to;
public boolean directed;

public Edge (Vertex f, Vertex t, boolean dir)
{	from = f;	to = t;	directed = dir;
}

public boolean equals (Object o)
{	if (o==null || !(o instanceof Edge)) return false;
	Edge other = (Edge) o;
	if (directed)
		return (from.equals(other.from) && to.equals(other.to));
	else return (!other.directed && 
			((from.equals(other.from) && to.equals(other.to))
			|| (from.equals(other.to) && to.equals(other.from)))
			);
}


public int hashCode()
{	return from.hashCode()+to.hashCode();
}

/*****************/}/*************************************END CLASS Edge.java***/

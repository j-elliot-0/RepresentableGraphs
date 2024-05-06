package graph;

//IMPORTS------------------------------/
import graph.SemiTransitiveReason;

import java.util.Set;

import word.Word;

/***BEGIN INTERFACE Graph.java**************************************************
 * The interface defining a graph. Has methods for setting and unsetting
 * vertices and edges, and for checking word-representability/semi-transitivity
 *
 * @author julia
 *****************/public interface Graph {/************************************/


//Methods for getting the sets of vertices and edges
public Set<Vertex> vertices()
;
public Set<Edge> edges()
;


//Methods for checking the existence of connections between vertices
public boolean      edge (Vertex v , Vertex u )
;
public boolean   dirEdge (Vertex fr, Vertex to)
;
public boolean undirEdge (Vertex v , Vertex u )
;
public boolean   dirPath (Vertex fr, Vertex to)
;


//Methods for checking that this graph is fully oriented or fully non-oriented
public boolean oriented()
;
public boolean nonOriented()
;


//Methods for adding vertices and edges. Note that adding a directed edge
//from x to y replaces an undirected edge x to y, and vice versa; and of course,
//adding a directed edge x->y replaces a directed edge y->x
public void setVertex() //set a vertex without caring what its label will be
;
public void set (Vertex v)
;
public void setEdge (Vertex v, Vertex u)
;
public void setDirEdge (Vertex fr, Vertex to)
;


//Methods for removing vertices and edges.
public void unset (Vertex v)
;
public void unsetEdge (Vertex v, Vertex u)
;


//Methods related to word-representability: checking wheter a word represents
//a graph, checking if an orientation is semi-transitive, and checking if a
//graph is word-representable
public boolean wordRepresents (Word w)
;
public SemiTransitiveReason semiTransitivelyOriented() throws Exception
;
public Graph admitsSemiTransitiveOrientation() throws Exception
;


/******************/}/*******************************END INTERFACE Graph.java***/

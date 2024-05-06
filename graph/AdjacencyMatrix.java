package graph;

//IMPORTS------------------------------/
import path.PathMatrix;
import path.PathList;
import path.Path;

import graph.SemiTransitiveReason.Reason;

import util.Matrix;
import util.EdgeData;
import word.GraphFromWord;
import word.Word;

import java.util.Observable;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.awt.Point;

/***BEGIN CLASS AdjacencyMatrix.java********************************************
 * Main class representing a graph as an n*n boolean adjacency matrix. Has
 * methods for adding and removing nodes, for adding, removing and toggling
 * oriented and non-oriented edges, and for checking for the existence of an
 * edge, as well as checking if one is oriented in some way or not.
 * Implements algorithms for checking the word-representability of the graph,
 * and can load a graph from a word as well as verify whether a particular word
 * represents the current graph.
 * 
 * @author julia
 *****************/public class AdjacencyMatrix extends Observable/*************/
					 implements Graph, Matrix<Boolean> {

//FIELDS-------------------------------/
private boolean[][] matrix;
static final int MIN_SIZE = 2;
static final int MAX_SIZE = 100;
private List<String> labels;


//CONSTRUCTORS--------------------------/
/* param sz - the TOTAL size of the graph in number of nodes.
 */
public AdjacencyMatrix (int sz) {
	if (sz < MIN_SIZE) sz = MIN_SIZE;
	if (sz > MAX_SIZE) sz = MAX_SIZE;

	matrix = new boolean[sz][sz];
	labels = new ArrayList<String>(sz);
	//using default labels 1-sz
	for (int i = 1; i <= sz; i++)
		labels.add(i+"");
}


/* Constructor that sets matrix and labels directly. Used for deep-copying
 */
public AdjacencyMatrix (boolean[][] mx, List<String> ls) {
	matrix = new boolean[mx.length][mx.length];
	labels = new ArrayList<String>(ls);
	
	for (int i = 0; i < mx.length; i++)
		System.arraycopy(mx[i], 0, matrix[i], 0, mx.length);
}


//METHODS------------------------------/
public Set<Vertex> vertices()
{	Set<Vertex> res = new HashSet<Vertex>(length());
	for (String l : labels)
		res.add(new Vertex(l));
	return res;
}


public Set<Edge> edges()
{	Set<Edge> res = new HashSet<Edge>((length() * length()) / 2);
	for (int v = 0; v < length()-1; v++)
		for (int u = v+1; u < length(); u++) {
			Vertex v_v = new Vertex(labels.get(v)),
				   u_v = new Vertex(labels.get(u));
			if (hasEdge(v,u))
				if (hasUndirEdge(v,u))
					res.add(new Edge(v_v,u_v,false));
				else if (hasDirEdge(v,u))
					res.add(new Edge(v_v,u_v,true));
				else res.add(new Edge(u_v,v_v,true));
		}
	return res;
}


public boolean wordRepresents (Word w)
{	//get all alternations in word
	Set<Edge> word = new HashSet<Edge>();
	for (String a : w)
		for (String b : w)
			if (a.equals(b))
				break;	//a repetition, so no more alternations with a
			else if (GraphFromWord.isAlterning(a, b, w))
				word.add(new Edge(new Vertex(a),new Vertex(b), false));

	//comparing with edges in graph, ignoring orientations
	Set<Edge> edges = new HashSet<Edge>(edges());
	for (Edge e : edges)
		e.directed = false;
	
	return (edges.equals(word));
}


public SemiTransitiveReason semiTransitivelyOriented() throws Exception
{	if (!oriented())
		throw new Exception("Graph is not yet fully oriented!");

	//first, produce matrix A^2
	PathMatrix pm = square();
	
	//check it has no non-zero along main diagonal
	PathList pl = pm.nonZeroAlongDiagonal();
	if (pl != null)
	  return new SemiTransitiveReason(Reason.CYCLE, pl.iterator().next());
	
	//for-each A^i where 3 <= i <= N
	for (int i=3; i <= length(); i++) {
		//get A^i
		pm = symbolicMultiply(pm);
		
		//check that A^i has no non-zero along the main diagonal
		pl = pm.nonZeroAlongDiagonal();
		if (pl != null) 
		  return new SemiTransitiveReason(Reason.CYCLE, pl.iterator().next());
		
		//evaluate the non-zero elements for shortcuts.
		Path p = evaluateNonZeroElements(pm);
		if (p != null)
		  return new SemiTransitiveReason(Reason.SHORTCUT, p);

	}
	//if all checks pass, graph is semi-transitively oriented.
	return new SemiTransitiveReason(Reason.SEMI_TRANSITIVE, null);
}


/* return a fully oriented Graph which is an orientation of the current one
 * and is semi-transitive, if one exists, or null otherwise.
 */
public AdjacencyMatrix admitsSemiTransitiveOrientation() throws Exception
{	if (oriented())
		throw new Exception("Graph is already fully oriented!");

	//create copy of matrix
	AdjacencyMatrix copy = (AdjacencyMatrix) copy();
	//check if graph is non-oriented, and, if so, assign arbitrary edge
	int v, u;
	boolean found = false;
	for (v = 0; v < length()-1 && !found; v++)
		for (u = v+1; u < length() && !found; u++)
			if (hasDirEdge(v,u))
				found = true;
	if (!found)
		copy.setDirEdge(new Vertex(labels.get(0)), new Vertex(labels.get(1)));
	
	/* ^!!! Note to self from 2015/16, from your self from 2024: You went and  
	 * made a dumb mistake and you went and oriented the edge (0,0) instead of (0,1)
	 * didn't you, that did nothing and costs you doubling processing time! You 
	 * realized shortly after changed it I think, but this was after being basically
	 * finished with uni, and most likely was on the "temporarily" loaned university
	 * laptop. I think that was the only newer copy than this one, haven't been
	 * able to find it anywhere else, not even sure if I gave Sergey the newer copy
	 * or not, and you wiped that old Windows 7 HP when you were planning to hand
	 * it back at the next opportunity to travel to Glasgow and go there, but alas, 
	 * the pandemic happened. Any newer copy of this, with any other improvements/fixes 
	 * that may exist I don't remember now, is lost like tears in the rain...
	 */
	
	return copy.recursiveOrient();
}


/* Recursively finds the next non-oriented edge, divides-and-conquers to
 * orient it one way and then the other, until all edges are oriented. Then it
 * queries whether it is semi-transitive.
 */
public AdjacencyMatrix recursiveOrient()
{	if (!optimize())
		return null;

	// take next non-oriented edge
	Edge edge = null;
	for (Edge e : edges())
		if (!e.directed) {
			edge = e;
			break;
		}

	//if there are no more non-oriented edges, check for semi-transitivity
	if (edge == null)
		try {
			if (semiTransitivelyOriented().result())
				return this;
			else return null;
		//all edges are assigned, so this should never throw an exception
		} catch (Exception e) { throw new IllegalStateException(e); }
	
	
	// left = do recursiveOrient on copy after assigning edge from->to
	AdjacencyMatrix left = (AdjacencyMatrix) copy();
	left.setDirEdge(edge.from, edge.to);
	left = left.recursiveOrient();
	
	// is it semi-transitive?
	if (left != null)
		return left;
	
	
	// right = do recursiveOrient on copy after assigning edge to->from
	AdjacencyMatrix right = (AdjacencyMatrix) copy();
	right.setDirEdge(edge.to, edge.from);
	right = right.recursiveOrient();
	
	// is it semi-transitive?
	if (right != null)
		return right;
	
	return null;
}


/* Perform a few optimizations to a partially oriented graph, checking for
 * edges that must be oriented in a certain way if the graph is to be
 * semi-transitively oriented. If a situation is found where the orientation
 * cannot possibly be semi-transitive, then this method returns false.
 * Otherwise, when it gets to the end it returns true.
 */
private boolean optimize() 
{	Set<Vertex> vertices = vertices();
search:	//<- will return here if an orientation for edge is found
	for (Edge edge : edges()) if (!edge.directed) {
	//for each unassigned edge, called edge...

		//for each node i connected with one of the nodes in edge...
		for (Vertex i : vertices) {
			if (edge(edge.from, i))
				; //this one is, so proceed
			else if (edge(edge.to, i)) {
				//as above, just switch around the names for convenience
				Vertex tmp = edge.from;
				edge.from = edge.to;
				edge.to = tmp;
			} else continue; //this isn't, go to next i


			//check for a triangle with i
			if (edge(i, edge.to) && !undirEdge(i,edge.to)) {
				//check for a path indicating a potential cycle
				
				//block path from edge.from -> i -> edge.to
				if (isPath(edge.from, i, edge.to)) {
					//block a cycle
					setDirEdge(edge.from, edge.to);
					continue search;	//go to next unassigned edge

				//block path from edge.to -> i -> edge.from
				} else if (isPath(edge.to, i, edge.from)) {
					//block a cycle
					setDirEdge(edge.to, edge.from);
					continue search;	//go to next unassigned edge
				}
			}

			//now, check for cycles and shortcuts on 4 nodes
			//for each node j...
			for (Vertex j : vertices) {
				//check for a square cycle edge.from -> i -> j -> edge.to
				if (edge(i, j) && edge(j, edge.to))
					;	//this is, so proceed
				else continue; //this isn't, so go to next j

				//call edge (i,j) "opposite" for convenience
				//the two edges connect at (edge.from, opposite.from)
				//and (edge.to, opposite.to)
				Edge opposite = new Edge(i, j, false);

				int count = 0; //keep a count of diagonals
				//look for diagonal from edge.from to opposite.to
				if (edge(edge.from, opposite.to)) 
					count++;
				//look for diagonal from edge.to to opposite.from
				if (edge(edge.to, opposite.from)) 
					count++;

				//if path opposite.from -> opposite.to -> edge.to
				if (isPath(opposite.from, opposite.to, edge.to)) {

					//if there's a path
					//edge.from -> opposite.from -> opposite.to -> edge.to,
					//there is a possibility of either a shortcut or a cycle
					if (dirEdge(edge.from, opposite.from)) {
						//if there is no possibility of shortcut,
						//(ie. no diagonals are missing) block the cycle
						if (count == 2) {
							setDirEdge(edge.from, edge.to);
							continue search;	//go to next unassigned edge
						}
						//if there is, then any orientation we add creates
						//either a shortcut or a cycle, so there is no
						//possibility of semi-transitivity.
						else return false;
					}

					//if not, block and follow
					if (count != 2) { //if there's a possibility of shortcut that is!
						setDirEdge(opposite.from, edge.from);
						setDirEdge(edge.from, edge.to);
					}
					continue search;	//go to next unassigned edge
				}

				//if path opposite.to -> opposite.from -> edge.from
				if (isPath(opposite.to, opposite.from, edge.from)) {

					//if there's a path
					//edge.to -> opposite.to -> opposite.from -> edge.from,
					//there is a possibility of either a shortcut or cycle
					if (dirEdge(edge.to, opposite.to)) {
						//if there is no possibility of shortcut,
						//(ie. no diagonals are missing), block the cycle
						if (count == 2) {
							setDirEdge(edge.to, edge.from);
							continue search;	//go to next unassigned edge
						}
						//if there is, then any orientation we add creates
						//either a shortcut or a cycle, so there is no
						//possibility of semi-transitivity.
						else return false;
					}

					//if not, block and follow
					if (count != 2) { //if there's a possibility of shortcut that is!
						setDirEdge(opposite.to, edge.to);
						setDirEdge(edge.to, edge.from);
					}
					continue search;	//go to next unassigned edge
				}


				//the rest only prevents shortcuts
				if (count != 2) {

					//if edges opposite.from -> edge.from
					//and edge.to -> opposite.to
					if (dirEdge(opposite.from, edge.from) &&
							dirEdge(edge.to, opposite.to)) {
				
						//block to prevent a path
						//opposite.from -> edge.from -> edge.to -> opposite.to
						//which would cause a shortcut.
						setDirEdge(edge.to, edge.from);
						continue search;	//go to next unassigned edge
					}

					//if edges opposite.to -> edge.to
					//and edge.from -> opposite.from
					if (dirEdge(opposite.to, edge.to) &&
							dirEdge(edge.from, opposite.from)) {

						//block to prevent a path
						//opposite.to -> edge.to -> edge.from -> opposite.from
						//which would cause a shortcut.
						setDirEdge(edge.from, edge.to);
						continue search;	//go to next unassigned edge
					}
					
				} //end if (count != 2)

			} //end for-each vertex j
		} //end for-each vertex i
	} //end for-each unassigned edge
	return true;
}


/* return true if there is an oriented path traversing all specified nodes
 * _in_order_, false otherwise
 */
private boolean isPath(Vertex... nodes)
{	if (!dirEdge(nodes[0], nodes[1]))
		return false;
	
	for (int i = 1; i < nodes.length; i++)
		if (!dirEdge(nodes[i-1], nodes[i]))
			return false;

	return true;
}


/* return the square of this matrix.
 */
private PathMatrix square()
{	PathMatrix res = new PathMatrix(length());
	
	//for-each row and column of matrix...
	for (int row = 0; row < length(); row++)
		for (int column = 0; column < length(); column++) {
			
			//for-each index of row and column, add paths
			PathList pl = res.get(row,column);
			
			//for-each i, check if (row,i) and (i,column) are set
			for (int i = 0; i < length(); i++)
				if (matrix[row][i] && matrix[i][column]) {
					Path path = new Path();	pl.add(path);
					path.add(new Vertex(labels.get(row)));	//start of path
					path.add(new Vertex(labels.get(i)));
					path.add(new Vertex(labels.get(column)));	//end of path
				}
		}
	return res;
}


/* return the result of symbolic multiplication with pm
 */
private PathMatrix symbolicMultiply (PathMatrix pm)
{	if (pm.length()!=length()) throw new IllegalArgumentException();

	PathMatrix res = new PathMatrix(length());
	
	//for-each row of pm and column of matrix...
	for (int row = 0; row < pm.length(); row++)
		for (int column = 0; column < length(); column++) {
			
			//for-each i in pm(row,i) and matrix(i,column),
			//if both are set, add to the new path-matrix entry in (row,column)
			PathList pl = res.get(row,column);
			for (int i = 0; i < pm.length(); i++)
				if ( matrix[i][column] && !pm.get(row,i).isEmpty() )
					pl.addAll(pm.get(row,i).addToPaths(
								new Vertex(labels.get(column))));
		}
	return res;
}


/* Evaluates each entry in the matrix along with a given path-matrix, pm. For
 * each index where both the matrix and path-matrix are non-zero, checks each
 * path in that entry for shortcuts.
 * return a Path describing a shortcut from one node to another, if a shortcut
 * exists; null otherwise. If the method returns non-null (and provided pm
 * correctly describes the original graph) then the graph cannot be
 * semi-transitively oriented.
 */
private Path evaluateNonZeroElements (PathMatrix pm)
{	if (pm.length()!=length()) throw new IllegalArgumentException();
	
	//for-each row and column of both matrices...
	for (int row = 0; row < length(); row++)
		for (int column = 0; column < length(); column++)
			
			//check if both (row,column) are non-empty
			if ( matrix[row][column] && !pm.get(row,column).isEmpty() )
				
				//if they both are, iterate through each path in pm...
				for (Path path : pm.get(row,column)) {
					
					//... checking that, for each i-th and j-th entry in the
					//path where 1<=i<j<=(length-1), matrix(i,j) is non-zero. 
					for (int i = 0; i < path.size()-1; i++)
						for (int j = i+1; j < path.size(); j++)

							//if it IS zero, graph is non-semi-transitively
							//oriented, so return shortcut
							if (!matrix
									[labels.indexOf(path.get(i).label)]
									[labels.indexOf(path.get(j).label)])
							  return path;
				}
	return null; //no shortcut was found
}


public String toString()
{	StringBuilder res = new StringBuilder();
	
	for (int r = 0; r < length(); r++) {
		for (int c = 0; c < length(); c++)
			res.append(toString(r,c));
		res.append("\n");
	}
	return res.toString();
}


public String toString(int r, int c)
{	//if looking to the "top" or "left" of the matrix, get the col/row label
	if (r < 0)
		return labels.get(c);
	if (c < 0)
		return labels.get(r);
	
	return matrix[r][c] ? "1" : "0";
}


/* convenience method to let the observers know that there's a new graph.
 */
public void refresh()
{	setChanged();
	notifyObservers();
}


//Methods for checking the existence of connections between vertices
public boolean edge (Vertex v, Vertex u)
{	int v_i = labels.indexOf(v.label);
	int u_i = labels.indexOf(u.label);
	return hasEdge(v_i,u_i);
}


public boolean dirEdge (Vertex fr, Vertex to)
{	int fr_i = labels.indexOf(fr.label);
	int to_i = labels.indexOf(to.label);
	return hasDirEdge(fr_i,to_i);
}


public boolean undirEdge (Vertex v, Vertex u)
{	int v_i = labels.indexOf(v.label);
	int u_i = labels.indexOf(u.label);
	return hasUndirEdge(v_i,u_i);
}


public boolean dirPath (Vertex fr, Vertex to)
{	//First check if there's a path of length 1!
	int fr_i = labels.indexOf(fr.label);
	int to_i = labels.indexOf(to.label);
	if (hasDirEdge(fr_i,to_i)) return true;
	
	//Look thru adjacency matrices A^2 -> A^N

	PathMatrix pm = square();

	if (!pm.get(fr_i,to_i).isEmpty())
		return true;

	for (int n = 3; n <= length(); n++) {
		pm = symbolicMultiply(pm);
		if (!pm.get(fr_i,to_i).isEmpty())
			return true;
	}
	return false;
}


// private methods for getting edges from node indices
private boolean hasEdge (int v, int u)
{	return matrix[v][u] || matrix[u][v];
}


private boolean hasDirEdge (int fr, int to)
{	return matrix[fr][to] && !matrix[to][fr];
}


private boolean hasUndirEdge (int v, int u)
{	return matrix[v][u] && matrix[u][v];
}
//


//Methods for adding vertices and edges. Note that adding a directed edge
//from x to y replaces an undirected edge x to y, and vice versa; and of course,
//adding a directed edge x->y replaces a directed edge y->x
public void setVertex()
{	if (length() >= MAX_SIZE) return;

	increaseByOne();
	
	labels.add(length()+"");

	refresh();
}


public void set (Vertex v)
{	if (length() >= MAX_SIZE) return;
	if (labels.contains(v.label)) return;

	increaseByOne();

	labels.add(v.label);

	refresh();
}


public void setEdge (Vertex v, Vertex u)
{	int v_i = labels.indexOf(v.label);
	int u_i = labels.indexOf(u.label);

	matrix[v_i][u_i] = true;
	matrix[u_i][v_i] = true;
	
	setChanged();
	notifyObservers(new EdgeData(new Edge(v, u, false), new Point(v_i,u_i)));
}


public void setDirEdge (Vertex fr, Vertex to)
{	int fr_i = labels.indexOf(fr.label);
	int to_i = labels.indexOf(to.label);

	matrix[fr_i][to_i] = true;
	matrix[to_i][fr_i] = false;
	
	setChanged();
	notifyObservers(new EdgeData(new Edge(fr, to, true), new Point(fr_i,to_i)));
}


//Methods for removing vertices and edges.
public void unset (Vertex v)
{	if (length() <= MIN_SIZE) return;

	int v_i = labels.indexOf(v.label);

	reduceByOne(v_i);

	labels.remove(v_i);

	refresh();
}


public void unsetEdge (Vertex v, Vertex u)
{	int v_i = labels.indexOf(v.label);
	int u_i = labels.indexOf(u.label);

	matrix[v_i][u_i] = matrix[u_i][v_i] = false;

	refresh();
}


public boolean oriented()
{	for (int v = 0; v < length()-1; v++)
		for (int u = v+1; u < length(); u++)
			if (matrix[v][u] && matrix[u][v])
				return false;
	return true;
}


public boolean nonOriented()
{	for (int v = 0; v < length()-1; v++)
		for (int u = v+1; u < length(); u++)
			if (matrix[v][u] != matrix[u][v])
				return false;
	return true;
}


/* return a deep copy of this graph
 */
public Graph copy()
{	return new AdjacencyMatrix(matrix, labels);
}


//METHODS INHERITED FROM Matrix--------/
public int length()
{	return matrix.length;
}


public Boolean get (int r, int c)
{	return matrix[r][c];
}


public void set (int r, int c, Boolean s)
{	matrix[r][c] = s;

	refresh();
}


/* Make this n*n matrix a n+1*n+1 matrix
 */
public void increase()
{	if (length() >= MAX_SIZE) return;

	increaseByOne();

	labels.add(length()+"");

	setChanged();
	notifyObservers();
}


/* Make this n*n matrix a n-1*n-1 matrix by removing the i-th row and column
 */
public void remove (int i)
{	if (length() <= MIN_SIZE) return;

	reduceByOne(i);
	
	labels.remove(i);

	setChanged();
	notifyObservers();
}


//Private methods for manipulating matrix
/* add one entry to the matrix
 */
private void increaseByOne()
{	boolean[][] copy = new boolean[length()+1][length()+1];
	for (int i = 0; i < length(); i++)
		copy[i] = Arrays.copyOfRange(matrix[i], 0, length()+1);
	matrix = copy;
}


/* remove one entry from the matrix
 */
private void reduceByOne (int i)
{	boolean[][] copy = new boolean[length()-1][length()-1];
	int r, c;
	for (r = 0; r <= i-1; r++) {
		//copy[r][0] = matrix[r][0];
		//copy[r][1] = matrix[r][1];
		//...
		//copy[r][i-1] = matrix[r][i-1];
		//copy[r][i] = matrix[r][i+1];
		//copy[r][i+1] = matrix[r][i+2];
		//...
		//copy[r][N-1] = matrix[r][N];
		//which would go like...
		for (c = 0; c < i; c++) 
			copy[r][c] = matrix[r][c];
		for (c = i; c < length()-1; c++) 
			copy[r][c] = matrix[r][c+1];
	}
	for (r = i; r < length()-1; r++) {
		for (c = 0; c < i; c++) 
			copy[r][c] = matrix[r][c];
		for (c = i; c < length()-1; c++) 
			copy[r][c] = matrix[r][c+1];
	}
	matrix = copy;
}

/*****************/}/**************************END CLASS AdjacencyMatrix.java***/

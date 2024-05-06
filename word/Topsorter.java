package word;

import graph.Graph;
import graph.Vertex;

import java.util.Set;
import java.util.HashSet;


/***BEGIN CLASS TopSorter.java**************************************************
 * Class encapsulating an algorithm for doing a topological ordering of a
 * subset of edges in a graph. Like a Strategy pattern.
 *
 * @author julia
 *****************/public class Topsorter {/************************************/

//FIELDS------------------------------/
private Graph graph;
private Set<Vertex> set;

private Set<Vertex> unvisited;
private Set<Vertex> tmp_mark;
private Word res;

//CONSTRUCTOR--------------------------/
public Topsorter (Graph g) {
	graph = g;
}


	/*
	 * The basic topsort algorithm (a depth-first search algorithm based on the
	 * one in (Cormen et al. 2009, p.613))
	 *
  L <- Empty list that will contain the sorted nodes
  while there are unmarked nodes do
      select an unmarked node n
      visit(n)
  function visit(node n)
      if n has a temporary mark then stop (not a DAG)
      if n is not marked (i.e. has not been visited yet) then
          mark n temporarily
          for each node m with an edge from n to m do
              visit(m)
          mark n permanently
          unmark n temporarily
          add n to head of L
	*/


public Word topsort(Set<Vertex> s)
{	set = s;	//the subset out of graph that has to be sorted

	unvisited = new HashSet<Vertex>(set); //vs that have not been visited yet
	tmp_mark = new HashSet<Vertex>();

	res = new Word(set.size()); //will contain the sorted nodes

	while (!unvisited.isEmpty()) { //while there are unvisited nodes
		Vertex n = unvisited.iterator().next();
		visit(n);
	}

	return res;
}


private void visit (Vertex n)
{
	if (tmp_mark.contains(n)) throw new IllegalStateException("not acyclic!");
	if (unvisited.contains(n)) {
		tmp_mark.add(n);
		//for each node m with an edge n->m
		for (Vertex m : set) if (graph.dirEdge(n,m))
			visit(m);
		unvisited.remove(n); //n has been visited
		tmp_mark.remove(n);
		res.prependLetter(n.label); //add n to head
	}
}


/*****************/}/********************************END CLASS TopSorter.java***/

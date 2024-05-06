package word;

import graph.Graph;
import graph.Vertex;

import java.util.Set;
import java.util.HashSet;

/***BEGIN CLASS WordFromGraph.java**********************************************
 * Implementation of the algorithm for obtaining a word representing a graph.
 * Based on the proof of Lemma 2 from (Halldorsson et al. 2015)
 * @author julia
 *****************/public class WordFromGraph {/********************************/

//FIELDS-------------------------------/
private Graph graph;
private Set<Vertex> vertices;


//CONSTRUCTOR--------------------------/
public WordFromGraph (Graph g)
{	graph = g;
	vertices = g.vertices();
}


//THE ALGORITHM------------------------/
public Word generateWord()
{	Vertex v = vertices.iterator().next(); //take some vertex from graph

	int wordlength = vertices.size()*2; //initial word will be 2-uniform
	Word word = new Word(wordlength);
	boolean finished = false;
	do {

		Topsorter ts = new Topsorter(graph);
	
		Set<Vertex>
			I = new HashSet<Vertex>(),	O = new HashSet<Vertex>(),
			A = new HashSet<Vertex>(),	B = new HashSet<Vertex>(),
			T = new HashSet<Vertex>();
	
		for (Vertex u : vertices)
			if (u.equals(v))
				;//disregard v itself
			else if (graph.dirEdge(u,v))
				//all in-neighbours of v belong in I
				I.add(u);
			else if (graph.dirEdge(v,u))
				//all out-neightbours of v belong in O
				O.add(u);
			else if (graph.dirPath(u,v))
				//check if there is a path u -> ... -> v
				A.add(u);
			else if (graph.dirPath(v,u))
				//check if there is a path v -> ... -> u
				B.add(u);
			else T.add(u);	//all the rest belong in T
	
		//get topsort permutations of above sets
		Word i = ts.topsort(I);
		Word o = ts.topsort(O);
		Word a = ts.topsort(A);
		Word b = ts.topsort(B);
		Word t = ts.topsort(T);
	
		//arcs, and non-arcs incident with v, are represented by the pattern
		//A I T A v O I v B T O B
	
		word.appendWord(a);	word.appendWord(i);
		word.appendWord(t);	word.appendWord(a);
	
		word.appendLetter(v.label);
	
		word.appendWord(o);	word.appendWord(i);
		
		word.appendLetter(v.label);
	
		word.appendWord(b);	word.appendWord(t);
		word.appendWord(o);	word.appendWord(b);
	
		if (!graph.wordRepresents(word)) {
			//find a non-arc not represented by word
			for (String l1 : word)
				for (String l2 : word)
					if (GraphFromWord.isAlterning(l1, l2, word) &&
							!graph.edge(new Vertex(l1), new Vertex(l2)))
						//then repeat the whole procedure with 1 vx in arc
						v = new Vertex(l1);
		} else finished = true;

	} while (!finished);

	return word;
}


/*****************/}/****************************END CLASS WordFromGraph.java***/

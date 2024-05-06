package word;

import graph.AdjacencyMatrix;
import graph.Vertex;

import java.util.List;
import java.util.ArrayList;


/***BEGIN CLASS GraphFromWord.java**********************************************
 * Class for dealing with getting a graph from a word. AdjacencyMatrix
 * implementation. (bit of a cludge)
 *
 * @author julia
 *****************/public class GraphFromWord {/********************************/

public static boolean isAlterning (String a, String b, Word w)
{	if (a.equals(b)) return false;	//a letter can't alternate with itself

	char last = '0';
	for (String s : w)
		if (s.equals(a))
			if (last == 'a')
				return false;
			else last = 'a';
		else if (s.equals(b))
			if (last == 'b')
				return false;
			else last = 'b';

	return true;
}


public static AdjacencyMatrix graphFromWord (Word w)
{	//first get number of distinct letters
	List<String> letters = new ArrayList<String>();
	for (String c : w)
		if (!letters.contains(c) && !c.equals(""))
			letters.add(c);

	AdjacencyMatrix g = new AdjacencyMatrix(
			new boolean[letters.size()][letters.size()], letters);

	//get alternations
	for (int i = 0; i < letters.size(); i++)
		for (int j = i+1; j < letters.size(); j++) {
			String a = letters.get(i), b = letters.get(j);
			if (isAlterning(a, b, w))
				g.setEdge(new Vertex(a), new Vertex(b));
		}

	return g;
}

/*****************/}/****************************END CLASS GraphFromWord.java***/

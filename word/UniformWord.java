package word;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;


/***BEGIN CLASS UniformWord.java************************************************
 * Algorithms for obtaining a uniform word for any given word: since all
 * word-representable graphs are k-word-representable for some k, any
 * non-uniform word has a uniform equivalent. The uniform word algorithm is
 * based on the proof of Theorem 7 in (Collins et al. 2015)
 *
 * @author julia
 *****************/public class UniformWord {/**********************************/


public static Word uniformWord (Word w)
{	//get occurrences of each letter
	Map<String, Integer> occs = new HashMap<String, Integer>();
	for (String l : w)
		occs.put(l, (occs.containsKey(l))
				? occs.get(l)+1
				: 1);


	//base case; return w if already uniform
	if (uniform(occs)) return w;
	
	int max = 0;
	for (int v : occs.values())
		if (v > max)
			max = v;
	
	//u = word with max occurring letters removed
	Word u = new Word(w);
	for (Map.Entry<String,Integer> l : occs.entrySet())
		if (max == l.getValue())
			u.removeLetter(l.getKey());

	//p(u) = initial permutation of u
	Word pu = u.initialPermutation();

	//obtain word p(u)w
	Word pu_w = new Word(pu); pu_w.appendWord(w);

	//recurse until a uniform word is found
	return uniformWord(pu_w);
}


private static boolean uniform (Map<String, Integer> occs)
{	Iterator<Integer> it = occs.values().iterator();
	int c = it.next(); //compare everything against first letter
	while (it.hasNext())
			if (it.next() != c)
				return false;
	return true;
}


/*****************/}/******************************END CLASS UniformWord.java***/

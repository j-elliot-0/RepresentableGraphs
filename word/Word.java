package word;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Iterator;

/***BEGIN CLASS Word.java*******************************************************
 * Represents a word as a sequence of letters (Strings).
 *
 * @author julia
 ****************/public class Word implements Iterable<String> {/**************/


//FIELD--------------------------------/
/* Composed of a deque, since operations involve adding to either end
 */
private Deque<String> list;


//CONSTRUCTORS-------------------------/
public Word (Word w) {
	list = new ArrayDeque<String>(w.list);
}


/* Take word from a string, where each character in word is a letter, except
 * stuff within brackets, "(" & ")", which are considered 1 letter
 */
public Word (String str) {
	this();
	int i,j;
	for (i = 0; i < str.length(); i++)
		if (str.charAt(i) != '(' && str.charAt(i) != ')')
			appendLetter(str.charAt(i)+"");
		else if (str.charAt(i) == '(' && ((j = str.indexOf(")", i)) != -1)) {
			appendLetter(str.substring(i+1, j));
			//skip past letter
			i = j;
		}
}


public Word() {
	list = new ArrayDeque<String>();
}


public Word (int size) {
	list = new ArrayDeque<String>(size);
}


//METHODS------------------------------/
public void appendWord (Word w)
{	for (String l : w)
		list.addLast(l);
}


public void appendLetter (String l)
{	list.addLast(l);
}


public void prependLetter (String l)
{	list.addFirst(l);
}


public void removeLetter (String l)
{	while (list.contains(l))
		list.remove(l);
}


/* Get the initial permutation, that is, the permutation obtained by removing
 * all but the leftmost occurrence of each letter
 */
public Word initialPermutation()
{	Word res = new Word();
	for (String l : this)
		if (!res.list.contains(l))
			res.appendLetter(l);
	return res;
}


public String toString()
{	StringBuilder res = new StringBuilder();
	for (String l : this)
		res.append((l.length()>1) ? "("+l+")" : l);
	return res.toString();
}


public Iterator<String> iterator()
{	return list.iterator();
}


/*****************/}/*************************************END CLASS Word.java***/

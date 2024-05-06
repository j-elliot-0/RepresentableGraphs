package path;

import graph.Vertex;

/***BEGIN CLASS PathList.java***************************************************
 * As part of a path matrix, this class represents an individual entry in that
 * type of matrix, that is, consisting of a set of paths.
 * 
 * @author julia
 *****************/public class PathList extends java.util.HashSet<Path> {/*****/

public PathList()
{	super();
}


public PathList addToPaths (Vertex n)
{	PathList newList = copy();
	
	for (Path path : newList)
		path.add(n);
	return newList;
}


public PathList copy()
{	PathList newList = new PathList();
	
	for (Path path : this) {
		Path newPath = new Path();
		for (Vertex node : path)
			newPath.add(node);
		newList.add(newPath);
	}
	return newList;
}


public String toString()
{	StringBuilder res = new StringBuilder();
	
	for (Path path : this)
		res.append(path);
	
	return res.toString();
}

/*****************/}/*********************************END CLASS PathList.java***/

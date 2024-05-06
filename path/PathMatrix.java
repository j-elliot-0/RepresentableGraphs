package path;

import util.Matrix;

/***BEGIN CLASS PathMatrix.java*************************************************
 * This class represents a matrix that will be the result of symbolic
 * multiplication of adjacency matrices. Specifically, each element of the
 * matrix will contain a list of paths with the row of the matrix as the origin
 * and the column as the destination. Each path will be length n where n is the
 * power the adjacency matrix has been raised to.
 * 
 * @author julia
 *****************/public class PathMatrix implements Matrix<PathList> {/*******/


//FIELDS-------------------------------/
private PathList[][] matrix;


//CONSTRUCTOR--------------------------/
public PathMatrix (int sz)
{	matrix = new PathList[sz][sz];
	
	for (int r = 0; r < matrix.length; r++)
		for (int c = 0; c < matrix.length; c++)
			matrix[r][c] = new PathList();
}


//METHODS------------------------------/
public int length()
{	return matrix.length;
}


public PathList get (int r, int c)
{	return matrix[r][c];
}


public void set (int r, int c, PathList s)
{	matrix[r][c] = s;
}

public String toString()
{	StringBuilder res = new StringBuilder();
	
	for (int r = 0; r < length(); r++){
		for (int c = 0; c < length(); c++)
			res.append("("+ toString(r,c) +")");
		res.append("\n");
	}
	
	return res.toString();
}


public String toString (int r, int c)
{	return (matrix[r][c].isEmpty()) ? "0" : matrix[r][c].toString();
}


/* return the first non-zero element along the main diagonal,
 * or null if none exists
 */
public PathList nonZeroAlongDiagonal()
{	for (int i = 0; i < matrix.length; i++)
		if (!matrix[i][i].isEmpty())
			return matrix[i][i];
	return null;
}


//Never used; do nothing
public void increase()
{	//
}


public void remove (int n)
{	//
}

/*****************/}/***END CLASS PathMatrix.java*******************************/

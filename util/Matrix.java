package util;

/***BEGIN INTERFACE Matrix.java*************************************************
 * Interface for an n*n matrix
 * 
 * @author julia
 ******************/public interface Matrix <E> {/******************************/


// The total number of rows/columns in the matrix, ie. n for this n*n matrix
// In practice it's just matrix.length
public int length()
;


public E get (int r, int c)
;


public void set (int r, int c, E s)
;


public String toString (int r, int c)
;


// Make this n*n matrix an n+1*n+1 matrix
public void increase()
;


// remove ith row/column, making this n*n matrix an n-1*n-1 matrix
public void remove (int i)
;

/*****************/}/*******************************END INTERFACE Matrix.java***/

package graph;

import path.Path;

/***BEGIN CLASS SemiTransitiveReason.java***************************************
 * Simple class that encapsulates the result of checking if an orientation is
 * semi-transitive, and a reason why, if it is not.
 * 
 * @author julia
 ******************/public class SemiTransitiveReason {/************************/

public static enum Reason {	SEMI_TRANSITIVE, CYCLE, SHORTCUT;};
public final Reason reason;
public final Path path;	//either a cycle or shortcut.


SemiTransitiveReason (Reason r, Path p)
{	reason = r;	path = p;
}


/* return whether semi-transitive or not
 */
public boolean result()
{	return reason.equals(Reason.SEMI_TRANSITIVE);
}

/******************/}/********************END CLASS SemiTransitiveReason.java***/

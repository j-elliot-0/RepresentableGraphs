package view;

//IMPORTS------------------------------/
import graph.Graph;
import graph.Vertex;
import graph.Edge;

import util.EdgeData;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Observer;
import java.util.Observable;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

/***BEGIN CLASS GraphCanvas.java************************************************
 * Canvas class which will hold visual representation of graphs.
 *
 * @author julia
 *****************/public class GraphCanvas extends Canvas/*********************/
					 implements Observer {

//IMPORTS------------------------------/
private Graph graph;
private Map<String, Point> nodes;	//mapping from node labels to coordinates

private Point centre, origin;

private Edge currentEdge; //records last edge to be created

//diameter and radius of a node-circle
static final int DIAM = 10;
static final int RAD = DIAM/2;

//CONSTRUCTOR--------------------------/
public GraphCanvas (Graph g)
{	super();
	graph = g;

	Dimension size = new Dimension(250, 500);
	setSize(size);
	setPreferredSize(size);

	initializeNodePositions();
}

//METHODS------------------------------/
public void setGraph (Graph g)
{	graph = g;
}



public void paint (Graphics g)
{	//paint each node on its ascribed point
	for (Point pt : nodes.values())
		//(offset by the radius so point refers to middle, not top-left)
		g.fillOval(pt.x-RAD, pt.y-RAD, DIAM, DIAM);

	//then draw each edge from the point of its 1st node to the point of its 2nd
	Point from, to;
	for (Edge e : graph.edges()) {
		from =	nodes.get(e.from.label);
		to   =	nodes.get(e.to.label);
		if (!e.from.equals(e.to))	{	//non-looping edge

			//highlight current edge
			if (e.equals(currentEdge))
				g.setColor(Color.RED);

			//line
			g.drawLine(from.x, from.y, to.x, to.y);

			//arrow, if the edge is directed
			if (graph.dirEdge(e.from, e.to))
				drawArrow(from, to, g);
			else if (graph.dirEdge(e.to, e.from))
				drawArrow(to, from, g);

			g.setColor(Color.BLACK); //reset colour

		}
		else //looping edge
			g.drawOval(to.x, to.y, 20, 20);
	}

	//finally, draw node labels last so they are unobstructed
	g.setColor(Color.RED);
	for (Map.Entry<String, Point> n : nodes.entrySet())
		g.drawString(n.getKey(), n.getValue().x-RAD, n.getValue().y-RAD);
}


private void drawArrow (Point from, Point to, Graphics g)
{	float midX = (from.x+to.x)/2;
	float midY = (from.y+to.y)/2;

	int qtrX = Math.round((to.x+midX)/2);
	int qtrY = Math.round((to.y+midY)/2);

	Point ar1 = new Point(qtrX, qtrY);
	Point ar2 = new Point(qtrX, qtrY);

	rotateAroundPoint(ar1, 10, to);
	rotateAroundPoint(ar2,-10, to);
	g.fillPolygon(
		new int[] {to.x, ar1.x, ar2.x},
		new int[] {to.y, ar1.y, ar2.y},
		3);
}


public void update (Observable o, Object arg)
{	if (arg != null) {
		EdgeData data = (EdgeData) arg;
		currentEdge = data.edge;
	}

	initializeNodePositions();
	repaint();
}


private void initializeNodePositions()
{	centre = new Point(getWidth()/2, getHeight()/2);
	origin = new Point(centre.x, centre.y-100);

	Set<Vertex> vertices = graph.vertices();

	nodes = new HashMap<String, Point>(vertices.size());
	Point pt = new Point(origin);
	int degrees = Math.round(360 / vertices.size());
	for (Vertex v : vertices) {
		nodes.put(v.label, new Point(pt));
		rotateAroundPoint(pt, degrees, centre);
	}
}


private static void rotateAroundPoint (Point pt, int deg, Point toPt)
{	pt.translate(-toPt.x, -toPt.y);
	
	rotateAroundOrigin (pt, deg);

	pt.translate(toPt.x, toPt.y);
}


private static void rotateAroundOrigin (Point pt, int deg)
{	double rad = Math.toRadians(deg);
	
	int newX = Math.round( (float)
			( pt.x*Math.cos(rad) - pt.y*Math.sin(rad) ));
	int newY = Math.round( (float)
			( pt.x*Math.sin(rad) + pt.y*Math.cos(rad) ));

	pt.x = newX;	pt.y = newY;
}

/*****************/}/******************************END CLASS GraphCanvas.java***/

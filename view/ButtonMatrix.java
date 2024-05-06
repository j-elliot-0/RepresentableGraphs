package view;

//IMPORTS------------------------------/
import util.Matrix;
import util.EdgeData;

import controller.MainController;

import java.util.Observer;
import java.util.Observable;
import java.util.Map;
import java.util.HashMap;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;


/***BEGIN CLASS ButtonMatrix.java***********************************************
 * 
 * @author julia
 *****************/public class ButtonMatrix extends JPanel/********************/
					 implements Matrix<JButton>, Observer {


//FIELDS-------------------------------/
private JButton[][] matrix;
private Matrix<Boolean> graph;
private Map<Integer,JLabel> topLabels;
private Map<Integer,JLabel> sideLabels;
private MainController controller;

private Point selected = null; //last selected button


//CONSTRUCTOR--------------------------/
public ButtonMatrix (Matrix<Boolean> a, MainController mc)
{	super();

	graph = a;
	controller = mc;

	newMatrix();

	Dimension size = new Dimension(500, 500);
	setSize(size);
	setPreferredSize(size);
	setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	setupMatrixButtons();
}


//METHODS------------------------------/
public int length()
{	return matrix.length;
}


public JButton get (int r, int c) 
{	return matrix[r][c];
}


public void set (int r, int c, JButton s)
{	matrix[r][c] = s;
}


public String toString (int r, int c)
{	return matrix[r][c].getText();
}


//for increase and remove, it's simpler to just redraw everything
public void increase()
{	removeAll();
	setupMatrixButtons();
}


public void remove (int n)
{	removeAll();
	setupMatrixButtons();
}


public void setGraph (Matrix<Boolean> g)
{	graph = g;
}


private void newMatrix()
{	matrix = new JButton[graph.length()][graph.length()];
	
	for (int r = 0; r < length(); r++)
		for (int c = 0; c < length(); c++)
			matrix[r][c] = new JButton();
}


/* Setup an n*n matrix of buttons that will represent the graph
 */
private void setupMatrixButtons()
{	setLayout(new GridLayout(graph.length()+1, graph.length()+1));
	newMatrix();

	add(new JLabel());
	topLabels = new HashMap<Integer,JLabel>();
	for (int i = 0; i < graph.length(); i++) {
		JLabel l = new JLabel(graph.toString(-1, i), SwingConstants.CENTER);
		topLabels.put(i, l);
		add(l);
	}

	sideLabels = new HashMap<Integer,JLabel>();
	for (int r = 0; r < length(); r++) {
		JLabel l = new JLabel(graph.toString(r, -1), SwingConstants.CENTER);
		sideLabels.put(r, l);
		add(l);
		for (int c = 0; c < length(); c++) {
			if (r != c) {
				JButton b = get(r, c);
				add(b);
				b.setText(graph.toString(r, c));
				b.addActionListener(controller);
				b.setActionCommand("edge " + 
						graph.toString(r,-1) + " " + graph.toString(-1,c));
			} else {	//main diagonal, make non-selectable to prevent loops
				JLabel b = new JLabel("*", SwingConstants.CENTER);
				add(b);
			}
		}
	}
}


/* Basically, refresh all buttons so they correctly show the current state
 */
public void update (Observable o, Object arg)
{	//if only one egde has changed
	if (arg != null) {

		//un-highlight last selected button, if any
		if (selected != null) {
			get(selected.x, selected.y).setForeground(Color.BLACK);
			sideLabels.get(selected.x).setForeground(Color.BLACK);
			topLabels.get(selected.y).setForeground(Color.BLACK);
		}

		EdgeData data = (EdgeData) arg;
		selected = data.indices;
		int x = selected.x, y = selected.y;
		
		//change selected edge
		get(x, y).setText(graph.toString(x, y));
		get(y, x).setText(graph.toString(y, x));

		//highlight selected button
		get(x, y).setForeground(Color.RED);
		sideLabels.get(x).setForeground(Color.RED);
		topLabels.get(y).setForeground(Color.RED);
	}

	//if only the size of graph has changed
	else if (graph.length() > length()) {
		selected = null;
		increase();
	}
	else if (graph.length() < length()) {
		selected = null;
		remove(length()-1);

	} else { //else, just refresh all buttons

		for (int r = 0; r < length(); r++) {
			topLabels.get(r).setForeground(Color.BLACK);
			for (int c = 0; c < length(); c++) {
				get(r, c).setText(graph.toString(r, c));
				get(r, c).setForeground(Color.BLACK);
				sideLabels.get(c).setForeground(Color.BLACK);
			}
		}
	}
}

/*****************/}/*****************************END CLASS ButtonMatrix.java***/

package view.sidepanel;

//IMPORTS------------------------------/
import util.Matrix;

import controller.MainController;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import javax.swing.plaf.basic.BasicArrowButton;


/***BEGIN CLASS SidePanel.java**************************************************
 * The side-panel part of the program's view. Contains useful buttons for
 * setting up the graph, such as changing graph size and toggling between
 * oriented and non-oriented edges.
 * 
 * @author julia
 *****************/public class SidePanel extends JPanel/***********************/
					 implements ActionListener {


//FIELDS-------------------------------/
private Matrix<Boolean> graph;


//CONSTRUCTOR--------------------------/
public SidePanel(Matrix<Boolean> g, MainController controller)
{	super(new GridLayout(5,1));
	setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	graph = g;

	add(new OrientedSelectionPanel(controller));

	addArrowButton("up", SwingConstants.NORTH);
	addArrowButton("down", SwingConstants.SOUTH);

	addButton("clear");
	addButton("clear orientations");
}


//METHODS------------------------------/
private void addArrowButton (String label, int direction)
{	JButton button = new BasicArrowButton(direction);
	button.setActionCommand(label);
	button.addActionListener(this);
	add(button);
}


private void addButton (String label)
{	JButton button = new JButton(label);
	button.setActionCommand(label);
	button.addActionListener(this);
	add(button);
}


public void setGraph (Matrix<Boolean> g)
{	graph = g;
}


public void actionPerformed (ActionEvent e)
{	if (e.getActionCommand().equals("up"))
		graph.remove(graph.length()-1);
	else if (e.getActionCommand().equals("down"))
		graph.increase();
	else if (e.getActionCommand().equals("clear"))
		for (int r = 0; r < graph.length(); r++)
			for (int c = 0; c < graph.length(); c++)
				graph.set(r,c,false);
	else //clear orientations
		for (int r = 0; r < graph.length()-1; r++)
			for (int c = r+1; c < graph.length(); c++)
				if (graph.get(r,c)) graph.set(c,r, true);
				else if (graph.get(c,r)) graph.set(r,c, true);
}

/*****************/}/********************************END CLASS SidePanel.java***/

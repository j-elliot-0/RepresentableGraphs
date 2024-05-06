package view;

//IMPORTS------------------------------/
import controller.BottomPanelController;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JButton;


/***BEGIN CLASS BottomPanel.java************************************************
 * 
 * @author julia
 *****************/public class BottomPanel extends JPanel {/*******************/


//FIELDS-------------------------------/
private BottomPanelController controller;


//CONSTRUCTOR--------------------------/
public BottomPanel (BottomPanelController c)
{	super(new GridLayout(3,1));

	controller = c;

	add("Is this orientation semi-transitive?");
	add("Is this graph word-representable?");
	add("Get word representing this graph");
}


//METHODS------------------------------/
void add (String label)
{	JButton button = new JButton(label);
	button.setActionCommand(label);
	button.addActionListener(controller);
	add(button);
}


/*****************/}/******************************END CLASS BottomPanel.java***/

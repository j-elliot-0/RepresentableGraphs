package view.sidepanel;

//IMPORTS------------------------------/
import controller.MainController;
import controller.MainController.Mode;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;


/***BEGIN CLASS OrientedSelectionPanel******************************************
 *	Contains the part of the sidepanel that lets the user switch between setting
 *	oriented and non-oriented edges.
 * @author julia
 *****************/public class OrientedSelectionPanel extends JPanel/**********/
					 implements ActionListener {


//FIELDS-------------------------------/
private MainController controller;

private ButtonGroup group;


//CONSTRUCTOR--------------------------/
public OrientedSelectionPanel(MainController c)
{	super(new GridLayout(2,1));
	setBorder(new RadioButtonBorder(null,null,null,null));
	
	controller = c;
	
	group = new ButtonGroup();
	add("non-oriented", true);
	add("oriented", false);
}


//METHODS------------------------------/
private void add (String label, boolean selected)
{	JRadioButton button = new JRadioButton(label, selected);
	button.setActionCommand(label);
	button.addActionListener(this);
	group.add(button);
	add(button);
}


public void actionPerformed(ActionEvent e) {
	if (e.getActionCommand().equals("oriented"))
		controller.mode = Mode.DIRECTED;
	else controller.mode = Mode.UNDIRECTED;
}

/*****************/}/*******************END CLASS OrientedSelectionPanel.java***/

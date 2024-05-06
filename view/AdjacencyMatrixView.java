package view;

//IMPORTS------------------------------/
import graph.AdjacencyMatrix;

import view.sidepanel.SidePanel;

import controller.AdjacencyMatrixBPanelController;
import controller.AdjacencyMatrixController;
import controller.BottomPanelController;
import controller.MainController;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


/***BEGIN CLASS AdjacencyMatrixView.java****************************************
 * 
 * @author julia
 *****************/public class AdjacencyMatrixView implements Observer{/*******/


//FIELDS-------------------------------/
private AdjacencyMatrix graph;

private MainController controller;
private BottomPanelController bpcontroller;

private JFrame frame;
private JMenuBar menuBar;

private GraphCanvas graphPane;
private SidePanel sidePane;
private BottomPanel bottomPane;

private ButtonMatrix buttons;


//CONSTRUCTOR--------------------------/
public AdjacencyMatrixView(AdjacencyMatrix g)
{	//make graph observable from this view
	graph = g;
	graph.addObserver(this);

	//initialize controller
	controller = new AdjacencyMatrixController(graph, this);

	frame = new JFrame("Word-representable Graphs");

	setupMenu();
	
	//setup matrix of buttons
	buttons = new ButtonMatrix(graph, controller);
	frame.getContentPane().add(buttons, BorderLayout.CENTER);
	graph.addObserver(buttons);

	//setup graph-drawing canvas
	graphPane = new GraphCanvas(graph);
	frame.getContentPane().add(graphPane, BorderLayout.EAST);
	graph.addObserver(graphPane);

	//setup side pane
	sidePane = new SidePanel(graph, controller);
	frame.getContentPane().add(sidePane, BorderLayout.WEST);

	//setup bottom pane
	bpcontroller = new AdjacencyMatrixBPanelController(graph, this);
	bottomPane = new BottomPanel(bpcontroller);
	frame.getContentPane().add(bottomPane, BorderLayout.SOUTH);

	setupWindow();
}


//METHODS------------------------------/
public JFrame getFrame()
{	return frame;
}


/* change the graph model for the whole view
 */
public void setGraph (AdjacencyMatrix g)
{	graph = g;
	graph.addObserver(this);

	graphPane.setGraph(graph);
	graph.addObserver(graphPane);

	buttons.setGraph(graph);
	graph.addObserver(buttons);

	//update everyone's view
	graph.refresh();

	controller.setGraph(graph);

	bpcontroller.setGraph(graph);

	sidePane.setGraph(graph);
}


private void setupWindow()
{	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	frame.pack();
	frame.setVisible(true);
}


private void setupMenu()
{	menuBar = new JMenuBar();
	frame.setJMenuBar(menuBar);

	final int ctrl = KeyEvent.CTRL_MASK;

	//file menu
	JMenu fileMenu = new JMenu("File");
	menuBar.add(fileMenu);

	addMenuItem("New" , fileMenu, KeyStroke.getKeyStroke(KeyEvent.VK_N, ctrl));
	addMenuItem("Load", fileMenu, KeyStroke.getKeyStroke(KeyEvent.VK_L, ctrl));
	addMenuItem("Save", fileMenu, KeyStroke.getKeyStroke(KeyEvent.VK_S, ctrl));
	addMenuItem("Quit", fileMenu, KeyStroke.getKeyStroke(KeyEvent.VK_Q, ctrl));

	//Word menu
	JMenu wordMenu = new JMenu("Word");
	menuBar.add(wordMenu);

	addMenuItem("Load graph from word", wordMenu,
			KeyStroke.getKeyStroke(KeyEvent.VK_W, ctrl));
	addMenuItem("Check if word represents this graph", wordMenu, 
			KeyStroke.getKeyStroke(KeyEvent.VK_W, ctrl + KeyEvent.SHIFT_MASK));
	addMenuItem("Get uniform word from word", wordMenu, 
			KeyStroke.getKeyStroke(KeyEvent.VK_U, ctrl));

	//help menu
	JMenu helpMenu = new JMenu("Help");
	menuBar.add(helpMenu);

	addMenuItem("User guide", helpMenu,
			KeyStroke.getKeyStroke(KeyEvent.VK_H, ctrl));
}


private void addMenuItem (String l, JMenu menu, KeyStroke shortcut)
{	JMenuItem newItem = new JMenuItem(l);
	newItem.addActionListener(controller);
	newItem.setActionCommand(l);
	newItem.setAccelerator(shortcut);
	menu.add(newItem);
}


public void update (Observable o, Object arg)
{	frame.repaint();
}


/*****************/}/**********************END CLASS AdjacencyMatrixView.java***/

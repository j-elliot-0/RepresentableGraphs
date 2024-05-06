package controller;

//IMPORTS------------------------------/
import graph.Graph;
import graph.Vertex;
import graph.Edge;

import view.Guide;
import word.UniformWord;
import word.Word;

import java.util.Scanner;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

/***BEGIN CLASS MainController.java*********************************************
 * 
 * @author julia
 *****************/public abstract class MainController/************************/
 							  implements ActionListener {


//FIELDS-------------------------------/
private Graph model;

public enum Mode {DIRECTED, UNDIRECTED};
public Mode mode = Mode.UNDIRECTED;

static File lastDir = new File(System.getProperty("user.home"));
static final String wordMessage =
		"Enter word to %s.\n"+
		"Use brackets \"(\" & \")\" to denote letters that are more than one\n"+
		"character long.";

//CONSTRUCTOR--------------------------/
public MainController (Graph m)
{	model = m;
}


//METHODS------------------------------/
public void setGraph(Graph g)
{	model = g;
}


public void actionPerformed (ActionEvent e)
{	String comm = e.getActionCommand();

	if (comm.startsWith("edge")) {

		Scanner scan = new Scanner(comm);
		scan.next();
		//get 2 nodes of edge
		Vertex x = new Vertex(scan.next());
		Vertex y = new Vertex(scan.next());
		scan.close();
	
		if (mode == Mode.DIRECTED)
			if (model.dirEdge(x, y))
				model.unsetEdge(x, y);
			else model.setDirEdge(x, y);
		else //undirected
			if (model.undirEdge(x, y))
				model.unsetEdge(x, y);
			else model.setEdge(x, y);

	}
	else if (comm.equals("New" ))
		newGraph();

	else if (comm.equals("Load"))
		loadGraph();

	else if (comm.equals("Save"))
		saveGraph();

	else if (comm.equals("Quit"))
		quit();


	else if (comm.equals("Load graph from word"))
		loadWord();

	else if (comm.equals("Check if word represents this graph"))
		checkWord();

	else if (comm.equals("Get uniform word from word"))
		getUniformWord();


	else if (comm.equals("User guide"))
		userGuide();
}


private void newGraph()
{	for (Edge e : model.edges())
		model.unsetEdge(e.from, e.to);
}


private void loadGraph()
{	JFileChooser fc = new JFileChooser(lastDir);
	if (fc.showOpenDialog(getFrame()) != JFileChooser.APPROVE_OPTION)
		return;
	lastDir = fc.getCurrentDirectory();

	loadGraphFromFile (fc.getSelectedFile());
}


private void saveGraph()
{	save(model.toString());
}


protected void showFileErrorMessage (IOException e)
{	JOptionPane.showMessageDialog(getFrame(), e.getMessage(),"Error",
				JOptionPane.ERROR_MESSAGE);
}


private void quit()
{	if (JOptionPane.showConfirmDialog(
			getFrame(),
			"Do you really want to quit?",
			"Quit?",
			JOptionPane.YES_NO_OPTION)

	== JOptionPane.YES_OPTION)
		System.exit(0);
}


private void loadWord()
{	String str = JOptionPane.showInputDialog(
			String.format(wordMessage, "load the graph it represents"));
	if (str == null) return;

	loadGraphFromWord(new Word(str));
}


private void checkWord()
{	String str = JOptionPane.showInputDialog(
			String.format(wordMessage, "verify if it represents this graph"));
	if (str == null) return;

	Word w = new Word(str);
	String res = model.wordRepresents(w) ?
		"The word "+w+" represents this graph!"
			: "The word "+w+" does NOT represent this graph";

	JOptionPane.showMessageDialog(getFrame(), res);
}

public void getUniformWord()
{	String str = JOptionPane.showInputDialog(
			String.format(wordMessage,
				"obtain a uniform word representing the same graph"));
	if (str == null) return;

	Word w = new Word(str);
	Word u = UniformWord.uniformWord(w);

	if (JOptionPane.showConfirmDialog(
				getFrame(),
				u + "\nDo you want to save this word?",
				"Uniform word",
				JOptionPane.YES_NO_OPTION)

	== JOptionPane.YES_OPTION)
		save(u.toString() + "\n");
}


private void userGuide()
{	JOptionPane.showMessageDialog(getFrame(), Guide.TEXT);
}


private void save (String data)
{	JFileChooser fc = new JFileChooser(lastDir);
	if (fc.showSaveDialog(getFrame()) != JFileChooser.APPROVE_OPTION)
		return;
	lastDir = fc.getCurrentDirectory();

	File f = fc.getSelectedFile();
	try {
		if (f.exists()) {
			int res = JOptionPane.showConfirmDialog(
				getFrame(),
				"File already exists. Do you want to overwrite it?",
				"Overwrite file?",
				JOptionPane.YES_NO_OPTION);
			if (res==JOptionPane.NO_OPTION || res==JOptionPane.CLOSED_OPTION)
				return;
		} else f.createNewFile();

		BufferedWriter buffer =
			new BufferedWriter(new FileWriter(f));
		buffer.write(data, 0, data.length());
		buffer.close();

	} catch (IOException e) {
		showFileErrorMessage(e);
	}
}


//ABSTRACT/PRIMITIVE METHODS-----------/
/* Get the parent frame for showing message dialogs in relation to it
 */
protected abstract JFrame getFrame()
;


protected abstract void loadGraphFromFile (File file)
;


protected abstract void loadGraphFromWord (Word word)
;


/*****************/}/***************************END CLASS MainController.java***/

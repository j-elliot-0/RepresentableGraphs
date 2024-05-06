package controller;

//IMPORTS------------------------------/
import graph.Graph;
import graph.SemiTransitiveReason;
import graph.SemiTransitiveReason.Reason;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

import word.Word;
import word.WordFromGraph;

/***BEGIN CLASS BottomPanelController.java**************************************
 * The controller for the bottom panel, which has functions for checking
 * semi-transitivity, word-representability and getting a word-representant.
 * 
 * @author julia
 *****************/public abstract class BottomPanelController/*****************/
							  implements ActionListener {


//FIELDS-------------------------------/
protected Graph model;


//CONSTRUCTOR--------------------------/
public BottomPanelController (Graph m)
{	model = m;
}


//METHODS------------------------------/
public void setGraph (Graph g)
{	model = g;
}


public void actionPerformed (ActionEvent e)
{	try {
		if (e.getActionCommand().equals(
				"Is this orientation semi-transitive?")) {
			SemiTransitiveReason res = model.semiTransitivelyOriented();
				
			String str;
			if (res.result())
				str = "Graph is semi-transitively oriented!";
			else {
				str = "Orientation contains a "+
					(res.reason.equals(Reason.CYCLE) ? "cycle" : "shortcut") +
					": " + res.path;
			}
			showResult (str, "Result", JOptionPane.INFORMATION_MESSAGE);
		}
		
		else {
			//whether to get word right away or ask for confirmation
			boolean getWord = e.getActionCommand().equals(
					"Get word representing this graph");
			
			boolean nonOriented = model.nonOriented();
			String str;
			Graph res = checkWordRepresentability();
			if (res != null) {
				
				if (getWord ||
					(!getWord && JOptionPane.showConfirmDialog(
					getFrame(),
					"Yes, this graph is word-representable.\n"+
					"Here's a semi-transitive orientation this graph admits.\n"+
					"Do you want to obtain a word representing this graph?",
					"Get Word?",
					JOptionPane.YES_NO_OPTION)

				== JOptionPane.YES_OPTION))
					
					getWord();
				


			} else {
				str = (nonOriented) ? "Not word-representable." :
					"This graph might be word-representable,\n" +
					"but there are no semi-transitive orientations with" +
					"these oriented edges.";

				showResult (str, "Result", JOptionPane.INFORMATION_MESSAGE);
			}
		}


	} catch (Exception ex) {
		showResult (ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
	} catch (OutOfMemoryError er) {
		showResult ("There's not enough memory to work out this graph!",
				"Error", JOptionPane.WARNING_MESSAGE);
	}
}


/* show word-representant, and show option to save it
 */
private void getWord()
{	WordFromGraph wfg = new WordFromGraph(model);
	Word word = wfg.generateWord();

	//Sanity check to make sure word actually represents the graph!
	if (!model.wordRepresents(word))
		throw new IllegalStateException(
				"Word-from-graph algorithm is wrong!");
	
	if (JOptionPane.showConfirmDialog(
			getFrame(),
			"This graph is represented by the uniform word: \n"+
			word+
			"\n Do you want to save the word to a file?",
			"Save Word?",
			JOptionPane.YES_NO_OPTION)
			
		== JOptionPane.YES_OPTION) {
		
			JFileChooser fc = new JFileChooser(MainController.lastDir);
			if (fc.showSaveDialog(getFrame()) != JFileChooser.APPROVE_OPTION)
				return;
			MainController.lastDir = fc.getCurrentDirectory();

			File f = fc.getSelectedFile();
			try {
				if (f.exists()) {
					int res = JOptionPane.showConfirmDialog(
						getFrame(),
						"File already exists. Do you want to overwrite it?",
						"Overwrite file?",
						JOptionPane.YES_NO_OPTION);
					if (res==JOptionPane.NO_OPTION ||
							res==JOptionPane.CLOSED_OPTION)
						return;
				} else f.createNewFile();

				BufferedWriter buffer =
						new BufferedWriter(new FileWriter(f));
				//write graph data
				String data = model.toString();
				buffer.write(data, 0, data.length());
				//then write word data
				data = "\nWord-representant:\n";
				buffer.write(data, 0, data.length());
				data = word.toString() + "\n";
				buffer.write(data, 0, data.length());
				buffer.close();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(getFrame(), e.getMessage(),"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}


private void showResult (String message, String title, int type)
{	JOptionPane.showMessageDialog(getFrame(), message, title, type);
}

//ABSTRACT/PRIMITIVE METHODS-----------/
/* Get the parent frame for drawing message dialogs
 */
protected abstract JFrame getFrame()
;

/* Check if the model graph is word-representable, and return a semi-transitive
 * orientation if so. Uses factory method.
 * throws Exception if the graph is already fully-oriented
 */
protected abstract Graph checkWordRepresentability() throws Exception
;

/*****************/}/********************END CLASS BottomPanelController.java***/

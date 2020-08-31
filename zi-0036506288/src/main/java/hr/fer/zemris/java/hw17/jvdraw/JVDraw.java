package hr.fer.zemris.java.hw17.jvdraw;

import static hr.fer.zemris.java.hw17.jvdraw.Util.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.fer.zemris.java.hw17.jvdraw.gui.CustomObjectList;
import hr.fer.zemris.java.hw17.jvdraw.gui.JColorArea;
import hr.fer.zemris.java.hw17.jvdraw.gui.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.gui.StatusBar;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.Tool;
import hr.fer.zemris.java.hw17.jvdraw.models.DrawingObjectListModel;
import hr.fer.zemris.java.hw17.jvdraw.models.JVDrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.tools.CircleTool;
import hr.fer.zemris.java.hw17.jvdraw.tools.FilledCircleTool;
import hr.fer.zemris.java.hw17.jvdraw.tools.FilledTriangleTool;
import hr.fer.zemris.java.hw17.jvdraw.tools.LineTool;

/**
 * This class contains the main method and is therefore the core class
 * of this GUI application. The program starts by simply initializing
 * the GUI thread and it then does the work necessary to make the app
 * work. 
 * 
 * @author Ivan Skorupan
 */
public class JVDraw extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Currently selected drawing tool.
	 */
	private Tool currentTool;

	/**
	 * The drawing model upon which this drawing application operates
	 * and with which it manages objects drawn inside its canvas.
	 */
	private DrawingModel model = new JVDrawingModel();

	/**
	 * Path of currently opened document.
	 * <p>
	 * If this variable is a <code>null</code> reference, then the document
	 * has never been saved yet (but it could still have been modified even
	 * if this variable is not <code>null</code>).
	 */
	private Path openedDocumentPath;

	/**
	 * An instance of {@link JFileChooser} used throughout this program
	 * to create message dialogues (used in several action implementations
	 * so is instantiated here for convenience).
	 */
	private JFileChooser jfc = new JFileChooser();

	/**
	 * Canvas on which all elements are drawn.
	 */
	private JDrawingCanvas canvas;

	/**
	 * Foreground color area is a foreground color provider.
	 */
	private JColorArea fgColorArea;

	/**
	 * Background color area is a background color provider.
	 */
	private JColorArea bgColorArea;

	/**
	 * Constructs a new {@link JVDraw} object.
	 */
	public JVDraw() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("JVDraw");
		setSize(800, 600);
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit.actionPerformed(null);
			}
		});
		initGUI();
		currentTool = new LineTool(model, canvas, fgColorArea);
	}

	/**
	 * Initializes and places GUI components on the window.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		JPanel editorPanel = new JPanel(new BorderLayout());
		cp.add(editorPanel);

		CustomObjectList objectList = new CustomObjectList(new DrawingObjectListModel(model));
		editorPanel.add(objectList, BorderLayout.LINE_END);

		canvas = new JDrawingCanvas(model, () -> {return currentTool;});
		editorPanel.add(canvas, BorderLayout.CENTER);

		createToolbarAndStatusBar(editorPanel);
		createMenuBar();
		createActions();
	}
	
	/**
	 * Helper method which configures all actions defined in this class.
	 */
	private void createActions() {
		line.putValue(Action.NAME, "Line");
		line.putValue(Action.SHORT_DESCRIPTION, "Line Tool");
		line.setEnabled(true);

		circle.putValue(Action.NAME, "Circle");
		circle.putValue(Action.SHORT_DESCRIPTION, "Circle Tool");
		circle.setEnabled(true);

		filledCircle.putValue(Action.NAME, "Filled Circle");
		filledCircle.putValue(Action.SHORT_DESCRIPTION, "Filled Circle Tool");
		filledCircle.setEnabled(true);
		
		filledTriangle.putValue(Action.NAME, "Filled Triangle");
		filledTriangle.putValue(Action.SHORT_DESCRIPTION, "Filled Triangle Tool");
		filledTriangle.setEnabled(true);
		
		saveAs.putValue(Action.NAME, "Save As");
		saveAs.putValue(Action.SHORT_DESCRIPTION, "Save document as...");
		saveAs.setEnabled(true);
		
		save.putValue(Action.NAME, "Save");
		save.putValue(Action.SHORT_DESCRIPTION, "Save document");
		save.setEnabled(true);
		
		open.putValue(Action.NAME, "Open");
		open.putValue(Action.SHORT_DESCRIPTION, "Open document");
		open.setEnabled(true);
		
		export.putValue(Action.NAME, "Export...");
		export.putValue(Action.SHORT_DESCRIPTION, "Export document as image");
		export.setEnabled(true);
		
		exit.putValue(Action.NAME, "Exit");
		exit.putValue(Action.SHORT_DESCRIPTION, "Exit application");
		exit.setEnabled(true);
	}

	/**
	 * Helper method that creates and places this GUI application's
	 * toolbar and status bar.
	 * 
	 * @param editorPanel - a panel on which to place the toolbar and status bar
	 */
	private void createToolbarAndStatusBar(JPanel editorPanel) {
		JToolBar toolbar = new JToolBar("Tools");
		toolbar.setFloatable(true);

		fgColorArea = new JColorArea(Color.RED);
		bgColorArea = new JColorArea(Color.BLUE);

		JToggleButton lineButton = new JToggleButton(line);
		JToggleButton circleButton = new JToggleButton(circle);
		JToggleButton filledCircleButton = new JToggleButton(filledCircle);
		JToggleButton filledTriangleButton = new JToggleButton(filledTriangle);
		lineButton.setFont(new Font("Arial", Font.ITALIC, 15));
		circleButton.setFont(new Font("Arial", Font.ITALIC, 15));
		filledCircleButton.setFont(new Font("Arial", Font.ITALIC, 15));
		filledTriangleButton.setFont(new Font("Arial", Font.ITALIC, 15));

		ButtonGroup drawingToolsGroup = new ButtonGroup();
		drawingToolsGroup.add(lineButton);
		drawingToolsGroup.add(circleButton);
		drawingToolsGroup.add(filledCircleButton);
		drawingToolsGroup.add(filledTriangleButton);

		lineButton.setSelected(true);

		toolbar.add(fgColorArea);
		toolbar.addSeparator();
		toolbar.add(bgColorArea);
		toolbar.addSeparator();
		toolbar.add(lineButton);
		toolbar.add(circleButton);
		toolbar.add(filledCircleButton);
		toolbar.add(filledTriangleButton);

		StatusBar statusBar = new StatusBar(fgColorArea, bgColorArea);

		editorPanel.add(toolbar, BorderLayout.PAGE_START);
		editorPanel.add(statusBar, BorderLayout.PAGE_END);
	}

	/**
	 * Helper method that creates and sets this GUI application's menu bar.
	 */
	private void createMenuBar() {
		JMenuBar menu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		
		menu.add(fileMenu);
		fileMenu.add(open);
		fileMenu.add(save);
		fileMenu.add(saveAs);
		fileMenu.add(export);
		fileMenu.add(exit);

		setJMenuBar(menu);
	}
	
	/**
	 * This action changes the current selected tool to a {@link LineTool}.
	 */
	private final Action line = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			currentTool = new LineTool(model, canvas, fgColorArea);
		}
	};

	/**
	 * This action changes the current selected tool to a {@link CircleTool}.
	 */
	private final Action circle = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			currentTool = new CircleTool(model, canvas, fgColorArea);
		}
	};

	/**
	 * This action changes the current selected tool to a {@link FilledCircleTool}.
	 */
	private final Action filledCircle = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			currentTool = new FilledCircleTool(model, canvas, fgColorArea, bgColorArea);
		}
	};
	
	/**
	 * This action changes the current selected tool to a {@link FilledTriangleTool}.
	 */
	private final Action filledTriangle = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			currentTool = new FilledTriangleTool(model, canvas, fgColorArea, bgColorArea);
		}
	};

	/**
	 * This action performs file saving regardless of whether or not there
	 * were any changes to the document.
	 */
	private final Action saveAs = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(jfc.showSaveDialog(JVDraw.this) == JFileChooser.APPROVE_OPTION) {
				Path selectedPath = jfc.getSelectedFile().toPath();
				performSaving(selectedPath, true);
			}
		}
	};

	/**
	 * This action will perform a saving operation if the document
	 * was not saved since its creation or if it was modified.
	 */
	private final Action save = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(openedDocumentPath == null) {
				saveAs.actionPerformed(e);
			} else {
				if(model.isModified()) {
					performSaving(openedDocumentPath, false);
				}
			}
		}
	};

	/**
	 * This action asks the user to save changes if document was modified and
	 * asks the user to choose a file to be opened, after which the selected
	 * file is open.
	 */
	private final Action open = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!saveBeforeClose("Do you want to save the changes before opening another document?", e)) {
				return;
			}

			if(jfc.showOpenDialog(JVDraw.this) == JFileChooser.APPROVE_OPTION) {
				Path selectedPath = jfc.getSelectedFile().toPath();
				try {
					openFile(selectedPath, model);
					openedDocumentPath = selectedPath;
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(JVDraw.this, ex.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	};

	/**
	 * This action asks the user for a file export location and then runs
	 * the export process where it creates an image from the document and
	 * exports it to the selected location in JPG, PNG or GIF format.
	 */
	private final Action export = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, PNG and GIF images", "jpg", "png", "gif");
			jfc.setFileFilter(filter);
			if(jfc.showSaveDialog(JVDraw.this) == JFileChooser.APPROVE_OPTION) {
				if(jfc.getFileFilter().accept(jfc.getSelectedFile())) {
					Path selectedPath = jfc.getSelectedFile().toPath();
					try {
						export(selectedPath, model);
						JOptionPane.showMessageDialog(JVDraw.this, "The image was successfully exported!",
								"Successful Export", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(JVDraw.this, ex.getMessage(),
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(JVDraw.this, "The file must have one of these extensions: JPG, PNG, GIF!",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}			
			jfc.setFileFilter(null);
			jfc.resetChoosableFileFilters();
		}
	};

	/**
	 * This action exits the application but first asks the user to save their work.
	 */
	private final Action exit = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!saveBeforeClose("Do you want to save the changes before exiting the application?", e)) {
				return;
			}

			dispose();
		}
	};
	
	/**
	 * This method is run whenever there is an action running which needs to
	 * close the current document in some way.
	 * <p>
	 * This method will then ask the user to save their work in case the document
	 * isn't saved already.
	 * 
	 * @param message - saving information message to user
	 * @param e - action event object reference
	 * @return <code>true</code> if the document was saved or is saved, <code>false</code> otherwise
	 */
	private boolean saveBeforeClose(String message, ActionEvent e) {
		if(openedDocumentPath == null && !model.isModified()) {
			return true;
		}
		
		if(openedDocumentPath == null || model.isModified()) {
			int option = JOptionPane.showConfirmDialog(JVDraw.this, message,
					"Save changes?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(option == JOptionPane.YES_OPTION) {
				save.actionPerformed(e);
			} else if(option == JOptionPane.CANCEL_OPTION) {
				return false;
			}
		}

		return true;
	}

	/**
	 * This method performs the saving operation on given <code>documentPath</code>
	 * and runs as a "save as" operation if <code>saveAs</code> is <code>true</code>.
	 * 
	 * @param documentPath - document to be saved
	 * @param saveAs - flag that indicates if method should act as save as or normal save
	 */
	private void performSaving(Path documentPath, boolean saveAs) {
		try {
			saveFile(documentPath, model);
			if(saveAs) {
				openedDocumentPath = documentPath;
			}
			JOptionPane.showMessageDialog(JVDraw.this, "The document was sucessfully saved!",
					"Saved", JOptionPane.INFORMATION_MESSAGE);
		} catch(Exception ex) {
			JOptionPane.showMessageDialog(JVDraw.this, ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Starting point of this program.
	 * 
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JVDraw().setVisible(true);
		});
	}

}

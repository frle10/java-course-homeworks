package hr.fer.zemris.java.hw11.jnotepadpp;

import static hr.fer.zemris.java.hw11.jnotepadpp.utility.ListenerFactory.*;
import static hr.fer.zemris.java.hw11.jnotepadpp.utility.Utility.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.text.Collator;
import java.util.Locale;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.CaretListener;
import javax.swing.text.DefaultEditorKit;

import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LJMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizedLengthLabel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizedRowColSelLabel;
import hr.fer.zemris.java.hw11.jnotepadpp.utility.Utility;

/**
 * This is a GUI text editor program that is based on the popular
 * Notepad++ program. {@link JNotepadPP} is a much simpler version
 * of that text editor, but still contains some useful functionality.
 * <p>
 * Some of the features of this text editor are:
 * <ul>
 * 	<li>users can manage multiple documents at the same time</li>
 * 	<li>operations such as saving, closing and opening a document</li>
 * 	<li>sorting selected lines</li>
 * 	<li>letter case operations (to uppercase, to lowercase, case inversion)</li>
 * 	<li>remove identical lines from selection (keep only the first occurrence)</li>
 * 	<li>localization in 3 languages: Croatian, English and German</li>
 * 	<li>cut, copy and paste operations</li>
 * 	<li>view statistics of document: number of characters, non-space characters and lines</li>
 * 	<li>status bar with: number of characters, current caret line and column and selection size</li>
 * 	<li>clock on the right-hand side of the status bar</li>
 * </ul>
 * 
 * @author Ivan Skorupan
 */
public class JNotepadPP extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * The multiple document model this text editor is using to manage documents.
	 * <p>
	 * This object is also a {@link JTabbedPane}.
	 */
	private DefaultMultipleDocumentModel dmdm;

	/**
	 * Multiple document model of interface type through which we communicate
	 * with documents.
	 */
	private MultipleDocumentModel mdm;

	/**
	 * An instance of {@link JFileChooser} used throughout this program
	 * to create message dialogues (often used so is instantiated here for
	 * convenience).
	 */
	private JFileChooser jfc = new JFileChooser();

	/**
	 * Internally stored {@link Timer} object used for
	 * updating the clock in the status bar of this
	 * text editor.
	 */
	private Timer timer;

	/**
	 * A localization provider used for this window.
	 */
	private FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

	/**
	 * Constructs a new {@link JNotepadPP} object.
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("JNotepad++");
		setSize(800, 600);
		setLocationRelativeTo(null);
		addWindowListener(createWindowListener(this, exitApplication));
		dmdm = new DefaultMultipleDocumentModel(flp);
		mdm = dmdm;
		initGUI();
	}

	/**
	 * Initializes and places GUI components on the window.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		JPanel editorPanel = new JPanel(new BorderLayout());

		cp.add(editorPanel, BorderLayout.CENTER);
		editorPanel.add(dmdm, BorderLayout.CENTER);

		JPanel statusBar = new JPanel(new GridLayout(0, 3));
		statusBar.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		cp.add(statusBar, BorderLayout.PAGE_END);

		LocalizedLengthLabel length = new LocalizedLengthLabel("length", flp, mdm);
		LocalizedRowColSelLabel lineColumnSelection = new LocalizedRowColSelLabel("line", "column", "selection", flp, mdm);
		JLabel clock = new JLabel();

		length.setHorizontalAlignment(SwingConstants.LEFT);
		length.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		lineColumnSelection.setHorizontalAlignment(SwingConstants.LEFT);
		lineColumnSelection.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		clock.setHorizontalAlignment(SwingConstants.RIGHT);
		clock.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

		statusBar.add(length);
		statusBar.add(lineColumnSelection);
		statusBar.add(clock);

		int delay = 1000;
		timer = new Timer(delay, createTimeTaskPerformer(clock));
		timer.start();

		createActions();
		createMenus();
		createToolbar(editorPanel);
		
		SingleDocumentListener saveListener = createSaveListener(mdm, saveDocument);
		CaretListener caretListener = createStatusBarDocumentListener(mdm, length, lineColumnSelection, flp,
				toLowercase, toUppercase, invertCase, copy, cut, sortAscending, sortDescending, unique);
		mdm.addMultipleDocumentListener(createDynamicInfoListener(mdm, this, caretListener, flp, length, lineColumnSelection,
				toLowercase, toUppercase, invertCase, copy, cut, sortAscending, sortDescending, unique));
		mdm.addMultipleDocumentListener(createSaveCloseListener(mdm, saveDocument, saveAsDocument, closeDocument,
				paste, showStatistics, saveListener));
	}

	/**
	 * Helper method that configures all actions used in this text editor.
	 * <p>
	 * For each action, its accelerator key and mnemonic key are set.
	 */
	private void createActions() {
		openDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);

		saveDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		saveDocument.setEnabled(false);

		saveAsDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));
		saveAsDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
		saveAsDocument.setEnabled(false);

		newDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		newDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);

		closeDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control P"));
		closeDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		closeDocument.setEnabled(false);

		copy.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		copy.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Y);
		copy.setEnabled(false);

		cut.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		cut.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
		cut.setEnabled(false);

		paste.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		paste.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
		paste.setEnabled(false);

		showStatistics.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));
		showStatistics.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
		showStatistics.setEnabled(false);

		exitApplication.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
		exitApplication.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);

		toLowercase.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
		toLowercase.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
		toLowercase.setEnabled(false);

		toUppercase.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control U"));
		toUppercase.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
		toUppercase.setEnabled(false);

		invertCase.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
		invertCase.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
		invertCase.setEnabled(false);

		sortAscending.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		sortAscending.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
		sortAscending.setEnabled(false);

		sortDescending.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control M"));
		sortDescending.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
		sortDescending.setEnabled(false);

		unique.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt U"));
		unique.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		unique.setEnabled(false);
	}

	/**
	 * Helper method that creates the menus of this text editor program and takes care
	 * of all the actions, separators and buttons.
	 */
	private void createMenus() {
		JMenuBar mb = new JMenuBar();

		JMenu file = new LJMenu("file", flp);
		mb.add(file);
		file.add(new JMenuItem(newDocument));
		file.add(new JMenuItem(openDocument));
		file.add(new JMenuItem(saveDocument));
		file.add(new JMenuItem(saveAsDocument));
		file.add(new JMenuItem(closeDocument));
		file.addSeparator();
		file.add(new JMenuItem(exitApplication));

		JMenu edit = new LJMenu("edit", flp);
		mb.add(edit);
		edit.add(new JMenuItem(copy));
		edit.add(new JMenuItem(cut));
		edit.add(new JMenuItem(paste));

		JMenu view = new LJMenu("view", flp);
		mb.add(view);
		view.add(showStatistics);

		JMenu language = new LJMenu("language", flp);
		mb.add(language);
		language.add(new JMenuItem(croatian));
		language.add(new JMenuItem(english));
		language.add(new JMenuItem(german));

		JMenu tools = new LJMenu("tools", flp);
		mb.add(tools);
		JMenu changeCase = new LJMenu("case", flp);
		JMenu sort = new LJMenu("sort", flp);
		tools.add(changeCase);
		tools.add(sort);
		tools.add(unique);

		changeCase.add(toLowercase);
		changeCase.add(toUppercase);
		changeCase.add(invertCase);

		sort.add(sortAscending);
		sort.add(sortDescending);

		setJMenuBar(mb);
	}

	/**
	 * Helper method that creates the toolbar of this text editor and
	 * configures its buttons, separators and actions.
	 */
	private void createToolbar(JPanel editorPanel) {
		JToolBar tb = new JToolBar();
		tb.setFloatable(true);

		tb.add(new JButton(newDocument));
		tb.add(new JButton(openDocument));
		tb.add(new JButton(saveDocument));
		tb.add(new JButton(saveAsDocument));
		tb.add(new JButton(closeDocument));
		tb.addSeparator();
		tb.add(new JButton(copy));
		tb.add(new JButton(cut));
		tb.add(new JButton(paste));
		tb.addSeparator();
		tb.add(new JButton(showStatistics));
		tb.addSeparator();
		tb.add(new JButton(exitApplication));

		editorPanel.add(tb, BorderLayout.PAGE_START);
	}

	/**
	 * This action checks if a selection exists in current document and
	 * if so, analyzes all the selected lines (if a line was partially
	 * selected by the user, it <b>is</b> taken into account) and removes
	 * any duplicate lines (only the first occurrence is kept).
	 */
	private final Action unique = new LocalizableAction("unique", null, flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			unique(mdm.getCurrentDocument());
		}
	};

	/**
	 * This action checks if there are any selected lines and if so,
	 * sorts the lines in ascending order.
	 */
	private final Action sortAscending = new LocalizableAction("ascending", null, flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			sortSelection(mdm.getCurrentDocument(), (s1, s2) -> getLocalizedCollator().compare(s1, s2));
		}
	};

	/**
	 * This action checks if there are any selected lines and if so,
	 * sorts the lines in descending order.
	 */
	private final Action sortDescending = new LocalizableAction("descending", null, flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			sortSelection(mdm.getCurrentDocument(), (s1, s2) -> getLocalizedCollator().compare(s2, s1));
		}
	};

	/**
	 * Transforms all letters in current selection to lowercase letters.
	 */
	private final Action toLowercase = new LocalizableAction("lowercase", null, flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			performCaseOperation(mdm.getCurrentDocument(), (s) -> s.toLowerCase());
		}
	};

	/**
	 * Transforms all letters in current selection to uppercase letters.
	 */
	private final Action toUppercase = new LocalizableAction("uppercase", null, flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			performCaseOperation(mdm.getCurrentDocument(), (s) -> s.toUpperCase());
		}
	};

	/**
	 * Inverts the case of all letters in current selection.
	 */
	private final Action invertCase = new LocalizableAction("invert", null, flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			performCaseOperation(mdm.getCurrentDocument(), (s) -> invertCase(s));
		}
	};

	/**
	 * Changes the current language to Croatian application-wide.
	 */
	private final Action croatian = new LocalizableAction("croatian", null, flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("hr");
		}
	};

	/**
	 * Changes the current language to English application-wide.
	 */
	private final Action english = new LocalizableAction("english", null, flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("en");
		}
	};

	/**
	 * Changes the current language to German application-wide.
	 */
	private final Action german = new LocalizableAction("german", null, flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("de");
		}
	};

	/**
	 * Copies the selected text to clipboard.
	 */
	private final Action copy = new LocalizableAction("copy", "copy_desc", flp) {

		private static final long serialVersionUID = 1L;

		private Action copyAction = new DefaultEditorKit.CopyAction();

		@Override
		public void actionPerformed(ActionEvent e) {
			copyAction.actionPerformed(e);
		}
	};

	/**
	 * Cuts the selected text.
	 */
	private final Action cut = new LocalizableAction("cut", "cut_desc", flp) {

		private static final long serialVersionUID = 1L;

		private Action cutAction = new DefaultEditorKit.CutAction();

		@Override
		public void actionPerformed(ActionEvent e) {
			cutAction.actionPerformed(e);
		}
	};

	/**
	 * Pasted text from clipboard into the document.
	 */
	private final Action paste = new LocalizableAction("paste", "paste_desc", flp) {

		private static final long serialVersionUID = 1L;

		private Action pasteAction = new DefaultEditorKit.PasteAction();

		@Override
		public void actionPerformed(ActionEvent e) {
			pasteAction.actionPerformed(e);
		}
	};

	/**
	 * Calculates currently opened document statistics, which includes
	 * number of characters, number of non-space characters and number
	 * of lines in the document and then shows the results in a
	 * {@link JOptionPane}.
	 */
	private final Action showStatistics = new LocalizableAction("stat", "stat_desc", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel currentDocument = mdm.getCurrentDocument();
			
			String message = flp.getString("your_doc_has") + " " + documentLength(currentDocument) + " " + flp.getString("chars") +
					", " + documentLengthWithoutSpaces(currentDocument) + " " + flp.getString("non_blank_chars") + " " +
					numberOfLines(currentDocument) + " " + flp.getString("lines") + ".";
			showInfoPopup(message, flp.getString("stat"));
		}
	};

	/**
	 * Creates a new document (automatically adds a new tab to the tabbed pane).
	 */
	private final Action newDocument = new LocalizableAction("new", "new_desc", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			mdm.createNewDocument();
		}
	};

	/**
	 * Opens an existing document from disk.
	 */
	private final Action openDocument = new LocalizableAction("open", "open_desc", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			jfc.setDialogTitle(flp.getString("open") + " " + flp.getString("file").toLowerCase());
			if(jfc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			Path openedFilePath = jfc.getSelectedFile().toPath();
			mdm.loadDocument(openedFilePath);
		}
	};

	/**
	 * Always prompts the user to choose a saving destination for current document.
	 */
	private final Action saveAsDocument = new LocalizableAction("save_as", "save_as_desc", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			performSaving(mdm.getCurrentDocument(), null);
		}
	};

	/**
	 * Saves the current document to its file path if such exists. If the file
	 * wasn't yet saved, the action prompts the user to choose the saving path.
	 */
	private final Action saveDocument = new LocalizableAction("save", "save_desc", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			performSaving(mdm.getCurrentDocument(), mdm.getCurrentDocument().getFilePath());
		}
	};

	/**
	 * Closes the current document (automatically removes its tab in the tabbed pane).
	 */
	private final Action closeDocument = new LocalizableAction("close", "close_desc", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(mdm.getCurrentDocument().isModified()) {
				String[] options = new String[] {flp.getString("yes"), flp.getString("no"), flp.getString("cancel")};
				int option = showQuestionPopup(flp.getString("save_first"), flp.getString("save_bef_close"), JOptionPane.YES_NO_CANCEL_OPTION, options);

				if(option == JOptionPane.YES_OPTION) {
					performSaving(mdm.getCurrentDocument(), mdm.getCurrentDocument().getFilePath());
				} else if(option == JOptionPane.CANCEL_OPTION) {
					return;
				}
			}

			mdm.closeDocument(mdm.getCurrentDocument());
		}
	};

	/**
	 * Exits the application, but prompts the user to save any unsaved files first.
	 */
	private final Action exitApplication = new LocalizableAction("exit", "exit_desc", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			String[] options = new String[] {flp.getString("yes"), flp.getString("no")};
			int option = showQuestionPopup(flp.getString("exit_sure"), flp.getString("exit_app"), JOptionPane.YES_NO_OPTION, options);

			if(option == JOptionPane.YES_OPTION) {
				for(int i = 0; i < mdm.getNumberOfDocuments(); i++) {
					SingleDocumentModel document = mdm.getDocument(i);
					if(document.isModified()) {
						mdm.activateDocument(document);
						option = showQuestionPopup(flp.getString("save") + " " + flp.getString("file").toLowerCase() + " " +
								getFileName(document) + " " + flp.getString("first") + "?", flp.getString("save"),
								JOptionPane.YES_NO_OPTION,
								options
						);

						if(option == JOptionPane.YES_OPTION) {
							performSaving(document, null);
						}
					}
				}
			} else if(option == JOptionPane.NO_OPTION) {
				return;
			}

			timer.stop();
			dispose();
		}
	};

	/**
	 * Starting point of this program.
	 * 
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JNotepadPP().setVisible(true);
		});
	}

	/**
	 * Shows an informational window, actually an option dialog
	 * that has an OK button and provided <code>title</code>
	 * and <code>message</code>.
	 * 
	 * @param message - message to show to the user
	 * @param title - title of the option dialog
	 * @return an integer indicating the option chosen by the user
	 */
	private int showInfoPopup(String message, String title) {
		return JOptionPane.showOptionDialog(this,
				message,
				title,
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, 
				null, null, null
		);
	}
	
	/**
	 * Shows a dialog that asks the user to decide on a
	 * certain operation to be performed.
	 * <p>
	 * The window has the given <code>title</code>, <code>message</code>
	 * and <code>option</code>.
	 * 
	 * @param message - message to show to the user
	 * @param title - title of the option dialog
	 * @param option - options to provide to the user
	 * @param options - localized strings for options provided to the user
	 * @return an integer indicating the option chosen by the user
	 */
	private int showQuestionPopup(String message, String title, int option, String[] options) {
		return JOptionPane.showOptionDialog(this,
				message,
				title,
				option,
				JOptionPane.QUESTION_MESSAGE,
				null, options, null
		);
	}

	/**
	 * Gets the name of given <code>document</code> if such exists.
	 * <p>
	 * If the document hasn't been saved yet then it has no name and so
	 * the {@link Utility#DEFAULT_FILE_NAME DEFAULT_FILE_NAME} is returned.
	 * 
	 * @param document - document whose name to get
	 * @return name of <code>document</code> or default name if document hasn't been saved yet
	 */
	private String getFileName(SingleDocumentModel document) {
		return document.getFilePath() == null ? flp.getString("unnamed") : document.getFilePath().getFileName().toString();
	}
	
	/**
	 * Opens a save dialog to prompt the user to save
	 * the file.
	 * <p>
	 * If the user decided not to save the file, <code>null</code>
	 * is returned, otherwise the chosen path is returned.
	 * 
	 * @param dialogTitle - title of the save dialog
	 * @return chosen save path or <code>null</code> if save operation was canceled
	 */
	private Path askForSave(String dialogTitle) {
		jfc.setDialogTitle(dialogTitle);
		if(jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		return jfc.getSelectedFile().toPath();
	}

	/**
	 * Performs a save operation based on given <code>document</code>
	 * and <code>savePath</code>.
	 * <p>
	 * If <code>savePath</code> is <code>null</code>, the user is prompted
	 * to choose the saving destination, otherwise the file is immediately
	 * saved to the given path.
	 * <p>
	 * If the user was prompted to save the file but decided not to, then
	 * the method does nothing and returns.
	 * 
	 * @param document - document to save
	 * @param savePath - save destination
	 */
	private void performSaving(SingleDocumentModel document, Path savePath) {
		savePath = (savePath == null) ? askForSave(flp.getString("save")) : savePath;
		if(savePath == null) return;
		mdm.saveDocument(document, savePath);
	}

	/**
	 * Generates and returns a {@link Collator} object synchronized
	 * with current language in the application as tracked by
	 * {@link LocalizationProvider}. 
	 * 
	 * @return a {@link Collator} object with {@link Locale} set to current application language
	 */
	private Collator getLocalizedCollator() {
		Locale locale = new Locale(LocalizationProvider.getInstance().getCurrentLanguage());
		return Collator.getInstance(locale);
	}

}

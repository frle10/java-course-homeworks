package hr.fer.zemris.java.hw11.jnotepadpp.utility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizedLengthLabel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizedRowColSelLabel;

/**
 * This class provides <code>public static</code> methods for
 * producing different listeners needed in {@link JNotepadPP}
 * class.
 * 
 * @author Ivan Skorupan
 */
public class ListenerFactory {

	/**
	 * Returns a {@link WindowListener} that runs the exit action on
	 * given <code>window</code> when the window is closing.
	 * 
	 * @param window - window to add the window listener to
	 * @param exitApplication - exit action
	 * @return window listener with mentioned {@link WindowListener#windowClosing(WindowEvent) windowClosing()}
	 * implementation
	 */
	public static WindowListener createWindowListener(JFrame window, Action exitApplication) {
		return new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitApplication.actionPerformed(null);
			}
		};
	}
	
	/**
	 * Creates a {@link SingleDocumentListener} that sets the enabled state
	 * of the save button in {@link JNotepadPP}.
	 * 
	 * @param mdm - multiple document model we're working with
	 * @param save - save action
	 * @return single document listener that does the described operation when triggered
	 */
	public static SingleDocumentListener createSaveListener(MultipleDocumentModel mdm, Action save) {
		return new SingleDocumentListener() {
			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				save.setEnabled(mdm.getCurrentDocument().isModified());
			}
			
			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {}
		};
	}
	
	/**
	 * Creates a {@link MultipleDocumentListener} that properly registers and
	 * unregisters the save button single document listener from old/to new
	 * document and also sets the enabled states of all other actions dependent
	 * on document change.
	 * 
	 * @param mdm - multiple document model we're working with
	 * @param save - save action
	 * @param saveAs - save as action
	 * @param close - close action
	 * @param paste - paste action
	 * @param statistics - action that shows document statistics
	 * @param saveListener - single document listener for the save action
	 * @return a multiple document listener that does the described operations when triggered
	 */
	public static MultipleDocumentListener createSaveCloseListener(MultipleDocumentModel mdm, Action save, Action saveAs,
			Action close, Action paste, Action statistics, SingleDocumentListener saveListener) {
		return new MultipleDocumentListenerAdapter() {
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				if(previousModel != null) {
					previousModel.removeSingleDocumentListener(saveListener);
				}
				
				if(currentModel != null) {
					currentModel.addSingleDocumentListener(saveListener);
				}
				
				if(currentModel == null) {
					save.setEnabled(false);
					saveAs.setEnabled(false);
					close.setEnabled(false);
					paste.setEnabled(false);
					statistics.setEnabled(false);
				} else {
					save.setEnabled(currentModel.isModified());
					saveAs.setEnabled(true);
					close.setEnabled(true);
					paste.setEnabled(true);
					statistics.setEnabled(true);
				}
			}
		};
	}

	/**
	 * Returns a multiple document listener that updates several GUI components in case
	 * the current document in <code>mdm</code> changes.
	 * <p>
	 * Updated components are the window title, JLabel's that are dependent on current document and
	 * actions.
	 * <p>
	 * This listener also properly registers and unregisters the {@link CaretListener} on old and new
	 * document, the old document being the one we switched from and the new document being the one that
	 * was switched to.
	 * 
	 * @param mdm - multiple document model we're working with
	 * @param window - window whose title needs to be changed on document switching
	 * @param caretListener - caret listener that needs to be registered and unregistered on document switching
	 * @param flp - form localization provider to use for fetching localized unnamed file string
	 * @param length - label on the left-hand side of the status bar that shows the length of the document
	 * @param lineColumnSelection - label that shows the current line, column and selection size of the caret
	 * @param docDependentActions - actions dependent on document change
	 * @return a multiple document listener that does described operations
	 */
	public static MultipleDocumentListener createDynamicInfoListener(MultipleDocumentModel mdm, JFrame window, CaretListener caretListener,
			ILocalizationProvider flp, LocalizedLengthLabel length, LocalizedRowColSelLabel lineColumnSelection,
			Action ... docDependentActions) {
		return new MultipleDocumentListenerAdapter() {
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				Path path = (currentModel != null) ? currentModel.getFilePath() : null;
				String title = ((path == null) ? "(" + flp.getString("unnamed") + ")" : path) + " - JNotepad++";
				window.setTitle(title);
				
				length.updateLabel();
				lineColumnSelection.updateLabel();
				
				if(previousModel != null) {
					previousModel.getTextComponent().removeCaretListener(caretListener);
				}
				
				if(currentModel != null) {
					currentModel.getTextComponent().addCaretListener(caretListener);
				}
				
				for(Action action : docDependentActions) {
					action.setEnabled(false);
				}
				caretListener.caretUpdate(null);
			}
		};
	}

	/**
	 * Returns a caret listener that does certain operations on components and
	 * actions when the caret position changes.
	 * <p>
	 * On caret change, the status bar information is updated and also actions
	 * that depend on selection size are enabled if the selection exists and
	 * disabled if there is no selection.
	 * 
	 * @param mdm - multiple document model we're working with
	 * @param length - label on the left-hand side of the status bar that shows the length of the document
	 * @param lineColumnSelection - label that shows the current line, column and selection size of the caret
	 * @param flp - form localization provider to use for fetching localized label strings
	 * @param selectionDependentActions - array of actions that depend on selection length
	 * @return caret listener that updates the given components as described
	 */
	public static CaretListener createStatusBarDocumentListener(MultipleDocumentModel mdm, LocalizedLengthLabel length,
			LocalizedRowColSelLabel lineColumnSelection, ILocalizationProvider flp, Action ... selectionDependentActions) {
		return new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				length.updateLabel();
				lineColumnSelection.updateLabel();
				
				SingleDocumentModel currentDocument = mdm.getCurrentDocument();
				for(Action action : selectionDependentActions) {
					if(currentDocument == null || currentDocument.getTextComponent().getSelectedText() == null) {
						action.setEnabled(false);
					} else {
						action.setEnabled(true);
					}
				}
			}
		};
	}
	
	/**
	 * Returns an action listener that updates the <code>clock</code> on the right-hand
	 * side of the status bar whenever the {@link Timer} from {@link JNotepadPP} expires.
	 * 
	 * @param clock - label that shows the current system time
	 * @return action listener that updates the clock on timer expiration
	 */
	public static ActionListener createTimeTaskPerformer(JLabel clock) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				clock.setText(sdf.format(cal.getTime()));
			}
		};
	}

}

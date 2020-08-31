package hr.fer.zemris.java.hw11.jnotepadpp.utility;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.SingleDocumentModel;

/**
 * This class provides <code>public static</code> methods and
 * constants that are used in {@link JNotepadPP} class.
 * <p>
 * Most of the methods here are the ones performed by actions
 * in {@link JNotepadPP}.
 * 
 * @author Ivan Skorupan
 */
public class Utility {
	
	/**
	 * Returns the given <code>document</code> length.
	 * 
	 * @param document - document whose length to get
	 * @return length of <code>document</code>
	 */
	public static Integer documentLength(SingleDocumentModel document) {
		return (document == null) ? null : document.getTextComponent().getText().length();
	}
	
	/**
	 * Returns the given <code>document</code> length
	 * but without space characters (spaces, tabs and newlines).
	 * 
	 * @param document - document whose length without space characters to get
	 * @return length of <code>document</code> without space characters
	 */
	public static int documentLengthWithoutSpaces(SingleDocumentModel document) {
		return document.getTextComponent().getText().replaceAll("\\s", "")
				.replaceAll("\\t", "")
				.replaceAll("\\n", "").length();
	}
	
	/**
	 * Returns the number of lines in <code>document</code>.
	 * 
	 * @param document - document whose number of lines to get
	 * @return number of lines in <code>document</code>
	 */
	public static int numberOfLines(SingleDocumentModel document) {
		return (int) document.getTextComponent().getText().lines().count();
	}
	
	/**
	 * Returns a {@link CaretInformer} object that holds the information
	 * about caret position and selection length.
	 * 
	 * @param document - document whose caret to analyze
	 * @return {@link CaretInformer} object containing the calculated caret information
	 */
	public static CaretInformer getCaretInfo(SingleDocumentModel document) {
		if(document == null) return new CaretInformer(null, null, null);
		
		JTextComponent textComponent = document.getTextComponent();
		int pos = textComponent.getCaretPosition();
		Document doc = textComponent.getDocument();
		Element root = doc.getDefaultRootElement();
		
		int row = getRowInDocument(document, pos);
		int col = pos - root.getElement(row).getStartOffset();
		
		Caret caret = textComponent.getCaret();
		int selectionLength = abs(caret.getMark() - caret.getDot());
		
		return new CaretInformer(row + 1, col + 1, selectionLength);
	}
	
	/**
	 * Gets the row in <code>document</code> for position
	 * <code>pos</code> in it.
	 * 
	 * @param document - document to work with
	 * @param pos - position in document to find the row for
	 * @return row in document for <code>pos</code>
	 */
	public static int getRowInDocument(SingleDocumentModel document, int pos) {
		Document doc = document.getTextComponent().getDocument();
		Element root = doc.getDefaultRootElement();
		
		return root.getElementIndex(pos);
	}
	
	/**
	 * Performs one of three different operations on letters in the selection.
	 * <p>
	 * The operations can technically be any strategies defined by <code>caseOperation</code>
	 * as we need to provide a lambda expression, but we only use operations uppercase,
	 * lowercase and case inversion.
	 * 
	 * @param document - document whose selection to take into account
	 * @param caseOperation - a strategy that defines an action to be done on the selection
	 */
	public static void performCaseOperation(SingleDocumentModel document, Function<String, String> caseOperation) {
		JTextComponent textComp = document.getTextComponent();
		StringBuilder text = new StringBuilder(textComp.getText());
		String selection = caseOperation.apply(textComp.getSelectedText());
		text.replace(textComp.getSelectionStart(), textComp.getSelectionEnd() + 1, selection);
		textComp.setText(text.toString());
	}
	
	/**
	 * Inverts the case of all letters in given <code>text</code>.
	 * 
	 * @param text - text whose case to invert
	 * @return a string with inverted case
	 */
	public static String invertCase(String text) {
		StringBuilder sb = new StringBuilder(text);
		for(int i = 0; i < text.length(); i++) {
			char c = sb.charAt(i);
			if(Character.isUpperCase(c)) {
				sb.setCharAt(i, Character.toLowerCase(c));
			} else if(Character.isLowerCase(c)) {
				sb.setCharAt(i, Character.toUpperCase(c));
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Takes all lines that contain any length of selection in
	 * <code>document</code> and then sorts them using the given
	 * <code>comparator</code> (so we can do both ascending and
	 * descending sort).
	 * 
	 * @param document - document whose selection to take into account
	 * @param comparator - rule by which to compare the selected lines
	 */
	public static void sortSelection(SingleDocumentModel document, Comparator<String> comparator) {
		JTextComponent textComp = document.getTextComponent();
		int markRow = getRowInDocument(document, textComp.getCaret().getMark());
		int dotRow = getRowInDocument(document, textComp.getCaret().getDot());
		
		Document doc = textComp.getDocument();
		Element root = doc.getDefaultRootElement();
		
		int fromRow = min(markRow, dotRow);
		int toRow = max(markRow, dotRow);
		
		List<String> lines = getSelectedLines(doc, fromRow, toRow);
		lines.sort(comparator);
		for(int i = fromRow, j = 0; i <= toRow; i++, j++) {
			Element elemI = root.getElement(i);
			try {
				doc.remove(elemI.getStartOffset(), elemI.getEndOffset() - elemI.getStartOffset() - 1);
				doc.insertString(elemI.getStartOffset(), lines.get(j), null);
			} catch (BadLocationException ignorable) {}
		}
	}
	
	/**
	 * Takes all lines that contain any length of selection in
	 * <code>document</code> and then removes duplicate lines
	 * (only the first occurrence of a duplicate line is kept).
	 * 
	 * @param document - document whose selection to take into account
	 */
	public static void unique(SingleDocumentModel document) {
		JTextComponent textComp = document.getTextComponent();
		int markRow = getRowInDocument(document, textComp.getCaret().getMark());
		int dotRow = getRowInDocument(document, textComp.getCaret().getDot());
		
		Document doc = textComp.getDocument();
		Element root = doc.getDefaultRootElement();
		
		int fromRow = min(markRow, dotRow);
		int toRow = max(markRow, dotRow);
		
		List<String> lines = getSelectedLines(doc, fromRow, toRow);
		for(int i = toRow; i >= fromRow; i--) {
			Element elemI = root.getElement(i);
			try {
				String rowText = doc.getText(elemI.getStartOffset(), elemI.getEndOffset() - elemI.getStartOffset() - 1);
				if(lines.indexOf(rowText) != i) {
					doc.remove(elemI.getStartOffset(), elemI.getEndOffset() - elemI.getStartOffset());
				}
			} catch(BadLocationException ignorable) {}
		}
	}
	
	/**
	 * Creates a list of strings that represent the lines of
	 * current selection from document <code>doc</code>.
	 * <p>
	 * The method takes lines from <code>fromRow</code> up to
	 * <code>toRow</code>.
	 * 
	 * @param doc - document whose selection to take into account
	 * @param fromRow - index of first line to take
	 * @param toRow - index of last line to take
	 * @return list of lines from <code>fromRow</code> to <code>toRow</code>
	 */
	public static List<String> getSelectedLines(Document doc, int fromRow, int toRow) {
		List<String> lines = new ArrayList<>();
		for(int i = fromRow; i <= toRow; i++) {
			Element elemI = doc.getDefaultRootElement().getElement(i);
			try {
				lines.add(doc.getText(elemI.getStartOffset(), elemI.getEndOffset() - elemI.getStartOffset() - 1));
			} catch (BadLocationException ignorable) {}
		}
		
		return lines;
	}
	
}

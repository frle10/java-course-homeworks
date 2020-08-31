package hr.fer.zemris.java.gui.calc;

import static hr.fer.zemris.java.gui.calc.ButtonListenerFactory.*;

import static java.lang.Math.*;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcModelImpl;
import hr.fer.zemris.java.gui.calc.model.buttons.CalculatorButton;
import hr.fer.zemris.java.gui.calc.model.buttons.DigitButton;
import hr.fer.zemris.java.gui.calc.model.buttons.InvertibleBinaryOperationButton;
import hr.fer.zemris.java.gui.calc.model.buttons.InvertibleUnaryOperationButton;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * This is a program which initializes the calculator GUI
 * connected with the calculator model {@link CalcModelImpl}.
 * <p>
 * This calculator works similarly to early calculators encountered
 * in operating systems such as Windows 95 or XP. Working with
 * parentheses and typing large expressions is not supported since
 * all operations are computed as soon as there are enough arguments
 * for them to happen.
 * <p>
 * What <b>is</b> supported is working with a stack using the "push"
 * and "pop" buttons on the calculator. The stack works as a kind of
 * "memory" that the calculator has.
 * 
 * @author Ivan Skorupan
 */
public class Calculator extends JFrame {
	
	private static final long serialVersionUID = 5016122500695877672L;

	/**
	 * Internally stored calculator model for working with values and
	 * mathematical operations.
	 */
	private CalcModel model = new CalcModelImpl();

	/**
	 * Internal calculator stack that can be used as "memory"
	 * using the "push" and "pop" buttons on the calculator.
	 */
	private Stack<Double> stack = new Stack<>();

	/**
	 * Constructs a new {@link Calculator} object.
	 */
	public Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Java Calculator v1.0");
		setLocation(20, 20);
		setSize(800, 600);
		initGUI();
	}

	/**
	 * Initializes and places GUI components on the window.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(5));

		Display display = new Display("0");
		cp.add(display, new RCPosition(1, 1));
		model.addCalcValueListener(display);

		JCheckBox invert = new JCheckBox("Inv");
		cp.add(invert, new RCPosition(5, 7));

		addDigitButtons(cp);
		addUnaryOperationButtons(cp, invert);
		addBinaryOperationButtons(cp, invert);
		addOtherButtons(cp);
	}

	/**
	 * Entry point of this program.
	 * 
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Calculator calculator = new Calculator();
				calculator.setVisible(true);
			}
		});
	}
	
	/**
	 * Adds digit buttons to the calculator GUI.
	 * 
	 * @param cp - content pane of the calculator to put the buttons on
	 */
	private void addDigitButtons(Container cp) {
		ActionListener digitButtonListener = digitInputListener(this, model);
		createDigitButton(cp, "0", digitButtonListener, new RCPosition(5, 3));
		createDigitButton(cp, "1", digitButtonListener, new RCPosition(4, 3));
		createDigitButton(cp, "2", digitButtonListener, new RCPosition(4, 4));
		createDigitButton(cp, "3", digitButtonListener, new RCPosition(4, 5));
		createDigitButton(cp, "4", digitButtonListener, new RCPosition(3, 3));
		createDigitButton(cp, "5", digitButtonListener, new RCPosition(3, 4));
		createDigitButton(cp, "6", digitButtonListener, new RCPosition(3, 5));
		createDigitButton(cp, "7", digitButtonListener, new RCPosition(2, 3));
		createDigitButton(cp, "8", digitButtonListener, new RCPosition(2, 4));
		createDigitButton(cp, "9", digitButtonListener, new RCPosition(2, 5));
	}
	
	/**
	 * Adds unary operation buttons to the calculator GUI.
	 * <p>
	 * Unary operations are: <b>sin, cos, tan, ctg, 1/x, log, ln, ...</b>
	 * 
	 * @param cp - content pane of the calculator to put the buttons on
	 * @param invert - check box that can invert a unary operation 
	 */
	private void addUnaryOperationButtons(Container cp, JCheckBox invert) {
		createCalculatorButton(cp, "1/x", unaryOperationListener(model, d -> 1. / d), new RCPosition(2, 1));
		createInvertibleUnaryOperationButton(cp, "sin", "arcsin", invert, Math::sin, Math::asin, new RCPosition(2, 2));
		createInvertibleUnaryOperationButton(cp, "log", "10^x", invert, Math::log10, d -> pow(10, d), new RCPosition(3, 1));
		createInvertibleUnaryOperationButton(cp, "cos", "arccos", invert, Math::cos, Math::acos, new RCPosition(3, 2));
		createInvertibleUnaryOperationButton(cp, "ln", "e^x", invert, Math::log, d -> pow(E, d), new RCPosition(4, 1));
		createInvertibleUnaryOperationButton(cp, "tan", "arctan", invert, Math::tan, Math::atan, new RCPosition(4, 2));
		createInvertibleUnaryOperationButton(cp, "ctg", "arcctg", invert, d -> 1. / tan(d), d -> PI / 2 - atan(d), new RCPosition(5, 2));
	}
	
	/**
	 * Adds binary operation buttons to the calculator GUI.
	 * <p>
	 * Binary operations are: <b>+, -, *, /, x^n</b>.
	 * 
	 * @param cp - content pane of the calculator to put the buttons on
	 * @param invert - check box that can invert a binary operation
	 */
	private void addBinaryOperationButtons(Container cp, JCheckBox invert) {
		createCalculatorButton(cp, "/", binaryOperationListener(model, (d1, d2) -> d1 / d2), new RCPosition(2, 6));
		createCalculatorButton(cp, "*", binaryOperationListener(model, (d1, d2) -> d1 * d2), new RCPosition(3, 6));
		createCalculatorButton(cp, "-", binaryOperationListener(model, (d1, d2) -> d1 - d2), new RCPosition(4, 6));
		createCalculatorButton(cp, "+", binaryOperationListener(model, Double::sum), new RCPosition(5, 6));
		createInvertibleBinaryOperationButton(cp, "x^n", "x^(1/n)", invert, Math::pow, (d1, d2) -> pow(d1, 1. / d2), new RCPosition(5, 1));
	}
	
	/**
	 * Adds all the other buttons to the calculator GUI.
	 * <p>
	 * Other buttons are all the buttons that aren't digit buttons
	 * or any of the buttons responsible for unary or binary mathematical
	 * operations.
	 * 
	 * @param cp - content pane of the calculator to put the buttons on
	 */
	private void addOtherButtons(Container cp) {
		createCalculatorButton(cp, "=", (equalsOperationListener(this, model)), new RCPosition(1, 6));
		createCalculatorButton(cp, "clr", e -> model.clear(), new RCPosition(1, 7));
		createCalculatorButton(cp, "reset", e -> model.clearAll(), new RCPosition(2, 7));
		createCalculatorButton(cp, "+/-", e -> model.swapSign(), new RCPosition(5, 4));
		createCalculatorButton(cp, ".", decimalPointListener(this, model), new RCPosition(5, 5));
		createCalculatorButton(cp, "push", e -> stack.push(model.getValue()), new RCPosition(3, 7));
		createCalculatorButton(cp, "pop", popListener(this, model, stack), new RCPosition(4, 7));
	}
	
	/**
	 * Creates and adds to this container an instance of {@link CalculatorButton} with all the relevant
	 * information passed and set.
	 * 
	 * @param cp - container to put the button on
	 * @param label - button's label
	 * @param listener - button's action listener
	 * @param position - button's position
	 */
	private void createCalculatorButton(Container cp, String label, ActionListener listener, RCPosition position) {
		cp.add(new CalculatorButton(label, listener), position);
	}
	
	/**
	 * Creates and adds to this container an instance of {@link DigitButton} with all the relevant
	 * information passed and set.
	 * 
	 * @param cp - container to put the button on
	 * @param label - button's label
	 * @param listener - button's action listener
	 * @param position - button's position
	 */
	private void createDigitButton(Container cp, String label, ActionListener listener, RCPosition position) {
		cp.add(new DigitButton(label, listener), position);
	}
	
	/**
	 * Creates and adds to this container an instance of {@link InvertibleUnaryOperationButton}
	 * with all the relevant information passed and set.
	 * 
	 * @param cp - container to put the button on
	 * @param label - button's label
	 * @param invertedLabel - button's label when inverted
	 * @param invertingCheckBox - check-box that inverts the button when selected
	 * @param op1 - non-inverted button operation
	 * @param op2 - button operation when inverted
	 * @param position - button's position
	 */
	private void createInvertibleUnaryOperationButton(Container cp, String label, String invertedLabel,
			JCheckBox invertingCheckBox, DoubleUnaryOperator op1, DoubleUnaryOperator op2, RCPosition position) {
		cp.add(new InvertibleUnaryOperationButton(label, invertedLabel, model, invertingCheckBox, op1, op2), position);
	}
	
	/**
	 * Creates and adds to this container an instance of {@link InvertibleBinaryOperationButton}
	 * with all the relevant information passed and set.
	 * 
	 * @param cp - container to put the button on
	 * @param label - button's label
	 * @param invertedLabel - button's label when inverted
	 * @param invertingCheckBox - check-box that inverts the button when selected
	 * @param op1 - non-inverted button operation
	 * @param op2 - button operation when inverted
	 * @param position - button's position
	 */
	private void createInvertibleBinaryOperationButton(Container cp, String label, String invertedLabel,
			JCheckBox invertingCheckBox, DoubleBinaryOperator op1, DoubleBinaryOperator op2, RCPosition position) {
		cp.add(new InvertibleBinaryOperationButton(label, invertedLabel, model, invertingCheckBox, op1, op2), position);
	}
	
}

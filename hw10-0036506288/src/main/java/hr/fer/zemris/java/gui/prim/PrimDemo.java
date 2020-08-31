package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * This is a demo GUI program for testing the implementation
 * of {@link PrimListModel}.
 * <p>
 * The program expects no command line arguments.
 * 
 * @author Ivan Skorupan
 */
public class PrimDemo extends JFrame {
	
	private static final long serialVersionUID = 2552614965736703542L;
	
	/**
	 * Constructs a new {@link PrimDemo} object.
	 */
	public PrimDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Prim Demo");
		setLocation(20, 20);
		setSize(600, 350);
		initGUI();
	}
	
	/**
	 * Initializes and places GUI components on the window.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		PrimListModel model = new PrimListModel();
		JList<Integer> left = new JList<>(model);
		JList<Integer> right = new JList<>(model);
		
		JPanel central = new JPanel(new GridLayout(1, 0));
		central.add(new JScrollPane(left));
		central.add(new JScrollPane(right));
		cp.add(central, BorderLayout.CENTER);
		
		JButton next = new JButton("Sljedeći");
		next.addActionListener(e -> model.next());
		cp.add(next, BorderLayout.PAGE_END);
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
				PrimDemo primDemo = new PrimDemo();
				primDemo.setVisible(true);
			}
		});
	}

}

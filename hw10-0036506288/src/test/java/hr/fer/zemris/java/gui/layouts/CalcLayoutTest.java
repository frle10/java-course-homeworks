package hr.fer.zemris.java.gui.layouts;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.Test;

class CalcLayoutTest {

	@Test
	void testAddLayoutComponentComponentObject() {
		Container container = new Container();
		container.setLayout(new CalcLayout(3));
		
		container.add(new JLabel("tekst"), new RCPosition(4, 5));
		assertThrows(CalcLayoutException.class, () -> container.add(new JLabel("druga"), new RCPosition(4, 5)));
		
		assertThrows(CalcLayoutException.class, () -> container.add(new JLabel("tekst 1"), new RCPosition(0,1)));
		assertThrows(CalcLayoutException.class, () -> container.add(new JLabel("tekst 2"), new RCPosition(1,0)));
		assertThrows(CalcLayoutException.class, () -> container.add(new JLabel("tekst 3"), new RCPosition(1, 2)));
		assertThrows(CalcLayoutException.class, () -> container.add(new JLabel("tekst 4"), new RCPosition(1, 5)));
		assertThrows(CalcLayoutException.class, () -> container.add(new JLabel("tekst 5"), new RCPosition(-2, 4)));
		assertThrows(CalcLayoutException.class, () -> container.add(new JLabel("tekst 6"), new RCPosition(4, -3)));
		assertThrows(CalcLayoutException.class, () -> container.add(new JLabel("tekst 7"), new RCPosition(2, 8)));
		assertThrows(CalcLayoutException.class, () -> container.add(new JLabel("tekst 1"), new RCPosition(6,3)));
	}
	
	@Test
	void testPreferredLayoutSize() {
		JPanel p = new JPanel(new CalcLayout(2));
		
		JLabel l1 = new JLabel("");
		l1.setPreferredSize(new Dimension(10,30));
		
		JLabel l2 = new JLabel("");
		l2.setPreferredSize(new Dimension(20,15));
		
		p.add(l1, new RCPosition(2,2));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		
		assertEquals(new Dimension(152, 158), dim);
	}
	
	@Test
	void testPreferredLayoutSize2() {
		JPanel p = new JPanel(new CalcLayout(2));
		
		JLabel l1 = new JLabel("");
		l1.setPreferredSize(new Dimension(108,15));
		
		JLabel l2 = new JLabel("");
		l2.setPreferredSize(new Dimension(16,30));
		
		p.add(l1, new RCPosition(1,1));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		
		assertEquals(new Dimension(152, 158), dim);
	}
	
}

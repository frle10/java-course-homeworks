package hr.fer.zemris.java.hw17.jvdraw.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;

import hr.fer.zemris.java.hw17.jvdraw.geometry.FilledTriangle;

public class FilledTriangleEditor extends GeometricalObjectEditor {
	
	private static final long serialVersionUID = 1L;
	
	private FilledTriangle filledTriangle;
	
	/**
	 * A color area previewing the object's foreground color.
	 */
	private JColorArea outlineColorPreview;
	
	/**
	 * A color area previewing the object's fill color.
	 */
	private JColorArea fillColorPreview;
	
	public FilledTriangleEditor(FilledTriangle filledTriangle) {
		this.filledTriangle = Objects.requireNonNull(filledTriangle);
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new GridLayout(2, 0));
		
		JLabel outlineColor = new JLabel("Outline color: ");
		JLabel fillColor = new JLabel("Fill color: ");
		
		outlineColorPreview = new JColorArea(filledTriangle.getFgColor());
		fillColorPreview = new JColorArea(filledTriangle.getFillColor());
		
		JButton chooseOutlineColor = new JButton("Choose outline color");
		chooseOutlineColor.addActionListener((e) -> {
			Color chosenColor = JColorChooser.showDialog(this, "Choose Outline Color", filledTriangle.getFgColor());
			if(chosenColor != null) {
				outlineColorPreview.setSelectedColor(chosenColor);
			}
		});
		
		JButton chooseFillColor = new JButton("Choose fill color");
		chooseFillColor.addActionListener((e) -> {
			Color chosenColor = JColorChooser.showDialog(this, "Choose Fill Color", filledTriangle.getFillColor());
			if(chosenColor != null) {
				fillColorPreview.setSelectedColor(chosenColor);
			}
		});

		add(outlineColor);
		add(chooseOutlineColor);
		add(outlineColorPreview);
		add(fillColor);
		add(chooseFillColor);
		add(fillColorPreview);
	}

	@Override
	public void checkEditing() {
		acceptEditing();
	}

	@Override
	public void acceptEditing() {
		filledTriangle.setFgColor(outlineColorPreview.getCurrentColor());
		filledTriangle.setFillColor(fillColorPreview.getCurrentColor());
	}
	
}

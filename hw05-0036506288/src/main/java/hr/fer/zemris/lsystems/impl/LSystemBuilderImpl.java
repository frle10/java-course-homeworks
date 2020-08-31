package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import static java.lang.Math.*;

import hr.fer.zemris.java.custom.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.ColorCommand;
import hr.fer.zemris.lsystems.impl.commands.DrawCommand;
import hr.fer.zemris.lsystems.impl.commands.PopCommand;
import hr.fer.zemris.lsystems.impl.commands.PushCommand;
import hr.fer.zemris.lsystems.impl.commands.RotateCommand;
import hr.fer.zemris.lsystems.impl.commands.ScaleCommand;
import hr.fer.zemris.lsystems.impl.commands.SkipCommand;
import hr.fer.zemris.math.Vector2D;

/**
 * This class is a concrete implementation of <code>LSystemBuilder</code>.
 * <p>
 * It uses two internal dictionaries to store registered turtle commands and
 * also registered productions.
 * 
 * @author Ivan Skorupan
 */
public class LSystemBuilderImpl implements LSystemBuilder {

	/**
	 * A dictionary containing registered productions.
	 */
	private Dictionary<Character, String> registeredProductions = new Dictionary<>();

	/**
	 * A dictionary containing registered commands.
	 */
	private Dictionary<Character, Command> registeredCommands = new Dictionary<>();

	/**
	 * Length of turtle's unit displacement.
	 */
	private double unitLength = 0.1;

	/**
	 * Value used for keeping proper fractal dimensions based on level.
	 */
	private double unitLengthDegreeScaler = 1;

	/**
	 * A point from which the turtle starts.
	 */
	private Vector2D origin = new Vector2D(0, 0);

	/**
	 * Angle between positive x-axis and turtle's direction vector.
	 * This angle is in degrees, not radians.
	 */
	private double angle = 0;

	/**
	 * Starting array from which system development starts. Can be one symbol or
	 * an array of characters.
	 */
	private String axiom = "";

	/**
	 * A concrete implementation of <code>LSystem</code>.
	 * 
	 * @author Ivan Skorupan
	 */
	private class LSystemImpl implements LSystem {

		@Override
		public void draw(int level, Painter painter) {
			Context context = new Context();
			TurtleState state = new TurtleState(origin.copy(), new Vector2D(1, 0).rotated(angleInRadians()), Color.BLACK, unitLength * pow(unitLengthDegreeScaler, level));
			context.pushState(state);
			
			char[] finalCharArray = generate(level).toCharArray();
			for(char symbol : finalCharArray) {
				Command command = registeredCommands.get(symbol);

				if(command != null) {
					command.execute(context, painter);
				}
			}
		}

		@Override
		public String generate(int level) {
			StringBuilder finalCharArray = new StringBuilder();
			finalCharArray.append(axiom);

			for(int i = 0; i < level; i++) {
				char[] finalCharArrayCopy = finalCharArray.toString().toCharArray();
				StringBuilder finalLevelString = new StringBuilder();
				
				for(int j = 0; j < finalCharArrayCopy.length; j++) {
					String replacement = registeredProductions.get(finalCharArrayCopy[j]);

					if(replacement != null) {
						finalLevelString.append(replacement);
					} else {
						finalLevelString.append(finalCharArrayCopy[j]);
					}
				}
				
				finalCharArray = finalLevelString;
			}
			
			return finalCharArray.toString();
		}
		
		/**
		 * Returns {@link LSystemBuilderImpl#angle angle}
		 * in radians.
		 * 
		 * @return
		 */
		private double angleInRadians() {
			return angle * (Math.PI / 180.);
		}

	}

	@Override
	public LSystem build() {
		return new LSystemImpl();
	}

	@Override
	public LSystemBuilder configureFromText(String[] lines) {
		for(String line : lines) {
			if(!line.isEmpty()) {
				String[] tokens = line.trim().replaceAll("/", " / ").replaceAll("\\s+", " ").split(" ");
				parseDirective(tokens);
			}
		}

		return this;
	}

	@Override
	public LSystemBuilder registerCommand(char symbol, String action) {
		String expandedAction = "command " + symbol + " " + action;
		String[] tokens = expandedAction.trim().replaceAll("/", " / ").replaceAll("\\s+", " ").split(" ");
		
		if(parsedCommandDirective(tokens)) {
			return this;
		}
		
		throw new IllegalArgumentException("The action is unsupported or invalid!");
	}

	@Override
	public LSystemBuilder registerProduction(char symbol, String production) {
		this.registeredProductions.put(symbol, production);
		return this;
	}

	@Override
	public LSystemBuilder setAngle(double angle) {
		this.angle = angle;
		return this;
	}

	@Override
	public LSystemBuilder setAxiom(String axiom) {
		this.axiom = axiom;
		return this;
	}

	@Override
	public LSystemBuilder setOrigin(double x, double y) {
		this.origin = new Vector2D(x, y);
		return this;
	}

	@Override
	public LSystemBuilder setUnitLength(double unitLength) {
		this.unitLength = unitLength;
		return this;
	}

	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
		this.unitLengthDegreeScaler = unitLengthDegreeScaler;
		return this;
	}

	/**
	 * Parses a directive from a string array containing this
	 * directive's tokens.
	 * 
	 * @param tokens - array of strings containing tokenized directive
	 */
	private void parseDirective(String[] tokens) {
		if(parsedOriginDirective(tokens)) {
			return;
		}

		if(parsedAngleDirective(tokens)) {
			return;
		}

		if(parsedUnitLengthDirective(tokens)) {
			return;
		}

		if(parsedUnitLengthDegreeScalerDirective(tokens)) {
			return;
		}

		if(parsedCommandDirective(tokens)) {
			return;
		}

		if(parsedAxiomDirective(tokens)) {
			return;
		}

		if(parsedProductionDirective(tokens)) {
			return;
		}
		
		throw new IllegalArgumentException("An unsupported directive was encountered: " + tokens[0]);
	}

	/**
	 * Parses a production directive.
	 * 
	 * @param tokens - array of strings containing tokenized directive
	 * @return <code>true</code> if a production directive was parsed, <code>false</code> otherwise
	 * @throws IllegalArgumentException if production directive is invalid
	 */
	private boolean parsedProductionDirective(String[] tokens) {
		String directive = tokens[0];

		if(directive.equals("production")) {
			if(tokens.length != 3) {
				throw new IllegalArgumentException("Production directive has wrong number of arguments!");
			}

			String symbolToken = tokens[1];

			if(symbolToken.length() != 1) {
				throw new IllegalArgumentException("Production directive's first argument should be a single character symbol!");
			}

			char symbol = symbolToken.charAt(0);
			String replacement = tokens[2];

			if(registeredProductions.get(symbol) == null) {
				registeredProductions.put(symbol, replacement);
			}

			return true;
		}

		return false;
	}

	/**
	 * Parses an axiom directive.
	 * 
	 * @param tokens - array of strings containing tokenized directive
	 * @return <code>true</code> if an axiom directive was parsed, <code>false</code> otherwise
	 * @throws IllegalArgumentException if axiom directive is invalid
	 */
	private boolean parsedAxiomDirective(String[] tokens) {
		String directive = tokens[0];

		if(directive.equals("axiom")) {
			if(tokens.length != 2) {
				throw new IllegalArgumentException("Axiom directive has wrong number of arguments!");
			}

			this.axiom = tokens[1];
			return true;
		}

		return false;
	}

	/**
	 * Parses a command directive.
	 * 
	 * @param tokens - array of strings containing tokenized directive
	 * @return <code>true</code> if a command directive was parsed, <code>false</code> otherwise
	 * @throws IllegalArgumentException if command directive is invalid
	 */
	private boolean parsedCommandDirective(String[] tokens) {
		final int COMMAND_DIRECTIVE_LENGTH_WITHOUT_ARGUMENT = 3;
		final int COMMAND_DIRECTIVE_LENGTH_WITH_ARGUMENT = 4;

		final boolean isCommandDirectiveOfValidLength = (tokens.length == COMMAND_DIRECTIVE_LENGTH_WITHOUT_ARGUMENT)
				|| (tokens.length == COMMAND_DIRECTIVE_LENGTH_WITH_ARGUMENT);

		String directive = tokens[0];

		if(directive.equals("command")) {
			if(!isCommandDirectiveOfValidLength) {
				throw new IllegalArgumentException("Number of command directive arguments is invalid!");
			}

			String symbolToken = tokens[1];

			if(symbolToken.length() != 1) {
				throw new IllegalArgumentException("The first argument of command directive needs to be a single character symbol!");
			}

			char symbol = symbolToken.charAt(0);
			String action = tokens[2];

			if(isCommandRegistered(symbol)) {
				return false;
			}

			if(tokens.length == COMMAND_DIRECTIVE_LENGTH_WITHOUT_ARGUMENT) {
				if(action.equals("push")) {
					registeredCommands.put(symbol, new PushCommand());
					return true;
				} else if(action.equals("pop")) {
					registeredCommands.put(symbol, new PopCommand());
					return true;
				}
			}

			if(tokens.length == COMMAND_DIRECTIVE_LENGTH_WITH_ARGUMENT) {
				String argument = tokens[3];
				
				if(action.equals("color")) {
					if(!checkColorArgument(argument.toUpperCase())) {
						throw new IllegalArgumentException("The color argument is invalid!");
					}

					registeredCommands.put(symbol, new ColorCommand(Color.decode("#" + argument)));
					return true;
				}

				if(!checkDoubleArgument(argument)) {
					throw new NumberFormatException("The argument to " + action + " command is invalid!");
				}
				
				double argumentAsDouble = Double.parseDouble(argument);
				
				if(action.equals("draw")) {
					registeredCommands.put(symbol, new DrawCommand(argumentAsDouble));
					return true;
				} else if(action.equals("skip")) {
					registeredCommands.put(symbol, new SkipCommand(argumentAsDouble));
					return true;
				} else if(action.equals("scale")) {
					registeredCommands.put(symbol, new ScaleCommand(argumentAsDouble));
					return true;
				} else if(action.equals("rotate")) {
					registeredCommands.put(symbol, new RotateCommand(argumentAsDouble));
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Parses a command directive.
	 * 
	 * @param tokens - array of strings containing tokenized directive
	 * @return <code>true</code> if a command directive was parsed, <code>false</code> otherwise
	 * @throws IllegalArgumentException if command directive is invalid
	 */
	private boolean isCommandRegistered(char symbol) {
		return (registeredCommands.get(symbol) == null) ? false : true;
	}

	private boolean checkColorArgument(String argument) {
		char[] argumentCharacters = argument.toCharArray();

		for(char character : argumentCharacters) {
			if(!Character.isDigit(character) && !(character >= 'A' && character <= 'F')) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks if a string contains a parsable double.
	 * 
	 * @param argument - string to check for parsable double value
	 * @return <code>true</code> if string contains parsable double, <code>false</code> otherwise
	 */
	@SuppressWarnings("unused")
	private boolean checkDoubleArgument(String argument) {
		double value = 0;

		try {
			value = Double.parseDouble(argument);
		} catch(NumberFormatException ex) {
			return false;
		}

		return true;
	}

	/**
	 * Parses a unit length degree scaler directive.
	 * 
	 * @param tokens - array of strings containing tokenized directive
	 * @return <code>true</code> if a unit length degree scaler directive was parsed, <code>false</code> otherwise
	 * @throws IllegalArgumentException if unit length degree scaler directive is invalid
	 * @throws NumberFormatException if unitLengthDegreeScaler argument is invalid
	 */
	private boolean parsedUnitLengthDegreeScalerDirective(String[] tokens) {
		String directive = tokens[0];

		if(directive.equals("unitLengthDegreeScaler")) {
			if(tokens.length == 2) {
				double unitLengthDegreeScaler = 0;
				
				try {
					unitLengthDegreeScaler = Double.parseDouble(tokens[1]);
				} catch(NumberFormatException ex) {
					throw new NumberFormatException("The unit length degree scaler value is invalid!");
				}

				this.unitLengthDegreeScaler = unitLengthDegreeScaler;
				return true;
			}

			if(tokens.length == 4) {
				double numerator = 0, denominator = 0;

				try {
					numerator = Double.parseDouble(tokens[1]);
					denominator = Double.parseDouble(tokens[3]);
				} catch(NumberFormatException ex) {
					throw new NumberFormatException("The unit length degree scaler value is invalid!");
				}

				this.unitLengthDegreeScaler = numerator / denominator;
				return true;
			}

			throw new IllegalArgumentException("Wrong number of arguments for unit length degree scaler directive!");
		}

		return false;
	}

	/**
	 * Parses a unit length directive.
	 * 
	 * @param tokens - array of strings containing tokenized directive
	 * @return <code>true</code> if a unit length directive was parsed, <code>false</code> otherwise
	 * @throws NumberFormatException if unit length directive value is invalid
	 */
	private boolean parsedUnitLengthDirective(String[] tokens) {
		String directive = tokens[0];

		if(directive.equals("unitLength")) {
			double unitLength = 0;

			try {
				unitLength = Double.parseDouble(tokens[1]);
			} catch(NumberFormatException ex) {
				throw new NumberFormatException("The unit length value is invalid!");
			}

			this.unitLength = unitLength;
			return true;
		}

		return false;
	}

	/**
	 * Parses an angle directive.
	 * 
	 * @param tokens - array of strings containing tokenized directive
	 * @return <code>true</code> if an angle directive was parsed, <code>false</code> otherwise
	 * @throws NumberFormatException if angle value is invalid
	 */
	private boolean parsedAngleDirective(String[] tokens) {
		String directive = tokens[0];

		if(directive.equals("angle")) {
			double angle = 0;

			try {
				angle = Double.parseDouble(tokens[1]);
			} catch(NumberFormatException ex) {
				throw new NumberFormatException("The angle value is invalid!");
			}

			this.angle = angle;
			return true;
		}

		return false;
	}

	/**
	 * Parses an origin directive.
	 * 
	 * @param tokens - array of strings containing tokenized directive
	 * @return <code>true</code> if an origin directive was parsed, <code>false</code> otherwise
	 * @throws NumberFormatException if origin coordinates are invalid
	 */
	private boolean parsedOriginDirective(String[] tokens) {
		String directive = tokens[0];

		if(directive.equals("origin")) {
			double x = 0, y = 0;

			try {
				x = Double.parseDouble(tokens[1]);
				y = Double.parseDouble(tokens[2]);
			} catch(NumberFormatException ex) {
				throw new NumberFormatException("Origin coordinates are invalid!");
			}

			this.origin = new Vector2D(x, y);
			return true;
		}

		return false;
	}

}

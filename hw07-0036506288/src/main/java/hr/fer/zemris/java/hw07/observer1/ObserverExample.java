package hr.fer.zemris.java.hw07.observer1;

/**
 * Demo program which tests and shows the use of classes implemented in heart
 * of the Observer design pattern.
 * 
 * @author Ivan Skorupan
 */
public class ObserverExample {

	/**
	 * Entry point of this program.
	 * 
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		IntegerStorage istorage = new IntegerStorage(20);

		IntegerStorageObserver observer = new SquareValue();

		istorage.addObserver(observer);
		istorage.setValue(5);
		istorage.setValue(2);
		istorage.setValue(25);

		/*istorage.removeObserver(observer);

		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue(5));*/

		istorage.removeObserver(observer);
		
		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue(1));
		istorage.addObserver(new DoubleValue(2));
		istorage.addObserver(new DoubleValue(2));

		istorage.setValue(13);
		istorage.setValue(22);
		istorage.setValue(15);
	}

}

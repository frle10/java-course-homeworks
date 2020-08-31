package searching.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import searching.algorithms.Node;
import searching.algorithms.SearchUtil;
import searching.slagalica.KonfiguracijaSlagalice;
import searching.slagalica.Slagalica;
import searching.slagalica.gui.SlagalicaViewer;

/**
 * A simple demo program that show-cases the use of implemented state sub-space exploring
 * algorithms for solving a simple puzzle.
 * <p>
 * The program expects exactly one argument in the form of a string of digits of length 9
 * where all the digits 0-8 are present (so there can be no digit that appears more than once).
 * 
 * @author Ivan Skorupan
 */
public class SlagalicaMain {
	
	/**
	 * Entry point of this demo program.
	 * 
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Expected exactly 1 argument!");
			System.exit(-1);
		}
		
		String slagalicaText = args[0];
		if(slagalicaText.length() != 9) {
			System.out.println("Expected a string of length 9 with all digits 0-8 present!");
			System.exit(-1);
		}
		
		int[] slagalicaPolje = new int[9];
		for(int i = 0; i < 9; i++) {
			if(!Character.isDigit(slagalicaText.charAt(i))) {
				System.out.println("There is a non-digit character present in the string!");
				System.exit(-1);
			}
			
			int number = Integer.parseInt(Character.valueOf(slagalicaText.charAt(i)).toString());
			if(alreadyPresent(number, slagalicaPolje, i)) {
				System.out.println("There needs to be exactly one of each digit 0-8 in the string!");
				System.exit(-1);
			}
			
			slagalicaPolje[i] = number;
		}
		
		Slagalica slagalica = new Slagalica(new KonfiguracijaSlagalice(slagalicaPolje));
		Node<KonfiguracijaSlagalice> rješenje = SearchUtil.bfsv(slagalica, slagalica, slagalica);
		
		if(rješenje == null) {
			System.out.println("Nisam uspio pronaći rješenje.");
		} else {
			System.out.println("Imam rješenje. Broj poteza je: " + rješenje.getCost());
			List<KonfiguracijaSlagalice> lista = new ArrayList<>();
			Node<KonfiguracijaSlagalice> trenutni = rješenje;
			
			while(trenutni != null) {
				lista.add(trenutni.getState());
				trenutni = trenutni.getParent();
			}
			
			Collections.reverse(lista);
			
			lista.stream().forEach(k -> {
				System.out.println(k);
				System.out.println();
			});
			
			SlagalicaViewer.display(rješenje);
		}
	}

	/**
	 * Tests if the given <code>number</code> is already present in the
	 * given <code>array</code>.
	 * <p>
	 * Since the array might not have been fully filled yet, we must
	 * check only the first <code>length</code> digits.
	 * 
	 * @param number - number to test for in the <code>array</code>
	 * @param array - array to check for <code>number</code>
	 * @param length - number of digits to search through
	 * @return <code>true</code> if <code>number</code> is in the <code>array</code>, <code>false</code> otherwise
	 */
	private static boolean alreadyPresent(int number, int[] array, int length) {
		for(int i = 0; i < length; i++) {
			if(array[i] == number) return true;
		}
		
		return false;
	}
}

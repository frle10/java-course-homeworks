package searching.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import searching.algorithms.Node;
import searching.algorithms.SearchUtil;
import searching.slagalica.KonfiguracijaSlagalice;
import searching.slagalica.Slagalica;

/**
 * A simple demo program that show-cases the use of implemented state sub-space exploring
 * algorithms for solving a simple puzzle.
 * 
 * @author Ivan Skorupan
 */
public class SlagalicaDemo {
	
	/**
	 * Entry point of this demo program.
	 * 
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		Slagalica slagalica = new Slagalica(new KonfiguracijaSlagalice(new int[] {2,3,0,1,4,6,7,5,8}));
		//Slagalica slagalica = new Slagalica(new KonfiguracijaSlagalice(new int[] {1,6,4,5,0,2,8,7,3}));
		//Node<KonfiguracijaSlagalice> rješenje = SearchUtil.bfs(slagalica, slagalica, slagalica);
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
		}
	}
}

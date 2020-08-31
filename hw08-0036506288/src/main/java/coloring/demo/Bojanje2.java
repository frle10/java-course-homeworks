package coloring.demo;

import java.util.Arrays;

import coloring.algorithms.Coloring;
import coloring.algorithms.Pixel;
import coloring.algorithms.SubspaceExploreUtil;
import marcupic.opjj.statespace.coloring.FillAlgorithm;
import marcupic.opjj.statespace.coloring.FillApp;
import marcupic.opjj.statespace.coloring.Picture;

/**
 * Simple demo program that demonstrates the use of some given
 * filling algorithms along with filling algorithms implemented
 * as a part of the 8th homework through a GUI coloring application.
 * 
 * @author Ivan Skorupan
 */
public class Bojanje2 {

	/**
	 * Entry point of this demo program.
	 * 
	 * @param args - command-line arguments
	 */
	public static void main(String[] args) {
		FillApp.run(FillApp.OWL, Arrays.asList(bfs, dfs, bfsv));
	}
	
	/**
	 * A breadth first search filling algorithm implementation.
	 */
	private static final FillAlgorithm bfs = new FillAlgorithm() {
		@Override
		public String getAlgorithmTitle() {
			return "Moj bfs!";
		}
		
		@Override
		public void fill(int x, int y, int color, Picture picture) {
			Coloring col = new Coloring(new Pixel(x,y), picture, color);
			SubspaceExploreUtil.bfs(col, col, col, col);
		}
	};

	/**
	 * A depth first search filling algorithm implementation.
	 */
	private static final FillAlgorithm dfs = new FillAlgorithm() {
		@Override
		public String getAlgorithmTitle() {
			return "Moj dfs!";
		}
		
		@Override
		public void fill(int x, int y, int color, Picture picture) {
			Coloring col = new Coloring(new Pixel(x,y), picture, color);
			SubspaceExploreUtil.dfs(col, col, col, col);
		}
	};

	/**
	 * An improved breadth first search filling algorithm implementation.
	 */
	private static final FillAlgorithm bfsv = new FillAlgorithm() {
		@Override
		public String getAlgorithmTitle() {
			return "Moj bfsv!";
		}
		
		@Override
		public void fill(int x, int y, int color, Picture picture) {
			Coloring col = new Coloring(new Pixel(x,y), picture, color);
			SubspaceExploreUtil.bfsv(col, col, col, col);
		}
	};

}

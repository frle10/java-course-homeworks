package coloring.algorithms;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import marcupic.opjj.statespace.coloring.Picture;

/**
 * Models objects that implement all relevant interfaces needed for
 * execution of complete walk through state sub-space.
 * <p>
 * A method that visits all the states in certain sub-space has the following
 * mathematical model:
 * <p>
 * <b>{s0, process(s), succ(s), acceptable(s)}</b>
 * <p>
 * Objects of this class provide implementation of methods that can return all
 * required members of the tuple described above.
 * 
 * @author Ivan Skorupan
 */
public class Coloring implements Consumer<Pixel>, Function<Pixel, List<Pixel>>, Predicate<Pixel>, Supplier<Pixel> {
	
	/**
	 * The pixel on which the user clicked and triggered the coloring process.
	 */
	private Pixel reference;
	
	/**
	 * A reference to the picture that we are coloring.
	 */
	private Picture picture;
	
	/**
	 * Current fill color.
	 */
	private int fillColor;
	
	/**
	 * Color of the {@link #reference} pixel.
	 */
	private int refColor;

	/**
	 * Constructs a new {@link Coloring} object and
	 * initializes it with reference pixel, picture
	 * and desired fillColor.
	 * 
	 * @param reference
	 * @param picture
	 * @param fillColor
	 */
	public Coloring(Pixel reference, Picture picture, int fillColor) {
		this.reference = Objects.requireNonNull(reference);
		this.picture = Objects.requireNonNull(picture);
		this.fillColor = fillColor;
		this.refColor = picture.getPixelColor(reference.x, reference.y);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Returns the reference pixel.
	 */
	@Override
	public Pixel get() {
		return reference;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * A pixel is inside the filling area if it is of the same color as the reference pixel.
	 * 
	 * @param t - pixel to be tested
	 * @return <code>true</code> if the given pixel is inside coloring area, <code>false</false> otherwise
	 * @throws NullPointerException if <code>t</code> is <code>null</code>
	 */
	@Override
	public boolean test(Pixel t) {
		Objects.requireNonNull(t);
		return picture.getPixelColor(t.x, t.y) == refColor;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Finds all adjacent pixel of the given pixel <code>t</code>
	 * and returns them in a list.
	 * 
	 * @param t - pixel whose neighbors are to be found
	 * @return a list of neighboring pixels
	 * @throws NullPointerException if <code>t</code> is <code>null</code>
	 */
	@Override
	public List<Pixel> apply(Pixel t) {
		Objects.requireNonNull(t);
		List<Pixel> neighbours = new LinkedList<>();
		neighbours.add(new Pixel(t.x + 1, t.y));
		neighbours.add(new Pixel(t.x, t.y + 1));
		neighbours.add(new Pixel(t.x - 1, t.y));
		neighbours.add(new Pixel(t.x, t.y - 1));
		return neighbours;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Sets the color of the given pixel <code>t</code> to
	 * the current {@link #fillColor}.
	 * 
	 * @param t - pixel whose color to set to fill color
	 * @throws NullPointerException if <code>t</code> is <code>null</code>
	 */
	@Override
	public void accept(Pixel t) {
		Objects.requireNonNull(t);
		picture.setPixelColor(t.x, t.y, fillColor);
	}
	
}

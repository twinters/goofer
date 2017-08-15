package be.thomaswinters.goofer.data;

/**
 * A class to hold ratings of XLikeYSolutions
 * 
 * @author Thomas Winters
 *
 */
public class Rating<E> implements Comparable<Rating<E>> {
	private final E solution;
	private final double rating;

	public Rating(E solution, double rating) {
		this.solution = solution;
		this.rating = rating;
	}

	public E getElement() {
		return solution;
	}

	public double getRating() {
		return rating;
	}

	@Override
	public String toString() {
		return solution + "->" + rating;
	}

	@Override
	public int compareTo(Rating<E> o) {
		return (int) Math.signum(getRating() - o.getRating());
	}

}

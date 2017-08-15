package be.thomaswinters.goofer.data;

import java.util.Collection;

public class MultiRating<E> {
	private final E element;
	private final Collection<Integer> ratings;

	public MultiRating(E element, Collection<Integer> ratings) {
		this.element = element;
		this.ratings = ratings;
	}

	public E getElement() {
		return element;
	}

	public Collection<Integer> getRatings() {
		return ratings;
	}

	@Override
	public String toString() {
		return "MultiRating[" + element.toString() + "->" + ratings + "]";
	}

}

package suncertify.client;

import java.util.Comparator;

public class NumberComparator implements Comparator<String> {
	@Override
	public int compare(String o1, String o2) {
		final int one = Integer.parseInt(o1);
		final int two = Integer.parseInt(o2);
		return Integer.compare(one, two);
	}
}
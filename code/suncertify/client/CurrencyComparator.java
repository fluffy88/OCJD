package suncertify.client;

import java.util.Comparator;

public class CurrencyComparator implements Comparator<String> {
	@Override
	public int compare(String o1, String o2) {
		final float one = Float.parseFloat(o1.substring(1));
		final float two = Float.parseFloat(o2.substring(1));
		return Float.compare(one, two);
	}
}
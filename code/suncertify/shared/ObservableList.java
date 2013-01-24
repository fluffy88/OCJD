package suncertify.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObservableList<E> extends ArrayList<E> {

	private static final long serialVersionUID = 2721127231070341266L;

	List<ListChangedListener> listeners = new ArrayList<ListChangedListener>();

	public boolean addListener(ListChangedListener l) {
		return listeners.add(l);
	}

	public boolean removeListener(ListChangedListener l) {
		return listeners.remove(l);
	}

	private void fireListChanged() {
		for (ListChangedListener l : listeners) {
			l.listChanged();
		}
	}

	@Override
	public boolean add(E e) {
		boolean result = super.add(e);
		fireListChanged();
		return result;
	}

	@Override
	public boolean remove(Object o) {
		boolean result = super.remove(o);
		fireListChanged();
		return result;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean result = super.addAll(c);
		fireListChanged();
		return result;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		boolean result = super.addAll(index, c);
		fireListChanged();
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean result = super.removeAll(c);
		fireListChanged();
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean result = super.retainAll(c);
		fireListChanged();
		return result;
	}

	@Override
	public void clear() {
		super.clear();
		fireListChanged();
	}

	@Override
	public E set(int index, E element) {
		E result = super.set(index, element);
		fireListChanged();
		return result;
	}

	@Override
	public void add(int index, E element) {
		super.add(index, element);
		fireListChanged();
	}

	@Override
	public E remove(int index) {
		E result = super.remove(index);
		fireListChanged();
		return result;
	}
}

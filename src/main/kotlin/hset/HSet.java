package main.kotlin.hset;

import main.kotlin.llist.LList;

public class HSet<E> extends LList<E> {

    @Override
    public void add(E element) {
        if (!contains(element))
            super.add(element);
    }

    public void remove(int element) {
        int position = getElements().indexOf(element);
        if (position >= 0)
            super.remove(getElements().size()-1-position);
    }

    public boolean contains(E element) {
        return getElements().contains(element);
    }
}

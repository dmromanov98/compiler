package main.kotlin.llist;

import java.util.ArrayList;
import java.util.List;

public class LList<E> {

    transient Entry<E> first;
    transient Entry<E> last;

    private int size = 0;

    public void add(E element) {
        final Entry<E> l = last;
        final Entry<E> newNode = new Entry<>(l, element, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
    }

    public E get(int position) {
        E[] els = getAllElements();
        return els[els.length - 1 - position];
    }

    public void remove(int position) {
        E[] els = getAllElements();
        els[els.length - 1 - position] = null;
        if (els.length > 0 && els.length - 1 - position < els.length)
            size--;
        first = null;
        last = null;
        for (int i = els.length - 1; i >= 0; i--) {
            if (els[i] != null)
                add(els[i]);
        }

    }

    public List<E> getElements() {
        List<E> lst = new ArrayList<>();
        Entry<E> entries = last;
        while (entries != null && entries.prev != null) {
            lst.add(entries.item);
            entries = entries.prev;
        }
        if (entries != null)
            lst.add(entries.item);
        return lst;
    }

    private E[] getAllElements() {
        return (E[]) getElements().toArray();
    }

    private static class Entry<E> {
        E item;
        Entry<E> next;
        Entry<E> prev;

        Entry(Entry<E> prev, E element, Entry<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
}

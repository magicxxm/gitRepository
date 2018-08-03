package com.mushiny.wms.masterdata.obbasics.common.structure;

/**
 * An iterator for traversing the elements of a circular list.
 * The iterator traverses the list beginning at the head, and heads toward
 * tail.
 * <p>
 * Typical use:
 * <p>
 * <pre>
 *      List l = new CircularList();
 *      // ...list gets built up...
 *      Iterator li = l.iterator();
 *      while (li.hasNext())
 *      {
 *          System.out.println(li.get());
 *          li.next();
 *      }
 *      li.reset();
 *      while (li.hasNext())
 *      { .... }
 * </pre>
 */
public class CircularListIterator<E> extends AbstractIterator<E> {

    /**
     * The tail of the traversed list.
     */
    protected Node<E> tail;
    /**
     * The current value of the iterator.
     */
    protected Node<E> current;

    /**
     * Constructs an iterator over circular list whose tail is t
     *
     * @param t The tail of the list to be traversed.
     * @pre t is a reference to a circular list element
     * @post constructs an iterator for traversing circular list
     */
    public CircularListIterator(Node<E> t) {
        tail = t;
        reset();
    }

    /**
     * Resets iterator to consider the head of the list.
     *
     * @post rests iterator to point to head of list
     */
    public void reset() {
        if (tail == null) current = null;
        else current = tail.next();
    }

    /**
     * Determine if there are unconsidered elements.
     *
     * @return True iff some element has not been considered.
     * @post returns true if some elements not visited
     */
    public boolean hasNext() {
        return current != null;
    }

    /**
     * Return the current value and increment iterator.
     *
     * @return The current value before incrementing.
     * @pre hasNext()
     * @post returns current element, increments iterator
     */
    public E next() {
        E result = current.value();
        if (current == tail) current = null;
        else current = current.next();
        return result;
    }

    /**
     * Determine the current value of iterator.
     *
     * @return The current value of the iterator.
     * @pre hasNext()
     * @post returns current value
     */
    public E get() {
        return current.value();
    }
}

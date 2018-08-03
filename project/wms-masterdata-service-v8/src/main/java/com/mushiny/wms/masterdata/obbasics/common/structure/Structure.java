package com.mushiny.wms.masterdata.obbasics.common.structure;

import java.util.Collection;
import java.util.Iterator;

/**
 * The interface of a basic, mutable data structure.
 * <p>
 * This interface is the basis for most mutable structures in the structure
 * package.  While most methods are easy implement, it is often sufficient
 * to simply extend a basic, abstract implementation of this class, the
 * AbstractStructure.  The AbstractStructure implements the isEmpty,
 * contains and collection methods.  They may be overridden if a particularly
 * efficient technique is to be preferred.
 */
public interface Structure<E> extends Iterable<E> {

    /**
     * Determine the size of the structure.
     *
     * @return the size of the structure
     * @post computes number of elements contained in structure
     */
    public int size();

    /**
     * Determine if there are elements within the structure.
     *
     * @return true if the structure is empty; false otherwise
     * @post return true iff the structure is empty
     */
    public boolean isEmpty();

    /**
     * Removes all elements from the structure.
     *
     * @post the structure is empty
     */
    public void clear();

    /**
     * Determines if the structure contains a value.
     *
     * @param value non-null value to be found within structure
     * @return true when some value equals value
     * @pre value is non-null
     * @post returns true iff value.equals some value in structure
     */
    public boolean contains(E value);

    /**
     * Inserts value in some structure-specific location.
     *
     * @param value the value to be added to the structure; non-null
     * @pre value is non-null
     * @post value has been added to the structure
     * replacement policy is not specified
     */
    public void add(E value);

    /**
     * Removes value from the structure.
     *
     * @param value value matching the value to be removed
     * @return returns the object that was removed, or null if none.
     * @pre value is non-null
     * @post an object equal to value is removed and returned, if found
     */
    public E remove(E value);

    /**
     * Returns an enumeration for traversing the structure.
     *
     * @return an enumeration for traversing the structure
     * @post returns an enumeration for traversing structure;
     * all <code>structure</code> package implementations return
     * an <code>AbstractIterator</code>
     * @see AbstractIterator
     * @see Iterator
     * @see java.util.Enumeration
     * @see #iterator
     */
    public java.util.Enumeration elements();

    /**
     * Returns an iterator for traversing the structure.
     *
     * @return an iterator for traversing the structure
     * @post returns an iterator for traversing structure;
     * all <code>structure</code> package implementations return
     * an <code>AbstractIterator</code>
     * @see AbstractIterator
     * @see Iterator
     * @see java.util.Enumeration
     * @see #elements
     */
    public Iterator<E> iterator();

    /**
     * Returns a java.util.Collection wrapping this structure.
     *
     * @return a Collection that is equivalent to this structure
     * @post returns a <code>Collection</code> that may be used with
     * Java's Collection Framework
     */
    public Collection<E> values();

    public void add(int i, E value);

    public E get(int i);

    public E set(int i, E value);

    public E remove(int i);

    public int indexOf(E value);

    public int lastIndexOf(E value);
}

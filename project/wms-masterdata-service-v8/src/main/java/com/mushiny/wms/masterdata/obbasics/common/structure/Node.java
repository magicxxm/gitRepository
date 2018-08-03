package com.mushiny.wms.masterdata.obbasics.common.structure;

/**
 * A class supporting a singly linked list element.  Each element
 * contains a value and maintains a single reference to the next
 * node in the list.
 *
 * @author
 */
public class Node<E> implements Cloneable {

    /**
     * The data value stored in this node.
     */
    protected E data; // value stored in this element
    /**
     * Reference to the next node in the list.
     */
    protected Node<E> nextElement; // ref to next

    /**
     * Construct a singly linked list element.
     *
     * @param v    The value to be referenced by this element.
     * @param next A reference to the next value in the list.
     * @pre v is a value, next is a reference to remainder of list
     * @post an element is constructed as the new head of list
     */
    public Node(E v, Node<E> next) {
        data = v;
        nextElement = next;
    }

    /**
     * Constructs a node not associated with
     * any list.  next reference is set to null.
     *
     * @param v The value to be inserted into the node.
     * @post constructs a new tail of a list with value v
     */
    public Node(E v) {
        this(v, null);
    }

    /**
     * @post returns reference to next value in list
     */
    public Node<E> next() {
        return nextElement;
    }

    /**
     * Update the next element.
     *
     * @param next The new value of the next element reference.
     * @post sets reference to new next value
     */
    public void setNext(Node<E> next) {
        nextElement = next;
    }

    /**
     * Fetch the value associated with this element.
     *
     * @return Reference to the value stored within this element.
     * @post returns value associated with this element
     */
    public E value() {
        return data;
    }

    /**
     * Set the value associated with this element.
     *
     * @param value The new value to be associated with this element.
     * @post sets value associated with this element
     */
    public void setValue(E value) {
        data = value;
    }

    /**
     * Construct a string representation of element.
     *
     * @return The string representing element.
     * @post returns string representation of element
     */
    public String toString() {
        return "<Node: " + value() + ">";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

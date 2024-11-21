package edu.grinnell.csc207.blockchains;

/**
 * A node wrapper for Block data.
 *
 * @param <T>
 *    The node type.
 *
 * @author Sara Jaljaa
 */
public class Node<T> {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The next node.
   */
  Node<T> next;

  /**
   * The value stored at the node.
   */
  T data;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new node.
   *
   * @param val
   *    The value to be stored at the new node.
   */
  public Node(T val) {
    this.next = null;
    this.data = val;
  } // Node(T)

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Insert a value after this node.
   *
   * @param val
   *    The value to insert.
   */
  public void insert(T val) {
    Node<T> node = new Node<T>(val);
    if (this.next != null) {
      node.next = this.next;
    } // if
    this.next = node;
  } // insert(T)
} // class Node

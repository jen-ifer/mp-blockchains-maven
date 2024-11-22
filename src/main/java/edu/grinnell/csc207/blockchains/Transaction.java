package edu.grinnell.csc207.blockchains;

/**
 * A simple transaction.
 *
 * @author
 *   Samuel A. Rebelsky
 */
public class Transaction {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The source of a transfer.
   */
  private String source;

  /**
   * The target of a transfer.
   */
  private String target;

  /**
   * The amount transferred.
   */
  private int amount;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new Transaction. For a deposit, rather than a transfer,
   * use the empty string for the source.
   *
   * @param src
   *   The source of the transaction (or empty for a deposit).
   * @param tgt
   *   The person receiving the transaction.
   * @param amt
   *   The funds transfered.
   */
  public Transaction(String src, String tgt, int amt) {
    this.source = src;
    this.target = tgt;
    this.amount = amt;
  } // Transaction(src)

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Get the source of the transaction.
   *
   * @return The source (or the empty string, if it'sa deposit).
   */
  public String getSource() {
    return this.source;
  } // getSource()

  /**
   * Get the target of the transaction.
   *
   * @return The target.
   */
  public String getTarget() {
    return this.target;
  } // getTarget()

  /**
   * Get the amount of the transaction.
   *
   * @return the amount.
   */
  public int getAmount() {
    return this.amount;
  } // getAmount()

  /**
   * Convert to string form.
   *
   * @return a string of the form [Source: source, Target: target,
   *    Amount: amounts] if the source is nonempty. Otherwise, returns
   *    a string in the form [Deposit, Target: target, Amount: amount].
   */
  public String toString() {
    return String.format("[%s, Target: %s, Amount: %s]",
        ("".equals(this.source)) ? "Deposit" : "Source: " + this.source,
        this.target,
        this.amount);
  } // toString()

  /**
   * Get a hash code.
   *
   * @return the hash code.
   */
  public int hashCode() {
    return this.toString().hashCode();
  } // hashCode()

  /**
   * Determine if this Transaction equals another object.
   *
   * @param other
   *   The object to compare to.
   *
   * @return true if the other object is a Transaction with the
   *   same fields.
   */
  public boolean equals(Object other) {
    return (other instanceof Transaction)
        && this.equals((Transaction) other);
  } // equals(Object)

  /**
   * Determine if this Transaction equals another Transaction.
   *
   * @param other
   *   The transaction to compare to.
   *
   * @return true if the other object has the same source, target,
   *   and value.
   */
  public boolean equals(Transaction other) {
    return other.source.equals(this.source)
        && other.target.equals(this.target)
        && other.amount == this.amount;
  } // equals(Transaction)
} // class Transaction

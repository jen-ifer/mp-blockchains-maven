package edu.grinnell.csc207.blockchains;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Blocks to be stored in blockchains.
 *
 * @author Sara Jaljaa
 * @author Jenifer Silva
 * @author Samuel A. Rebelsky
 */
public class Block {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The number of the block.
   */
  private int number;

  /**
   * The transaction that contains the source, target, and transaction
   * number.
   */
  private Transaction transaction;

  /**
   * The hash number of the previous block.
   */
  private Hash prevHash;

  /**
   * The nonce value of the block.
   */
  private long nonce;

  /**
   * The hash number of the block.
   */
  private Hash hash;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new block from the specified block number, transaction, and
   * previous hash, mining to choose a nonce that meets the requirements
   * of the validator.
   *
   * @param num
   *   The number of the block.
   * @param transaction
   *   The transaction for the block.
   * @param prevHash
   *   The hash of the previous block.
   * @param check
   *   The validator used to check the block.
   */
  public Block(int num, Transaction transaction, Hash prevHash,
      HashValidator check) throws NoSuchAlgorithmException {
    /* Assign normal fields */
    this.number = num;
    this.transaction = transaction;
    this.prevHash = prevHash;

    /* Iterate over all possible long values, if the nonce is valid,
    then break from the loop. */
    for (long l = 0; l < Long.MAX_VALUE; l++) {
      if (check.isValid(computeHash(num, transaction, prevHash, l))) {
        this.nonce = l;
        break;
      } // if
    } // for
  } // Block(int, Transaction, Hash, HashValidator)

  /**
   * Create a new block, computing the hash for the block.
   *
   * @param num
   *   The number of the block.
   * @param transaction
   *   The transaction for the block.
   * @param prevHash
   *   The hash of the previous block.
   * @param nonce
   *   The nonce of the block.
   */
  public Block(int num, Transaction transaction, Hash prevHash,
      long nonce) throws NoSuchAlgorithmException {
    this.number = num;
    this.transaction = transaction;
    this.prevHash = prevHash;
    this.nonce = nonce;
    this.hash = computeHash((num), transaction, prevHash, nonce);
  } // Block(int, Transaction, Hash, long)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Compute the hash of the block given all the other info already
   * stored in the block.
   *
   * @param number
   *    The number of the block.
   * @param transaction
   *    The transaction meta-information of the block.
   * @param prevHash
   *    The previous block's hash value (if it exists).
   * @param nonse
   *    The nonce value, unhashed.
   *
   * @return
   *    The block's computed hash value.
   *
   * (Self-note) Implement in this order:
   * 1) Block number
   * 2) Source, target, amount (transaction)
   * 5) Previous block's hash (if not first block)
   * 6) The nonce hash
   */
  protected static Hash computeHash(int number, Transaction transaction,
      Hash prevHash, long nonce) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("sha-256");

    /* Update the block number's hash */
    md.update(ByteBuffer.allocate(Integer.BYTES).putInt(number).array());

    /* Update the transaction's hash */
    md.update(transaction.getSource().getBytes());
    md.update(transaction.getTarget().getBytes());
    md.update(ByteBuffer.allocate(Integer.BYTES).putInt(transaction.getAmount()).array());

    /* Update the previous block's hash (if it exists) */
    if (prevHash != null) {
      md.update(prevHash.getBytes());
    } // if

    /* Update the nonce's hash */
    md.update(ByteBuffer.allocate(Long.BYTES).putLong(nonce).array());

    /* Digest */
    return new Hash(md.digest());
  } // computeHash()

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Get the number of the block.
   *
   * @return the number of the block.
   */
  public int getNum() {
    return this.number;
  } // getNum()

  /**
   * Get the transaction stored in this block.
   *
   * @return the transaction.
   */
  public Transaction getTransaction() {
    return this.transaction;
  } // getTransaction()

  /**
   * Get the nonce of this block.
   *
   * @return the nonce.
   */
  public long getNonce() {
    return this.nonce;   // STUB
  } // getNonce()

  /**
   * Get the hash of the previous block.
   *
   * @return the hash of the previous block.
   */
  Hash getPrevHash() {
    return this.prevHash;
  } // getPrevHash

  /**
   * Get the hash of the current block.
   *
   * @return the hash of the current block.
   */
  Hash getHash() {
    return this.hash;
  } // getHash

  /**
   * Get a string representation of the block.
   *
   * @return a string representation of the block.
   */
  public String toString() {
  //   if(transaction.getSource() != null){
  //   return "Block" + this.number +
  //    "[Source" + transaction.getSource() + ", Target:" + transaction.getTarget() + "], Amount" 
  //    + transaction.getAmount() + ", Nonce" + this.theNonce + "prevHash:" + this.previousHash + "hash:" + this.blockHash;
  // } // toString()
  return "ehhh";
  // } // class Block
  } // toString()
} // class Block

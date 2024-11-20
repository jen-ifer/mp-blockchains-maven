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
   * The transaction that contains the source, target, and
   * transaction number.
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
   * Create a new block from the specified block number, transaction,
   * and previous hash, mining to choose a nonce that meets the requirements
   * of the validator.
   *
   * @param num
   *    The number of the block.
   * @param transaction
   *    The transaction for the block.
   * @param prevHash
   *    The hash of the previous block.
   * @param check
   *    The validator used to check the block.
   */
  public Block(int num, Transaction transaction, Hash prevHash,
      HashValidator check) {
    this.number = num;
    this.transaction = transaction;
    this.prevHash = prevHash;

    /* Mine for a nonce value to find the block's hash. */
    try {
      this.nonce = mine(num, transaction, prevHash, check);
      this.hash = computeHash(num, transaction, prevHash, this.nonce);
    } catch (NoSuchAlgorithmException e) {
      return;
    } // try/catch
  } // Block(int, Transaction, Hash, HashValidator)

  /**
   * Create a new block, computing the hash for the block.
   *
   * @param num
   *    The number of the block.
   * @param transaction
   *    The transaction for the block.
   * @param prevHash
   *    The hash of the previous block.
   * @param nonce
   *    The nonce of the block.
   */
  public Block(int num, Transaction transaction, Hash prevHash, long nonce) {
    this.number = num;
    this.transaction = transaction;
    this.prevHash = prevHash;
    this.nonce = nonce;

    /* Find the block's hash value. */
    try {
      this.hash = computeHash((num), transaction, prevHash, nonce);
    } catch (NoSuchAlgorithmException e) {
      return;
    } // try/catch
  } // Block(int, Transaction, Hash, long)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Mine for a nonce value.
   *
   * @param num
   *    The number of the block.
   * @param transaction
   *    The transaction for the block.
   * @param prevHash
   *    The hash of the previous block.
   * @param check
   *    The validator used to check the block.
   * @return
   *    The mined nonce value.
   *
   * @throws NoSuchAlgorithmException
   *    When the hashing algorithm fails to be instantiated.
   */
  protected static long mine(int num, Transaction transaction,
      Hash prevHash, HashValidator check) throws NoSuchAlgorithmException {
    Hash hash = null;

    /* Iterate through all possible long values to find the nonce. */
    for (long nonce = 0; nonce < Long.MAX_VALUE; nonce++) {
      hash = computeHash(num, transaction, prevHash, nonce);
      if (check.isValid(hash)) {
        return nonce;
      } // if
    } // for(nonce)
    throw new NoSuchAlgorithmException();
  } // mine(int, Transaction, Hash, HashValidator)

  /**
   * Compute the hash of the block given all the other info already stored
   * in the block.
   *
   * @param number
   *    The number of the block.
   * @param transaction
   *    The transaction meta-information of the block.
   * @param prevHash
   *    The previous block's hash value (if it exists).
   * @param nonce
   *    The nonce value, unhashed.
   * @return
   *    The block's computed hash value.
   *
   * @throws NoSuchAlgorithmException
   *    When the hashing algorithm fails to be instantiated.
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
  } // computeHash(int, Transaction, Hash, long)

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
    return this.nonce;
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
   * @return
   *    A string representation of the block.
   */
  public String toString() {
    StringBuilder str = new StringBuilder();
    Transaction receipt = this.getTransaction();

    /* Start with block # */
    str.append(String.format("Block %d (Transaction [", this.number));

    /* Format depending on transaction type */
    if (receipt.getSource() == null) {
      str.append(String.format(
          "Deposit, "));
    } else {
      str.append(String.format(
          "Source: %s, ",
          receipt.getSource()));
    } // elif

    /* The rest of the transaction formatting */
    str.append(String.format("Target: %s, Amount: %d], ",
        receipt.getTarget(), receipt.getAmount()));

    /* Format hashes */
    str.append(String.format("Nonce: %d, prevHash: %s, hash: %s)",
        this.getNonce(), this.getPrevHash().toString(), this.getHash().toString()));
    return str.toString();
  } // toString()
} // class Block

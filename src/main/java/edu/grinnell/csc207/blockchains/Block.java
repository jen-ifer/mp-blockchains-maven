package edu.grinnell.csc207.blockchains;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

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
  int blockNumber;
  Transaction theTransaction;
  Hash previousHash;
  long theNonce;
  Hash blockHash;
 //MessageDigest updating;


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
      HashValidator check) {
    this.blockNumber = num;
    this.theTransaction = transaction;
    this.previousHash = prevHash;
    HashValidator standardValidator =
    (hash) -> (hash.length() >= 3) && (hash.get(0) == 0)
        && (hash.get(1) == 0) && (hash.get(2) == 0);

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
  public Block(int num, Transaction transaction, Hash prevHash, long nonce) {
    this.blockNumber = num;
    this.theTransaction = transaction;
    this.previousHash = prevHash;
    this.theNonce = nonce;
  } // Block(int, Transaction, Hash, long)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

/**
   * Compute the hash of the block given all the other info already
   * stored in the block.
   */
  public MessageDigest computing() throws Exception {
    MessageDigest md = MessageDigest.getInstance("sha-256");
    md.update(ByteBuffer.allocate(Integer.BYTES).putInt(this.blockNumber).array());
    md.update((this.theTransaction.getSource()).getBytes());
    md.update((this.theTransaction.getTarget()).getBytes());
    md.update(ByteBuffer.allocate(Integer.BYTES).putInt(this.theTransaction.getAmount()).array());
    md.update(this.previousHash.getBytes());
    md.update(ByteBuffer.allocate(Long.BYTES).putLong(this.theNonce).array());

    return md;
  } // calculateHash(String)

  public MessageDigest computing2(int num, Transaction transaction, Hash prevHash, long nonce){
    computing().
  }


  /**
   * Compute the hash of the block given all the other info already
   * stored in the block.
   */
  static void computeHash() throws Exception {
     md = ;
  } // calculateHash(String)

 // computeHash()

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Get the number of the block.
   *
   * @return the number of the block.
   */
  public int getNum() {
    return this.blockNumber;
  } // getNum()

  /**
   * Get the transaction stored in this block.
   *
   * @return the transaction.
   */
  public Transaction getTransaction() {
    return this.theTransaction;
  } // getTransaction()

  /**
   * Get the nonce of this block.
   *
   * @return the nonce.
   */
  public long getNonce() {
    return this.theNonce;   // STUB
  } // getNonce()

  /**
   * Get the hash of the previous block.
   *
   * @return the hash of the previous block.
   */
  Hash getPrevHash() {
    return this.previousHash;
  } // getPrevHash

  /**
   * Get the hash of the current block.
   *
   * @return the hash of the current block.
   */
  Hash getHash() {
    return this.blockHash;
  } // getHash

  /**
   * Get a string representation of the block.
   *
   * @return a string representation of the block.
   */
  public String toString() {
    if(theTransaction.getSource() != null){
    return "Block" + this.blockNumber +
     "[Source" + theTransaction.getSource() + ", Target:" + theTransaction.getTarget() + "], Amount" 
     + theTransaction.getAmount() + ", Nonce" + this.theNonce + "prevHash:" + this.previousHash + "hash:" + this.blockHash;
  } // toString()
  return "ehhh";
} // class Block
}



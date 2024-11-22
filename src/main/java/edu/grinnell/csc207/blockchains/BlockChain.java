package edu.grinnell.csc207.blockchains;

import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
// import java.util.NoSuchElementException;

/**
 * A full blockchain.
 *
 * @author Sara Jaljaa
 * @author Jenifer Silva
 * @author Samuel A. Rebelsky
 */
public class BlockChain implements Iterable<Transaction> {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The first block in the chain.
   */
  private Node<Block> start;

  /**
   * The block before the last block.
   */
  private Node<Block> penultimate;

  /**
   * The HashValidator to check the hashes (for continuity).
   */
  private HashValidator validator;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new blockchain using a validator to check elements.
   *
   * @param check The validator used to check elements.
   */
  public BlockChain(HashValidator check) {
    this.start = new Node<Block>(new Block(0, new Transaction("", "", 0), null, check));
    this.penultimate = this.start;
    this.validator = check;
  } // BlockChain(HashValidator)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Format strings given some iterator.
   *
   * @param it The iterator.
   * @return A new string of the formatted data.
   */
  public String format(Iterator<? extends Object> it) {
    StringBuilder str = new StringBuilder();

    while (it.hasNext()) {
      str.append(it.next().toString());
      str.append("\n");
    } // while
    return new String(str);
  } // format(Iterator<? extends Object>)

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Mine for a new valid block for the end of the chain, returning that block.
   *
   * @param t The transaction that goes in the block.
   *
   * @return a new block with correct number, hashes, and such.
   */
  public Block mine(Transaction t) throws Exception {
    /* Find the next number for the block & the previous block's hash */
    int num = this.penultimate.next.data.getNum() + 1;
    Hash prevHash = this.penultimate.next.data.getHash();

    /* Mine for the nonce */
    long nonce = Block.mine(num, t, prevHash, this.validator);
    return new Block(num, t, prevHash, nonce);
  } // mine(Transaction)

  /**
   * Get the number of blocks currently in the chain.
   *
   * @return The number of blocks in the chain, including the initial block.
   */
  public int getSize() {
    return this.penultimate.next.data.getNum() + 1;
  } // getSize()

  /**
   * Add a block to the end of the chain.
   *
   * @param blk The block to add to the end of the chain.
   *
   * @throws IllegalArgumentException if (a) the hash is not valid, (b) the hash is not appropriate
   *         for the contents, or (c) the previous hash is incorrect.
   */
  public void append(Block blk) {
    Hash hash;

    /* Find the hash value of the block */
    try {
      hash =
          Block.computeHash(blk.getNum(), blk.getTransaction(), blk.getPrevHash(), blk.getNonce());
    } catch (NoSuchAlgorithmException e) {
      return;
    } // try/catch

    /* Check if the hash is a valid hash */
    if (!this.validator.isValid(blk.getPrevHash())
        || !this.penultimate.next.data.getPrevHash().equals(blk.getPrevHash())
        || !hash.equals(blk.getHash())) {
      throw new IllegalArgumentException();
    } // elif

    /* Add the block to the end, adjust previous block */
    this.penultimate.next.insert(blk);
    this.penultimate = this.penultimate.next;
  } // append(Block)

  /**
   * Attempt to remove the last block from the chain.
   *
   * @return false if the chain has only one block (in which case it's
   *   not removed) or true otherwise (in which case the last block
   *   is removed).
   */
  public boolean removeLast() {
  
    if (this.start.next == null) {
      return false;
    } // if
    this.penultimate.next = null;
    this.penultimate.next
    return true;
  } // removeLast()

  /**
   * Get the hash of the last block in the chain.
   *
   * @return the hash of the last block in the chain.
   */
  public Hash getHash() {
    return this.penultimate.next.data.getHash();
  } // getHash()

  /**
   * Determine if the blockchain is correct in that (a) the balances are legal/correct at every
   * step, (b) that every block has a correct previous hash field, (c) that every block has a hash
   * that is correct for its contents, and (d) that every block has a valid hash.
   *
   * @return true if the blockchain is correct and false otherwise.
   */
  public boolean isCorrect() {
    return true; // STUB
  } // isCorrect()

  /**
   * Determine if the blockchain is correct in that (a) the balances are legal/correct at every
   * step, (b) that every block has a correct previous hash field, (c) that every block has a hash
   * that is correct for its contents, and (d) that every block has a valid hash.
   *
   * @throws Exception If things are wrong at any block.
   */
  public void check() throws Exception {
    // STUB
  } // check()

  /**
   * Return an iterator of all the people who participated in the system.
   *
   * @return an iterator of all the people in the system.
   *
   *         *Not yet tested (still finishing tests).
   */
  public Iterator<String> users() {
    return new Iterator<String>() {
      private Node<Block> block = BlockChain.this.start;
      Transaction receipt;

      /* Check for next node */
      public boolean hasNext() {
        return (block != null) && (block.data.getTransaction() != null);
      } // hasNext()

      /* Get the next user */
      public String next() {
        receipt = block.data.getTransaction();
        block = block.next;
        return ("".equals(receipt.getSource()) ? receipt.getTarget() : receipt.getSource());
      } // next()
    };
  } // users()

  /**
   * Find one user's balance.
   *
   * @param user The user whose balance we want to find.
   *
   * @return that user's balance (or 0, if the user is not in the system).
   */
  public int balance(String user) {
    int total = 0;
    Transaction id;
    Iterator<Block> it = this.blocks();
    while (it.hasNext()) {
      id = it.next().getTransaction();
      if (user.equals(id.getSource())) {
        total = total + id.getAmount();
      }
      if (user.equals(id.getTarget())) {
        total = total - id.getAmount();
      }
    }
    return total; // STUB
  } // balance()

  /**
   * Get an iterator for all the blocks in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   *
   *         *Not yet tested (still finishing tests).
   */
  public Iterator<Block> blocks() {
    return new Iterator<Block>() {
      private Node<Block> block = BlockChain.this.start;

      /* Check for next node */
      public boolean hasNext() {
        return (block != null);
      } // hasNext()

      /* Get the next block */
      public Block next() {
        Block b = block.data;
        block = block.next;
        return b;
      } // next()
    };
  } // blocks()

  /**
   * Get an iterator for all the transactions in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   *
   *         *Not yet tested (still finishing tests).
   */
  public Iterator<Transaction> iterator() {
    return new Iterator<Transaction>() {
      private Node<Block> block = BlockChain.this.start;

      /* Check for next node */
      public boolean hasNext() {
        return (block != null) && (block.data.getTransaction() != null);
      } // hasNext()

      /* Get the next transaction */
      public Transaction next() {
        block = block.next;
        return block.data.getTransaction();
      } // next()
    };
  } // iterator()
} // class BlockChain

package edu.grinnell.csc207.blockchains;

import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.NoSuchElementException;
import edu.grinnell.csc207.util.Node;

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
  public Node<Block> start;

  /**
   * The block before the last block.
   */
  public Node<Block> penultimate;

  /**
   * The HashValidator to check the hashes (for continuity).
   */
  public HashValidator validator;

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
  public Block mine(Transaction t) {
    /* Find the next number for the block & the previous block's hash */
    int num = this.penultimate.next.data.getNum() + 1;
    Hash prevHash = this.penultimate.next.data.getHash();

    /* Mine for the nonce */
    try {
    long nonce = Block.mine(num, t, prevHash, this.validator);
    return new Block(num, t, prevHash, nonce);
    } catch (Exception e) {
      return new Block(num, t, prevHash, this.validator);
    } // try/catch
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
        // || !this.penultimate.next.data.getPrevHash().equals(blk.getPrevHash())
        || !hash.equals(blk.getHash())) {
      throw new IllegalArgumentException();
    } // if

    /* Add the block to the end, adjust previous block */
    this.penultimate.insert(blk);
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

    /* The node position and the previous node's position */
    Node<Block> node = this.start;
    Node<Block> prev = null;

    /* Loop through the nodes; if the node is equal to the second to
    last element, then:
    - Set the end element to null (delete the node)
    - Set the second to last element equal to the previous element (move it back)
    - Set the end to the new terminating node (move it back) */
    while (node.next != null) {
      if (node.data.equals(this.penultimate.data)) {
        node.next = null;
        this.penultimate = prev;
        this.penultimate.next = node;
        break;
      } // if
      prev = node;
      node = node.next;
    } // while
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
      private Iterator<Block> it = BlockChain.this.blocks();
      private Transaction receipt;

      /* Check for next node */
      @Override
      public boolean hasNext() {
        return it.hasNext();
      } // hasNext()

      /* Get the next user */
      @Override
      public String next() {
        if (!this.hasNext()) {
          throw new NoSuchElementException();
        } // if (!hasNext())
        receipt = it.next().getTransaction();
        return ("".equals(receipt.getSource())
            ? receipt.getTarget() : receipt.getSource());
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
   */
  public Iterator<Block> blocks() {
    return new Iterator<Block>() {
      private Node<Block> block = BlockChain.this.start;

      /* Check for next node */
      @Override
      public boolean hasNext() {
        return this.block != null;
      } // hasNext()

      /* Get the next block */
      @Override
      public Block next() {
        if (block == null) {
          throw new NoSuchElementException();
        } // if (!hasNext())
        block = this.block.next;
        return block.data;
      } // next()
    };
  } // blocks()

  /**
   * Get an iterator for all the transactions in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   */
  public Iterator<Transaction> iterator() {
    return new Iterator<Transaction>() {
      private Iterator<Block> it = BlockChain.this.blocks();

      /* Check for next node */
      @Override
      public boolean hasNext() {
        return it.hasNext();
      } // hasNext()

      /* Get the next transaction */
      @Override
      public Transaction next() {
        if (!it.hasNext()) {
          throw new NoSuchElementException();
        } // if (!hasNext())
        return it.next().getTransaction();
      } // next()
    };
  } // iterator()
} // class BlockChain

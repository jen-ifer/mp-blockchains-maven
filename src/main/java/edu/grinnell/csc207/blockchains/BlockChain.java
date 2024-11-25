package edu.grinnell.csc207.blockchains;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.Node;
import edu.grinnell.csc207.util.NullKeyException;
import java.io.PrintWriter;

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
   * The last block in the chain.
   */
  private Node<Block> end;

  /**
   * The HashValidator to check the hashes (for continuity).
   */
  private HashValidator validator;

  /**
   * The size of the chain.
   */
  private int size;

  /**
   * To store the users and their balance.
   */
  private AssociativeArray<String, Integer> users;

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
    this.end = this.start;
    this.validator = check;
    this.size = 1;
    this.users = new AssociativeArray<>();
  } // BlockChain(HashValidator)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Update/add a user to the blockchain.
   *
   * @param b The added block containing the transaction data.
   */
  private static void updateUser(AssociativeArray<String, Integer> users, Block b) {
    Transaction receipt = b.getTransaction();
    try {
      if ("".equals(receipt.getSource())) {
        users.set(receipt.getTarget(), receipt.getAmount());
        return;
      }
      users.set(receipt.getSource(), receipt.getAmount());
      users.set(receipt.getTarget(), receipt.getAmount());
    } catch (KeyNotFoundException | NullKeyException e) {
      return;
    } // try/catch
  } // updateUser(AssociativeArray<String, Integer>, Block)

  /**
   * Remove a user from the blockchain.
   *
   * @param b The block to be removed containing the transaction data.
   */
  private static void removeUser(AssociativeArray<String, Integer> users, Block b) {
    Transaction receipt = b.getTransaction();
    users.remove(receipt.getSource());
    users.remove(receipt.getTarget());
  } // removeUser(AssociativeArray<String, Integer>, Block)

  /**
   * Format strings given some iterator (mainly a helper for the UI later & personal testing).
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
   * @return A new block with correct number, hashes, and such.
   */
  public Block mine(Transaction t) {
    /* Find the next number for the block & the previous block's hash */
    Hash prevHash = this.end.data.getHash();

    /* Mine for the nonce */
    try {
      long nonce = Block.mine(this.size, t, prevHash, this.validator);
      return new Block(this.size, t, prevHash, nonce);
    } catch (Exception e) {
      return null;
    } // try/catch
  } // mine(Transaction)

  /**
   * Get the number of blocks currently in the chain.
   *
   * @return The number of blocks in the chain, including the initial block.
   */
  public int getSize() {
    return this.size;
  } // getSize()

  /**
   * Add a block to the end of the chain.
   *
   * @param blk The block to add to the end of the chain.
   *
   * @throws IllegalArgumentException If (a) the hash is not valid, (b) the hash is not appropriate
   *         for the contents, or (c) the previous hash is incorrect.
   */
  public void append(Block blk) {
    /* The re-computed hash of the block (validity check) */
    Hash hash;

    /* The values associated with the block */
    int num = blk.getNum();
    Transaction t = blk.getTransaction();
    Hash prev = blk.getPrevHash();
    long nonce = blk.getNonce();

    /* Re-compute the hash value */
    try {
      hash = Block.computeHash(num, t, prev, nonce);
    } catch (NoSuchAlgorithmException e) {
      return;
    } // try/catch

    /* Check if the hash is a valid hash */
    if (!this.validator.isValid(prev) || !hash.equals(blk.getHash())
        || !this.end.data.getHash().equals(prev)) {
      throw new IllegalArgumentException();
    } // if

    /* Add the block to the end, adjust previous block */
    this.end.insert(blk);
    this.end = this.end.next;

    /* Update the list of users */
    updateUser(this.users, blk);
    size++;
  } // append(Block)

  /**
   * Attempt to remove the last block from the chain.
   *
   * @return False if the chain has only one block (in which case it's not removed) or true
   *         otherwise (in which case the last block is removed).
   */
  public boolean removeLast() {
    if (this.size <= 1) {
      return false;
    } // if

    /* The node position and the previous node's position */
    Node<Block> node = this.start;
    Node<Block> prev = null;

    /*
     * Loop through the nodes; if the node is equal to the second to last element, then: - Set the
     * end element to null (delete the node) - Set the second to last element equal to the previous
     * element (move it back) - Set the end to the new terminating node (move it back)
     */
    while (node != null) {
      if (node.data.equals(this.end.data)) {
        removeUser(this.users, this.end.data);
        this.end = prev;
        this.end.next = null;
        size--;
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
   * @return The hash of the last block in the chain.
   */
  public Hash getHash() {
    return this.end.data.getHash();
  } // getHash()

  /**
   * Determine if the blockchain is correct in that (a) the balances are legal/correct at every
   * step, (b) that every block has a correct previous hash field, (c) that every block has a hash
   * that is correct for its contents, and (d) that every block has a valid hash.
   *
   * @return True if the blockchain is correct and false otherwise.
   */
  public boolean isCorrect() throws Exception {
    return (legalTransaction() && legalHash());
  } // isCorrect()

  /**
   * Checks for legal transactions between users.
   * 
   * @return True or false if the transaction is not negative.
   *
   * @throws KeyNotFoundException If the source or target cannot be found in the users.
   * @throws NullKeyException If the source or target is null.
   */
  private boolean legalTransaction() throws KeyNotFoundException, NullKeyException {
    AssociativeArray<String, Integer> users = new AssociativeArray<>();
    Iterator<Transaction> itCorrect = BlockChain.this.iterator();

    Transaction t;
    int balance;
    String src;
    String tgt;
    int amt;

    while (itCorrect.hasNext()) {
      t = itCorrect.next();
      src = t.getSource();
      tgt = t.getTarget();
      amt = t.getAmount();
      balance = amt;

      if (balance < 0) {
        return false;
      } // if

      if ("".equals(src)) {
        if (users.hasKey(tgt)) {
          balance = users.get(tgt);
          balance = balance + amt;
        } // if
        if (balance < 0) {
          return false;
        } // if
        users.set(tgt, amt);
      } else {
        if (users.hasKey(src)) {
          balance = users.get(src);
          balance = balance - amt;
        } else {
          return false;
        } // elif
        if (balance < 0) {
          return false;
        } // if
        users.set(src, amt);
      } // elif
      if (users.hasKey(tgt)) {
        balance = users.get(tgt);
        balance = balance + amt;
      } // if
      if (balance < 0) {
        return false;
      } // if
      users.set(tgt, balance);
    } // while
    return true;
  } // legalTransaction()

  public void update() {
    Iterator<Transaction> itCorrect = BlockChain.this.iterator();

    Transaction t;
    int balance;
    String src;
    String tgt;
    int amt;


      t = itCorrect.next();
      src = t.getSource();
      tgt = t.getTarget();
      amt = t.getAmount();
      balance = amt;

      try {
        if ("".equals(src)) {
          if (this.users.hasKey(tgt)) {
            balance = this.users.get(tgt) + amt;
            this.users.set(tgt, balance);
            return;
          } // if
        } else {
          if (this.users.hasKey(src)) {
            balance = this.users.get(src);
            balance = balance - amt;
            this.users.set(src, balance);
            if (!this.users.hasKey(src)) {
            this.users.set(src, 0);
          }
        }
          if (this.users.hasKey(tgt)) {
            balance = this.users.get(tgt);
            balance = balance + amt;
            this.users.set(tgt, balance);
          }
        }
      } catch (Exception e) {
        return;
       // try/catch
    } // while
  } // update()

  /**
   * Check that a block has the correct previous hash, calculated hash, and is true for the
   * validator.
   * 
   * @return True or false if the hash values are legal for a block.
   * @throws NoSuchAlgorithmException When the hashing algorithm fails to be instantiated.
   */
  public boolean legalHash() throws NoSuchAlgorithmException {
    Node<Block> current = this.start;
    Node<Block> previous = null;
    Hash correctValueHash;

    while (current.next != null) {
      previous = current;
      current = current.next;
      correctValueHash = Block.computeHash(current.data.getNum(), current.data.getTransaction(),
          current.data.getPrevHash(), current.data.getNonce());
      if (!(this.validator.isValid(previous.data.getHash()))
          || (!previous.data.getHash().equals(current.data.getPrevHash()))
          || !(correctValueHash.equals(current.data.getHash()))) {
        return false;
      } // if
    } // while
    return true;
  } // legalHash()

  /**
   * Determine if the blockchain is correct in that (a) the balances are legal/correct at every
   * step, (b) that every block has a correct previous hash field, (c) that every block has a hash
   * that is correct for its contents, and (d) that every block has a valid hash.
   *
   * @throws Exception If things are wrong at any block.
   */
  public void check() throws Exception {
    if (!isCorrect()) {
      throw new Exception();
    } // if
    return;
  } // check()

  /**
   * Return an iterator of all the people who participated in the system.
   *
   * @return An iterator of all the people in the system.
   */
  public Iterator<String> users() {
    return new Iterator<String>() {
      private AssociativeArray<String, Integer> users = BlockChain.this.users;
      private final int MAX_SIZE = users.size();
      private int count = 0;

      /* Check for next user */
      @Override
      public boolean hasNext() {
        return count < MAX_SIZE;
      } // hasNext()

      /* Get the next user */
      @Override
      public String next() {
        if (!this.hasNext()) {
          throw new NoSuchElementException();
        } // if (!hasNext())
        String user = users.getKey(count++);
        return user;
      } // next()
    };
  } // users()

  /**
   * Find one user's balance.
   *
   * @param user The user whose balance we want to find.
   * @return That user's balance (or 0, if the user is not in the system).
   */
  public int balance(String user) {
    // updateBalance(user, null);
    try{
    isCorrect();
    return users.get(user);
    }catch(Exception e){
      return 0;
    }
  } // balance()

  /**
   * Get an iterator for all the blocks in the chain.
   *
   * @return An iterator for all the blocks in the chain.
   */
  public Iterator<Block> blocks() {
    return new Iterator<Block>() {
      private Node<Block> block = BlockChain.this.start;

      /* Check for next node */
      @Override
      public boolean hasNext() {
        return !(this.block == null);
      } // hasNext()

      /* Get the next block */
      @Override
      public Block next() {
        if (block == null) {
          throw new NoSuchElementException();
        } // if (!hasNext())
        Block b = block.data;
        block = this.block.next;
        return b;
      } // next()
    };
  } // blocks()

  /**
   * Get an iterator for all the transactions in the chain.
   *
   * @return An iterator for all the transactions in the chain.
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

// package edu.grinnell.csc207.util;

// import edu.grinnell.csc207.blockchains.*;
// import java.util.Iterator;
// import static org.junit.jupiter.api.Assertions.assertArrayEquals;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.junit.jupiter.api.Assertions.assertNotEquals;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Test;

// /**
//  * Personal tests to track implementation.
//  *
//  * @author Sara Jaljaa
//  */
// public class TestStudent {

//   // +-----------+---------------------------------------------------
//   // | Constants |
//   // +-----------+

//   /**
//    * A simple hash validator for early-stage tests.
//    */
//   // @BeforeAll
//   // public void setup() {
//   HashValidator simple =
//       (hash) -> (hash.length() >= 1) && (hash.get(0) == 0);

//   /**
//    * A standard hash validator for later tests.
//    */
//   HashValidator standard =
//       (hash) -> (hash.length() >= 3) && (hash.get(0) == 0)
//       && (hash.get(1) == 0) && (hash.get(2) == 0);
//   // } // setup()

//   // +-------+-------------------------------------------------------
//   // | Tests |
//   // +-------+

//   /**
//    * Checking print/string formatting for Hash and Block.
//    */
//   @Test
//   public void stringTest() {
//     // Transaction t = new Transaction("Jerry", "Roman", 500);
//     // Transaction t1 = new Transaction("Roman", "Alek", 416);
//     // Transaction t2 = new Transaction("Alek", "Jerry", 90);

//     // StringBuilder str = new StringBuilder();
//     // BlockChain bc = new BlockChain(simple);
//     // Block blockA = bc.mine(t);
//     // bc.append(blockA);
//     // // Block blockB = bc.mine(t1);
//     // // Block blockC = bc.mine(t2);
//     // Iterator<Block> bl = bc.blocks();
    
//     // // bc.append(blockB);
//     // // bc.append(blockC);

//     // // str.append(bc.start.next.data.getTransaction().getSource());

//     // while (bl.hasNext()) {
//     //   str.append(bl.next());
//     //   str.append(" ");
//     // } // while

//     // assertEquals("J", str.toString(), "user iterator works");
//   } // stringTest()

//   /**
//    * Are all the iterators implemented correctly?
//    */
//   @Test
//   public void iterableTest() {
//     // STUB
//   } // iterableTest()

//   /**
//    * Check for nonce mining in Block and BlockChain.
//    */
//   @Test
//   public void mineTest() {
//     // STUB
//   } // mineTest()

//   /**
//    * Check for invalid nonce mining in Block and BlockChain.
//    */
//   @Test
//   public void invalidMineTest() {
//     // STUB
//   } // invalidMineTest()

//   /**
//    * Check if a Block in a BlockChain is valid.
//    * > isCorrect() & check()
//    */
//   @Test
//   public void blockValidityTest() {
//     // STUB
//   } // blockValidityTest()

//   /**
//    * Can a block be successfully appended to a BlockChain?
//    */
//   @Test
//   public void appendTest() {
//     // STUB
//   } // appendTest()

//   /**
//    * Check edge-cases of appending to a BlockChain.
//    */
//   @Test
//   public void invalidAppendTest() {
//     // STUB
//   } // invalidAppendTest()

//   /**
//    * Can a block be successfully removed from the end of a BlockChain?
//    */
//   @Test
//   public void removeTest() {
//     // STUB
//   } // removeTest()

//   /**
//    * Check edge-cases of removing from a BlockChain.
//    */
//   @Test
//   public void invalidRemoveTest() {
//     // STUB
//   } // invalidRemoveTest()

//   /**
//    * Basic hashing of values.
//    */
//   @Test
//   public void basicHashTest() {
//     // STUB
//   } // basicHashTest()

//   /**
//    * Edge-case value hashing.
//    */
//   @Test
//   public void edgeHashTest() {
//     // STUB
//   } // edgeHashTest()

//   /*
//    * Checks basic block functionality across normal blocks.
//    */
//   @Test
//   public void basicBlocksTest() {
//     // STUB
//   } // basicBlocksTest()

//   /*
//    * Checks basic block functionality on edge case blocks.
//    */
//   @Test
//   public void edgeBlockTest() {
//     // STUB
//   } // edgeBlockTest()

//   /**
//    * Can we create a simple BlockChain?
//    */
//   @Test
//   public void basicBlockChainTest() {
//     // STUB
//   } // basicBlockChainTest()

//   /**
//    * Edge-cases for BlockChains.
//    */
//   @Test
//   public void edgeBlockChainTest() {
//     // STUB
//   } // edgeBlockChainTest()
// } // class TestStudent

// public static void main(String[] args) {
//   PrintWriter pen = new PrintWriter(System.out, true);
//   HashValidator v =
//       (hash) ->
//           (hash.length() >= 2) && (hash.get(0) == 64) && (hash.get(1) == 64);
//   BlockChain chain = new BlockChain(v);

//   pen.println(users(chain).length);
//   chain.append(chain.mine(new Transaction("", "B", 100)));
//   pen.println(iterate(users(chain)));
//   chain.append(chain.mine(new Transaction("B", "D", 10)));
//   pen.println(iterate(users(chain)));
//   pen.println(users(chain).length);
//   chain.append(chain.mine(new Transaction("B", "C", 10)));
//   pen.println(iterate(users(chain)));
//   pen.println(users(chain).length);
//   chain.append(chain.mine(new Transaction("B", "C", 10)));
//   pen.println(iterate(users(chain)));
//   pen.println(users(chain).length);
//   chain.append(chain.mine(new Transaction("C", "D", 10)));
//   pen.println(iterate(users(chain)));
//   pen.println(users(chain).length);
//   chain.append(chain.mine(new Transaction("D", "E", 10)));
//   pen.println(iterate(users(chain)));
//   pen.println(users(chain).length);
//   chain.append(chain.mine(new Transaction("B", "A", 10)));
//   pen.println(iterate(users(chain)));
//   pen.println(users(chain).length);
// }

// public static String iterate(String[] stri) {
//   StringBuilder str = new StringBuilder();
//   for (int i = 0; i < stri.length - 1; i++) {
//     str.append(stri[i]);
//     str.append(", ");
//   } // for
//   str.append(stri[stri.length -1]);
//   return str.toString();
// } // iterate(String[])

package edu.grinnell.csc207.blockchains;

import java.util.Arrays;

/**
 * Encapsulated hashes.
 *
 * @author Sara Jaljaa
 * @author Jenifer Silva
 * @author Samuel A. Rebelsky
 */
public class Hash {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  byte[] arrayBytes;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new encapsulated hash.
   *
   * @param data The data to copy into the hash.
   */
  public Hash(byte[] data) {
    this.arrayBytes = data;

  } // Hash(byte[])

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Determine how many bytes are in the hash.
   *
   * @return the number of bytes in the hash.
   */
  public int length() {
    return this.arrayBytes.length; // STUB
  } // length()

  /**
   * Get the ith byte.
   *
   * @param i The index of the byte to get, between 0 (inclusive) and length() (exclusive).
   *
   * @return the ith byte
   */
  public byte get(int i) {
    return this.arrayBytes[i];
  } // get()

  /**
   * Get a copy of the bytes in the hash. We make a copy so that the client cannot change them.
   *
   * @return a copy of the bytes in the hash.
   */
  public byte[] getBytes() {
    byte[] copy = new byte[this.arrayBytes.length];
    for (int i = 0; i < this.arrayBytes.length; i++) {
      copy[i] = this.arrayBytes[i];
    }
    return copy;
  } // getBytes()

  /**
   * Convert to a hex string.
   *
   * @return the hash as a hex string.
   */
  public String toString() {
    StringBuilder hexString = new StringBuilder();
    for(int i = 0; i < this.arrayBytes.length; i++){
      Integer conversion = Byte.toUnsignedInt(this.arrayBytes[i]);
      hexString.append(Integer.toHexString(conversion.hashCode()));
    }
    return new String(hexString);
  } // toString()

  /**
   * Determine if this is equal to another object.
   *
   * @param other The object to compare to.
   *
   * @return true if the two objects are conceptually equal and false otherwise.
   */
  public boolean equals(Object other) {
    Hash o = null;
    if (!(other instanceof Hash)) {
      o = (Hash) other;
    }
    return Arrays.equals(o.arrayBytes, this.arrayBytes);
  } // equals(Object)

  /**
   * Get the hash code of this object.
   *
   * @return the hash code.
   */
  public int hashCode() {
    return this.toString().hashCode();
  } // hashCode()
} // class Hash

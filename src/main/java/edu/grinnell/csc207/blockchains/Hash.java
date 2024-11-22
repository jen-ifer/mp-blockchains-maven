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

  /**
   * The data to be hashed.
   */
  private byte[] data;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new encapsulated hash.
   *
   * @param data The data to copy into the hash.
   */
  public Hash(byte[] data) {
    this.data = Arrays.copyOf(data, data.length);
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
    return this.data.length;
  } // length()

  /**
   * Get the ith byte.
   *
   * @param i The index of the byte to get, between 0 (inclusive) and length() (exclusive).
   *
   * @return the ith byte
   */
  public byte get(int i) {
    return this.data[i];
  } // get()

  /**
   * Get a copy of the bytes in the hash. We make a copy so that the
   * client cannot change them.
   *
   * @return a copy of the bytes in the hash.
   */
  public byte[] getBytes() {
    if (this.data == null) {
      return new byte[0];
    } // if
    return Arrays.copyOf(this.data, this.data.length);
  } // getBytes()

  /**
   * Convert to a hex string.
   *
   * @return the hash as a hex string.
   */
  public String toString() {
    StringBuilder hexString = new StringBuilder();
    if (this.data.equals(null)) {
      return null;
    } // if
    for (int i = 0; i < this.data.length; i++) {
      hexString.append(String.format("%02X", this.data[i]));
    } // for
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
    return (other instanceof Hash)
        && Arrays.equals(((Hash) other).data, this.getBytes());
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

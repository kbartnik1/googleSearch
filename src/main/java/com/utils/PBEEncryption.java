package com.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class PBEEncryption {

    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    // The following constants may be changed without breaking existing hashes.
    private static final int HASH_BYTES = 64;
    private final int PBKDF2_ITERATIONS;
    private byte[] SALT;

    /**
     * PBKDF2 password creation and verification object
     *
     * @param iterations number of iterations used to create password hash
     */
    public PBEEncryption(int iterations) {
        // Generate a random salt
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[HASH_BYTES];
        sr.nextBytes(salt);
        SALT=salt;
        PBKDF2_ITERATIONS = iterations;
    }

    /**
     *
     * @param salt byte[] representation of salt that will be used during hash creation
     */

    public void setSalt(byte[] salt) {
        if (salt.length < 8 || salt.length % 8 != 0)
            System.out.println("Incorrect salt (must be multiplicative by 8). Not changing anything.");
        else
            this.SALT = salt;
    }

    /**
     * Sets salt that is used to generate password hash
     *
     * @param hexSalt String representing hex value of salt that will be used during hash creation
     */
    public void setSalt(String hexSalt){
        if (fromHex(hexSalt).length < 8 || fromHex(hexSalt).length % 8 !=0)
            System.out.println("Incorrect salt, not changing anything.");
        else
            this.SALT = fromHex(hexSalt);
    }

    /**
     * Returns hex string of salt used durinh hashing
     *
     * @return salt that is used during hashing pwds
     */

    public String getHexSalt() {
        return toHex(SALT);
    }

    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param password the password to hash
     * @return a salted PBKDF2 hash of the password
     */
    public String createHash(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return createHash(password.toCharArray());
    }

    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param password the password to hash
     * @return a salted PBKDF2 hash of the password
     */
    public String createHash(char[] password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Hash the password
        byte[] hash = pbkdf2(password, SALT, PBKDF2_ITERATIONS, HASH_BYTES);
        // format iterations:salt:hash
        return toHex(hash);
    }

    /**
     * Validates a password using a hash.
     *
     * @param password the password to check
     * @param goodHash the hash of the valid password
     * @return true if the password is correct, false if not
     */
    public boolean validatePassword(String password, String goodHash)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return validatePassword(password.toCharArray(), goodHash);
    }

    /**
     * Validates a password using a hash.
     *
     * @param password         the password to check
     * @param goodPasswordHash the hash of the valid password
     * @return true if the password is correct, false if not
     */
    public boolean validatePassword(char[] password, String goodPasswordHash)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Decode the hash into its parameters
        // Compute the hash of the provided password, using the same salt,
        // iteration count, and hash length
        byte[] testHash = pbkdf2(password, SALT, PBKDF2_ITERATIONS, HASH_BYTES);
        // Compare the hashes in constant time. The password is correct if
        // both hashes match.
        return slowEquals(fromHex(goodPasswordHash), testHash);
    }

    /**
     * Compares two byte arrays in length-constant time. This comparison method
     * is used so that password hashes cannot be extracted from an on-line
     * system using a timing attack and then attacked off-line.
     *
     * @param a the first byte array
     * @param b the second byte array
     * @return true if both byte arrays are the same, false if not
     */
    private boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }

    /**
     * Computes the PBKDF2 hash of a password.
     *
     * @param password   the password to hash.
     * @param salt       the salt
     * @param iterations the iteration count (slowness factor)
     * @param bytes      the length of the hash to compute in bytes
     * @return the PBKDF2 hash of the password
     */
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    /**
     * Converts a string of hexadecimal characters into a byte array.
     *
     * @param hex the hex string
     * @return the hex string decoded into a byte array
     */
    private byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }

    /**
     * Converts a byte array into a hexadecimal string.
     *
     * @param array the byte array to convert
     * @return a length*2 character string encoding the byte array
     */
    private String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }
}

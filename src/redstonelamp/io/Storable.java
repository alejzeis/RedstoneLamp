package redstonelamp.io;

/**
 * Represents a class that can be stored and loaded into a ByteArray.
 *
 * @author jython234
 */
public interface Storable {
    /**
     * Stores this class into a byte array, ready for saving or sending.
     * @return The serialized class as a byte array.
     */
    byte[] store();

    /**
     * Loads this class froma  byte array.
     * @param source The raw byte array that was stored before.
     */
    void load(byte[] source);
}

package fr.utarwyn.superjukebox.nbs;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Give access to some utility methods
 * to manage input streams when decoding NBS files.
 *
 * @author Utarwyn <maxime.malgorn@laposte.net>
 * @since 0.3.0
 */
public class InputStreamUtil {

    private InputStreamUtil() {

    }

    /**
     * Read a short value in little-endian (32bit)
     *
     * @param dis The data input stream
     * @return Read value in the stream
     * @throws IOException Throwed if the method cannot read a short at the stream cursor
     */
    public static short readShort(DataInputStream dis) throws IOException {
        int b1 = dis.readUnsignedByte();
        int b2 = dis.readUnsignedByte();

        return (short) (b1 + (b2 << 8));
    }

    /**
     * Read an integer value in little-endian (32bit)
     *
     * @param dis The data input stream
     * @return Read value in the stream
     * @throws IOException Throwed if the method cannot read an integer at the stream cursor
     */
    public static int readInt(DataInputStream dis) throws IOException {
        int b1 = dis.readUnsignedByte();
        int b2 = dis.readUnsignedByte();
        int b3 = dis.readUnsignedByte();
        int b4 = dis.readUnsignedByte();

        return b1 + (b2 << 8) + (b3 << 16) + (b4 << 24);
    }

    /**
     * Read a string, byte by byte.
     *
     * @param dis The data input stream
     * @return Read value in the stream
     * @throws IOException Throwed if the method cannot read a string at the stream cursor
     */
    public static String readString(DataInputStream dis) throws IOException {
        int length = readInt(dis);

        StringBuilder sb = new StringBuilder();

        while (length > 0) {
            char c = (char) dis.readByte();
            sb.append(c);
            length--;
        }

        return sb.toString();
    }

}

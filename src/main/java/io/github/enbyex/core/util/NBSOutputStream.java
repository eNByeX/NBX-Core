package io.github.enbyex.core.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author soniex2
 */
public class NBSOutputStream extends FilterOutputStream {
    /**
     * Creates a NBSOutputStream which uses the specified underlying OutputStream.
     */
    public NBSOutputStream(OutputStream out) {
        super(out);
    }

    /**
     * Writes a byte in an unspecified manner.
     */
    public final void writeByte(byte b) throws IOException {
        write(b);
    }

    /**
     * Writes a short in an unspecified manner.
     */
    public final void writeShort(short s) throws IOException {
        // AA BB -> BB AA
        write(s);
        write(s >> 8);
    }

    /**
     * Writes an int in an unspecified manner.
     */
    public final void writeInt(int i) throws IOException {
        // AA BB CC DD -> DD CC BB AA
        write(i);
        write(i >> 8);
        write(i >> 16);
        write(i >> 24);
    }

    /**
     * Writes a boolean in an unspecified manner.
     */
    public final void writeBoolean(boolean b) throws IOException {
        write(b ? 1 : 0);
    }

    /**
     * Writes a String in an unspecified manner.
     */
    public final void writeString(String s) throws IOException {
        // TODO
    }
}

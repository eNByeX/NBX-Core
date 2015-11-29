package io.github.enbyex.core.util;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author soniex2
 */
public class NBSInputStream extends FilterInputStream {
    /**
     * Creates a NBSInputStream which uses the specified underlying InputStream.
     */
    public NBSInputStream(InputStream in) {
        super(in);
    }

    /**
     * Reads a byte in an unspecified manner.
     */
    public final byte readByte() throws IOException {
        int b = read();
        if (b < 0) throw new EOFException();
        return (byte) b;
    }

    /**
     * Reads a short in an unspecified manner.
     */
    public final short readShort() throws IOException {
        // AA BB -> BB AA
        int low = read(); // AA
        int hi = read(); // BB
        if ((low | hi) < 0) throw new EOFException();
        return (short) (low | (hi << 8));
    }

    /**
     * Reads an int in an unspecified manner.
     */
    public final int readInt() throws IOException {
        // AA BB CC DD -> DD CC BB AA
        int lowest = read();
        int low = read();
        int hi = read();
        int highest = read();
        if ((lowest | low | hi | highest) < 0) throw new EOFException();
        return lowest | (low << 8) | (hi << 16) | (highest << 24);
    }

    /**
     * Reads a boolean in an unspecified manner.
     */
    public final boolean readBoolean() throws IOException {
        int b = read();
        if (b < 0) throw new EOFException();
        return b != 0;
    }

    private char toWindows1252(char c) {
        switch (c) {
            case 0x80:
                c = 0x20AC;
                break;
            case 0x82:
                c = 0x201A;
                break;
            case 0x83:
                c = 0x0192;
                break;
            case 0x84:
                c = 0x201E;
                break;
            case 0x85:
                c = 0x2026;
                break;
            case 0x86:
                c = 0x2020;
                break;
            case 0x87:
                c = 0x2021;
                break;
            case 0x88:
                c = 0x02C6;
                break;
            case 0x89:
                c = 0x2030;
                break;
            case 0x8A:
                c = 0x0160;
                break;
            case 0x8B:
                c = 0x2039;
                break;
            case 0x8C:
                c = 0x0152;
                break;
            case 0x8E:
                c = 0x017D;
                break;
            case 0x91:
                c = 0x2018;
                break;
            case 0x92:
                c = 0x2019;
                break;
            case 0x93:
                c = 0x201C;
                break;
            case 0x94:
                c = 0x201D;
                break;
            case 0x95:
                c = 0x2022;
                break;
            case 0x96:
                c = 0x2013;
                break;
            case 0x97:
                c = 0x2014;
                break;
            case 0x98:
                c = 0x02DC;
                break;
            case 0x99:
                c = 0x2122;
                break;
            case 0x9A:
                c = 0x0161;
                break;
            case 0x9B:
                c = 0x203A;
                break;
            case 0x9C:
                c = 0x0153;
                break;
            case 0x9E:
                c = 0x017E;
                break;
            case 0x9F:
                c = 0x0178;
                break;
        }
        return c;
    }

    /**
     * Reads a String in an unspecified manner.
     */
    public final String readString() throws IOException {
        int byteLen = readInt();
        byte[] bytes = new byte[byteLen];
        if (read(bytes, 0, bytes.length) != bytes.length) throw new EOFException();
        // Note Block Studio uses Windows-1252
        char[] str = new char[byteLen];
        boolean useUTF8 = false;
        for (int i = 0; i < byteLen; i++) {
            char c = (char) (bytes[i] & 0xFF);
            if (c >= 0x80 && c <= 0x9F) { // Windows-1252 NOT ISO-8859-1!
                c = toWindows1252(c);
                if (c == 0x81) {
                    useUTF8 = true;
                    break;
                }
            }
            str[i] = c;
        }
        if (useUTF8) {
            int pos = 0;
            for (int i = 0; i < byteLen; i++) {
                char b = (char) (bytes[i] & 0xFF);
                if ((b & 0x80) == 0) {
                    str[pos++] = b;
                } else {
                    switch ((b >> 4) & 15) {
                        case 8:
                        case 9:
                        case 10:
                        case 11: // 10xx xxxx
                            // invalid, use Windows-1252 fallback
                            str[pos++] = toWindows1252(b);
                            break;
                        case 12:
                        case 13: {// 110x xxxx
                            if (b == 0xC0 || b == 0xC1) {
                                // invalid, use fallback
                                str[pos++] = toWindows1252(b);
                            } else {
                                char sec = (char) (bytes[++i] & 0xFF);
                                if ((sec & 0x40) != 0) {
                                    // invalid, use Windows-1252 fallback
                                    str[pos++] = toWindows1252(b);
                                    str[pos++] = toWindows1252(sec);
                                } else {
                                    str[pos++] = (char) ((b & 0x1F) << 6 | (sec & 0x3F));
                                }
                            }
                            break;
                        }
                        case 14: {// 1110 xxxx
                            char sec = (char) (bytes[++i] & 0xFF);
                            char third = (char) (bytes[++i] & 0xFF);
                            if ((sec & 0x40) != 0 || (third & 0x40) != 0) {
                                // invalid, use Windows-1252 fallback
                                str[pos++] = toWindows1252(b);
                                str[pos++] = toWindows1252(sec);
                                str[pos++] = toWindows1252(third);
                            } else {
                                str[pos++] = (char) (((b & 0xF) << 12) | ((sec & 0x3F) << 6) | (sec & 0x3F));
                            }
                            break;
                        }
                        case 15: // 1111 xxxx
                            // TODO: surrogates
                            switch (b & 15) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4: // 1111 0xxx (valid)
                                    break;
                                case 5:
                                case 6:
                                case 7: // 1111 0xxx (invalid)
                                case 8:
                                case 9:
                                case 10:
                                case 11: // 1111 10xx
                                case 12:
                                case 13: // 1111 110x
                                case 14: // 1111 1110
                                case 15: // 1111 1111
                                    str[pos++] = toWindows1252(b);
                                    break;
                            }
                    }
                }
            }
        }
        return new String(str);
    }
}

package io.github.enbyex.core.nbs;

import io.github.enbyex.core.util.NBSInputStream;
import io.github.enbyex.core.util.NBSOutputStream;
import io.github.enbyex.core.util.Readable;
import io.github.enbyex.core.util.Writable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author soniex2
 */
public class NBSInstruments implements Readable,
                                       Writable {
    private List<NBSInstrument> instruments = Arrays.asList(new NBSInstrument[9]);

    private int currentSize = -1;

    public int instruments() {
        if (currentSize >= 0) return currentSize;
        for (int i = 0; i < instruments.size(); i++) {
            if (instruments.get(i) == null) return currentSize = i;
        }
        return currentSize = instruments.size();
    }

    public NBSInstrument getInstrument(int i) {
        NBSInstrument inst = instruments.get(i);
        if (inst == null) throw new IndexOutOfBoundsException();
        return inst;
    }

    public void setInstrument(int i, @Nullable NBSInstrument inst) {
        if (i > instruments()) throw new IndexOutOfBoundsException();
        instruments.set(i, inst);
        if (i == instruments()) currentSize++;
        if (inst == null) {
            currentSize--;
            for (int j = i; j < instruments.size() - 1; j++) {
                instruments.set(j, instruments.get(j + 1));
            }
        }
    }

    @Override
    public void readFrom(@Nonnull NBSInputStream s) throws IOException {
        int count = s.readByte();
        try {
            for (int i = 0; i < count; i++) {
                NBSInstrument.Builder b = new NBSInstrument.Builder();
                b.readFrom(s);
                instruments.set(i, b.build());
            }
            currentSize = count;
        } catch (EOFException e) {
            throw new IOException("Not in NBS format", e);
        }
    }

    @Override
    public void writeTo(@Nonnull NBSOutputStream s) throws IOException {
        byte count = (byte) instruments();
        s.writeByte(count);
        for (int i = 0; i < count; i++) {
            instruments.get(i).writeTo(s);
        }
    }
}

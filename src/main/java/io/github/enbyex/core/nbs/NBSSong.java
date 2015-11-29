package io.github.enbyex.core.nbs;

import io.github.enbyex.core.util.NBSInputStream;
import io.github.enbyex.core.util.NBSOutputStream;
import io.github.enbyex.core.util.Readable;
import io.github.enbyex.core.util.Writable;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Optional;

/**
 * @author soniex2
 */
public class NBSSong implements Readable,
                                Writable {
    @Nonnull
    private NBSHeader header = new NBSHeader.Builder().build(); // dummy header
    @Nonnull
    private NBSNotes notes = new NBSNotes();
    // private NBSLayers layers;
    // private NBSInstruments instruments;

    @Override
    public void readFrom(@Nonnull NBSInputStream s) throws IOException {
        NBSHeader.Builder b = new NBSHeader.Builder();
        b.readFrom(s);
        header = b.build();
        notes.readFrom(s);
    }

    @Nonnull
    public NBSHeader getHeader() {
        return header;
    }

    public void setHeader(@Nonnull NBSHeader header) {
        this.header = header;
    }

    public int length() {
        return notes.length();
    }

    public void setNote(int tick, int layer, Optional<NBSNote> note) {
        notes.setNote(tick, layer, note);
    }

    public Optional<NBSNote> getNote(int tick, int layer) {
        return notes.getNote(tick, layer);
    }

    public int height() {
        return notes.height();
    }

    @Override
    public void writeTo(@Nonnull NBSOutputStream s) throws IOException {
        header.writeTo(s);
        notes.writeTo(s);
    }
}

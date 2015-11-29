package io.github.enbyex.core.nbs;

import io.github.enbyex.core.util.NBSInputStream;
import io.github.enbyex.core.util.NBSOutputStream;
import io.github.enbyex.core.util.Readable;
import io.github.enbyex.core.util.Writable;

import javax.annotation.Nonnull;
import java.io.IOException;

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
        // TODO sync notes and header
    }

    @Override
    public void writeTo(@Nonnull NBSOutputStream s) throws IOException {
        header.writeTo(s);
        notes.writeTo(s);
    }
}

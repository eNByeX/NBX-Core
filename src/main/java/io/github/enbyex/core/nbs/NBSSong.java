package io.github.enbyex.core.nbs;

import io.github.enbyex.core.util.NBSInputStream;
import io.github.enbyex.core.util.NBSOutputStream;
import io.github.enbyex.core.util.Readable;
import io.github.enbyex.core.util.Writable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.EOFException;
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
    private NBSLayers layers = new NBSLayers();
    private NBSInstruments instruments = new NBSInstruments();

    @Override
    public void readFrom(@Nonnull NBSInputStream s) throws IOException {
        NBSHeader.Builder b = new NBSHeader.Builder();
        b.readFrom(s);
        header = b.build();
        notes.readFrom(s);
        layers = new NBSLayers(header.getHeight());
        try {
            layers.readFrom(s);
            instruments.readFrom(s);
        } catch (EOFException ignored) {
            // allowed by NBS spec
        }
    }

    @Nonnull
    public NBSHeader getHeader() {
        return header;
    }

    public void setHeader(@Nonnull NBSHeader header) {
        if (header.getHeight() != layers.height()) {
            NBSHeader.Builder b = new NBSHeader.Builder(header);
            b.setHeight((short) layers.height());
            header = b.build();
        }
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

    public int songHeight() {
        return notes.height();
    }

    public void setLayer(int i, NBSLayer layer) {
        layers.setLayer(i, layer);
        if (header.getHeight() != layers.height()) {
            NBSHeader.Builder b = new NBSHeader.Builder(header);
            b.setHeight((short) layers.height());
            header = b.build();
        }
    }

    public NBSLayer getLayer(int i) {
        return layers.getLayer(i);
    }

    public int instruments() {
        return instruments.instruments();
    }

    public NBSInstrument getInstrument(int i) {
        return instruments.getInstrument(i);
    }

    public void setInstrument(int i, @Nullable NBSInstrument inst) {
        instruments.setInstrument(i, inst);
    }

    @Override
    public void writeTo(@Nonnull NBSOutputStream s) throws IOException {
        // TODO check
        header.writeTo(s);
        notes.writeTo(s);
        layers.writeTo(s);
        instruments.writeTo(s);
    }
}

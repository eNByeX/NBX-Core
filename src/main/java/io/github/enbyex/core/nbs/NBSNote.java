package io.github.enbyex.core.nbs;

import io.github.enbyex.core.util.NBSInputStream;
import io.github.enbyex.core.util.NBSOutputStream;
import io.github.enbyex.core.util.Readable;
import io.github.enbyex.core.util.Writable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

/**
 * @author soniex2
 */
public class NBSNote implements Writable {
    private final byte instrument;
    private final byte note;

    protected NBSNote(Builder b) {
        this.instrument = b.getInstrument();
        this.note = b.getNote();
    }

    public byte getInstrument() {
        return instrument;
    }

    public byte getNote() {
        return note;
    }

    @Override
    public void writeTo(@Nonnull NBSOutputStream s) throws IOException {
        s.writeByte(instrument);
        s.writeByte(note);
    }

    public static class Builder implements Readable,
                                           Writable {
        private byte instrument = 0;
        private byte note = 39; // C4

        public Builder() {

        }

        public Builder(NBSNote note) {
            this.instrument = note.getInstrument();
            this.note = note.getNote();
        }

        public byte getInstrument() {
            return instrument;
        }

        public Builder setInstrument(byte instrument) {
            this.instrument = instrument;
            return this;
        }

        public byte getNote() {
            return note;
        }

        public Builder setNote(byte note) {
            this.note = note;
            return this;
        }

        public NBSNote build() {
            return new NBSNote(this);
        }

        @Override
        public void readFrom(@Nonnull NBSInputStream s) throws IOException {
            instrument = s.readByte();
            note = s.readByte();
        }

        @Override
        public void writeTo(@Nonnull NBSOutputStream s) throws IOException {
            s.writeByte(instrument);
            s.writeByte(note);
        }
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NBSNote nbsNote = (NBSNote) o;
        return instrument == nbsNote.instrument && note == nbsNote.note;

    }

    @Override
    public int hashCode() {
        return instrument << 8 | note;
    }
}

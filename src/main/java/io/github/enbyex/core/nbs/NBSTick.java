package io.github.enbyex.core.nbs;

import io.github.enbyex.core.util.NBSInputStream;
import io.github.enbyex.core.util.NBSOutputStream;
import io.github.enbyex.core.util.Readable;
import io.github.enbyex.core.util.Writable;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author soniex2
 */
public class NBSTick implements Readable,
                                Writable {
    private List<Optional<NBSNote>> notes = new ArrayList<>();

    private int noteCount = 0;

    public Optional<NBSNote> getNote(int layer) {
        if (layer < 0 || layer >= notes.size()) return Optional.empty();
        return notes.get(layer);
    }

    public int height() {
        return notes.size();
    }

    public int notes() {
        return noteCount;
    }

    public boolean isEmpty() {
        return noteCount == 0;
    }

    @Override
    public void readFrom(@Nonnull NBSInputStream s) throws IOException {
        short jumps;
        while ((jumps = s.readShort()) != 0) {
            for (int i = 1; i < jumps; i++) {
                notes.add(Optional.empty());
            }
            NBSNote.Builder note = new NBSNote.Builder();
            note.readFrom(s);
            notes.add(Optional.of(note.build()));
            noteCount++;
        }
    }
    
    public void setNote(int layer, Optional<NBSNote> note) {
        while (notes.size() <= layer) {
            notes.add(Optional.empty());
        }
        if (!notes.get(layer).isPresent() && note.isPresent()) {
            noteCount++;
        }
        if (notes.get(layer).isPresent() && !note.isPresent()) {
            noteCount--;
        }
        notes.set(layer, note);
    }
    
    @Override
    public void writeTo(@Nonnull NBSOutputStream s) throws IOException {
        short jumps = 0;
        for (Optional<NBSNote> n : notes) {
            jumps++;
            if (n.isPresent()) {
                s.writeShort(jumps);
                n.get().writeTo(s);
                jumps = 0;
            }
        }
        s.writeShort((short) 0);
    }
}

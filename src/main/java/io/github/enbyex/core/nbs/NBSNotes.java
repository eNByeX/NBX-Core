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
public class NBSNotes implements Readable,
                                 Writable {
    @Override
    public void readFrom(@Nonnull NBSInputStream s) throws IOException {
        short tick = -1;
        short jumps;
        while ((jumps = s.readShort()) != 0) {
            tick += jumps;
            short layer = -1;
            while ((jumps = s.readShort()) != 0) {
                layer += jumps;
                byte inst = s.readByte();
                byte key = s.readByte();
                addNoteBlock(tick, layer, inst, key);
            }
        }
    }

    private void addNoteBlock(short tick, short layer, byte inst, byte key) {
        // TODO
    }

    @Override
    public void writeTo(@Nonnull NBSOutputStream s) throws IOException {

    }
}

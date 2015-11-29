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
public class NBSNotes implements Readable,
                                 Writable {
    private List<NBSTick> ticks = new ArrayList<>();
    private int height = 0;

    public int length() {
        return ticks.size();
    }

    public int height() {
        return height;
    }

    public int notes() {
        int ac = 0;
        for (NBSTick t : ticks) {
            ac += t.notes();
        }
        return ac;
    }

    public void setNote(int tick, int layer, Optional<NBSNote> note) {
        while (ticks.size() <= tick) {
            ticks.add(new NBSTick());
        }
        ticks.get(tick).setNote(layer, note);
        height = Math.max(height, ticks.get(tick).height());
    }

    public Optional<NBSNote> getNote(int tick, int layer) {
        if (tick < 0 || tick >= ticks.size()) return Optional.empty();
        return ticks.get(tick).getNote(layer);
    }

    @Override
    public void readFrom(@Nonnull NBSInputStream s) throws IOException {
        short jumps;
        int height = 0;
        while ((jumps = s.readShort()) != 0) {
            for (int i = 1; i < jumps; i++) {
                ticks.add(new NBSTick());
            }
            NBSTick tick = new NBSTick();
            tick.readFrom(s);
            ticks.add(tick);
            height = Math.max(height, tick.height());
        }
        this.height = Math.max(this.height, height);
    }

    @Override
    public void writeTo(@Nonnull NBSOutputStream s) throws IOException {
        short jumps = 0;
        for (NBSTick t : ticks) {
            jumps++;
            if (!t.isEmpty()) {
                s.writeShort(jumps);
                t.writeTo(s);
                jumps = 0;
            }
        }
        s.writeShort((short) 0);
    }
}

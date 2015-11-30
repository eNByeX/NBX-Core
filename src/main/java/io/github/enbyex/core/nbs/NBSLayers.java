package io.github.enbyex.core.nbs;

import io.github.enbyex.core.util.NBSInputStream;
import io.github.enbyex.core.util.NBSOutputStream;
import io.github.enbyex.core.util.Readable;
import io.github.enbyex.core.util.Writable;

import javax.annotation.Nonnull;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author soniex2
 */
public class NBSLayers implements Readable,
                                  Writable {
    private ArrayList<NBSLayer> layers = new ArrayList<>();

    private int readingCount = 0;

    public NBSLayers() {

    }

    public NBSLayers(int count) {
        this.readingCount = count;
    }

    public int height() {
        return layers.size();
    }

    /**
     * A dummy/filler "empty" layer. Layers are immutable anyway.
     */
    private static final NBSLayer DUMMY_LAYER = new NBSLayer.Builder().build();

    public void setLayer(int i, NBSLayer layer) {
        while (layers.size() <= i) {
            layers.add(DUMMY_LAYER);
        }
        layers.set(i, layer);
    }

    public NBSLayer getLayer(int i) {
        if (i < 0 || i >= layers.size()) return DUMMY_LAYER;
        return layers.get(i);
    }

    @Override
    public void readFrom(@Nonnull NBSInputStream s) throws IOException {
        int i = 0;
        try {
            for (i = 0; i < readingCount; i++) {
                NBSLayer.Builder b = new NBSLayer.Builder();
                b.readFrom(s);
                layers.add(b.build());
            }
        } catch (EOFException e) {
            if (i != 0) throw new IOException("Not in NBS format", e);
            throw e;
        }
    }

    @Override
    public void writeTo(@Nonnull NBSOutputStream s) throws IOException {
        for (NBSLayer l : layers) {
            l.writeTo(s);
        }
    }
}

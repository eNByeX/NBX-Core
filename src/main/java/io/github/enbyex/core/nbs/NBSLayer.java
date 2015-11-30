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
public class NBSLayer implements Writable {
    private final String name;
    private final byte volume;

    protected NBSLayer(Builder b) {
        name = b.getName();
        volume = b.getVolume();
    }

    public String getName() {
        return name;
    }

    public byte getVolume() {
        return volume;
    }

    public static class Builder implements Readable,
                                           Writable {
        private String name = "";
        private byte volume = 100;

        public NBSLayer build() {
            return new NBSLayer(this);
        }

        public String getName() {
            return name;
        }

        @Override
        public void readFrom(@Nonnull NBSInputStream s) throws IOException {
            name = s.readString();
            volume = s.readByte();
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public byte getVolume() {
            return volume;
        }

        public Builder setVolume(byte volume) {
            this.volume = volume;
            return this;
        }

        public Builder() {

        }

        public Builder(NBSLayer l) {
            this.name = l.getName();
            this.volume = l.getVolume();
        }

        @Override
        public void writeTo(@Nonnull NBSOutputStream s) throws IOException {
            s.writeString(name);
            s.writeByte(volume);
        }
    }

    @Override
    public void writeTo(@Nonnull NBSOutputStream s) throws IOException {
        s.writeString(name);
        s.writeByte(volume);
    }
}

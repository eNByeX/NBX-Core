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
public class NBSInstrument implements Writable {
    private final String name;
    private final String sound;
    private final byte pitch;
    private final boolean press;

    protected NBSInstrument(Builder builder) {
        name = builder.getName();
        sound = builder.getSound();
        pitch = builder.getPitch();
        press = builder.doPress();
    }

    public String getName() {
        return name;
    }

    public String getSound() {
        return sound;
    }

    public byte getPitch() {
        return pitch;
    }

    public boolean doPress() {
        return press;
    }

    @Override
    public void writeTo(@Nonnull NBSOutputStream s) throws IOException {
        s.writeString(name);
        s.writeString(sound);
        s.writeByte(pitch);
        s.writeBoolean(press);
    }

    public static class Builder implements Readable,
                                           Writable {
        private String name = "";
        private String sound = "";
        private byte pitch = 45;
        private boolean press = false;

        public Builder() {

        }

        public  Builder(NBSInstrument i) {
            name = i.getName();
            sound = i.getSound();
            pitch = i.getPitch();
            press = i.doPress();
        }

        public String getName() {
            return name;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public String getSound() {
            return sound;
        }

        public Builder setSound(String sound) {
            this.sound = sound;
            return this;
        }

        public byte getPitch() {
            return pitch;
        }

        public Builder setPitch(byte pitch) {
            this.pitch = pitch;
            return this;
        }

        public boolean doPress() {
            return press;
        }

        public Builder setPress(boolean press) {
            this.press = press;
            return this;
        }

        public NBSInstrument build() {
            return new NBSInstrument(this);
        }

        @Override
        public void readFrom(@Nonnull NBSInputStream s) throws IOException {
            name = s.readString();
            sound = s.readString();
            pitch = s.readByte();
            press = s.readBoolean();
        }

        @Override
        public void writeTo(@Nonnull NBSOutputStream s) throws IOException {
            s.writeString(name);
            s.writeString(sound);
            s.writeByte(pitch);
            s.writeBoolean(press);
        }
    }
}

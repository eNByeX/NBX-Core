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
public class NBSHeader implements Writable {
    private final short length;
    private final short height;
    @Nonnull
    private final String name;
    @Nonnull
    private final String author;
    @Nonnull
    private final String originalAuthor; // TODO find a better variable name
    @Nonnull
    private final String description;
    private final short tempo;
    private final boolean autosaveEnabled;
    private final byte autosaveMinutes;
    private final byte timeSignature;
    private final int minutes;
    private final int leftClicks;
    private final int rightClicks;
    private final int blocksAdded;
    private final int blocksRemoved;
    @Nonnull
    private final String importName;

    protected NBSHeader(Builder builder) {
        length = builder.getLength();
        height = builder.getHeight();
        name = builder.getName();
        author = builder.getAuthor();
        originalAuthor = builder.getOriginalAuthor();
        description = builder.getDescription();
        tempo = builder.getTempo();
        autosaveEnabled = builder.isAutosaveEnabled();
        autosaveMinutes = builder.getAutosaveMinutes();
        timeSignature = builder.getTimeSignature();
        minutes = builder.getMinutes();
        leftClicks = builder.getLeftClicks();
        rightClicks = builder.getRightClicks();
        blocksAdded = builder.getBlocksAdded();
        blocksRemoved = builder.getBlocksRemoved();
        importName = builder.getImportName();
    }

    /**
     * Gets the author of the song.
     */
    @Nonnull
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the time, in minutes, between each autosave.
     */
    public byte getAutosaveMinutes() {
        return autosaveMinutes;
    }

    /**
     * Gets how many times a block has been added. May be negative, if overflow happened.
     */
    public int getBlocksAdded() {
        return blocksAdded;
    }

    /**
     * Gets how many times a block has been removed. May be negative, if overflow happened.
     */
    public int getBlocksRemoved() {
        return blocksRemoved;
    }

    /**
     * Gets the description of the song.
     */
    @Nonnull
    public String getDescription() {
        return description;
    }

    /**
     * Gets the number of layers in the song.
     */
    public short getHeight() {
        return height;
    }

    /**
     * Gets the name of the imported file.
     */
    @Nonnull
    public String getImportName() {
        return importName;
    }

    /**
     * Gets how many left clicks happened. May be negative, if overflow happened.
     */
    public int getLeftClicks() {
        return leftClicks;
    }

    /**
     * Gets the number of ticks in the song.
     */
    public short getLength() {
        return length;
    }

    /**
     * Gets the minutes spent on the project. May be negative, if overflow happened.
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Gets the name of the song.
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Gets the original author of the song.
     */
    @Nonnull
    public String getOriginalAuthor() {
        return originalAuthor;
    }

    /**
     * Gets how many right clicks happened. May be negative, if overflow happened.
     */
    public int getRightClicks() {
        return rightClicks;
    }

    /**
     * Gets the tempo of the song.
     */
    public short getTempo() {
        return tempo;
    }

    /**
     * Gets the time signature of the song.
     */
    public byte getTimeSignature() {
        return timeSignature;
    }

    /**
     * Returns whether autosaving is enabled.
     */
    public boolean isAutosaveEnabled() {
        return autosaveEnabled;
    }

    @Override
    public void writeTo(@Nonnull NBSOutputStream s) throws IOException {
        s.writeShort(getLength());
        s.writeShort(getHeight());
        s.writeString(getName());
        s.writeString(getAuthor());
        s.writeString(getOriginalAuthor());
        s.writeString(getDescription());
        s.writeShort(getTempo());
        s.writeBoolean(isAutosaveEnabled());
        s.writeByte(getAutosaveMinutes());
        s.writeByte(getTimeSignature());
        s.writeInt(getMinutes());
        s.writeInt(getLeftClicks());
        s.writeInt(getRightClicks());
        s.writeInt(getBlocksAdded());
        s.writeInt(getBlocksRemoved());
        s.writeString(getImportName());
    }

    public static class Builder implements Readable,
                                           Writable {
        private short length = 0;
        private short height = 0;
        @Nonnull
        private String name = "";
        @Nonnull
        private String author; // filled in by constructor
        @Nonnull
        private String originalAuthor = ""; // TODO find a better variable name
        @Nonnull
        private String description = "";
        private short tempo = 1000;
        private boolean autosaveEnabled = false;
        private byte autosaveMinutes = 10;
        private byte timeSignature = 4;
        private int minutes = 0;
        private int leftClicks = 0;
        private int rightClicks = 0;
        private int blocksAdded = 0;
        private int blocksRemoved = 0;
        @Nonnull
        private String importName = "";

        public Builder() {
            String author;
            try {
                author = System.getProperty("user.name");
                if (author == null) author = "";
            } catch (SecurityException e) {
                author = "";
            }
            this.author = author;
        }

        public Builder(NBSHeader header) {
            length = header.getLength();
            height = header.getHeight();
            name = header.getName();
            author = header.getAuthor();
            originalAuthor = header.getOriginalAuthor();
            description = header.getDescription();
            tempo = header.getTempo();
            autosaveEnabled = header.isAutosaveEnabled();
            autosaveMinutes = header.getAutosaveMinutes();
            timeSignature = header.getTimeSignature();
            minutes = header.getMinutes();
            leftClicks = header.getLeftClicks();
            rightClicks = header.getRightClicks();
            blocksAdded = header.getBlocksAdded();
            blocksRemoved = header.getBlocksRemoved();
            importName = header.getImportName();
        }

        /**
         * Gets the author of the song.
         */
        @Nonnull
        public String getAuthor() {
            return author;
        }

        /**
         * Sets the author of the song.
         */
        public Builder setAuthor(@Nonnull String author) {
            this.author = author;
            return this;
        }

        /**
         * Gets the time, in minutes, between each autosave.
         */
        public byte getAutosaveMinutes() {
            return autosaveMinutes;
        }

        /**
         * Sets the time, in minutes, between each autosave. Valid values: 1-60.
         */
        public Builder setAutosaveMinutes(byte autosaveMinutes) {
            if (autosaveMinutes < 1 || autosaveMinutes > 60) throw new IllegalArgumentException();
            this.autosaveMinutes = autosaveMinutes;
            return this;
        }

        /**
         * Gets how many times a block has been added. May be negative, if overflow happened.
         */
        public int getBlocksAdded() {
            return blocksAdded;
        }

        /**
         * Sets how many times a block has been added.
         */
        public Builder setBlocksAdded(int blocksAdded) {
            this.blocksAdded = blocksAdded;
            return this;
        }

        /**
         * Gets how many times a block has been removed. May be negative, if overflow happened.
         */
        public int getBlocksRemoved() {
            return blocksRemoved;
        }

        /**
         * Sets how many times a block has been removed.
         */
        public Builder setBlocksRemoved(int blocksRemoved) {
            this.blocksRemoved = blocksRemoved;
            return this;
        }

        /**
         * Gets the description of the song.
         */
        @Nonnull
        public String getDescription() {
            return description;
        }

        /**
         * Sets the description of the song.
         */
        public Builder setDescription(@Nonnull String description) {
            this.description = description;
            return this;
        }

        /**
         * Gets the number of layers in the song.
         */
        public short getHeight() {
            return height;
        }

        /**
         * Sets the number of layers in the song.
         */
        public Builder setHeight(short height) {
            this.height = height;
            return this;
        }

        /**
         * Gets the name of the imported file.
         */
        @Nonnull
        public String getImportName() {
            return importName;
        }

        /**
         * Sets the name of the imported file.
         */
        public Builder setImportName(@Nonnull String importName) {
            this.importName = importName;
            return this;
        }

        /**
         * Gets how many left clicks happened. May be negative, if overflow happened.
         */
        public int getLeftClicks() {
            return leftClicks;
        }

        /**
         * Sets how many left clicks happened.
         */
        public Builder setLeftClicks(int leftClicks) {
            this.leftClicks = leftClicks;
            return this;
        }

        /**
         * Gets the number of ticks in the song.
         */
        public short getLength() {
            return length;
        }

        /**
         * Sets the number of ticks in the song.
         */
        public Builder setLength(short length) {
            this.length = length;
            return this;
        }

        /**
         * Gets the minutes spent on the project. May be negative, if overflow happened.
         */
        public int getMinutes() {
            return minutes;
        }

        /**
         * Sets the minutes spent on the project.
         */
        public Builder setMinutes(int minutes) {
            this.minutes = minutes;
            return this;
        }

        /**
         * Gets the name of the song.
         */
        @Nonnull
        public String getName() {
            return name;
        }

        /**
         * Sets the name of the song.
         */
        public Builder setName(@Nonnull String name) {
            this.name = name;
            return this;
        }

        /**
         * Gets the original author of the song.
         */
        @Nonnull
        public String getOriginalAuthor() {
            return originalAuthor;
        }

        /**
         * Sets the original author of the song.
         */
        public Builder setOriginalAuthor(@Nonnull String originalAuthor) {
            this.originalAuthor = originalAuthor;
            return this;
        }

        /**
         * Gets how many right clicks happened. May be negative, if overflow happened.
         */
        public int getRightClicks() {
            return rightClicks;
        }

        /**
         * Sets how many right clicks happened.
         */
        public Builder setRightClicks(int rightClicks) {
            this.rightClicks = rightClicks;
            return this;
        }

        /**
         * Gets the tempo of the song.
         */
        public short getTempo() {
            return tempo;
        }

        /**
         * Sets the tempo of the song multiplied by 100. This is in ticks per second. Must be positive.
         * <p>
         * E.g. 1225 = 12.25 TPS
         */
        public Builder setTempo(short tempo) {
            if (tempo <= 0) throw new IllegalArgumentException();
            this.tempo = tempo;
            return this;
        }

        /**
         * Gets the time signature of the song.
         */
        public byte getTimeSignature() {
            return timeSignature;
        }

        /**
         * Sets the time signature of the song. Valid values: 2-8.
         */
        public Builder setTimeSignature(byte timeSignature) {
            if (timeSignature < 2 || timeSignature > 8) throw new IllegalArgumentException();
            this.timeSignature = timeSignature;
            return this;
        }

        /**
         * Returns whether autosaving is enabled.
         */
        public boolean isAutosaveEnabled() {
            return autosaveEnabled;
        }

        /**
         * Sets whether autosaving is enabled.
         */
        public Builder setAutosaveEnabled(boolean autosaveEnabled) {
            this.autosaveEnabled = autosaveEnabled;
            return this;
        }

        public NBSHeader build() {
            return new NBSHeader(this);
        }

        @Override
        public void readFrom(@Nonnull NBSInputStream s) throws IOException {
            setLength(s.readShort());
            setHeight(s.readShort());
            setName(s.readString());
            setAuthor(s.readString());
            setOriginalAuthor(s.readString());
            setDescription(s.readString());
            setTempo(s.readShort());
            setAutosaveEnabled(s.readBoolean());
            setAutosaveMinutes(s.readByte());
            setTimeSignature(s.readByte());
            setMinutes(s.readInt());
            setLeftClicks(s.readInt());
            setRightClicks(s.readInt());
            setBlocksAdded(s.readInt());
            setBlocksRemoved(s.readInt());
            setImportName(s.readString());
        }

        @Override
        public void writeTo(@Nonnull NBSOutputStream s) throws IOException {
            s.writeShort(getLength());
            s.writeShort(getHeight());
            s.writeString(getName());
            s.writeString(getAuthor());
            s.writeString(getOriginalAuthor());
            s.writeString(getDescription());
            s.writeShort(getTempo());
            s.writeBoolean(isAutosaveEnabled());
            s.writeByte(getAutosaveMinutes());
            s.writeByte(getTimeSignature());
            s.writeInt(getMinutes());
            s.writeInt(getLeftClicks());
            s.writeInt(getRightClicks());
            s.writeInt(getBlocksAdded());
            s.writeInt(getBlocksRemoved());
            s.writeString(getImportName());
        }
    }
}
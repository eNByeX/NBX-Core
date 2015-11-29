package io.github.enbyex.core.util;

/**
 * @author soniex2
 */
public class Note {

    private static final char[] notechar = new char[] {
            'A', 'A', 'B', 'C', 'C', 'D', 'D', 'E', 'F', 'F', 'G', 'G'
    };

    public static String noteName(int note) {
        // 0 1 2 3 4 5 6 7 8 9 A B
        // A A B C C D D E F F G G ...
        //   #     #   #     #   # ...
        // 0 0 0 1 1 1 1 1 1 1 1 1 ...
        int group = note % 12;
        char c = notechar[group];
        String sharp = "";
        switch (group) {
            case 1:
            case 4:
            case 6:
            case 9:
            case 11:
                sharp = "#";
        }
        int octave = (9 + note) / 12;
        return c + sharp + octave;
    }
}

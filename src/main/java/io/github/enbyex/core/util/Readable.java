package io.github.enbyex.core.util;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author soniex2
 */
public interface Readable {
    void readFrom(@Nonnull NBSInputStream s) throws IOException;
}

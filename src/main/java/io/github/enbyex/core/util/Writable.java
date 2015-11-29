package io.github.enbyex.core.util;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author soniex2
 */
public interface Writable {
    void writeTo(@Nonnull NBSOutputStream s) throws IOException;
}

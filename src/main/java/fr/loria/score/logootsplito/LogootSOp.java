package fr.loria.score.logootsplito;

import java.io.Serializable;
import java.util.List;

public interface LogootSOp<T> extends Serializable, Cloneable {

    public LogootSOp<T> clone() throws CloneNotSupportedException;

    public List<TextOperation> execute(LogootSDoc<T> doc);
}

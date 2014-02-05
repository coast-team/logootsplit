package crdt;

import java.io.Serializable;

/**
 * An update operation (remote or local)
 * @author urso
 */
public interface Operation<T> extends Cloneable, Serializable {
    public Operation<T> clone();
}

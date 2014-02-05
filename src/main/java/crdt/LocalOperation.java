package crdt;

import crdt.Operation;
import crdt.CRDT;

/**
 * Local operation.
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public interface LocalOperation extends Operation {
    public LocalOperation adaptTo(CRDT replica);
}

package crdt;

import jbenchmarker.core.LocalOperation;

/**
 * A replica.
 * @author urso
 */
public interface Replica<L> {

    CRDTMessage applyLocal(LocalOperation op) throws Exception;

    void applyRemote(CRDTMessage msg);

    int getReplicaNumber();

    L lookup();

    void setReplicaNumber(int replicaNumber);   
}

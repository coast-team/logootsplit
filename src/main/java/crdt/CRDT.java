package crdt;

import java.io.Serializable;

/**
 * A CRDT is a factory. create() returns a new CRDT with the same behavior.
 *
 * @author urso
 */
public abstract class CRDT<L> implements Serializable, Replica<L> {

    private int replicaNumber;

    public CRDT(int replicaNumber) {
        this.replicaNumber = replicaNumber;
    }

    @Override
    public void setReplicaNumber(int replicaNumber) {
        this.replicaNumber = replicaNumber;
    }

    @Override
    public int getReplicaNumber() {
        return replicaNumber;
    }

    @Override
    final public void applyRemote(CRDTMessage message) {
        message.execute(this);
    }

    public abstract void applyOneRemote(CRDTMessage op);

}

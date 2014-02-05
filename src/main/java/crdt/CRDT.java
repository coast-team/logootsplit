package crdt;

import java.io.Serializable;
import java.util.Observable;

/**
 * A CRDT is a factory. create() returns a new CRDT with the same behavior.
 *
 * @author urso
 */
public abstract class CRDT<L> extends Observable implements Factory<CRDT<L>>, Serializable, Replica<L> {

    private int replicaNumber;
    public int nbrCleanMerge, nbrRedo, nbrInsConcur, nbrInsDelConcur, nbrDelDelConcur;
    public CRDT(int replicaNumber) {
        nbrCleanMerge = 0;
        nbrRedo=0;
        nbrInsConcur=0;
        nbrInsDelConcur=0;
        nbrDelDelConcur=0;
        this.replicaNumber = replicaNumber;
    }

    public CRDT() {
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
    final public void applyRemote(CRDTMessage msg) {
        msg.execute(this);
    }

    abstract public void applyOneRemote(CRDTMessage op);

    @Deprecated
    public Long lastExecTime() {
        return 0L;
    }
}

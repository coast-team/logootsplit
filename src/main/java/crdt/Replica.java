package crdt;

/**
 * A replica.
 *
 * @author urso
 */
public interface Replica<L> {

    public CRDTMessage applyLocal(LocalOperation op) throws Exception;

    public void applyRemote(CRDTMessage msg);

    public L lookup();

    public int getReplicaNumber();

    public void setReplicaNumber(int replicaNumber);
}

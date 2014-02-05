package crdt;

import crdt.Factory;

/**
 * Abstract factory to create replicas.
 * DEPRECATED : A MergeAlgorithm is itself a factory.
 * @author urso
 */
public abstract class ReplicaFactory implements Factory<MergeAlgorithm>{
   abstract public MergeAlgorithm create(int r);

    @Override
    public MergeAlgorithm create() {
        return create(-1);
    }
}

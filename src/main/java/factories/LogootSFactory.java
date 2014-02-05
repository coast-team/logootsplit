package factories;

import crdt.CRDT;
import crdt.Factory;
import logootsplit.LogootSAlgorithm;
import logootsplit.LogootSDocument;

public class LogootSFactory implements Factory<CRDT>{

    @Override
    public CRDT create() {
        return new LogootSAlgorithm(new LogootSDocument(Integer.MAX_VALUE), 1);
    }
    
}
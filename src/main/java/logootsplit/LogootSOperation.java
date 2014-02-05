package logootsplit;

import crdt.Operation;

public interface LogootSOperation extends Operation {
    
    
    public abstract void apply(LogootSDocument doc);
    
    
    
}
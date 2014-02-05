package jbenchmarker.core;

import crdt.Operation;
import java.io.Serializable;

/**
 * Interface for a document. 
 * @author urso
 */
public interface Document extends Serializable {
    
    /* 
     * View of the document (without metadata)
     */
    public String view();
    
    /*
     * Length of the view (to generate random operations)
     */
    public int viewLength();
    
    /**
     * Applies a character operation
     */ 
    public void apply(Operation op);
}

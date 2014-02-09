package facade;

import crdt.CRDT;
import logootsplitO.LogootSAlgo;
import logootsplitO.LogootSDocumentD;

/**
 *
 * @author oster
 */
public class LogootSFactory {
    
    public CRDT<String> createSite(int replicaNumber) {
        return new LogootSAlgo<Character>(new LogootSDocumentD<Character>(), replicaNumber);
    }   
    
}

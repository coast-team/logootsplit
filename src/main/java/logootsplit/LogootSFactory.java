package logootsplit;

import crdt.MergeAlgorithm;

/**
 *
 * @author oster
 */
public class LogootSFactory {

    public static MergeAlgorithm create(int siteId) {
        return new LogootSAlgorithm(new LogootSDocument(Integer.MAX_VALUE), siteId);
    }
    
}

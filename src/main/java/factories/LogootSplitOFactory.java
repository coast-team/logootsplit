package factories;

import crdt.MergeAlgorithm;
import crdt.ReplicaFactory;
import logootsplitO.LogootSAlgo;
import logootsplitO.LogootSDocumentD;
import logootsplitO.LogootSRopes;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class LogootSplitOFactory extends ReplicaFactory {

    static public enum TypeDoc {

        Ropes, String
    };
    TypeDoc type;

    public LogootSplitOFactory(TypeDoc type) {
        this.type = type;
    }

    public LogootSplitOFactory() {
        this.type = TypeDoc.String;
    }

    @Override
    public MergeAlgorithm create(int r) {
        switch (type) {
            case Ropes:
                return new LogootSAlgo(new LogootSRopes(), r);
            default:
                return new LogootSAlgo(new LogootSDocumentD(), r);
        }
    }

    public static class Ropes extends LogootSplitOFactory {

        public Ropes() {
            this.type= TypeDoc.Ropes;
        }
        
    }
}

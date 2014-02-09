package logootsplitO;

import facade.TextOperation;
import crdt.Document;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public interface LogootSDoc<T> extends Document {

    public LogootSDoc create();

    public void setReplicaNumber(int i);

    public LogootSOp insertLocal(int pos, List<T> l);

    public LogootSOp delLocal(int begin, int end);

    public List<TextOperation> addBlock(Identifier id, List<T> l);
    //public void addBlock(LogootSBlock block);
    
    //void delBlock(LogootSBlock block, int begin, int fin);
    public List<TextOperation> delBlock(IdentifierInterval id);

    //public void setAlgo(LogootSAlgo algo);
}

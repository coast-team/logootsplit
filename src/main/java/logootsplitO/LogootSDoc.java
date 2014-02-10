package logootsplito;

import java.util.List;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public interface LogootSDoc<T> {

    public LogootSDoc<T> duplicate(int newReplicaNumber);

    public void setReplicaNumber(int i);
    
    public LogootSOp insertLocal(int pos, List<T> l);

    public LogootSOp delLocal(int begin, int end);

    public List<TextOperation> addBlock(Identifier id, List<T> l);

    public List<TextOperation> delBlock(IdentifierInterval id);

    /* 
     * View of the document (without metadata)
     */
    public String view();
    
    /*
     * Length of the view (to generate random operations)
     */
    public int viewLength();
}

package logootsplito;

import java.util.List;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class LogootSAlgo {

    private LogootSDoc<Character> doc;
    
    public LogootSAlgo(LogootSDoc<Character> doc, int siteId) {
        this.doc = doc;
        this.doc.setReplicaNumber(siteId);
    }

    public void setReplicaNumber(int replicaNumber) {
        this.doc.setReplicaNumber(replicaNumber);
    }

    public LogootSDoc<Character> getDoc() {
        return this.doc;
    }
    
    public String lookup() {
        return this.doc.view();
    }
    
    public List<TextOperation> integrateRemote(LogootSOp op) {
        return op.execute(this.doc);
    }
    
    public LogootSOp localInsert(TextOperation op) {
        return op.applyTo(doc);
    }

    public LogootSOp localDelete(TextOperation op) {
        return op.applyTo(doc);
    }

    public LogootSOp insert(int position, String content) {
        return (new TextInsert(position, content)).applyTo(this.doc);
    }
        
    public LogootSOp remove(int position, int length) {
        return (new TextDelete(position, length)).applyTo(this.doc);
    }
    
    public List<TextOperation> applyRemote(LogootSOp op) {
        return op.execute(this.doc);
    }
}

package fr.loria.score.logootsplito;

import java.util.List;

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

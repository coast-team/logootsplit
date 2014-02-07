package logootsplitO;

import crdt.CRDT;
import crdt.Operation;
import crdt.IncorrectTraceException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import crdt.MergeAlgorithm;
import crdt.SequenceOperation;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class LogootSAlgo extends MergeAlgorithm implements Serializable {

    public LogootSAlgo(LogootSDoc doc, int siteId) {
        super(doc, siteId);
    }

    @Override
    public void setReplicaNumber(int replicaNumber) {
        super.setReplicaNumber(replicaNumber);
        this.getLDoc().setReplicaNumber(replicaNumber);
    }

    LogootSDoc getLDoc() {
        return (LogootSDoc) this.getDoc();
    }

    @Override
    protected void integrateRemote(Operation message) throws IncorrectTraceException {
        ((LogootSOp) message).apply((LogootSDoc) this.getDoc());
    }

    @Override
    protected List<Operation> localInsert(SequenceOperation opt) throws IncorrectTraceException {
        List ret = new LinkedList();
        ret.add(getLDoc().insertLocal(opt.getPosition(), opt.getContent()));

        return ret;
    }

    @Override
    protected List<Operation> localDelete(SequenceOperation opt) throws IncorrectTraceException {
        List ret = new LinkedList();
        ret.add(getLDoc().delLocal(opt.getPosition(), opt.getLenghOfADel() + opt.getPosition() - 1));

        return ret;
    }
}

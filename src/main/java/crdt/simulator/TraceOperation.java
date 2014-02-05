package crdt.simulator;

import collect.VectorClock;
import crdt.CRDT;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import jbenchmarker.core.LocalOperation;
import crdt.Operation;

/**
 *
 * @author urso
 */
abstract public class TraceOperation implements Serializable{
    
     private int replica;
    private VectorClock vectorClock;

    public TraceOperation() {
    }
    
    public TraceOperation(int replica, VectorClock VC) {
        this.replica = replica;
        this.vectorClock = VC;
    }

   /* public VectorClock getVC() {
        return VC;
    }*/

    public void setVectorClock(VectorClock VC) {
        this.vectorClock = VC;
    }

    public VectorClock getVectorClock() {
        return vectorClock;
    }

    public void setReplica(int replica) {
        this.replica = replica;
    }

    public int getReplica() {
        return replica;
    }
    
    public abstract LocalOperation getOperation();
    //abstract public Operation getOperation(CRDT replica);/* Why an operation on a trace depend on replica ?*/

    @Override
    public String toString() {
        return "TO{N°Rep="+replica+", VC=" + vectorClock +'}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TraceOperation other = (TraceOperation) obj;
        if (this.replica != other.replica) {
            return false;
        }
        if (this.vectorClock != other.vectorClock && (this.vectorClock == null || !this.vectorClock.equals(other.vectorClock))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.replica;
        hash = 23 * hash + (this.vectorClock != null ? this.vectorClock.hashCode() : 0);
        return hash;
    }
      @Override
    public TraceOperation clone() {
        try {
            return (TraceOperation) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(TraceOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

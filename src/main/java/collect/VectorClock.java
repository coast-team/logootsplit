package collect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Implementation of a vector clock. 
 * A map replica identifier -> logical clock.
 * @author urso
 * @author oster
 */
public class VectorClock extends HashMap<Integer, Integer> {

	public enum Causality {HB, CO, HA};
	
    public VectorClock() {
        super();
    }

    // TODO: test me, plz
    public VectorClock(VectorClock siteVC) {
        super(siteVC);
    }

    /*
     * Is this VC is ready to integrate O ?
     * true iff VCr = Or - 1 && for all i!=r, VCi >= Oi
     */
    public boolean readyFor(int r, VectorClock O) {
        if (this.getSafe(r) != O.get(r) - 1) {
            return false;
        }
        Iterator<Map.Entry<Integer, Integer>> it = O.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> e = it.next();
            if ((e.getKey() != r) && (this.getSafe(e.getKey()) < e.getValue())) {
                return false;
            }
        }
        return true;
    }
    
    /** 
     * Get the sum of all entries
     * added by Roh. 
     */ 
    public int getSum(){
        int sum = 0;
        Iterator<Map.Entry<Integer, Integer>> it = this.entrySet().iterator();
        while (it.hasNext()) sum+= it.next().getValue();        
        return sum;
    }
    
    /*
     * Increment an entry.
     */
    public void inc(int r) {
        put(r, getSafe(r) + 1);
    }

    /*
     * Returns the entry for replica r. 0 if none.
     */
    public int getSafe(int r) {
        Integer v = get(r);
        return (v != null) ? v : 0;
    }

    /*
     * Is this VC > T ? 
     * true iff for all i, VCi >= Ti and exists j VCj > Tj
     */
    public boolean greaterThan(VectorClock T) {
        boolean gt = false;
        Iterator<Map.Entry<Integer, Integer>> it = T.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> i = it.next();
            if (this.getSafe(i.getKey()) < i.getValue()) {
                return false;
            } else if (this.getSafe(i.getKey()) > i.getValue()) {
                gt = true;
            }
        }
        if (gt) {
            return true;
        }
        it = this.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> i = it.next();
            if (T.getSafe(i.getKey()) < i.getValue()) {
                return true;
            }
        }
        return false;
    }

    
    
    /**
     * This function works correctly only when causality is preserved. 
     * added by Roh
     */    
    public static Causality comp(int s1, VectorClock T1, int s2, VectorClock T2){
    	int e11=T1.getSafe(s1);
    	int e12=T2.getSafe(s1);
    	int e21=T1.getSafe(s2);
    	int e22=T2.getSafe(s2);
    	if(e11 > e12 && e21 >= e22) return Causality.HA; 		// T1 happened after T2;  T2->T1
    	else if(e11 <= e12 && e21 < e22) return Causality.HB; 	// T1 Happened before T2; T1->T2 
    	return Causality.CO;
    }
    
    
    @Override
    public String toString(){
    	StringBuilder ret = new StringBuilder("{");
    	Iterator<Map.Entry<Integer, Integer>> it = this.entrySet().iterator();
    	while(it.hasNext()){
    		Map.Entry<Integer, Integer> i = it.next();
    		ret.append("(").append(i.getKey()).append(",").append(i.getValue()).append("),");
    	}    	
    	return ret.append("}").toString();
    }
    
    /*
     * Is this VC // T ? 
     * true iff nor VC > T nor T > VC
     */
    public boolean concurrent(VectorClock T) {
        return !(this.greaterThan(T) || T.greaterThan(this));
    }

    /**
     * Sets each entry of the VC to max(VCi, Oi)
     */
    public void upTo(VectorClock O) {
        for (Entry<Integer, Integer> k : O.entrySet()) {
            if (k.getValue() > this.getSafe(k.getKey())) {
                this.put(k.getKey(), k.getValue());
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final VectorClock other = (VectorClock) obj;

        Set<Integer> h = new HashSet<Integer>(this.keySet());
        h.addAll(other.keySet());

        for (Integer k : h) {
            if (this.getSafe(k) != other.getSafe(k)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        for (Entry<Integer, Integer> k : this.entrySet()) {
            hash += 7 * k.getKey() * k.getValue();
        }
        return hash;
    }
    
    /*
     * computes minimal vector from current and vector clocks provided in parameters.
     * for each vc in {this} U otherVectorClocks, for each i in min, min[i] <= vc[i] 
     */
    public VectorClock min(int localSiteId, Map<Integer, VectorClock> otherVectorClocks) {
        VectorClock min = new VectorClock(this);
//        min.put(localSiteId, Math.max(min.getSafe(localSiteId), 0));
        
        for (Entry<Integer, VectorClock> clockEntry : otherVectorClocks.entrySet()) {
            for (Entry<Integer, Integer> entry : min.entrySet()) {
                int e = entry.getKey();
                min.put(e, Math.min(min.getSafe(e), clockEntry.getValue().getSafe(e)));
            }
        }
        return min;
    }


}

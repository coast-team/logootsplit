package logootsplitO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LIDFactory{
    
    
    static public LIdentifier createBetweenPosition(LIdentifier l1,LIdentifier l2, int siteId, int clock){
        
        
        l1.setDefaultValue(Integer.MIN_VALUE);
        l2.setDefaultValue(Integer.MAX_VALUE);
        
        Iterator<Triple> it1=l1.iterator();
        Iterator<Triple> it2=l2.iterator();
        
        List<Triple> l=new ArrayList<Triple>();
        
        Triple t1=it1.next();
        Triple t2=it2.next();
        
        long d = t2.getPosition()-t1.getPosition();
        while(d<=1){
            l.add(t1);
            t1=it1.next();
            t2=it2.next();
            d=t2.getPosition()-t1.getPosition();
        }
        int p =(int)(Math.random()*(d-1))+1+t1.getPosition();
        l.add(new Triple(p, siteId, 0));
        
        return new LIdentifier(l, clock);
    }
}
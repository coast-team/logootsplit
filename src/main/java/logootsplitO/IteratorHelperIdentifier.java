package logootsplitO;

import java.util.Iterator;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class IteratorHelperIdentifier {

    int nextOffset;

    boolean samebase = false;
    IdentifierInterval id1;
    IdentifierInterval id2;
    Result result = null;

    public static enum Result {

        B1AfterB2, B1BeforeB2, B1InsideB2, B2insideB1, B1concatB2, B2ConcatB1
    };

    public IteratorHelperIdentifier(IdentifierInterval id1, IdentifierInterval id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

   private Result compareBase() {
        Iterator<Integer> b1 = id1.base.iterator();
        Iterator<Integer> b2 = id2.base.iterator();
        
        while (b1.hasNext() && b2.hasNext()) {
            int i1 = b1.next();
            int i2 = b2.next();
            if (i1 > i2) {
                return Result.B1AfterB2;
            } else if (i1 < i2) {
                return Result.B1BeforeB2;
            }
        }
        if (b1.hasNext()) {//b2 is shorter than b1
            this.nextOffset=b1.next();
            if(this.nextOffset<id2.begin){
                return Result.B1BeforeB2;
            }else if(this.nextOffset>=id2.end){
                return Result.B1AfterB2;
            }else {
                return Result.B1InsideB2;
            }
        } else if (b2.hasNext()) {//b1 is shorter than b2
             this.nextOffset=b2.next();
            if(this.nextOffset<id1.begin){
                return Result.B1AfterB2;
            }else if(this.nextOffset>=id1.end){
                return Result.B1BeforeB2;
            }else {
                return Result.B2insideB1;
            }
        } else { // they have same size
            if (id1.end + 1 == id2.begin) {
                return Result.B1concatB2;
            } else if (id1.begin == id2.end + 1) {
                return Result.B2ConcatB1;
            } else if (id1.end < id2.begin) {
                return Result.B1BeforeB2;
            } else {
                return Result.B1AfterB2;
            }
        }

    }
    public Result computeResults() {
        if (result == null) {
            result = compareBase();
        }
        return result;
    }
    public int getNextOffset() {
        return nextOffset;
    }
}

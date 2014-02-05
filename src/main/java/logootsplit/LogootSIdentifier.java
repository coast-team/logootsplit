package logootsplit;

import java.io.Serializable;

public class LogootSIdentifier implements Comparable<LogootSIdentifier>,Serializable{
    
    private int position;
    private int siteId;
    private int offset;

    private LogootSIdentifier(int position, int siteId, int offset) {
        this.siteId = siteId;
        this.position = position;
        this.offset = offset;
    }

    public LogootSIdentifier(int position, int siteId) {
        this(position, siteId, 0);
    }

    public LogootSIdentifier(LogootSIdentifier clone) {
        this(clone.position, clone.siteId, clone.offset);
    }

    public LogootSIdentifier(LogootSIdentifier other, int offset) {
        this(other.position, other.siteId, offset);
    }

    public int getPosition() {
        return this.position;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int i) {
        this.offset = i;
    }


    @Override
    public int compareTo(LogootSIdentifier other) {
        int diff = this.position - other.position;
        if (diff != 0)
            return diff;
        
        diff = this.siteId - other.siteId;
        if (diff != 0)
            return diff;
        
        diff = this.offset - other.offset;
        return diff;
    }

    @Override
    public String toString() {
        return "(" + this.position + "," + this.siteId + "," + this.offset + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LogootSIdentifier) {
            return compareTo((LogootSIdentifier) o) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.position;
        hash = 47 * hash + this.siteId;
        hash = 47 * hash + this.offset;
        return hash;
    }    
    
}
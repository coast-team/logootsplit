package facade;

import java.util.List;
import logootsplitO.LogootSDoc;
import logootsplitO.LogootSOp;

public class TextInsert implements TextOperation {

    private int offset;
    private List<Character> content;

    public TextInsert(int offset, List content) {
        this.offset = offset;
        this.content = content;
    }

    public int getOffset() {
        return this.offset;
    }
    
    public List<Character> getContent() {
        return this.content;
    }
       
    @Override
    public void applyToText() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LogootSOp toCDRTOp(LogootSDoc<Character> doc) {
        return doc.insertLocal(offset, content);

    }

}

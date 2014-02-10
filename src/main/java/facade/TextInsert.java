package facade;

import java.util.List;
import logootsplitO.LogootSDoc;
import logootsplitO.LogootSOp;
import logootsplitO.Utils;

public class TextInsert implements TextOperation {

    private int offset;
    private List<Character> content;

    public TextInsert(int offset, String content) {
        this.offset = offset;
        this.content = Utils.convertStringToCharactersList(content);
    }

    public int getOffset() {
        return this.offset;
    }
    
    public String getContent() {
        return Utils.convertCharactersListToString(this.content);
    }

    @Override
    public LogootSOp applyTo(LogootSDoc<Character> doc) {
        return doc.insertLocal(offset, content);

    }

}

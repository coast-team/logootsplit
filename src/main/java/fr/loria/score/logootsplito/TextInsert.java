package fr.loria.score.logootsplito;

import java.util.List;

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

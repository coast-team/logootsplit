package fr.loria.score.logootsplito;

public class TextDelete implements TextOperation {

    private int offset;
    private int length;

    public TextDelete(int offset, int length) {
        this.offset = offset;
        this.length = length;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getLength() {
        return this.length;
    }

    @Override
    public LogootSOp applyTo(LogootSDoc<Character> doc) {
        return doc.delLocal(offset, offset + length - 1);
    }
}

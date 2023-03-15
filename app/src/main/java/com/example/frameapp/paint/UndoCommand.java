package debtechllc.deb.talez.paint;

public interface UndoCommand {
    void undo();
    void redo();
    boolean canUndo();
    boolean canRedo();
}

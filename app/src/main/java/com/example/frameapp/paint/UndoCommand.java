package com.example.frameapp.paint;

public interface UndoCommand {
    void undo();
    void redo();
    boolean canUndo();
    boolean canRedo();
}

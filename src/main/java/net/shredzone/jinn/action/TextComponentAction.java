/**
 * jinn - A property translation editor
 *
 * Copyright (C) 2009 Richard "Shred" Körber
 *   http://jinn.shredzone.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.shredzone.jinn.action;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

import net.shredzone.jinn.i18n.L;
import net.shredzone.jinn.pool.ImgPool;

/**
 * This is the base class for JTextComponent related actions. There are also inner
 * subclasses for the standard actions: cut, copy, paste, undo, redo.
 *
 * @author Richard "Shred" Körber
 */
public abstract class TextComponentAction extends BaseAction implements DocumentListener, UndoableEditListener, CaretListener {
    private static final long serialVersionUID = -8651443172426392912L;
    protected final JTextComponent comp;

    /**
     * Create a new JTextComponent Action.
     *
     * @param comp
     *            JTextComponent this action refers to
     * @param name
     *            Action Name
     * @param icon
     *            Action Icon or null
     * @param tip
     *            Action Tooltip or null
     * @param accel
     *            Accelerator Key or null
     */
    public TextComponentAction(JTextComponent comp, String name, Icon icon, String tip, KeyStroke accel) {
        super(name, icon, tip, accel);
        this.comp = comp;
    }

    /**
     * This method is invoked whenever the component should update its enabled state.
     */
    protected abstract void updateState();

    /**
     * Helper method, returns true when a text is selected. A text is selected when the
     * selection start and the selection end is different.
     *
     * @return true: text is selected
     */
    protected boolean isSelected() {
        final int start = comp.getSelectionStart();
        final int end = comp.getSelectionEnd();
        return (start != end);
    }

    /**
     * Invoke updateState whenever a text change occured. This is either a change in the
     * document or an undoable edit.
     */
    protected void notifyTextChanges() {
        comp.getDocument().addDocumentListener(this);
        comp.getDocument().addUndoableEditListener(this);
    }

    /**
     * Invoke updateState whenever a caret change occured. This is either a change in the
     * caret position or in the selection range.
     */
    protected void notifyCaretChanges() {
        comp.addCaretListener(this);
    }

    /**
     * DocumentListener implementation, do not use.
     *
     * @param e
     *            DocumentEvent
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        updateState();
    }

    /**
     * DocumentListener implementation, do not use.
     *
     * @param e
     *            DocumentEvent
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        updateState();
    }

    /**
     * DocumentListener implementation, do not use.
     *
     * @param e
     *            DocumentEvent
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
        updateState();
    }

    /**
     * UndoableEditListener implementation, do not use.
     *
     * @param e
     *            UndoableEditEvent
     */
    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        updateState();
    }

    /**
     * CaretListener implementation, do not use.
     *
     * @param e
     *            CaretEvent
     */
    @Override
    public void caretUpdate(CaretEvent e) {
        updateState();
    }

    /**
     * Cut operation.
     */
    public static class CutTextAction extends TextComponentAction {
        private static final long serialVersionUID = 1265428506138842235L;

        /**
         * Create a new CutTextAction.
         *
         * @param comp
         *            JTextComponent this action refers to
         */
        public CutTextAction(JTextComponent comp) {
            super(comp, L.tr("action.cut"), ImgPool.get("cut.png"), L.tr("action.cut.tt"), KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
            notifyCaretChanges();
            updateState();
        }

        /**
         * Update the own state.
         */
        @Override
        protected void updateState() {
            setEnabled(isSelected());
        }

        /**
         * Perform the operation.
         *
         * @param e
         *            ActionEvent
         */
        @Override
        public void perform(ActionEvent e) {
            comp.cut();
        }
    }

    /**
     * Copy operation.
     */
    public static class CopyTextAction extends TextComponentAction {
        private static final long serialVersionUID = -2621402181754230726L;

        /**
         * Create a new CopyTextAction.
         *
         * @param comp
         *            JTextComponent this action refers to
         */
        public CopyTextAction(JTextComponent comp) {
            super(comp, L.tr("action.copy"), ImgPool.get("copy.png"), L.tr("action.copy.tt"), KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
            notifyCaretChanges();
            updateState();
        }

        /**
         * Update the own state.
         */
        @Override
        protected void updateState() {
            setEnabled(isSelected());
        }

        /**
         * Perform the operation.
         *
         * @param e
         *            ActionEvent
         */
        @Override
        public void perform(ActionEvent e) {
            comp.copy();
        }
    }

    /**
     * Paste operation.
     */
    public static class PasteTextAction extends TextComponentAction {
        private static final long serialVersionUID = 3623311082154332570L;

        /**
         * Create a new PasteTextAction.
         *
         * @param comp
         *            JTextComponent this action refers to
         */
        public PasteTextAction(JTextComponent comp) {
            super(comp, L.tr("action.paste"), ImgPool.get("paste.png"), L.tr("action.paste.tt"), KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
            updateState();
        }

        /**
         * Update the own state.
         */
        @Override
        protected void updateState() {
        // paste is always enabled
        }

        /**
         * Perform the operation.
         *
         * @param e
         *            ActionEvent
         */
        @Override
        public void perform(ActionEvent e) {
            comp.paste();
        }
    }

    /**
     * Undo operation.
     */
    public static class UndoTextAction extends TextComponentAction {
        private static final long serialVersionUID = -7082929743059312417L;
        private final UndoManager undo;

        /**
         * Create a new UndoTextAction.
         *
         * @param comp
         *            JTextComponent this action refers to
         * @param undo
         *            UndoManager used
         */
        public UndoTextAction(JTextComponent comp, UndoManager undo) {
            super(comp, L.tr("action.undo"), ImgPool.get("undo.png"), L.tr("action.undo.tt"), KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
            this.undo = undo;
            notifyTextChanges();
            updateState();
        }

        /**
         * Update the own state.
         */
        @Override
        protected void updateState() {
            setEnabled(undo.canUndo());
        }

        /**
         * Perform the operation.
         *
         * @param e
         *            ActionEvent
         */
        @Override
        public void perform(ActionEvent e) {
            if (undo.canUndo()) {
                undo.undo();
            } else {
                // Nothing to undo
                updateState();
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    /**
     * Redo operation.
     */
    public static class RedoTextAction extends TextComponentAction {
        private static final long serialVersionUID = 5636347237880346025L;
        private final UndoManager undo;

        /**
         * Create a new RedoTextAction.
         *
         * @param comp
         *            JTextComponent this action refers to
         * @param undo
         *            UndoManager used
         */
        public RedoTextAction(JTextComponent comp, UndoManager undo) {
            super(comp, L.tr("action.redo"), ImgPool.get("redo.png"), L.tr("action.redo.tt"), KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
            this.undo = undo;
            notifyTextChanges();
            updateState();
        }

        /**
         * Update the own state.
         */
        @Override
        protected void updateState() {
            setEnabled(undo.canRedo());
        }

        /**
         * Perform the operation.
         *
         * @param e
         *            ActionEvent
         */
        @Override
        public void perform(ActionEvent e) {
            if (undo.canRedo()) {
                undo.redo();
            } else {
                // Nothing to redo
                updateState();
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

}

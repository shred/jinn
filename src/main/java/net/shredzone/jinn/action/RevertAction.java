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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;

import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jinn.pool.ImgPool;
import net.shredzone.jinn.property.PropertyModel;

/**
 * Revert to the reference text.
 * 
 * @author Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: RevertAction.java 315 2009-05-13 19:32:40Z shred $
 */
public class RevertAction extends BaseAction {
    private static final long serialVersionUID = 7145249004953005243L;
    protected final Registry registry;
    protected final JTextComponent editor;
    protected final JTextComponent reference;
    protected final UndoManager undo;

    /**
     * Create a new RevertAction.
     * 
     * @param registry
     *            The application's Registry
     */
    public RevertAction(Registry registry, JTextComponent editor, JTextComponent reference, UndoManager undo) {
        super(L.tr("action.revert"), ImgPool.get("revert.png"), L.tr("action.revert.tt"), KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));

        this.registry = registry;
        this.editor = editor;
        this.reference = reference;
        this.undo = undo;

        setEnabled(registry.get(JinnRegistryKeys.FILE_REFERENCE) != null);

        registry.addPropertyChangeListener(JinnRegistryKeys.FILE_REFERENCE, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                setEnabled(evt.getNewValue() != null);
            }
        });
    }

    /**
     * The action implementation itself.
     * 
     * @param e
     *            ActionEvent, may be null if directly invoked
     */
    @Override
    public void perform(ActionEvent e) {
        final PropertyModel mReference = (PropertyModel) registry.get(JinnRegistryKeys.MODEL_REFERENCE);

        if (mReference != null) {
            // Copy the reference text

            CompoundEdit compound = new CompoundEdit();
            undo.addEdit(compound);
            editor.setText(reference.getText());
            compound.end();

        } else {
            // No reference, just beep
            Toolkit.getDefaultToolkit().beep();
        }

    }

}

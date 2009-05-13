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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jinn.pool.ImgPool;
import net.shredzone.jinn.property.PropertyModel;

/**
 * Start a new set of properties.
 * 
 * @author Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: NewAction.java 315 2009-05-13 19:32:40Z shred $
 */
public class NewAction extends BaseAction {
    private static final long serialVersionUID = -5321317574685533552L;
    protected final Registry registry;

    /**
     * Create a new NewAction.
     * 
     * @param registry
     *            The application's Registry
     */
    public NewAction(Registry registry) {
        super(L.tr("action.new"), ImgPool.get("new.png"), L.tr("action.new.tt"), KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));

        this.registry = registry;
    }

    /**
     * The action implementation itself.
     * 
     * @param e
     *            ActionEvent, may be null if directly invoked
     */
    @Override
    public void perform(ActionEvent e) {
        if (registry.is(JinnRegistryKeys.FLAG_CHANGED)) {
            int result = JOptionPane.showConfirmDialog(getFrame(e), L.tr("save.confirm"), L.tr("save.confirm.new"), JOptionPane.YES_NO_CANCEL_OPTION);

            if (result == JOptionPane.CANCEL_OPTION) {
                return;
            }

            if (result == JOptionPane.YES_OPTION) {
                SaveAction save = (SaveAction) registry.get(JinnRegistryKeys.ACTION_SAVE);
                if (!save.doSave(getFrame(e))) {
                    return; // Save failed: do not quit!
                }
            }
        }

        registry.put(JinnRegistryKeys.FILE_TRANSLATION, null);
        registry.put(JinnRegistryKeys.MODEL_TRANSLATION, new PropertyModel());
        registry.put(JinnRegistryKeys.FILE_REFERENCE, null);
        registry.put(JinnRegistryKeys.MODEL_REFERENCE, new PropertyModel());
        registry.put(JinnRegistryKeys.FLAG_CHANGED, false);
    }

}

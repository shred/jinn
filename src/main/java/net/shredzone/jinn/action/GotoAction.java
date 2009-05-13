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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.gui.PropertyKeyModel;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jinn.pool.ImgPool;

/**
 * Go to a key.
 * 
 * @author Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: GotoAction.java 315 2009-05-13 19:32:40Z shred $
 */
public class GotoAction extends BaseAction {
    private static final long serialVersionUID = -6185052702783447904L;
    private final Registry registry;

    /**
     * Create a new GotoAction.
     */
    public GotoAction(Registry registry) {
        super(L.tr("action.goto"), ImgPool.get("goto.png"), L.tr("action.goto.tt"), KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        this.registry = registry;

        setEnabled(registry.get(JinnRegistryKeys.MODEL_TRANSLATION) != null);

        registry.addPropertyChangeListener(JinnRegistryKeys.MODEL_TRANSLATION, new PropertyChangeListener() {
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
        String input = JOptionPane.showInputDialog(getFrame(e), L.tr("goto.msg"), L.tr("goto.title"), JOptionPane.QUESTION_MESSAGE);

        if (input != null) {
            final PropertyKeyModel keyModel = (PropertyKeyModel) registry.get(JinnRegistryKeys.MODEL_REFERENCE_KEY);
            if (keyModel == null) return;

            if (keyModel.hasKey(input)) {
                registry.put(JinnRegistryKeys.CURRENT_KEY, input);
            } else {
                JOptionPane.showMessageDialog(getFrame(e), L.tr("goto.notfound.msg"), L.tr("goto.notfound.title"), JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

}

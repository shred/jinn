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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.gui.PropertyKeyModel;
import net.shredzone.jinn.gui.PropertyKeyRefModel;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jinn.pool.ImgPool;

/**
 * Jump to the next untranslated string.
 *
 * @author Richard "Shred" Körber
 */
public class NextAction extends BaseAction {
    private static final long serialVersionUID = 7168091727814398003L;
    protected final Registry registry;

    /**
     * Create a new NextAction.
     *
     * @param registry
     *            The application's Registry
     */
    public NextAction(Registry registry) {
        super(L.tr("action.next"), ImgPool.get("next.png"), L.tr("action.next.tt"), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, ActionEvent.CTRL_MASK));

        this.registry = registry;

        setEnabled(registry.get(JinnRegistryKeys.FILE_REFERENCE) != null);

        registry.addPropertyChangeListener(JinnRegistryKeys.FILE_REFERENCE, new PropertyChangeListener() {
            @Override
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

        final PropertyKeyModel keyModel = (PropertyKeyModel) registry.get(JinnRegistryKeys.MODEL_REFERENCE_KEY);
        if (keyModel instanceof PropertyKeyRefModel) {
            final PropertyKeyRefModel pkrm = (PropertyKeyRefModel) keyModel;
            final String currentKey = registry.getString(JinnRegistryKeys.CURRENT_KEY);
            String nextKey = pkrm.findNext(currentKey);

            if (nextKey == null) {
                JOptionPane.showMessageDialog((Component) registry.get(JinnRegistryKeys.FRAME_MAIN), L.tr("a.next.eof"), L.tr("a.next.eof.title"), JOptionPane.INFORMATION_MESSAGE);

                if (pkrm.getSize() > 0) {
                    // Start all over again
                    nextKey = (String) pkrm.getElementAt(0);
                }
            }

            registry.put(JinnRegistryKeys.CURRENT_KEY, nextKey);
        }

    }

}

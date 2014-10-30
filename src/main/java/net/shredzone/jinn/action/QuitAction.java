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

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jinn.pool.ImgPool;
import net.shredzone.jinn.property.PropertyModel;

/**
 * Quit the application.
 *
 * @author Richard "Shred" Körber
 */
public class QuitAction extends BaseAction {
    private static final long serialVersionUID = -2916484363510929249L;
    private Window window;
    protected final Registry registry;

    /**
     * Create a new QuitAction.
     *
     * @param registry
     *            The application's Registry
     */
    public QuitAction(Registry registry) {
        super(L.tr("action.quit"), ImgPool.get("quit.png"), L.tr("action.quit.tt"), KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

        this.registry = registry;
        this.window = (Window) registry.get(JinnRegistryKeys.FRAME_MAIN);
    }

    /**
     * The action implementation itself.
     *
     * @param e
     *            ActionEvent, may be null if directly invoked
     */
    @Override
    public void perform(ActionEvent e) {
        final File target = (File) registry.get(JinnRegistryKeys.FILE_TRANSLATION);
        final PropertyModel model = (PropertyModel) registry.get(JinnRegistryKeys.MODEL_TRANSLATION);

        if (target != null && model != null && registry.is(JinnRegistryKeys.FLAG_CHANGED)) {
            int result = JOptionPane.showConfirmDialog(window, L.tr("save.confirm"), L.tr("save.confirm.quit"), JOptionPane.YES_NO_CANCEL_OPTION);

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

        if (window != null) {
            window.setVisible(false);
            window.dispose();
        } else {
            System.exit(0); // nothing else would take care for quitting
        }
    }

}

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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.KeyStroke;

import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.gui.ExceptionDialog;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jinn.pool.ImgPool;
import net.shredzone.jinn.property.PropertyModel;

/**
 * Save a properties file
 * 
 * @author Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: SaveAction.java 315 2009-05-13 19:32:40Z shred $
 */
public class SaveAction extends AsyncBaseAction {
    private static final long serialVersionUID = -1701159023065148732L;
    protected final Registry registry;

    /**
     * Create a new SaveAction.
     * 
     * @param registry
     *            The application's Registry
     */
    public SaveAction(Registry registry) {
        super(L.tr("action.save"), ImgPool.get("save.png"), L.tr("action.save.tt"), KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

        this.registry = registry;

        setEnabled((registry.get(JinnRegistryKeys.FILE_TRANSLATION) != null)
            && (registry.is(JinnRegistryKeys.FLAG_CHANGED)));

        registry.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                String name = evt.getPropertyName();
                if (name.equals(JinnRegistryKeys.FILE_TRANSLATION)
                    || name.equals(JinnRegistryKeys.FLAG_CHANGED)) {
                    setEnabled((SaveAction.this.registry.get(JinnRegistryKeys.FILE_TRANSLATION) != null)
                        && (SaveAction.this.registry.is(JinnRegistryKeys.FLAG_CHANGED)));
                }
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
        doSave(getFrame(e));
    }

    /**
     * Saves the current properties file.
     * 
     * @param parent
     *            Parent component to block
     * @return true: Save was successful, false: Save failed
     */
    public boolean doSave(Component parent) {
        boolean success = true;

        final File target = (File) registry.get(JinnRegistryKeys.FILE_TRANSLATION);

        final PropertyModel model = (PropertyModel) registry.get(JinnRegistryKeys.MODEL_TRANSLATION);

        if (target != null && model != null) {
            // We've got a file we can save into...

            OutputStream out = null;
            try {
                out = new BufferedOutputStream(new FileOutputStream(target));
                model.write(out);
                out.flush();

                registry.put(JinnRegistryKeys.FLAG_CHANGED, false);

            } catch (IOException ex) {
                ExceptionDialog.show(parent, L.tr("a.save.ex.writing"), ex);
                success = false;
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ex2) {
                        ExceptionDialog.show(parent, L.tr("a.save.ex.reading"), ex2);
                        success = false;
                    }
                }
            }

        } else {
            // We ain't got no file to save, so give a visual feedback...
            Toolkit.getDefaultToolkit().beep();
        }

        return success;
    }

}

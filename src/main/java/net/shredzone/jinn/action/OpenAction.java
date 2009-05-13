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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.gui.ExceptionDialog;
import net.shredzone.jinn.gui.PropertiesFileFilter;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jinn.pool.ImgPool;
import net.shredzone.jinn.property.PropertyModel;

/**
 * Open a properties file
 * 
 * @author Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: OpenAction.java 315 2009-05-13 19:32:40Z shred $
 */
public class OpenAction extends AsyncBaseAction {
    private static final long serialVersionUID = -1701159023065148732L;
    protected final Registry registry;

    /**
     * Create a new OpenAction.
     * 
     * @param registry
     *            The application's Registry
     */
    public OpenAction(Registry registry) {
        super(L.tr("action.open"), ImgPool.get("open.png"), L.tr("action.open.tt"), KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));

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
        /* TODO: Ask for confirmance */

        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory((File) registry.get(JinnRegistryKeys.FILE_LASTDIR));
        fc.setFileFilter(PropertiesFileFilter.DEFAULT);
        int result = fc.showOpenDialog(getFrame(e));
        if (result == JFileChooser.APPROVE_OPTION) {
            final File file = fc.getSelectedFile();

            InputStream in = null;

            try {
                PropertyModel src = new PropertyModel();
                in = new BufferedInputStream(new FileInputStream(file));
                src.read(in);
                registry.put(JinnRegistryKeys.FILE_LASTDIR, file.getParentFile());
                registry.put(JinnRegistryKeys.FILE_TRANSLATION, file);
                registry.put(JinnRegistryKeys.MODEL_TRANSLATION, src);
                registry.put(JinnRegistryKeys.FLAG_CHANGED, false);

            } catch (Exception ex) {
                ExceptionDialog.show(getFrame(e), L.tr("a.open.ex.reading"), ex);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex2) {
                        ExceptionDialog.show(getFrame(e), L.tr("a.open.ex.closing"), ex2);
                    }
                }
            }

        }
    }

}

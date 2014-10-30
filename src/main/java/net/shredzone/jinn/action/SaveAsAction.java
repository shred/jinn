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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.gui.ExceptionDialog;
import net.shredzone.jinn.gui.PropertiesFileFilter;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jinn.pool.ImgPool;
import net.shredzone.jinn.property.PropertyModel;

/**
 * Save a properties file to another file.
 *
 * @author Richard "Shred" Körber
 */
public class SaveAsAction extends AsyncBaseAction {
    private static final long serialVersionUID = -5321317574685533552L;
    protected final Registry registry;

    /**
     * Create a new SaveAsAction.
     *
     * @param registry
     *            The application's Registry
     */
    public SaveAsAction(Registry registry) {
        super(L.tr("action.saveas"), ImgPool.get("save-as.png"), L.tr("action.saveas.tt"), KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));

        this.registry = registry;

        setEnabled((registry.get(JinnRegistryKeys.FILE_TRANSLATION) != null)
            && (registry.is(JinnRegistryKeys.FLAG_CHANGED)));

        registry.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String name = evt.getPropertyName();
                if (name.equals(JinnRegistryKeys.FILE_TRANSLATION)
                    || name.equals(JinnRegistryKeys.FLAG_CHANGED)) {
                    setEnabled((SaveAsAction.this.registry.get(JinnRegistryKeys.FILE_TRANSLATION) != null)
                        && (SaveAsAction.this.registry.is(JinnRegistryKeys.FLAG_CHANGED)));
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
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory((File) registry.get(JinnRegistryKeys.FILE_LASTDIR));
        fc.setFileFilter(PropertiesFileFilter.DEFAULT);
        int result = fc.showSaveDialog(getFrame(e));
        if (result == JFileChooser.APPROVE_OPTION) {
            final File target = fc.getSelectedFile();

            // --- Check for Overwrite ---
            if (target.exists()) {
                final int appr = JOptionPane.showConfirmDialog(getFrame(e), L.tr("a.saveas.confirm.msg"), L.tr("a.saveas.confirm.title"), JOptionPane.YES_NO_OPTION);

                if (appr != JOptionPane.YES_OPTION) {
                    return; // Bail out, because user changed his mind.
                }
            }

            // --- Register the new File ---
            registry.put(JinnRegistryKeys.FILE_LASTDIR, target.getParentFile());
            registry.put(JinnRegistryKeys.FILE_TRANSLATION, target);

            // --- Write the File ---
            final PropertyModel model = (PropertyModel) registry.get(JinnRegistryKeys.MODEL_TRANSLATION);
            if (model != null) {
                // We've got a file we can save into...

                OutputStream out = null;
                try {
                    out = new BufferedOutputStream(new FileOutputStream(target));
                    model.write(out);
                    out.flush();

                    registry.put(JinnRegistryKeys.FLAG_CHANGED, false);
                } catch (IOException ex) {
                    ExceptionDialog.show(getFrame(e), L.tr("a.save.ex.writing"), ex);
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ex2) {
                            ExceptionDialog.show(getFrame(e), L.tr("a.save.ex.reading"), ex2);
                        }
                    }
                }

            } else {
                // No model, so give at least a visual feedback
                Toolkit.getDefaultToolkit().beep();
            }

        }
    }

}

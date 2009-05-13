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
package net.shredzone.jinn.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import net.shredzone.jinn.Jinn;
import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jshred.swing.MenuActionProxy;
import net.shredzone.jshred.swing.SwingUtils;

/**
 * This is the MenuBar of Jinn.
 * 
 * @author Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: JinnMenuBar.java 315 2009-05-13 19:32:40Z shred $
 */
public class JinnMenuBar extends JMenuBar {
    private static final long serialVersionUID = 302069297128892725L;
    private final Registry registry;
    private JRadioButtonMenuItem jrbmiAuto;
    private JRadioButtonMenuItem jrbmiDe;
    private JRadioButtonMenuItem jrbmiEn;

    public JinnMenuBar(Registry registry) {
        this.registry = registry;

        final JMenu jmFile = SwingUtils.createJMenu(L.tr("menu.file"));
        {
            jmFile.add(getItem(JinnRegistryKeys.ACTION_NEW));
            jmFile.add(getItem(JinnRegistryKeys.ACTION_OPEN));
            jmFile.add(getItem(JinnRegistryKeys.ACTION_SAVE));
            jmFile.add(getItem(JinnRegistryKeys.ACTION_SAVEAS));
            jmFile.addSeparator();
            jmFile.add(getItem(JinnRegistryKeys.ACTION_MERGE));
            jmFile.addSeparator();
            jmFile.add(getItem(JinnRegistryKeys.ACTION_QUIT));
        }
        add(jmFile);

        final JMenu jmEdit = SwingUtils.createJMenu(L.tr("menu.edit"));
        {
            jmEdit.add(getItem(JinnRegistryKeys.ACTION_UNDO));
            jmEdit.add(getItem(JinnRegistryKeys.ACTION_REDO));
            jmEdit.addSeparator();
            jmEdit.add(getItem(JinnRegistryKeys.ACTION_CUT));
            jmEdit.add(getItem(JinnRegistryKeys.ACTION_COPY));
            jmEdit.add(getItem(JinnRegistryKeys.ACTION_PASTE));
            jmEdit.addSeparator();
            jmEdit.add(getItem(JinnRegistryKeys.ACTION_REVERT));
            jmEdit.add(getItem(JinnRegistryKeys.ACTION_CLEAN));
        }
        add(jmEdit);

        final JMenu jmSearch = SwingUtils.createJMenu(L.tr("menu.search"));
        {
            jmSearch.add(getItem(JinnRegistryKeys.ACTION_SEARCH));
            jmSearch.add(getItem(JinnRegistryKeys.ACTION_SEARCH_NEXT));
            jmSearch.add(getItem(JinnRegistryKeys.ACTION_SEARCH_PREV));
            jmSearch.addSeparator();
            jmSearch.add(getItem(JinnRegistryKeys.ACTION_NEXT));
            jmSearch.add(getItem(JinnRegistryKeys.ACTION_GOTO));
        }
        add(jmSearch);

        final JMenu jmOptions = SwingUtils.createJMenu(L.tr("menu.options"));
        {
            final LanguageActionListener lal = new LanguageActionListener();
            final Preferences prefs = Preferences.userNodeForPackage(Jinn.class);
            String lname = prefs.get("lang", "");

            JMenu jmLang = new JMenu(L.tr("menu.options.lang"));

            jrbmiAuto = new JRadioButtonMenuItem(L.tr("menu.options.lang.auto"), lname.length() == 0);
            jrbmiDe = new JRadioButtonMenuItem("Deutsch", lname.equals("de"));
            jrbmiEn = new JRadioButtonMenuItem("English", lname.equals("en"));

            jrbmiAuto.addActionListener(lal);
            jrbmiDe.addActionListener(lal);
            jrbmiEn.addActionListener(lal);

            ButtonGroup languageGroup = new ButtonGroup();
            languageGroup.add(jrbmiAuto);
            languageGroup.add(jrbmiDe);
            languageGroup.add(jrbmiEn);

            jmLang.add(jrbmiAuto);
            jmLang.add(jrbmiDe);
            jmLang.add(jrbmiEn);

            jmOptions.add(jmLang);
        }
        add(jmOptions);

        add(Box.createGlue());

        final JMenu jmHelp = SwingUtils.createJMenu(L.tr("menu.help"));
        {
            jmHelp.add(getItem(JinnRegistryKeys.ACTION_ABOUT));
        }
        add(jmHelp);
    }

    /**
     * Create a menu Action for a registry Action key.
     * 
     * @param key
     *            ACTION key to be used
     * @return Created MenuActionProxy
     */
    protected MenuActionProxy getItem(String key) {
        return new MenuActionProxy((Action) registry.get(key));
    }

    private class LanguageActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Preferences prefs = Preferences.userNodeForPackage(Jinn.class);

            if (jrbmiDe.isSelected()) {
                prefs.put("lang", "de");
            } else if (jrbmiEn.isSelected()) {
                prefs.put("lang", "en");
            } else {
                prefs.remove("lang");
            }

            JOptionPane.showMessageDialog(JinnMenuBar.this, L.tr("options.restart"));
        }

    }

}

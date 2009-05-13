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
package net.shredzone.jinn;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.shredzone.jinn.action.AboutAction;
import net.shredzone.jinn.action.GotoAction;
import net.shredzone.jinn.action.MergeAction;
import net.shredzone.jinn.action.NewAction;
import net.shredzone.jinn.action.NextAction;
import net.shredzone.jinn.action.OpenAction;
import net.shredzone.jinn.action.QuitAction;
import net.shredzone.jinn.action.SaveAction;
import net.shredzone.jinn.action.SaveAsAction;
import net.shredzone.jinn.action.SearchAction;
import net.shredzone.jinn.action.SearchNextAction;
import net.shredzone.jinn.gui.JinnPane;
import net.shredzone.jinn.i18n.L;

/**
 * Jinn is a tool for easier translation of properties files.
 * <p>
 * It was designed with KISS (Keep It Simple Stupid) in mind. Most translators are living
 * all over the world, most probably living in a country where their native language is
 * spoken. Also most translators are not software developers, but normal users who don't
 * know too much about computers and especially about software localizion.
 * <p>
 * This means that a translation tool must be:
 * <ul>
 * <li>easy to install
 * <li>easy to use
 * <li>hiding the complexity of properties files (and believe me, they ARE complex for
 * common computer users)
 * </ul>
 * <p>
 * The name jinn consists of 'j' for Java, 'in' for i18n (the abbreviation of
 * 'internationalization') without the 18, and 'n' because 'jin' was already taken by
 * another project. ;-)
 * 
 * @author Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: Jinn.java 315 2009-05-13 19:32:40Z shred $
 */
public class Jinn {
    private final static Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(Jinn.class);

    private final Registry registry = new Registry();
    private JFrame frame;

    public Jinn() {
        frame = new JFrame();
        frame.setTitle(L.tr("generic.title") + Style.VERSION);

        registry.put(JinnRegistryKeys.FRAME_MAIN, frame);

        createActions();

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                storeFrame();
                QuitAction aQuit = (QuitAction) registry.get(JinnRegistryKeys.ACTION_QUIT);
                aQuit.perform();
            }
        });

        JinnPane jinnpane = new JinnPane(registry);
        frame.getContentPane().add(jinnpane);
        recallFrame();

        // Why using invokeLater here? See:
        // http://java.sun.com/developer/JDCTechTips/2003/tt1208.html#1
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    /**
     * Store the frame position and size.
     */
    protected void storeFrame() {
        final Point pnt = frame.getLocation();
        final Dimension dim = frame.getSize();
        prefs.putInt("win.pos.x", pnt.x);
        prefs.putInt("win.pos.y", pnt.y);
        prefs.putInt("win.size.w", dim.width);
        prefs.putInt("win.size.h", dim.height);
    }

    /**
     * Recall the frame position and size.
     */
    protected void recallFrame() {
        final int pw = prefs.getInt("win.size.w", -1);
        final int ph = prefs.getInt("win.size.h", -1);
        if (pw >= 0 && ph >= 0) {
            frame.setSize(pw, ph);
        } else {
            frame.pack();
        }

        final int px = prefs.getInt("win.pos.x", -9999);
        final int py = prefs.getInt("win.pos.y", -9999);
        if (px != -9999 && py != -9999) {
            frame.setLocation(px, py);
        } else {
            frame.setLocationRelativeTo(null);
        }
    }

    /**
     * Create all Actions and put them into the registry.
     */
    protected void createActions() {
        registry.put(JinnRegistryKeys.ACTION_QUIT, new QuitAction(registry));
        registry.put(JinnRegistryKeys.ACTION_NEW, new NewAction(registry));
        registry.put(JinnRegistryKeys.ACTION_OPEN, new OpenAction(registry));
        registry.put(JinnRegistryKeys.ACTION_MERGE, new MergeAction(registry));
        registry.put(JinnRegistryKeys.ACTION_SAVE, new SaveAction(registry));
        registry.put(JinnRegistryKeys.ACTION_SAVEAS, new SaveAsAction(registry));
        registry.put(JinnRegistryKeys.ACTION_NEXT, new NextAction(registry));
        registry.put(JinnRegistryKeys.ACTION_SEARCH, new SearchAction(registry));
        registry.put(JinnRegistryKeys.ACTION_SEARCH_NEXT, new SearchNextAction(registry, SearchNextAction.NEXT));
        registry.put(JinnRegistryKeys.ACTION_SEARCH_PREV, new SearchNextAction(registry, SearchNextAction.PREVIOUS));
        registry.put(JinnRegistryKeys.ACTION_GOTO, new GotoAction(registry));
        registry.put(JinnRegistryKeys.ACTION_ABOUT, new AboutAction(registry));
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // --- Sprache setzen ---
        Preferences prefs = Preferences.userNodeForPackage(Jinn.class);
        String lname = prefs.get("lang", "");
        if (lname.length() != 0) {
            Locale.setDefault(new Locale(lname));
        }

        // --- Do some MacX patches ---
        final boolean isMacOS = (System.getProperty("mrj.version") != null);
        if (isMacOS) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "jinn");
        }

        new Jinn();
    }

}

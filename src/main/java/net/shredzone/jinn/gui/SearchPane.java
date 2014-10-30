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

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jshred.swing.JLabelGroup;

/**
 * A search pane provides input fields for a search dialog. A static method
 * <code>showSearchPane()</code> will open an appropriate modal search dialog.
 *
 * @author Richard "Shred" Körber
 */
public class SearchPane extends JPanel {
    private static final long serialVersionUID = 9034069296224720962L;

    public final static int SEARCH_OPTION = 0;
    public final static int REPLACE_OPTION = 1;
    public final static int REPLACEALL_OPTION = 2;
    public final static int CLOSED_OPTION = JOptionPane.CLOSED_OPTION;

    private JTextField jtfTerm;
    private JCheckBox jcbCaseSensitive;
    private JTextField jtfReplace;
    private Registry registry;

    /**
     * Create a new main Jinn pane, using the given Registry.
     *
     * @param registry
     *            Registry for this Jinn instance
     */
    public SearchPane(Registry registry) {
        // --- Build GUI ---
        build();
        this.registry = registry;
        setup();

        // --- Listen for Property Changes ---
        new MyPropertyChangeListener(registry);
    }

    /**
     * Set the search term, which is the string to be searched for.
     *
     * @param term
     *            Search term
     */
    public void setSearchTerm(String term) {
        jtfTerm.setText(term);
    }

    /**
     * Get the search term currently entered in the text field.
     *
     * @return Search term
     */
    public String getSearchTerm() {
        return jtfTerm.getText();
    }

    /**
     * Set the replacement string.
     *
     * @param replace
     *            Replacement
     */
    public void setReplacement(String replace) {
        jtfReplace.setText(replace);
    }

    /**
     * Get the replacement string currently entered in the text field.
     *
     * @return Replacement
     */
    public String getReplacement() {
        return jtfReplace.getText();
    }

    /**
     * Set the search mode. If true is provided, the searching will be case sensitive.
     *
     * @param cs
     *            true: case sensitive search, false: case insensitive
     */
    public void setCaseSensitive(boolean cs) {
        jcbCaseSensitive.setSelected(cs);
    }

    /**
     * Get the currently set search mode. If true is returned, the searching will be case
     * sensitive.
     *
     * @return true: case sensitive search, false: case insensitive
     */
    public boolean isCaseSensitive() {
        return jcbCaseSensitive.isSelected();
    }

    /**
     * Build the pane GUI.
     */
    protected void build() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabelGroup group = null;

        jtfTerm = new JTextField();
        add(group = new JLabelGroup(jtfTerm, L.tr("search.for"), group));

        jcbCaseSensitive = new JCheckBox(L.tr("search.case"));
        add(group = new JLabelGroup(jcbCaseSensitive, "", group));

        jtfReplace = new JTextField();
        add(group = new JLabelGroup(jtfReplace, L.tr("search.replace"), group));

        group.rearrange();
    }

    /**
     * Set up the gui with the registry content.
     */
    protected void setup() {
        String term = registry.getString(JinnRegistryKeys.SEARCH_TERM);
        if (term != null) setSearchTerm(term);
        Boolean val = (Boolean) registry.get(JinnRegistryKeys.SEARCH_CASE_SENSITIVE);
        if (val != null) setCaseSensitive(val.booleanValue());
        String repl = registry.getString(JinnRegistryKeys.SEARCH_REPLACEMENT);
        if (repl != null) setReplacement(repl);
    }

    /**
     * Write the current settings into the registry.
     */
    protected void commit() {
        registry.put(JinnRegistryKeys.SEARCH_TERM, getSearchTerm());
        registry.put(JinnRegistryKeys.SEARCH_CASE_SENSITIVE, new Boolean(isCaseSensitive()));
        registry.put(JinnRegistryKeys.SEARCH_REPLACEMENT, getReplacement());
    }

    /**
     * Show the search dialog.
     *
     * @param registry
     *            Registry to be used
     * @param parent
     *            Parent component
     * @param title
     *            Title of the search dialog
     * @return SEARCH_OPTION, REPLACE_OPTION, REPLACEALL_OPTION, or CLOSED_OPTION
     */
    public static int showSearchDialog(Registry registry, Component parent, String title) {
        Object[] options = { L.tr("search.do.search"), L.tr("search.do.replace"),
                            L.tr("search.do.replaceall"), L.tr("search.do.cancel"), };

        SearchPane pane = new SearchPane(registry);

        int rc = JOptionPane.showOptionDialog(parent, pane, title, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (rc == 3 || rc == JOptionPane.CLOSED_OPTION) { // CANCEL
            rc = CLOSED_OPTION;
        }

        if (rc != CLOSED_OPTION) {
            pane.commit();
        }

        return rc;
    }

    /**
     * This private inner class waits for property changes in the registry, and invokes
     * appropriate methods for updating the representation.
     */
    private class MyPropertyChangeListener implements PropertyChangeListener {

        /**
         * Create a new PropertyChangeListener, and register it with the registry.
         *
         * @param registry
         *            Registry to register with
         */
        public MyPropertyChangeListener(Registry registry) {
            registry.addPropertyChangeListener(JinnRegistryKeys.SEARCH_TERM, this);
            registry.addPropertyChangeListener(JinnRegistryKeys.SEARCH_CASE_SENSITIVE, this);
            registry.addPropertyChangeListener(JinnRegistryKeys.SEARCH_REPLACEMENT, this);
        }

        /**
         * A property was changed in the registry.
         *
         * @param evt
         *            PropertyChangeEvent giving further details
         */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            final String prop = evt.getPropertyName();

            if (JinnRegistryKeys.SEARCH_TERM.equals(prop)) {
                setSearchTerm((String) evt.getNewValue());

            } else if (JinnRegistryKeys.SEARCH_CASE_SENSITIVE.equals(prop)) {
                Boolean val = (Boolean) evt.getNewValue();
                setCaseSensitive(val.booleanValue());

            } else if (JinnRegistryKeys.SEARCH_REPLACEMENT.equals(prop)) {
                setReplacement((String) evt.getNewValue());

            }
        }

    }

}

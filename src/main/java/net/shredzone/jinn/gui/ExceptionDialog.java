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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.shredzone.jinn.i18n.L;

/**
 * Informs the user about an Exception that has occured.
 *
 * @author Richard "Shred" Körber
 */
public class ExceptionDialog {
    private static boolean debugMode = false;

    /**
     * Set the debug mode. If set, a stacktrace will be shown along with the dialog.
     *
     * @param mode
     *            true: debug mode, false: standard mode
     */
    public static void setDebug(boolean mode) {
        debugMode = mode;
    }

    /**
     * Check if the debug mode is enabled.
     *
     * @return true: debug mode, false: standard mode
     */
    public static boolean isDebug() {
        return debugMode;
    }

    /**
     * Show an exception dialog if an exception occured.
     *
     * @param parent
     *            Parent component to be locked during dialog
     * @param op
     *            Operation that was being performed when the exception was raised.
     * @param ex
     *            Exception that was raised.
     */
    public static void show(Component parent, String op, Exception ex) {
        // --- Write a stacktrace to stderr ---
        ex.printStackTrace();

        // --- Create a message string ---
        String message = MessageFormat.format(L.tr("exception.msg"), op, ex.getLocalizedMessage());

        // --- Show the dialog ---
        Object msg = message;

        // --- Assemble the debug message ---
        if (isDebug()) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));

            JTextArea jtaTrace = new JTextArea();
            jtaTrace.setRows(3);
            jtaTrace.setColumns(40);
            jtaTrace.setEditable(false);
            jtaTrace.setText(sw.toString());
            jtaTrace.setCaretPosition(0);

            msg = new Object[] { message, new JScrollPane(jtaTrace) };
        }

        // --- Debug: Show a stacktrace ---
        JOptionPane.showMessageDialog(parent, msg, L.tr("exception.title"), JOptionPane.ERROR_MESSAGE);
    }

}

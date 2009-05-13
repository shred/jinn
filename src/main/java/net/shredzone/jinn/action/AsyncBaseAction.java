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

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 * The base class for asynchronous actions. The action itself will be executed in a
 * separate thread, keeping the GUI thread running. The GUI will be blocked meanwhile,
 * though. This kind is meant for actions which take a rather long time to execute, but do
 * not open a modal dialog.
 * <p>
 * The <code>perform()</code> method is always executed in an own thread, not in the AWT
 * dispatch thread. If you manipulate Swing objects, remember to use
 * <code>EventQueue.invokeLater()</code>.
 * 
 * @author Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: AsyncBaseAction.java 315 2009-05-13 19:32:40Z shred $
 */
public abstract class AsyncBaseAction extends BaseAction {
    private static final long serialVersionUID = 8756416480105882092L;

    /**
     * Create a new, asynchronous Action.
     * 
     * @param name
     *            Action Name
     * @param icon
     *            Action Icon or null
     * @param tip
     *            Action Tooltip or null
     * @param accel
     *            Accelerator Key or null
     */
    public AsyncBaseAction(String name, Icon icon, String tip, KeyStroke accel) {
        super(name, icon, tip, accel);
    }

    /**
     * The action has been invoked from a button, menu item etc.
     * 
     * @param e
     *            ActionEvent
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        // --- Create background thread ---
        Thread thread = new Thread(new Runnable() {
            public void run() {
                Cursor cursor = null;

                // --- Lock the frame ---
                final Frame frame = getFrame(e);
                if (frame != null) {
                    frame.setEnabled(false);
                    cursor = frame.getCursor();
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                }

                // --- Invoke the action ---
                try {
                    perform(e);
                } finally {
                    // --- In any case, unlock the frame ---
                    if (frame != null) {
                        frame.setCursor(cursor);
                        frame.setEnabled(true);
                    }
                }
            }
        });

        // --- Run it ---
        // http://java.sun.com/developer/JDCTechTips/2005/tt0727.html#1
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.start();
    }

}

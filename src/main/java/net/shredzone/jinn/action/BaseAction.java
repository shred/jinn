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
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import net.shredzone.jshred.swing.SwingUtils;

/**
 * The basic class for all Jinn actions. The action is executed synchronously with in the
 * GUI thread, so it is rather meant to be used for actions that either take very little
 * time to execute, or for actions which open a modal dialog.
 * <p>
 * The <code>perform()</code> method is usually executed in the AWT dispatch thread. You
 * may manipulate Swing objects immediately, but changes are reflected only after the
 * <code>perform()</code> method exits.
 * 
 * @author Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: BaseAction.java 315 2009-05-13 19:32:40Z shred $
 */
public abstract class BaseAction extends AbstractAction {
    private static final long serialVersionUID = -8894889050966762864L;

    /**
     * Create a new, synchronous Action. Event processing will be stopped during
     * execution, which also means that there is no GUI update.
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
    public BaseAction(String name, Icon icon, String tip, KeyStroke accel) {
        SwingUtils.setMenuKey(this, name);
        if (icon != null) putValue(Action.SMALL_ICON, icon);
        if (tip != null) putValue(Action.SHORT_DESCRIPTION, tip);
        if (accel != null) putValue(Action.ACCELERATOR_KEY, accel);
    }

    /**
     * The action has been invoked from a button, menu item etc.
     * 
     * @param e
     *            ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        perform(e);
    }

    /**
     * The action implementation itself. Overwrite this method with your desired action.
     * <p>
     * This method is usually performed within the AWT dispatch thread, so you can
     * manipulate Swing objects directly, but changes won't be painted while this method
     * executes.
     * 
     * @param e
     *            ActionEvent, may be null if directly invoked
     */
    public abstract void perform(ActionEvent e);

    /**
     * A convenience call to invoke <code>perform()</code> directly, e.g. from a batch
     * process.
     */
    public void perform() {
        perform(null);
    }

    /**
     * Get the owner Frame of the ActionEvent's source object. If there is no ActionEvent,
     * or if the source was not a Component, null will be returned.
     * 
     * @param e
     *            ActionEvent, may be null
     * @return Frame or null if no Frame could be evaluated
     */
    protected Frame getFrame(ActionEvent e) {
        if (e == null) return null;
        Object src = e.getSource();
        if (src == null || !(src instanceof Component)) return null;
        return SwingUtils.getComponentFrame((Component) src);
    }

}

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

/**
 * This interface documents the keys available in Jinn's Registry.
 * 
 * @author Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: JinnRegistryKeys.java 315 2009-05-13 19:32:40Z shred $
 */
public interface JinnRegistryKeys {

    /*--- Main Frame ------------------------------------------------------ */
    public static final String FRAME_MAIN = "frame.main";

    /*--- Actions --------------------------------------------------------- */

    /** QuitAction: Close the application */
    public static final String ACTION_QUIT = "action.quit";

    /** OpenAction: A new set of translation properties */
    public static final String ACTION_NEW = "action.new";

    /** OpenAction: Read a translation properties file */
    public static final String ACTION_OPEN = "action.open";

    /** MergeAction: Merge a reference properties file */
    public static final String ACTION_MERGE = "action.merge";

    /** SaveAction: Save a translation properties file */
    public static final String ACTION_SAVE = "action.save";

    /** SaveAction: Save a named translation properties file */
    public static final String ACTION_SAVEAS = "action.saveas";

    /** NextAction: Jump to the next untranslated string */
    public static final String ACTION_NEXT = "action.next";

    /** RevertAction: Revert translation to the reference string */
    public static final String ACTION_REVERT = "action.revert";

    /** CleanAction: Clean translation */
    public static final String ACTION_CLEAN = "action.clean";

    /** CutAction: Cut marked text */
    public static final String ACTION_CUT = "action.cut";

    /** CopyAction: Copy marked text */
    public static final String ACTION_COPY = "action.copy";

    /** PasteAction: Paste into text */
    public static final String ACTION_PASTE = "action.paste";

    /** UndoAction: Undo change */
    public static final String ACTION_UNDO = "action.undo";

    /** RedoAction: Redo change */
    public static final String ACTION_REDO = "action.redo";

    /** SearchAction: Search for text */
    public static final String ACTION_SEARCH = "action.search";

    /** SearchNextAction: Search for next occurence */
    public static final String ACTION_SEARCH_NEXT = "action.search.next";

    /** SearchNextAction: Search for previous occurence */
    public static final String ACTION_SEARCH_PREV = "action.search.prev";

    /** GotoAction: Goto a key */
    public static final String ACTION_GOTO = "action.goto";

    /** AboutAction: Show an about dialog */
    public static final String ACTION_ABOUT = "action.about";

    /*--- GUI Objects ----------------------------------------------------- */

    /** JTextComponent containing the current translation */
    public static final String TRANSLATION_TEXT = "gui.translationtext";

    /*--- Files ----------------------------------------------------------- */

    /** File of the translation */
    public static final String FILE_TRANSLATION = "file.translation";

    /** File of the reference */
    public static final String FILE_REFERENCE = "file.reference";

    /** Last opened directory */
    public static final String FILE_LASTDIR = "file.lastdir";

    /** The key String that is currenty selected */
    public static final String CURRENT_KEY = "current.key";

    /*--- Models ---------------------------------------------------------- */

    /** PropertyModel containing the translation */
    public static final String MODEL_TRANSLATION = "model.translation";

    /** PropertyModel containing the reference */
    public static final String MODEL_REFERENCE = "model.reference";

    /** PropertyKeyModel or PropertyKeyRefModel for translation */
    public static final String MODEL_REFERENCE_KEY = "model.referencekey";

    /*--- Search and Replace ---------------------------------------------- */

    /** The current search term */
    public static final String SEARCH_TERM = "search.term";

    /** The current replacement */
    public static final String SEARCH_REPLACEMENT = "search.replace";

    /** Match case? */
    public static final String SEARCH_CASE_SENSITIVE = "search.casesensitive";

    /*--- Flags ----------------------------------------------------------- */

    public static final String FLAG_CHANGED = "flag.changed";

}

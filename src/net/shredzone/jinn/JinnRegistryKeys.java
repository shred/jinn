/*
 * jinn -- A tool for easier translation of properties files
 *
 * Copyright (c) 2005 Richard "Shred" Körber
 *   http://www.shredzone.net/go/jinn
 *
 *-----------------------------------------------------------------------
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is JINN.
 *
 * The Initial Developer of the Original Code is
 * Richard "Shred" Körber.
 * Portions created by the Initial Developer are Copyright (C) 2005
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK *****
 */
 
package net.shredzone.jinn;


/**
 * This interface documents the keys available in Jinn's Registry.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: JinnRegistryKeys.java 68 2006-02-02 12:51:43Z shred $
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
  
}

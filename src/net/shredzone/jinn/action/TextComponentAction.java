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

package net.shredzone.jinn.action;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

import net.shredzone.jinn.i18n.L;
import net.shredzone.jinn.pool.ImgPool;

/**
 * This is the base class for JTextComponent related actions. There are also
 * inner subclasses for the standard actions: cut, copy, paste, undo, redo.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: TextComponentAction.java 285 2009-04-26 22:42:14Z shred $
 */
public abstract class TextComponentAction extends BaseAction implements DocumentListener, UndoableEditListener, CaretListener {
  private static final long serialVersionUID = -8651443172426392912L;
  protected final JTextComponent comp;
  
  /**
   * Create a new JTextComponent Action.
   *
   * @param   comp        JTextComponent this action refers to
   * @param   name        Action Name
   * @param   icon        Action Icon or null
   * @param   tip         Action Tooltip or null
   * @param   accel       Accelerator Key or null
   */
  public TextComponentAction( JTextComponent comp, String name, Icon icon, String tip, KeyStroke accel ) {
    super( name, icon, tip, accel );
    this.comp = comp;
  }
  
  /**
   * This method is invoked whenever the component should update its
   * enabled state.
   */
  protected abstract void updateState();

  /**
   * Helper method, returns true when a text is selected. A text is
   * selected when the selection start and the selection end is different.
   * 
   * @return  true: text is selected
   */
  protected boolean isSelected() {
    final int start = comp.getSelectionStart();
    final int end   = comp.getSelectionEnd();
    return (start != end);
  }
  
  /**
   * Invoke updateState whenever a text change occured. This is either
   * a change in the document or an undoable edit.
   */
  protected void notifyTextChanges() {
    comp.getDocument().addDocumentListener( this );
    comp.getDocument().addUndoableEditListener( this );
  }

  /**
   * Invoke updateState whenever a caret change occured. This is either
   * a change in the caret position or in the selection range.
   */
  protected void notifyCaretChanges() {
    comp.addCaretListener( this );
  }

  /**
   * DocumentListener implementation, do not use.
   * 
   * @param  e      DocumentEvent
   */
  public void insertUpdate( DocumentEvent e ) {
    updateState();
  }
  
  /**
   * DocumentListener implementation, do not use.
   * 
   * @param  e      DocumentEvent
   */
  public void removeUpdate( DocumentEvent e ) {
    updateState();
  }
  
  /**
   * DocumentListener implementation, do not use.
   * 
   * @param  e      DocumentEvent
   */
  public void changedUpdate( DocumentEvent e ) {
    updateState();
  }

  /**
   * UndoableEditListener implementation, do not use.
   * 
   * @param  e      UndoableEditEvent
   */
  public void undoableEditHappened( UndoableEditEvent e ) {
    updateState();
  }
  
  /**
   * CaretListener implementation, do not use.
   * 
   * @param  e      CaretEvent
   */
  public void caretUpdate( CaretEvent e ) {
    updateState();
  }
  
/* ------------------------------------------------------------------------ */
  
  /**
   * Cut operation.
   */
  public static class CutTextAction extends TextComponentAction {
    private static final long serialVersionUID = 1265428506138842235L;

    /**
     * Create a new CutTextAction.
     * 
     * @param   comp        JTextComponent this action refers to
     */
    public CutTextAction( JTextComponent comp ) {
      super (
          comp,
          L.tr( "action.cut" ),
          ImgPool.get( "cut.png" ),
          L.tr( "action.cut.tt" ),
          KeyStroke.getKeyStroke( KeyEvent.VK_X, ActionEvent.CTRL_MASK )
      );
      notifyCaretChanges();
      updateState();
    }

    /**
     * Update the own state.
     */
    @Override
    protected void updateState() {
      setEnabled( isSelected() );
    }
    
    /**
     * Perform the operation.
     * 
     * @param  e      ActionEvent
     */
    @Override
    public void perform( ActionEvent e ) {
      comp.cut();
    }
  }
  
/* ------------------------------------------------------------------------ */
  
  /**
   * Copy operation.
   */
  public static class CopyTextAction extends TextComponentAction {
    private static final long serialVersionUID = -2621402181754230726L;

    /**
     * Create a new CopyTextAction.
     * 
     * @param   comp        JTextComponent this action refers to
     */
    public CopyTextAction( JTextComponent comp ) {
      super (
          comp,
          L.tr( "action.copy" ),
          ImgPool.get( "copy.png" ),
          L.tr( "action.copy.tt" ),
          KeyStroke.getKeyStroke( KeyEvent.VK_C, ActionEvent.CTRL_MASK )
      );
      notifyCaretChanges();
      updateState();
    }
    
    /**
     * Update the own state.
     */
    @Override
    protected void updateState() {
      setEnabled( isSelected() );
    }
    
    /**
     * Perform the operation.
     * 
     * @param  e      ActionEvent
     */
    @Override
    public void perform( ActionEvent e ) {
      comp.copy();
    }
  }

/* ------------------------------------------------------------------------ */
  
  /**
   * Paste operation.
   */
  public static class PasteTextAction extends TextComponentAction {
    private static final long serialVersionUID = 3623311082154332570L;

    /**
     * Create a new PasteTextAction.
     * 
     * @param   comp        JTextComponent this action refers to
     */
    public PasteTextAction( JTextComponent comp ) {
      super (
          comp,
          L.tr( "action.paste" ),
          ImgPool.get( "paste.png" ),
          L.tr( "action.paste.tt" ),
          KeyStroke.getKeyStroke( KeyEvent.VK_V, ActionEvent.CTRL_MASK )
      );
      updateState();
    }
    
    /**
     * Update the own state.
     */
    @Override
    protected void updateState() {
      // paste is always enabled
    }
    
    /**
     * Perform the operation.
     * 
     * @param  e      ActionEvent
     */
    @Override
    public void perform( ActionEvent e ) {
      comp.paste();
    }
  }

/* ------------------------------------------------------------------------ */
  
  /**
   * Undo operation.
   */
  public static class UndoTextAction extends TextComponentAction {
    private static final long serialVersionUID = -7082929743059312417L;
    private final UndoManager undo;
    
    /**
     * Create a new UndoTextAction.
     * 
     * @param   comp        JTextComponent this action refers to
     * @param   undo        UndoManager used
     */
    public UndoTextAction( JTextComponent comp, UndoManager undo ) {
      super (
          comp,
          L.tr( "action.undo" ),
          ImgPool.get( "undo.png" ),
          L.tr( "action.undo.tt" ),
          KeyStroke.getKeyStroke( KeyEvent.VK_Z, ActionEvent.CTRL_MASK )
      );
      this.undo = undo;
      notifyTextChanges();
      updateState();
    }
    
    /**
     * Update the own state.
     */
    @Override
    protected void updateState() {
      setEnabled( undo.canUndo() );
    }
    
    /**
     * Perform the operation.
     * 
     * @param  e      ActionEvent
     */
    @Override
    public void perform( ActionEvent e ) {
      if (undo.canUndo()) {
        undo.undo();
      }else {
        // Nothing to undo
        updateState();
        Toolkit.getDefaultToolkit().beep();
      }
    }
  }

/* ------------------------------------------------------------------------ */
  
  /**
   * Redo operation.
   */
  public static class RedoTextAction extends TextComponentAction {
    private static final long serialVersionUID = 5636347237880346025L;
    private final UndoManager undo;
    
    /**
     * Create a new RedoTextAction.
     * 
     * @param   comp        JTextComponent this action refers to
     * @param   undo        UndoManager used
     */
    public RedoTextAction( JTextComponent comp, UndoManager undo ) {
      super (
          comp,
          L.tr( "action.redo" ),
          ImgPool.get( "redo.png" ),
          L.tr( "action.redo.tt" ),
          KeyStroke.getKeyStroke( KeyEvent.VK_Y, ActionEvent.CTRL_MASK )
      );
      this.undo = undo;
      notifyTextChanges();
      updateState();
    }
    
    /**
     * Update the own state.
     */
    @Override
    protected void updateState() {
      setEnabled( undo.canRedo() );
    }
    
    /**
     * Perform the operation.
     * 
     * @param  e      ActionEvent
     */
    @Override
    public void perform( ActionEvent e ) {
      if (undo.canRedo()) {
        undo.redo();
      }else {
        // Nothing to redo
        updateState();
        Toolkit.getDefaultToolkit().beep();
      }
    }
  }

}


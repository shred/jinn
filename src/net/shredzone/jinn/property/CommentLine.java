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
 
package net.shredzone.jinn.property;

import java.io.IOException;

/**
 * This class represents a single comment line of a properties file.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: CommentLine.java,v 1.4 2005/11/14 09:46:25 shred Exp $
 */
public class CommentLine extends AbstractLine {
  private String comment;
  
  /**
   * Create a new CommentLine with the given comment.
   * 
   * @param line    The comment of this line, without line feeding.
   */
  public CommentLine( String line ) {
    setComment( line );
  }
  
  /**
   * Write the internal state to a content line.
   * 
   * @param     pw     PropertiesWriter to write to
   * @throws IOException    Could not write to stream
   */
  public void write( PropertiesWriter pw ) throws IOException {
    pw.writeEscaped( comment );
    pw.newLine();
  }
  
  /**
   * Set a new comment for this line. This is always a single line,
   * without any line end markers, and always starting with a hash
   * '#' character.
   * 
   * @param   cmt     New comment to be set.
   */
  public void setComment( String cmt ) {
    if (cmt.length() == 0 || (cmt.charAt(0) != '#' && cmt.charAt(0) != '!'))
      throw new IllegalArgumentException( "This is not a valid comment line" );

    final String old = getComment();
    comment = cmt;
    firePropertyChange( "comment", old, cmt );
  }
  
  /**
   * Return the comment stored in this line. This is always a single
   * comment line, without the line end marker.
   * 
   * @return  Comment
   */
  public String getComment() {
    return comment;
  }

}

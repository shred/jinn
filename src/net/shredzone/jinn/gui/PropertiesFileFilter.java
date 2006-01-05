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
 
package net.shredzone.jinn.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import net.shredzone.jinn.i18n.L;


/**
 * A FileFilter that only shows ".properties" files.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: PropertiesFileFilter.java,v 1.1 2005/11/06 13:23:14 shred Exp $
 */
public class PropertiesFileFilter extends FileFilter {

  /**
   * A public default instance for this filter.
   */
  public static final PropertiesFileFilter DEFAULT = new PropertiesFileFilter();
  
  /**
   * Check if this filter accepts the given file. It will be accepted if
   * it is either a directory, or if it is a file that is visible and having
   * a ".properties" suffix.
   * 
   * @param  f      File to check
   * @return true: Accept this file
   */
  public boolean accept( File f ) {
    if( f.isDirectory() ) return true;
    if( f.isHidden() ) return false;
    return f.getName().toLowerCase().endsWith( ".properties" );
  }

  /**
   * Get a human readable description for this filter.
   * 
   * @return  Description
   */
  public String getDescription() {
    return L.tr("pff.desc");
  }
  
}

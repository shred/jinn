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
import java.io.InputStream;
import java.io.Reader;

/**
 * This Reader reads a properties file and converts it into a uniform
 * stream.
 * <p>
 * While reading, unicode escape sequences will be decoded, and extended
 * lines (using a backslash at the end of a line) will be joined into a
 * single line.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: PropertiesReader.java,v 1.3 2005/10/26 23:08:00 shred Exp $
 */
public class PropertiesReader extends Reader {
  private final InputStream in;
  private boolean stage0sto = false;
  private int stage0char;
  private boolean stage1sto = false;
  private int stage1char;
  
  /**
   * Create a new PropertiesReader for the given InputStream
   * 
   * @param in   InputStream to be used
   */
  public PropertiesReader( InputStream in ) {
    this.in = in;
  }

  /*--------------------------------------------------------------------------
   * Part 1: the elementary methods for reading from the InputStream.
   * There are three stages of reading:
   *   0) Read the raw data from InputStream.
   *   1) Read the data from stage 0 and convert all CRLF and CR to LF.
   *   2) Read data from stage 1 and join two lines if a line ends with
   *      backslash and LF.
   * All stages allow to put back one character into the stream for later
   * reading.
   */
  
  /**
   * Stage 0: A simple read/unread mechanism.
   * 
   * @return  A single character, or -1 if EOF was found.
   * @throws IOException if it did not read.
   */
  private int readChar() throws IOException {
    if (stage0sto) {
      stage0sto = false;
      return stage0char;
    }
    return in.read();
  }
  
  /**
   * Stage 0: Unread a single character.
   * 
   * @param ch      Character to unread
   * @throws IOException if it did not unread.
   */
  private void unreadChar( int ch ) throws IOException {
    if (stage0sto) {
      throw new IOException( "Stage 0 character store is full" );
    }
    stage0char = ch;
    stage0sto = true;
  }
  
  /**
   * Stage 1: Read a single character from the raw input stream. Convert
   * all CRLF and CR to LF.
   * 
   * @return  A single character, or -1 if EOF was found.
   * @throws IOException if it did not read.
   */
  private int readUniform() throws IOException {
    if (stage1sto) {
      stage1sto = false;
      return stage1char;
    }
    
    int ch = readChar();
    if (ch == '\r') {
      ch = readChar();
      if (ch != '\n') {
        unreadChar( ch );
        ch = '\n';
      }
    }
    return ch;
  }
  
  /**
   * Stage 1: Unread a single character.
   * 
   * @param ch      Character to unread
   * @throws IOException if it did not unread.
   */
  private void unreadUniform( int ch ) throws IOException {
    if (stage1sto) {
      throw new IOException( "Stage 1 character store is full" );
    }
    stage1char = ch;
    stage1sto = true;
  }
  
  /**
   * Stage 2: Read a single character from stage 1. If it is a backslash
   * followed by a LF, ignore those characters and all subsequent whitespaces.
   * 
   * @return  A single character, or -1 if EOF was found.
   * @throws IOException if it did not read.
   */
  private int readUnwrapped() throws IOException {
    int ch = readUniform();
    if (ch == '\\') {
      int ch2 = readUniform();
      if (ch2 == '\n') {
        // Okay, we've found a line wrap. Now skip all whitespaces
        // and return the first non-whitespace character.
        
        do {
          ch = readUniform();
        }while (ch == ' ' || ch == '\t' || ch == '\f');
        
      }else {
        unreadUniform( ch2 );
      }
    }
    return ch;
  }

  
  /*--------------------------------------------------------------------------
   * Part 2: The methods that are required for the Reader class.
   * Actually, read() does all the work, but read(char[],int,int) is
   * required to be implemented from the Reader class.
   */

  /**
   * Read a single character of the uniformed properties files.
   * These constraints are guaranteed due to the uniformation process:
   * <ul>
   *   <li>Each line always ends with a single LF (no CR or CRLF).
   *   <li>Wrapped lines (with backslash at the end) are unwrapped and
   *     subsequent white spaces are stripped. The resulting stream
   *     will have virtually no line wraps.
   * </ul>
   * 
   * @return  Character read, or -1 if EOF was reached
   * @throws IOException if it could not read
   */
  public int read() throws IOException {
    synchronized( lock ) {
      return readUnwrapped();
      
/* This is a little hack for reading a properties file that is not
 * ISO-8859-1 encoded. By specification, a properties file MUST be
 * ISO-8859-1 encoded, and foreign characters have to be unicode escaped.
 * Anyhow, sometimes you might get a properties file with a non-conformous
 * encoding. With this little hack you are able to read it into Jinn and
 * then save it in a conformous fashion. Maybe I will later add an action
 * for it...
 * 
 *    String charset = "ISO-8859-2";
 *    int ch = readUnwrapped();
 *    if( ch<0 ) return ch;
 *    
 *    byte[] buf = new byte[] {(byte) ch};
 *    String str = new String( buf, charset );
 *    ch = str.charAt( 0 );
 *    
 *    return ch; 
 */
    }
  }

  /**
   * Read into an array.
   * 
   * @param   cbuf      Array to be filled
   * @param   off       Offset in this array
   * @param   len       Number of characters to read
   * @return  Number of characters actually read, or -1 if EOF was detected
   * @throws IOException if it could not read
   */
  public int read( char[] cbuf, int off, int len ) throws IOException {
    synchronized( lock ) {
      int readcnt = 0;
      for( ; readcnt < len; readcnt++ ) {
        final int ch = read();
        if (readcnt == 0 && ch == -1) return -1;  // EOF was already reached
        if (ch == -1) break;                      // EOF was reached while reading
        cbuf[off++] = (char) ch;
      }
      return readcnt;
    }
  }
  
  /**
   * Close the PropertiesReader.
   * 
   * @throws IOException  if it could not close.
   */
  public void close() throws IOException {
    synchronized( lock ) {
      in.close();
    }
  }

  
  /*--------------------------------------------------------------------------
   * Part 3: All the useful functions for reading the properties file into
   * Line objects.
   */
  
  /**
   * Read a line from the properties file, as String. The String will not
   * include the line termination. Leading whitespace is automatically
   * trimmed.
   * 
   * @return  A line from the properties file, or null if EOF was reached.
   * @throws IOException if it could not read.
   */
  public String readString() throws IOException {
    synchronized( lock ) {
      int ch = read();
      if (ch == -1) return null;      // EOF was already reached

      // Trim leading whitespace
      while (ch != -1 && (ch == ' ' || ch == '\t' || ch == '\f')) {
        ch = read();
      }
      
      // Read until EOL
      final StringBuffer buff = new StringBuffer();
      while (ch != -1 && ch != '\n') {
        buff.append( (char) ch );
        ch = read();
      }
      
      return buff.toString();
    }
  }
  
  /**
   * Read a line from the resource stream, and return a Line object
   * that contains the content of the line.
   * 
   * @return  An already decoded Line object, or null if EOF was reached.
   * @throws IOException  if it could not read.
   */
  public Line readLine() throws IOException {
    final String line = readString();
    if (line == null) return null;      // EOF was reached
    
    if (line.length() == 0) {
      // This is an empty line...
      return new EmptyLine();

    }else if ( line.charAt(0) == '#' || line.charAt(0) == '!') {
      // This is a comment line...
      return new CommentLine( unescape( line ) );

    }else {
      // This is a resource line...
      
      // Find the split position
      int split;
      boolean breakChar = false;
      for (split = 0; split < line.length(); split++) {
        final char ch = line.charAt( split );
        if (ch == '\\') {   // Ignore escaped characters
          split++;
          continue;
        }
        if (ch == ':' || ch == '=' || ch == ' ' || ch == '\t' || ch == '\f') {
          if (ch == ':' || ch == '=') {
            breakChar = true;
          }
          break;    // found the split character!
        }
      }
      
      // Key is from beginning to split (exclusive)
      int keypos = split-1;
      
      // Value is after split, but trimming whitespaces
      int valpos = split+1;
      while (valpos < line.length()) {
        final char ch = line.charAt( valpos );
        if (ch == ':' || ch == '=') {
          if (breakChar) {
            // we already had a ':' or '='. This one is part of the value!
            break;
          }else {
            // remember that we found one, but continue
            breakChar = true;
          }
        }else if (ch != ' ' && ch != '\t' && ch != '\f') {
          break;
        }
        valpos++;
      }
      
      if (keypos == 0) {
        // There is no key. Shouldn't happen, though, since it means that
        // we got an empty line, which should have been used earlier...
        throw new IOException( "Key or value missing" );
      }
      
      String key = unescape( line.substring( 0, keypos+1 ) );
      String val = "";
      if (valpos < line.length()) {   // value may be empty!
        val = unescape( line.substring( valpos ) );
      }
      return new PropertyLine( key, val );
    }
  }
 
  
  /*--------------------------------------------------------------------------
   * Part 4: Some nice private helpers.
   */
  
  /**
   * Unescape a String. Unicode escapes and other escaped characters
   * (namely '&#92;t', '&#92;r', '&#92;n', '&#92;f') are converted. For
   * other backslash escapes, the backslash is swallowed, according to
   * java.util.Properties javadoc.
   * 
   * @param  str    String to be converted
   * @return  Unescaped String
   */
  private String unescape( String str ) {
    StringBuffer buff = new StringBuffer( str );
    
    int pos = buff.indexOf( "\\" );
    while (pos>=0) {
      int endpos = pos+1;
      char ch = buff.charAt( endpos );
      switch (ch) {
        case 'u':
          try {
            ch = (char) Integer.parseInt( buff.substring( pos+2, pos+5 ), 16 );
          }catch (NumberFormatException ex) {
            throw new IllegalArgumentException( "Malformed unicode escape sequence" );
          }
          endpos = pos+4;
          break;

        case 't': ch = '\t'; break;
        case 'r': ch = '\r'; break;
        case 'n': ch = '\n'; break;
        case 'f': ch = '\f'; break;
      }
      buff.replace( pos, endpos+1, String.valueOf( (char) ch ) );
      
      pos = buff.indexOf( "\\", pos+1 );
    }
    
    return buff.toString();
  }

}

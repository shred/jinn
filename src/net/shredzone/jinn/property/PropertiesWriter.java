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
import java.io.OutputStream;
import java.io.Writer;

/**
 * This Writer writes a properties file and converts it into a uniform
 * stream. Unicode characters will be escaped. Too long lines will be
 * wrapped properly.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: PropertiesWriter.java 106 2006-08-08 08:06:51Z shred $
 */
public class PropertiesWriter extends Writer {
  private final static char[] HEX = new char[] {
    '0', '1', '2', '3', '4', '5', '6', '7',
    '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
  };

  private final OutputStream out;
  private String lineseparator = System.getProperty( "line.separator" );
  private int    indent        = 24;
  private int    wrapindent    = 4;
  private int    eolmark       = 79;

  /**
   * Create a new PropertiesWriter for the given OutputStream.
   * 
   * @param out     OutputStream to be used
   */
  public PropertiesWriter( OutputStream out ) {
    this.out = out;
  }

  /**
   * Change the line separator. By default, the operating system's line
   * separator will be used.
   * 
   * @param sep   Line separator to be used
   */
  public void setLineSeparator( String sep ) {
    synchronized( lock ) {
      this.lineseparator = sep;
    }
  }
  
  /**
   * Set the column where the equal sign appears on property lines.
   * Defaults to 24. Setting the indent to 0 means that the equal sign
   * will not be aligned at all.
   * 
   * @param pos     Column of the equal sign.
   */
  public void setValueIndent( int pos ) {
    synchronized( lock ) {
      this.indent = pos;
    }
  }
  
  /**
   * Set the column where the text continues after line wrapping.
   * Defaults to 4. Setting the wrap indent to 0 means that after
   * wrapping, the next line will start left aligned to position 0.
   * 
   * @param pos     Column where text continues after wrapping.
   */
  public void setWrapIndent( int pos ) {
    synchronized( lock ) {
      this.wrapindent = pos;
    }
  }
  
  /**
   * Set the maximum line length. Defaults to 79. Setting the maximum line
   * length to 0 will disable wrapping at all.
   * <p>
   * Note that this is just a hint for the Writer. A line may still
   * exceed this limit.
   * 
   * @param max     Proposed maximum line length.
   */
  public void setLineLength( int max ) {
    synchronized( lock ) {
      this.eolmark = max;
    }
  }

  
  /*--------------------------------------------------------------------------
   * Part 1: The methods that are required for the Writer class.
   */
  
  /**
   * Write a char array to the output stream. It is required that all
   * characters in the array are ISO-8859-1 encodable. If a char cannot
   * be encoded, an exception will be thrown.
   * 
   * @param   cbuf    Char array to be sent.
   * @param   off     Offset in that buffer.
   * @param   len     Length of the buffer.
   * @throws IOException if could not write.
   */
  public void write( char[] cbuf, int off, int len ) throws IOException {
    synchronized( lock ) {
      byte[] bbuf = new byte[len];
      
      for (int ix = 0; ix < len; ix++) {
        if (cbuf[ix+off]>255) {
          // Should not happen since we take care for proper escaping.
          // Anyhow, we now got a character we cannot write, so bail out.
          throw new IOException( "Unencoded character in stream: '"+cbuf[ix]+"'" );
        }
        bbuf[ix] = (byte) (cbuf[ix+off] & 0xFF);
      }
      
      out.write( bbuf );
    }
  }

  /**
   * Flushes the stream.
   * 
   * @throws IOException if could not flush.
   */
  public void flush() throws IOException {
    synchronized( lock ) {
      out.flush();
    }
  }

  /**
   * Closes the stream.
   * 
   * @throws IOException if could not close.
   */
  public void close() throws IOException {
    synchronized( lock ) {
      out.close();
    }
  }
  

  /*--------------------------------------------------------------------------
   * Part 3: All the useful functions for writing the Line objects into
   * a properties file.
   */

  /**
   * Write a line separator according to the underlying operating system.
   * 
   * @throws IOException if could not write.
   */
  public void newLine() throws IOException {
    write( lineseparator );
  }

  /**
   * Write an escaped string. All characters that are not ISO-8859-1
   * encodable, will be unicode escaped. Special control characters are
   * specially escaped. Leading spaces will also be escaped. This method
   * is the only proper way to send Strings to the stream that are fully
   * unicode capable.
   * 
   * @param  str      String to be sent
   * @throws IOException if could not write.
   */
  public void writeEscaped( String str ) throws IOException {
    write( escape ( str ) );
  }
  
  /**
   * Write an indented string. It is guaranteed that at least the indention
   * position of a line is reached after this method. It must only be invoked
   * at the beginning of a line, though.
   * 
   * @param str   String to be sent. Will be properly escaped.
   * @return  Number of characters actually written.
   * @throws IOException if could not write.
   */
  public int writeIndented( String str ) throws IOException {
    while (str.length() < indent) {
      str += " ";     //TODO: yep, this is not really performant... ;)
    }
    writeEscaped( str );
    return str.length();
  }

  /**
   * Write a properties key as indented string. It is guaranteed that at
   * least the indention position of a line is reached after this method.
   * This method is only made for keys, and takes care for proper escaping
   * of special chars within the key.
   * 
   * @param str   String to be sent. Will be properly escaped.
   * @return  Number of characters actually written.
   * @throws IOException if could not write.
   */
  public int writeKey( String str ) throws IOException {
    //--- Indent the subsequent text ---
    String spc = "";
    while (str.length() + spc.length() < indent) {
      spc += " ";     // yep, this is not really performant... ;)
    }
    final int actualLength = str.length() + spc.length();

    //--- Replace all spaces within the key ---
    str = str.replaceAll( "\\ ", "\\\\ " ) + spc;

    //--- Take care for comment signs ---
    if (str.charAt(0) == '#' || str.charAt(0) == '!') {
      // Starting like a comment, escape it
      str = '\\'+str;
    }
    
    //--- Replace all separator marks ---
    str = str.replaceAll( "\\:", "\\\\:" );
    str = str.replaceAll( "\\=", "\\\\=" );
    
    //--- Write escaped string ---
    writeEscaped( str );
    return actualLength;
  }
  
  /**
   * Write a line that may be wrapped.
   * <p>
   * If the string fits within the line, regarding the given mark, it will
   * be written immediately.
   * <p>
   * If the string would not fit the line, a line wrap is inserted and the
   * string contents are written to the next line or lines.
   * 
   * @param str   String to be sent. Will be properly escaped.
   * @param mark  Current mark position, i.e. number of characters already
   *    written to the current line.
   * @throws IOException if could not write.
   */
  public void writeWrapable( String str, int mark ) throws IOException {
    if (str.indexOf('\r') >= 0 || str.indexOf('\n') >= 0) {
      // Line contains line breaks
      writeWrapped( str );
    }else if ((eolmark == 0) || (mark + str.length() < eolmark)) {
      // Line ends before EOL marker
      writeEscaped( str );
    }else {
      // Line exceeds EOL marker
      writeWrapped( str );
    }
  }
  
  /**
   * Write a line that will be wrapped.
   * <p>
   * A line wrap marker will be written first.
   * <p>
   * Then the String is split up into its single lines. Each line will
   * be written out separately. If a line does not fit, it will be broken
   * at a space.
   * <p>
   * Note that this method will always wrap, even if the String would fit
   * into the current line.
   * 
   * @param str   String to be written.
   * @throws IOException if could not write.
   */
  public void writeWrapped( String str ) throws IOException {
    synchronized( lock ) {
      if (eolmark == 0) {     // No wrapping
        writeEscaped( str );
        return;
      }
      
      // First split mark is at every line break
      int startpos = 0;
      int endpos = str.indexOf( '\n' );
      while (endpos >= 0) {
        writeWrappedLine( str.substring( startpos, endpos+1 ) );
        startpos = endpos+1;
        endpos = str.indexOf( '\n', startpos );
      }
      if (startpos < str.length()) {
        writeWrappedLine( str.substring( startpos ) );
      }
    }
  }
  
  /**
   * Write a single wrapped line. This line will also be wrapped at spaces
   * if it gets too long.
   * 
   * @param line    Line to be written.
   * @throws IOException if could not write.
   */
  private void writeWrappedLine( String line ) throws IOException {
    synchronized( lock ) {
      // Now split line into several lines breaking at eolmark
      while (line.length() > eolmark-4) {
        write( '\\' );              // Line wrap marker
        newLine();
        writeWrapIndent();          // Indent the new line
  
        int spcpos = line.lastIndexOf( ' ', eolmark-wrapindent );
        if (spcpos < 0) {
          // Oh dear, no space we could line break at...
          spcpos = eolmark-wrapindent;
          if (spcpos >= line.length()) {
            spcpos = line.length()-1;
          }
        }
        writeEscaped( line.substring( 0, spcpos+1) );
        line = line.substring( spcpos+1 );
      }
    
      // Write the remaining part of the line
      if (line.length() > 0) {
        write( '\\' );              // Line wrap marker
        newLine();
        writeWrapIndent();          // Indent the new line
        writeEscaped( line );
      }
    }
  }

  /**
   * Write the indention spaces for wrap indention.
   * 
   * @throws IOException if could not write.
   */
  public void writeWrapIndent() throws IOException {
    synchronized( lock ) {
      for (int cnt = wrapindent; cnt > 0; cnt-- ) {
        write( ' ' );
      }
    }
  }
  

  /**
   * Write a Line object to the Writer stream.
   * 
   * @param l     Line to be written.
   * @throws IOException if it could not write.
   */
  public void writeLine( Line l ) throws IOException {
    synchronized( lock ) {
      l.write( this );
    }
  }
  

  /*--------------------------------------------------------------------------
   * Part 4: Some nice private helpers.
   */
  
  /**
   * Escape a String. Characters not encodable in ISO-8859-1 are unicode
   * escaped. Additionally, some special control characters are replaced
   * by their escape sequence. Optionally leading white spaces can be
   * escaped too.
   * 
   * @param  str    String to be converted
   * @param  esc    true: escape leading white spaces
   * @return  Escaped String
   */
  private String escape( String str ) {
    boolean esc = true;
    StringBuilder buff = new StringBuilder();
    
    final int len = str.length();
    for (int ix = 0; ix < len; ix++) {
      final char ch = str.charAt(ix);
      
      if (esc && ch == ' ') {
        // A blank which is to be escaped
        buff.append( "\\ " );
        
      }else if ((ch >= 32 && ch <= 127) || (ch >= 160 && ch <= 255)) {
        // A displayable ISO-8859-1 character
        buff.append( ch );
        
      }else {
        // Something else...
        switch (ch) {
          case '\t': buff.append( "\\t" ); break;
          case '\r': buff.append( "\\r" ); break;
          case '\n': buff.append( "\\n" ); break;
          case '\f': buff.append( "\\f" ); break;
          default:
            buff.append( "\\u" );
            buff.append( HEX[ ch>>12 & 0xF ] );
            buff.append( HEX[ ch>> 8 & 0xF ] );
            buff.append( HEX[ ch>> 4 & 0xF ] );
            buff.append( HEX[ ch     & 0xF ] );
            break;
        }
      }
      
      esc = false;  // No more space escaping after one char was written
    }
    
    return buff.toString();
  }

}

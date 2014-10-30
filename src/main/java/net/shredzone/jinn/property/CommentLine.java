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
package net.shredzone.jinn.property;

import java.io.IOException;

/**
 * This class represents a single comment line of a properties file.
 *
 * @author Richard "Shred" Körber
 */
public class CommentLine extends AbstractLine {
    private String comment;

    /**
     * Create a new CommentLine with the given comment.
     *
     * @param line
     *            The comment of this line, without line feeding.
     */
    public CommentLine(String line) {
        setComment(line);
    }

    /**
     * Write the internal state to a content line.
     *
     * @param pw
     *            PropertiesWriter to write to
     * @throws IOException
     *             Could not write to stream
     */
    @Override
    public void write(PropertiesWriter pw) throws IOException {
        pw.writeEscaped(comment);
        pw.newLine();
    }

    /**
     * Set a new comment for this line. This is always a single line, without any line end
     * markers, and always starting with a hash '#' character.
     *
     * @param cmt
     *            New comment to be set.
     */
    public void setComment(String cmt) {
        if (cmt.length() == 0 || (cmt.charAt(0) != '#' && cmt.charAt(0) != '!'))
            throw new IllegalArgumentException("This is not a valid comment line");

        final String old = getComment();
        comment = cmt;
        firePropertyChange("comment", old, cmt);
    }

    /**
     * Return the comment stored in this line. This is always a single comment line,
     * without the line end marker.
     *
     * @return Comment
     */
    public String getComment() {
        return comment;
    }

}

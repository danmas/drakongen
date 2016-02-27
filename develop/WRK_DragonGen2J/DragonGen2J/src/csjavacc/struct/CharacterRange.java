/*
 * Copyright © 2002 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * California 95054, U.S.A. All rights reserved.  Sun Microsystems, Inc. has
 * intellectual property rights relating to technology embodied in the product
 * that is described in this document. In particular, and without limitation,
 * these intellectual property rights may include one or more of the U.S.
 * patents listed at http://www.sun.com/patents and one or more additional
 * patents or pending patent applications in the U.S. and in other countries.
 * U.S. Government Rights - Commercial software. Government users are subject
 * to the Sun Microsystems, Inc. standard license agreement and applicable
 * provisions of the FAR and its supplements.  Use is subject to license terms.
 * Sun,  Sun Microsystems,  the Sun logo and  Java are trademarks or registered
 * trademarks of Sun Microsystems, Inc. in the U.S. and other countries.  This
 * product is covered and controlled by U.S. Export Control laws and may be
 * subject to the export or import laws in other countries.  Nuclear, missile,
 * chemical biological weapons or nuclear maritime end uses or end users,
 * whether direct or indirect, are strictly prohibited.  Export or reexport
 * to countries subject to U.S. embargo or to entities identified on U.S.
 * export exclusion lists, including, but not limited to, the denied persons
 * and specially designated nationals lists is strictly prohibited.
 */

package csjavacc.struct;

import csjavacc.parser.CSJavaCCErrors;

/**
 * Describes character range descriptors in a character list.
 */

public class CharacterRange {

  /**
   * The line and column number of the construct that corresponds
   * most closely to this node.
   */
  public int line, column;

  /**
   * The leftmost and the rightmost characters in this character range.
   */
  public char left, right;

  public CharacterRange() { }

  public CharacterRange(char l, char r)
  {
     if (l > r)
        CSJavaCCErrors.semantic_error(this, "Invalid range : \"" + (int)l + "\" - \"" 
              + (int)r + "\". First character shoud be less than or equal to the second one in a range.");

     left = l;
     right = r;
  }
}
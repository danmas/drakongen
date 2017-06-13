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

package csjavacc.parser;


public class Main {

  static void help_message() {
    System.err.println("Usage:");
    System.err.println("    csjavacc option-settings inputfile");
    System.err.println("");
    System.err.println("\"option-settings\" is a sequence of settings separated by spaces.");
    System.err.println("Each option setting must be of one of the following forms:");
    System.err.println("");
    System.err.println("    -optionname=value (e.g., -STATIC=false)");
    System.err.println("    -optionname:value (e.g., -STATIC:false)");
    System.err.println("    -optionname       (equivalent to -optionname=true.  e.g., -STATIC)");
    System.err.println("    -NOoptionname     (equivalent to -optionname=false. e.g., -NOSTATIC)");
    System.err.println("");
    System.err.println("Option settings are not case-sensitive, so one can say \"-nOsTaTiC\" instead");
    System.err.println("of \"-NOSTATIC\".  Option values must be appropriate for the corresponding");
    System.err.println("option, and must be either an integer, a bool, or a string value.");
    System.err.println("");
    System.err.println("The integer valued options are:");
    System.err.println("");
    System.err.println("    LOOKAHEAD              (default 1)");
    System.err.println("    CHOICE_AMBIGUITY_CHECK (default 2)");
    System.err.println("    OTHER_AMBIGUITY_CHECK  (default 1)");
    System.err.println("");
    System.err.println("The bool valued options are:");
    System.err.println("");
    System.err.println("    STATIC                 (default true)");
    System.err.println("    DEBUG_PARSER           (default false)");
    System.err.println("    DEBUG_LOOKAHEAD        (default false)");
    System.err.println("    DEBUG_TOKEN_MANAGER    (default false)");
    System.err.println("    OPTIMIZE_TOKEN_MANAGER (default true)");
    System.err.println("    ERROR_REPORTING        (default true)");
    System.err.println("    CS_UNICODE_ESCAPE    (default false)");
    System.err.println("    UNICODE_INPUT          (default false)");
    System.err.println("    IGNORE_CASE            (default false)");
    System.err.println("    COMMON_TOKEN_ACTION    (default false)");
    System.err.println("    USER_TOKEN_MANAGER     (default false)");
    System.err.println("    USER_CHAR_STREAM       (default false)");
    System.err.println("    BUILD_PARSER           (default true)");
    System.err.println("    BUILD_TOKEN_MANAGER    (default true)");
    System.err.println("    TOKEN_MANAGER_USES_PARSER (default false)");
    System.err.println("    SANITY_CHECK           (default true)");
    System.err.println("    FORCE_LA_CHECK         (default false)");
    System.err.println("    CACHE_TOKENS           (default false)");
    System.err.println("    KEEP_LINE_COLUMN       (default true)");
    System.err.println("");
    System.err.println("The string valued options are:");
    System.err.println("");
    System.err.println("    OUTPUT_DIRECTORY       (default Current Directory)");
    System.err.println("");
    System.err.println("EXAMPLE:");
    System.err.println("    csjavacc -STATIC=false -LOOKAHEAD:2 -debug_parser mygrammar.jj");
    System.err.println("");
  }

  /**
   * A main program that exercises the parser.
   */
  public static void main(String args[]) throws Exception {
    int errorcode = mainProgram(args);
    System.exit(errorcode);
  }

  /**
   * The method to call to exercise the parser from other Java programs.
   * It returns an error code.  See how the main program above uses
   * this method.
   */
  public static int mainProgram(String args[]) throws Exception {

    // Initialize all static state
    reInitAll();

    CSJavaCCGlobals.bannerLine("Parser Generator", "");

    CSJavaCCParser parser = null;
    if (args.length == 0) {
      System.err.println("");
      help_message();
      return 1;
    } else {
      System.err.println("(type \"csjavacc\" with no arguments for help)");
    }

    if (Options.isOption(args[args.length-1])) {
      System.err.println("Last argument \"" + args[args.length-1] + "\" is not a filename.");
      return 1;
    }
    for (int arg = 0; arg < args.length-1; arg++) {
      if (!Options.isOption(args[arg])) {
        System.err.println("Argument \"" + args[arg] + "\" must be an option setting.");
        return 1;
      }
      Options.setCmdLineOption(args[arg]);
    }

    try {
      java.io.File fp = new java.io.File(args[args.length-1]);
      if (!fp.exists()) {
         System.err.println("File " + args[args.length-1] + " not found.");
         return 1;
      }
      if (fp.isDirectory()) {
         System.err.println(args[args.length-1] + " is a directory. Please use a valid file name.");
         return 1;
      }
      parser = new CSJavaCCParser(new java.io.FileReader(args[args.length-1]));
    } catch (NullPointerException ne) { // Should never happen
    } catch (SecurityException se) {
      System.err.println("Security voilation while trying to open " + args[args.length-1]);
      return 1;
    } catch (java.io.FileNotFoundException e) {
      System.err.println("File " + args[args.length-1] + " not found.");
      return 1;
    }

    try {
      System.err.println("Reading from file " + args[args.length-1] + " . . .");
      CSJavaCCGlobals.fileName = CSJavaCCGlobals.origFileName = args[args.length-1];
      CSJavaCCGlobals.jjtreeGenerated = CSJavaCCGlobals.isGeneratedBy("JJTree", args[args.length-1]);
      CSJavaCCGlobals.jjcovGenerated = CSJavaCCGlobals.isGeneratedBy("JJCov", args[args.length-1]);
      CSJavaCCGlobals.toolNames = CSJavaCCGlobals.getToolNames(args[args.length-1]);
      parser.csjavacc_input();
      CSJavaCCGlobals.createOutputDir(Options.getOutputDirectory());

      if (Options.getUnicodeInput())
      {
         NfaState.unicodeWarningGiven = true;
         System.err.println("Note: UNICODE_INPUT option is specified. " +
              "Please make sure you create the parser/lexer using a Reader with the correct character encoding.");
      }

      Semanticize.start();
      ParseGen.start();
      LexGen.start();
      OtherFilesGen.start();

      if ((CSJavaCCErrors.get_error_count() == 0) && (Options.getBuildParser() || Options.getBuildTokenManager())) {
        if (CSJavaCCErrors.get_warning_count() == 0) {
          System.err.println("Parser generated successfully.");
        } else {
          System.err.println("Parser generated with 0 errors and "
                             + CSJavaCCErrors.get_warning_count() + " warnings.");
        }
        return 0;
      } else {
        System.err.println("Detected " + CSJavaCCErrors.get_error_count() + " errors and "
                           + CSJavaCCErrors.get_warning_count() + " warnings.");
        return (CSJavaCCErrors.get_error_count()==0)?0:1;
      }
    } catch (MetaParseException e) {
      System.err.println("Detected " + CSJavaCCErrors.get_error_count() + " errors and "
                         + CSJavaCCErrors.get_warning_count() + " warnings.");
      return 1;
    } catch (ParseException e) {
      System.err.println(e.toString());
      System.err.println("Detected " + (CSJavaCCErrors.get_error_count()+1) + " errors and "
                         + CSJavaCCErrors.get_warning_count() + " warnings.");
      return 1;
    }
  }

   public static void reInitAll()
   {
      csjavacc.struct.Expansion.reInit();
      csjavacc.parser.CSJavaCCErrors.reInit();
      csjavacc.parser.CSJavaCCGlobals.reInit();
      Options.init();
      csjavacc.parser.CSJavaCCParserInternals.reInit();
      csjavacc.parser.RStringLiteral.reInit();
      csjavacc.parser.CSJavaFiles.reInit();
      csjavacc.parser.LexGen.reInit();
      csjavacc.parser.NfaState.reInit();
      csjavacc.struct.MatchInfo.reInit();
      csjavacc.parser.LookaheadWalk.reInit();
      csjavacc.parser.Semanticize.reInit();
      csjavacc.parser.ParseGen.reInit();
      csjavacc.parser.OtherFilesGen.reInit();
      csjavacc.parser.ParseEngine.reInit();
   }

}

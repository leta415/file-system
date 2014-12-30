package src;

import java.util.ArrayDeque;
import java.util.Deque;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class that imitates Unix commands for a file system. Commands implemented: ls, pwd, cd, cd .., cd
 * ., cd <path>, mkdir, rmdir, exit
 * @author Leta
 * 
 */
public class CommandParser {
   private final static Item rootDirectory = new Directory("home", null);
   private final static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));;
   private final static BufferedOutputStream bs = new BufferedOutputStream(System.out);

   private static Item currentDirectory;

   /**
    * Inner class to store a directory and a file name from a parsed path. Used as return type for
    * parsePath().
    */
   private static class ParsedPathInfo {
      Item directory;
      String fileName;

      ParsedPathInfo(Item directory, String fileName) {
         this.directory = directory;
         this.fileName = fileName;
      }
   }

   /**
    * Parses the entire command string by splitting it by white space, ideally separating the
    * command and the command argument(s).
    * @param input Command string to be parsed.
    * @throws IOException
    */
   private static void parseInput(String input) throws IOException {
      // Trim the string of white space and separate the command and command arg(s)
      input.trim();
      String[] splitInput = input.split("\\s+");
      if (splitInput.length == 0) {
         return;
      }

      // Determine which command was used and perform actions accordingly
      switch (splitInput[0]) {
      case "cd":
         cd(splitInput.length == 1 ? "" : splitInput[1]);
         break;
      case "pwd":
         pwd();
         break;
      case "mkdir":
         mkdir(splitInput[1]);
         break;
      case "rmdir":
         rmdir(splitInput[1]);
         break;
      case "ls":
         ls();
         break;
      default:
         bs.write("Invalid command.\n".getBytes());
         break;
      }
   }

   /**
    * Implementation of the 'change directory' command.
    * @param arg The command argument(s) for the change directory command
    * @throws IOException
    */
   private static void cd(String path) throws IOException {
      path.trim();

      if (path.isEmpty()) {
         currentDirectory = rootDirectory;
         return;
      }

      ParsedPathInfo parsedInfo = parsePath(path, true, "Invalid path.\n");
      if (parsedInfo != null) {
         currentDirectory = parsedInfo.directory;
      }
   }

   /**
    * Helper method to parse a path and return reference to a ParsedPathInfo instance. Precondition:
    * Variable 'path' is not empty.
    * @param path Path to parse.
    * @param last True if looking for the last directory in the path. False if looking for second to
    *           last directory, such as for mkdir().
    * @return Reference to the Directory needed. If isMkdir is true, returns newly created
    *         Directory, but null if already exists.
    * @throws IOException
    */
   private static ParsedPathInfo parsePath(String path, boolean last, String errorMsg)
         throws IOException {
      Item newDirectory = currentDirectory;

      // if (firstDir == '/') {
      // bs.write("Invalid file path.".getBytes());
      // bs.flush();
      // return;
      // }

      String[] argSplit = path.split("/");

      int i = 0;

      // Check if first directory is '~'
      if (argSplit[0].equals("~")) {
         newDirectory = rootDirectory;
         i = 1;
      }

      for (; i < (last ? argSplit.length : argSplit.length - 1); i++) {
         // System.out.println("Current dir: " + argSplit[i] + "\n");
         switch (argSplit[i]) {
         case ".":
            break;
         case "..":
            if (newDirectory.parentDir != null) {
               newDirectory = newDirectory.parentDir;
            }
            break;
         default:
            Item child;
            if ((child = ((Directory) newDirectory).findChild(argSplit[i])) == null) {
               bs.write(errorMsg.getBytes());
               return null;
            } else {
               newDirectory = child;
            }
         }
      }

      path = argSplit[argSplit.length - 1];

      return new ParsedPathInfo(newDirectory, path);
   }

   /**
    * Implementation of 'print working directory' command.
    * @throws IOException
    */
   private static void pwd() throws IOException {
      Deque<String> pathDirs = new ArrayDeque<String>();
      Item dir = currentDirectory;
      while (dir != null) {
         pathDirs.push(dir.name);
         dir = dir.parentDir;
      }

      while (!pathDirs.isEmpty()) {
         bs.write(pathDirs.pop().getBytes());
         if (!pathDirs.isEmpty()) {
            bs.write("/".getBytes());
         } else {
            bs.write("\n".getBytes());
         }
      }
   }

   /**
    * Implementation of 'make directory' command.
    * @param arg Command argument(s)
    * @throws IOException
    */
   private static void mkdir(String arg) throws IOException {
      // TODO validate not empty and not null
      arg.trim();
      String[] newDirectoryPaths = arg.split("\\s+");
      for (int i = 0; i < newDirectoryPaths.length; i++) {
         String newDirectoryPath = newDirectoryPaths[i];
         ParsedPathInfo parsedInfo = parsePath(newDirectoryPath, false,
               "Invalid path. Could not make new directory '" + newDirectoryPath + "'.\n");
         if (parsedInfo == null) {
            continue;
         }
         boolean added = ((Directory) parsedInfo.directory).addItem(new Directory(
               parsedInfo.fileName, parsedInfo.directory));
         if (!added) {
            bs.write("File exists.\n".getBytes());
         }
      }
   }

   /**
    * Implementation of the 'remove directory' command.
    * @param arg Command argument(s).
    * @throws IOException
    */
   private static void rmdir(String arg) throws IOException {
      // TODO validate not empty and not null
      arg.trim();
      String[] removeDirectories = arg.split("\\s+");
      for (int i = 0; i < removeDirectories.length; i++) {
         ParsedPathInfo parsedInfo = parsePath(removeDirectories[i], true,
               "Invalid path. Could not remove directory '" + removeDirectories[i] + "'.\n");
         if (parsedInfo == null) {
            continue;
         }
         boolean removed = ((Directory) parsedInfo.directory.parentDir)
               .removeItem(parsedInfo.fileName);
         if (!removed) {
            bs.write(("Directory not empty: " + removeDirectories[i] + ".\n").getBytes());
         }
      }
   }

   /**
    * Implementation for 'list' command. Prints items in the current directory in alphabetical
    * order.
    * @throws IOException
    */
   private static void ls() throws IOException {
      bs.write(currentDirectory.toString().getBytes());
   }

   public static void main(String[] args) throws IOException {
      currentDirectory = rootDirectory;

      String input;
      while (!((input = br.readLine()) == null || input.equalsIgnoreCase("exit"))) {
         parseInput(input);
         bs.flush();
      }

      br.close();
      bs.flush();
      bs.close();
   }
}
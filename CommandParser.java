import java.util.ArrayDeque;
import java.util.Deque;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class that imitates Unix commands for a file system.
 * ls, pwd, cd, cd .., cd ., cd <dir>, cp, mv, rm, mkdir, rmdir, exit  
 * @author Leta
 *
 */
public class CommandParser {
   private static BufferedReader br;
   private static BufferedOutputStream bs;
   private static Item rootDirectory;
   private static Item currentDirectory;
   
   /**
    * Parses the entire command string by splitting it by white space, ideally separating the
    * command and the command argument(s).
    * @param input Command string to be parsed.
    * @throws IOException
    */
   private static void parseInput (String input) throws IOException {
      //Trim the string of white space and separate the command and command arg(s)
      input.trim();
      String[] splitInput = input.split("\\s+");
      if (splitInput.length == 0) {
         return;
      }
      
      //Determine which command was used and perform actions accordingly
      switch (splitInput[0]) {
         case "cd" : cd(splitInput.length == 1 ? "" : splitInput[1]);
                     break;
         case "pwd" : pwd();
                      break;
         case "mkdir" : mkdir(splitInput[1]);
                        break;
         default   : bs.write("Invalid command.".getBytes());
                     bs.flush();
                     break;
      }
   }
   
   /**
    * Implementation of the 'change directory' command.
    * @param arg The command argument(s) for the change directory command
    * @throws IOException
    */
   private static void cd (String arg) throws IOException {
      arg.trim();
      
      if (arg.isEmpty()) {
         currentDirectory = rootDirectory;
         return;
      }
      
      char firstDir = arg.charAt(0);
      
//      if (firstDir == '/') {
//         bs.write("Invalid file path.".getBytes());
//         bs.flush();
//         return;
//      }
      
      if (firstDir == '~') {
         currentDirectory = rootDirectory;
         if (arg.length() > 1) {
            arg = arg.substring(1);
         }
      }
            
      String[] argSplit = arg.split("/");
      
      for (String dir : argSplit) {
         switch (dir) {
            case "." : break;
            case ".." : currentDirectory = currentDirectory.parentDir;
                        break;
            default : if (!findItem(dir)) {
                         bs.write("Invalid file path.".getBytes());
                         bs.flush();
                         return;
                      };
         }
      }
      
   }
   
   /**
    * Implementation of 'print working directory' command. 
    * @throws IOException
    */
   private static void pwd () throws IOException {
      Deque<String> pathDirs = new ArrayDeque<String>();
      Item dir = currentDirectory;
      while (dir != null) {
         pathDirs.push(dir.name);
         dir = dir.parentDir;
      }
      
      while (!pathDirs.isEmpty()) {
         System.out.print(pathDirs.pop());
         if (!pathDirs.isEmpty()) {
            bs.write("/".getBytes());
         } else {
            bs.write("\n".getBytes());
         }
      }
      
      bs.flush();
   }
   
   private static void mkdir (String arg) {
      arg.trim();
      
   }
   
   private static boolean findItem(String dirName) {
      Item item = ((Directory)currentDirectory).fileMap.get(dirName);
      
      if (!item.isDir) {
         return false;
      }
      
      currentDirectory = item;
      
      return true;
   }
   
	public static void main (String[] args) throws IOException {
	   br = new BufferedReader(new InputStreamReader(System.in));
      bs = new BufferedOutputStream(System.out);
      
      rootDirectory = new Directory("home", null);
      currentDirectory = rootDirectory;
      
      String input;
      while ((input=br.readLine()) != null) {
         parseInput(input);
      }
      
      
      br.close();
      bs.flush();
      bs.close();
	}
}
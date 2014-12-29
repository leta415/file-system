package src;
/**
 * Class that extends Item. Represents a file type in a file system.
 * @author Leta
 */
public class File extends Item {
   /**
    * Constructor for this class. Must take the name of the file and the parent directory of the
    * file.
    * @param name Name of this file
    * @param parentDir Parent directory of this file
    */
   public File(String name, Item parentDir) {
      this.name = name;
      this.parentDir = parentDir;
   }
}

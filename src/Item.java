package src;
/**
 * Abstract class to represent an item inside a directory. Classes that extend are different types
 * of items, such as the existing File and Directory classes.
 * @author Leta
 */
abstract class Item {
   protected String name;  //Name of the item that will show up when 'ls' command is used
   protected Item parentDir;  //The parent directory of this Item
   protected boolean isDirectory = false; //True if this Item is a directory
//   protected boolean isHidden;

   /* Getters */
   public String getName() {
      return name;
   }

   public Item getParentDir() {
      return parentDir;
   }

   public boolean getIsDir() {
      return isDirectory;
   }
}

package src;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class that extends Item. This class represents a directory type of item in a file system.
 * @author Leta
 */
public class Directory extends Item {
   /* Map of all Items in this Directory */
   private Map<String, Item> fileMap = new HashMap<String, Item>();

   /* Storage of all Item names in this directory in alphabetical order */
   private Set<String> itemNamesABCOrder = new TreeSet<String>();

   /**
    * Constructor for this class. Must pass in a name and parent directory.
    * @param name Name of this Directory
    * @param parentDir Parent directory of this one
    */
   public Directory(String name, Item parentDir) {
      this.name = name;
      this.parentDir = parentDir;
      this.isDirectory = true;
   }

   /**
    * Find a child Item in this Directory.
    * @param childToFind The name of the Item to find inside this Directory.
    * @return The reference to the Item found. Null if not found.
    */
   public Item findChild(String childToFind) {
      return fileMap.get(childToFind);
   }

   /**
    * Add an Item inside this Directory.
    * @param item Item to add.
    */
   public boolean addItem(Item item) {
      if (fileMap.containsKey(item.name)) {
         return false;
      }
      fileMap.put(item.name, item);
      itemNamesABCOrder.add(item.name);
      return true;
   }

   /**
    * Remove an Item from this Directory
    * @param name Finds the Item by name and removes it from where ever stored.
    */
   public boolean removeItem(String name) {
      Item i = fileMap.get(name);
      if (i == null || !((Directory)i).fileMap.isEmpty()) {
         return false;
      }
      
      itemNamesABCOrder.remove(name);
      return true;
   }
   
   public void removeItem(Item item) {
      fileMap.remove(item.name);
      itemNamesABCOrder.remove(item.name);
   }

   /**
    * Returns an alphabetically ordered list of Item names delimited by new line.
    */
   public String toString() {
      String str = "";
      Iterator<String> iter = itemNamesABCOrder.iterator();
      while (iter.hasNext()) {
         str += iter.next() + "\n";
      }
      return str;
   }
}

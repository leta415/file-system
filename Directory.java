import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Directory extends Item {
   private Map<String, Item> fileMap = new HashMap<String, Item>();
   private Set<String> fileNamesABCOrder = new TreeSet<String>();
   
   public Directory(String name, Item parentDir) {
      this.name = name;
      this.parentDir = parentDir;
      this.isDir = true;
   }
   
   public Item findChild(String childToFind) {
      return fileMap.get(childToFind);
   }
   
   public void addItem(Item item) {
      fileMap.put(item.name, item);
      fileNamesABCOrder.add(item.name);
   }
   public void removeItem(String name) {
      fileMap.remove(name);
      fileNamesABCOrder.remove(name);
   }
   public String toString() {
      String str = "";
      Iterator<String> iter = fileNamesABCOrder.iterator();
      while (iter.hasNext()) {
         str += iter.next() + "\n";
      }
      return str;
   }
   
   public static void main (String[] args) {
      Directory dir = new Directory("dir", null);
      dir.addItem(new File("file1", dir));
//      dir.addItem(new File("file2", dir));
      System.out.println(dir.toString());
   }
}

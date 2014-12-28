
abstract class Item {
   protected String name;
   protected Item parentDir;
   protected boolean isDir = false;
   
   /* Getters */
   public String getName() {
      return name;
   }
   public Item getParentDir() {
      return parentDir;
   }
   public boolean getIsDir() {
      return isDir;
   }
}

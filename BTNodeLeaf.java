import java.util.ArrayList;

public class BTNodeLeaf extends BTNode
{
   public ArrayList<Integer> keyCounts;   //instead of duplicates we increast keycounts
   public BTNodeLeaf nextLeaf;   //points to the leaf next to it

   /*
   store up to 3 values "keys[]"
    */
   public BTNodeLeaf()
   {
      this.keys = new ArrayList<String>();
      this.keyCounts = new ArrayList<Integer>();
   }
   
   public void insert(String word, BPlusTree tree) {
      if(this.keys == null) {
         this.keys.add(word);
         this.keyCounts.add(1);
      }else{
         if (this.keys.contains(word)) {
            incrementDupe(word);
         } else {
            if (this.keys.size() == SIZE) {         //split to new node
               BTNodeLeaf newLeaf = new BTNodeLeaf();
               if (word.compareTo(keys.get(1)) < 0) {         //word is staying in old node
                  newLeaf.keys.add(0, this.keys.get(1));
                  newLeaf.keyCounts.add(0, this.keyCounts.get(1));
                  newLeaf.keys.add(1, this.keys.get(2));
                  newLeaf.keyCounts.add(1, this.keyCounts.get(2));
                  this.keys.remove(2);
                  this.keyCounts.remove(2);
                  this.keys.remove(1);
                  this.keyCounts.remove(1);
                  if(word.compareTo(keys.get(0)) < 0) {
                     this.keys.add(0,word);
                     this.keyCounts.add(0,1);
                  }else{   //word is after 1st word
                     this.keys.add(1, word);
                     this.keyCounts.add(1, 1);
                  }
               } else {                                       //word is placed in a new node
                  if (word.compareTo(this.keys.get(2)) < 0) {  //word will be first position of newLeaf
                     newLeaf.keys.add(0, word);
                     newLeaf.keyCounts.add(0, 1);
                     newLeaf.keys.add(1, this.keys.get(2));
                     newLeaf.keyCounts.add(1, this.keyCounts.get(2));
                     this.keys.remove(2);
                     this.keyCounts.remove(2);
                  } else { //word is 2nd position of new leaf
                     newLeaf.keys.add(this.keys.get(2));
                     this.keys.remove(2);
                     newLeaf.keyCounts.add(this.keyCounts.get(2));
                     this.keyCounts.remove(2);
                     newLeaf.keys.add(1, word);
                     newLeaf.keyCounts.add(1, 1);
                  }
               }
               newLeaf.parent = this.parent;
               newLeaf.nextLeaf = this.nextLeaf;
               this.nextLeaf = newLeaf;
               //update parent pointer with new leaf
               if (this.parent == null) {  //parent is null | After: [NL[0], , ]
                  this.parent = new BTNodeInternal();
                  this.parent.children.add(this);
                  this.parent.keys.add(0, newLeaf.keys.get(0));
                  this.parent.children.add(1, newLeaf);
                  newLeaf.parent = this.parent;
               } else {

                  if (this.parent.keys.size() == SIZE) {
                     this.parent.insertFull(newLeaf.keys.get(0), newLeaf, tree);
                  } else {
                     this.parent.insertNotFull(newLeaf.keys.get(0), newLeaf, tree);
                  }

               }
            } else {
               if(!this.keys.isEmpty()) {
                  if(this.keys.size() == 2) {
                     if (word.compareTo(keys.get(0)) < 0) {          //word is before index 1, after 0 [a,x,b]
                        keys.add(0, word);
                        keyCounts.add(0, 1);
                     } else if (word.compareTo(keys.get(1)) < 0) {                                       //word is at end of list
                        keys.add(1, word);
                        keyCounts.add(1, 1);
                     } else {
                        keys.add(2, word);
                        keyCounts.add(2, 1);
                     }
                  }else{
                     if(word.compareTo(keys.get(0)) < 0){
                        this.keys.add(0,word);
                        this.keyCounts.add(0,1);
                     }else{
                        this.keys.add(1,word);
                        this.keyCounts.add(1,1);
                     }
                  }
               }else{
                  this.keys.add(word);
                  this.keyCounts.add(1);
               }
            }
         }
      }
   }

   public void incrementDupe(String word){
      int index = keys.indexOf(word);
      int cnt = keyCounts.get(index);
      keyCounts.remove(index);
      keyCounts.add(index,cnt+1);
      //keyCounts.(index,keyCounts.get(index) + 1);//not add new index, replace current index value

   }
   
   public void printLeavesInSequence()
   {
      for(int i=0; i < keys.size(); i++){
         System.out.print(keys.get(i) + " ");
      }
      
   }
   
   public void printStructureWKeys(int space,String mark)
   {

      String s = "";
      for(int l = 0; l < space; l++){
         s+="    ";
      }
      for(int i = this.keys.size(); i > 0; i--){
         System.out.println(s + mark + " " + keys.get(i-1) + " " + keyCounts.get(i-1));
      }


   }

   public int findInsertIndex(String word) {
      return 0;
   }

   public Boolean rangeSearch(String startWord, String endWord) {
      boolean searchResult = false;
      int startIndex = 0;
      BTNode currentNode = this;
      if (startWord.compareTo(this.keys.get(0)) < 0) {
         startIndex = 0;
      }else{
         int i = 0;
         while(i < this.keys.size() && startWord.compareTo(currentNode.keys.get(i)) > 0){
            i++;
            startIndex = i;
         }
         if(i == this.keys.size()){
          startIndex = 0;
          currentNode = this.nextLeaf;
         }
      }
      while((startIndex < currentNode.keys.size()) && (currentNode.keys.get(startIndex).compareTo(endWord) <= 0)){
         System.out.println(currentNode.keys.get(startIndex));
         searchResult = true;
         startIndex++;

         if(startIndex == currentNode.keys.size() && ((BTNodeLeaf) currentNode).nextLeaf != null){
            currentNode = ((BTNodeLeaf) currentNode).nextLeaf;
            startIndex = 0;
         }
      }
      return searchResult;
   }
   
   public Boolean searchWord(String word){
      if (this.keys.contains(word)) {
         System.out.println("Contained in: " + this);
         return true;
      }
      System.out.println("Not in Tree");
      return false;
   }
}
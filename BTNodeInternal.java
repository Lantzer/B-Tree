import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BTNodeInternal extends BTNode {
    public ArrayList<BTNode> children;//can point to 4

    public BTNodeInternal() {
        this.children = new ArrayList<BTNode>();
        this.keys = new ArrayList<String>();
    }

    public void insert(String key, BPlusTree tree) {
   /*
   search keys, see which key it is >=, and call insert on that node.

    */
        int i = 0; //iterator
        while ((i < this.keys.size()) && (key.compareTo(this.keys.get(i)) >= 0)) {
            i++;
        }
        //stops when i is the child path to take
        children.get(i).insert(key, tree);
    }

    public void insertFull(String key, BTNode node, BPlusTree tree) {
        //split
        BTNodeInternal newParent = new BTNodeInternal();
        if (key.compareTo(this.keys.get(0)) < 0) { //split child(0)
            newParent.keys.add(this.keys.get(2));
            this.keys.remove(2);
            newParent.children.add(this.children.get(3));
            this.children.get(3).parent = newParent;
            this.children.remove(3);
            newParent.children.add(this.children.get(2));
            this.children.remove(2);
            this.keys.add(0, key);
            this.children.add(1, node);
            node.parent = newParent;
            newParent.parent = this.parent;
            if (this.parent != null) {
                if (this.parent.keys.size() == SIZE) {
                    this.parent.insertFull(this.keys.get(2), newParent, tree);
                } else {
                    this.parent.insertNotFull(this.keys.get(2), newParent, tree);
                }
            } else {
                this.parent = new BTNodeInternal();
                this.parent.children.add(this);
                this.parent.keys.add(this.keys.get(2));
                this.parent.children.add(1, newParent);
                newParent.parent = this.parent;
            }
            this.keys.remove(2);
        } else if ((key.compareTo(this.keys.get(0)) > 0) && (key.compareTo(this.keys.get(1)) < 0)) {   //split child(1)
            newParent.keys.add(this.keys.get(2));
            this.keys.remove(2);
            newParent.children.add(this.children.get(3));
            this.children.get(3).parent = newParent;
            this.children.remove(3);
            newParent.children.add(0, this.children.get(2));
            this.children.remove(2);
            this.keys.add(1, key);
            this.children.add(2, node);
            node.parent = newParent;
            newParent.parent = this.parent;
            if (this.parent != null) {
                if (this.parent.keys.size() == SIZE) {
                    this.parent.insertFull(this.keys.get(2), newParent, tree);
                } else {
                    this.parent.insertNotFull(this.keys.get(2), newParent, tree);
                }
            } else {
                this.parent = new BTNodeInternal();
                this.parent.children.add(this);
                this.parent.keys.add(this.keys.get(2));
                this.parent.children.add(1, newParent);
                newParent.parent = this.parent;
            }
            this.keys.remove(2);
        } else if ((key.compareTo(this.keys.get(1)) > 0) && (key.compareTo(this.keys.get(2)) < 0)) {   //split child(2)
            newParent.keys.add(this.keys.get(2));
            this.keys.remove(2);
            newParent.children.add(this.children.get(3));
            this.children.get(3).parent = newParent;
            this.children.remove(3);
            newParent.children.add(0, node);
            node.parent = newParent;
            newParent.parent = this.parent;
            if (this.parent != null) {
                if (this.parent.keys.size() == SIZE) {
                    this.parent.insertFull(key, newParent, tree);
                } else {
                    this.parent.insertNotFull(key, newParent, tree);  //check
                }
            } else {
                this.parent = new BTNodeInternal();
                this.parent.children.add(this);
                this.parent.keys.add(key);
                this.parent.children.add(1, newParent);
                newParent.parent = this.parent;
            }
        } else {     //split child(3)
            newParent.keys.add(key);
            newParent.children.add(node);
            node.parent = newParent;
            newParent.children.add(0, this.children.get(3));
            this.children.get(3).parent = newParent;
            this.children.remove(3);
            newParent.parent = this.parent;
            if (this.parent != null) {
                if (this.parent.keys.size() == SIZE) {
                    this.parent.insertFull(this.keys.get(2), newParent, tree);  //*need to remove 170//
                } else {
                    this.parent.insertNotFull(this.keys.get(2), newParent, tree);
                }
                this.keys.remove(2);
            } else {
                this.parent = new BTNodeInternal();
                this.parent.keys.add(this.keys.get(2));
                this.keys.remove(2);
                this.parent.children.add(newParent);
                newParent.parent = this.parent;
                this.parent.children.add(0, this);
            }
        }
    }

    public void insertNotFull(String key, BTNode node, BPlusTree tree) {
        if (key.compareTo(this.keys.get(0)) < 0) {  //split children(0)
            this.keys.add(0, key);
            this.children.add(1, node);//key is less than first key
        } else if (key.compareTo(this.keys.get(0)) > 0) { //we split child(1) or (2)
            if (this.keys.size() == 2) { //checking if there is a kids(2)
                if (key.compareTo(this.keys.get(1)) > 0) { //we split child(2)
                    this.keys.add(2, key);
                    this.children.add(3, node);
                } else {
                    this.keys.add(1, key);
                    this.children.add(2, node);
                }
            } else {//we split child(1)
                this.keys.add(1, key);
                this.children.add(2, node);
            }
            node.parent = this;
        }
    }


    public void printLeavesInSequence() {
        for (int i = 0; i < this.children.size(); i++) {
            this.children.get(i).printLeavesInSequence();
        }
    }

    public void printStructureWKeys(int space, String mark) {

        LinkedList<Integer> queue = new LinkedList<Integer>();
        for (int i = this.keys.size(); i > 0; i--) {
            queue.add(this.keys.indexOf(this.keys.get(i - 1)));   //adds starting from right most to left strings in keys
        }

        String s = "";
        for (int l = 0; l < space; l++) {
            s += "    ";
        }

        while (!queue.isEmpty()) {
            if (queue.peek() + 1 == this.keys.size()) {
                this.children.get(queue.peek() + 1).printStructureWKeys(space + 1, "/");
            } else {
                this.children.get(queue.peek() + 1).printStructureWKeys(space + 1, "-");
            }
            System.out.println(s + mark + " " + this.keys.get(queue.pop()));
        }
        this.children.get(0).printStructureWKeys(space + 1, "\\");

    }

    public int findInsertIndex(String word) {
        return 0;
    }

    public Boolean rangeSearch(String startWord, String endWord) {
        int childIndex = 0;
        while (childIndex < keys.size() && startWord.compareTo(keys.get(childIndex)) > 0) {
            childIndex++;
        }
        if (childIndex == 0 && startWord.equals(keys.get(0))) {
            childIndex = 1;
        }
        return children.get(childIndex).rangeSearch(startWord, endWord);
    }

    public Boolean searchWord(String word) {
        if (word.compareTo(this.keys.get(0)) < 0) {
            this.children.get(0).searchWord(word);
        } else if (word.compareTo(this.keys.get(0)) >= 0) {
            if (this.keys.size() == 2) {
                if (word.compareTo(this.keys.get(1)) >= 0) {
                    this.children.get(2).searchWord(word);
                } else {
                    this.children.get(1).searchWord(word);
                }
            } else if (this.keys.size() == 3) {
                if (word.compareTo(this.keys.get(2)) >= 0) {
                    this.children.get(3).searchWord(word);
                } else if (word.compareTo(this.keys.get(1)) >= 0) {
                    this.children.get(2).searchWord(word);
                } else if (word.compareTo(this.keys.get(0)) >= 0) {
                    this.children.get(1).searchWord(word);
                }
            } else {
                this.children.get(1).searchWord(word);
            }
        }
        return true;
    }
}
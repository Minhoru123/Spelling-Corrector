package spell;


public class TrieNode implements INode{
  private final char ch;
  private int count;
  private final TrieNode[] child;
  private final TrieNode parent;

  public TrieNode() {
    this('\0', null);
  }

  //TrieNode constructor
  public TrieNode(char ch, TrieNode parent) {
    this.ch=ch;
    this.count=0;
    this.child=new TrieNode[26];
    this.parent=parent;
  }

  @Override
  public int getValue() {
    return count;
  }

  @Override
  public void incrementValue() {
    count++;
  }

  @Override
  public INode[] getChildren() {
    return child;
  }

  //toString
  @Override
  public String toString() {
    if (parent == null || ch == '\0')
      return "";
    return parent.toString() + ch;
  }

  //equals needs to be simpler
  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (!(obj instanceof TrieNode))
      return false;
    TrieNode node = (TrieNode) obj;

    if (node == this)
      return  true;
    if (count != node.count)
      return false;
    if (ch != node.ch)
      return false;

    for (int i = 0; i < 26; i++) {
      if (child[i] == null) {
        if (node.child[i] != null)
          return false;
      } else {
        if (node.child[i] == null)
          return false;
        else {
          if (!child[i].equals(node.child[i]))
            return false;
        }
      }
    }
    return true;
  }

  //hasChild method
  public boolean hasChild(int index) {
    if (index > 26 - 1)
      return false;
    return (child[index] != null);
  }

  //getChild method
  public TrieNode getChild(int index) {
    if (index > 26 - 1)
      return null;
    return child[index];
  }

  //addNewChild method
  public void addNewChild(char ch, int index) {
    if (index > 26 - 1)
      return;
    child[index] = new TrieNode(ch, this);
  }
}

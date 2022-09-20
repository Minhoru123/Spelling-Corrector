package spell;

public class Trie implements ITrie {

  private final TrieNode rootNode;
  private int wordCount;
  private int nodeCount;
  int hashCode;

  //Trie constructor
  public Trie() {
    rootNode = new TrieNode();
    wordCount = 0;
    nodeCount = 1;
  }

  //add method
  @Override
  public void add(String word) {
    String lowercase = word.toLowerCase();
    TrieNode currNode = rootNode;

    for (int i = 0; i< lowercase.length(); i++) {
      char ch=lowercase.charAt(i);
      int index = ch - 'a';

      if (!currNode.hasChild(index)) {
        if (i == 0)
          hashCode+=word.hashCode() * 31;

        currNode.addNewChild(lowercase.charAt(i), index);
        nodeCount++;
      }
      currNode=currNode.getChild(index);

      if (i == lowercase.length() - 1) {
        if (currNode.getValue() == 0) {
          wordCount++;
        }
        currNode.incrementValue();
      }
    }

  }

  //find method
  @Override
  public INode find(String word) {
    String lowercase = word.toLowerCase();
    TrieNode currNode = rootNode;

    for (int i = 0; i < lowercase.length(); i++) {
      int index = lowercase.charAt(i) - 'a';
      if (!currNode.hasChild(index))
        return null;

      currNode = currNode.getChild(index);
    }

    if (currNode.getValue() == 0)
      return null;

    return currNode;
  }

  //getWordCount method
  @Override
  public int getWordCount() {
    return wordCount;
  }

  //getNodeCountMethod
  @Override
  public int getNodeCount() {
    return nodeCount;
  }

  //toString
  @Override
  public String toString() {
    return printNode(rootNode);
  }

  //kind of toString helper
  public String printNode(TrieNode node) {
    StringBuilder sb = new StringBuilder();
    if (node.getValue() > 0)
      sb.append(node).append("\n");

    for (int i = 0; i < 26; i++)
      if (node.hasChild(i))
        sb.append(printNode(node.getChild(i)));

    return sb.toString();
  }

  //equals method
  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (!(obj instanceof Trie))
      return false;

    Trie trie = (Trie) obj;

    if (trie == this)
      return  true;
    if (nodeCount != trie.nodeCount)
      return false;
    if (wordCount != trie.wordCount)
      return false;

    return rootNode.equals(trie.rootNode);
  }

  //simpler hashCode
  @Override
  public int hashCode() {
    int hash = 0;
    for (int i = 0; i < 26; i++) {
      if (rootNode.getChild(i) != null)
        hash = i;
    }
    return hash * wordCount * nodeCount;
  }
}


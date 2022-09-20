package spell;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

public class SpellCorrector implements ISpellCorrector{

  private final Trie dictionary;

  //constructor
  public SpellCorrector() {
    dictionary = new Trie();
  }

  //useDictionary
  @Override
  public void useDictionary(String dictionaryFileName) throws IOException {
    BufferedReader br=new BufferedReader(new FileReader(dictionaryFileName));

    Scanner readFile=new Scanner(br);

    while (readFile.hasNext()) {
      dictionary.add(readFile.next());
    }
  }

  //similarWords suggestion
  @Override
  public String suggestSimilarWord(String inputWord) {
    if (inputWord == null || inputWord.isEmpty() || inputWord.trim().isEmpty())
      return null;

    INode node=dictionary.find(inputWord);
    if (node != null && node.getValue() != 0)
      return inputWord.toLowerCase();

    //Creating HashSet
    HashSet<String> firstDistanceWords=new HashSet<>();
    HashSet<String> secondDistanceWords=new HashSet<>();

    deletionDistance(inputWord, firstDistanceWords, secondDistanceWords);
    transpositionDistance(inputWord, firstDistanceWords, secondDistanceWords);
    alternationDistance(inputWord, firstDistanceWords, secondDistanceWords);
    insertionDistance(inputWord, firstDistanceWords, secondDistanceWords);

    String similarWord=possibleSimilarWord(firstDistanceWords);
    if (similarWord == null) {
      HashSet<String> possibleWords=new HashSet<>(secondDistanceWords);
      firstDistanceWords.clear();
      secondDistanceWords.clear();

      for (String word : possibleWords) {
        deletionDistance(word, firstDistanceWords, secondDistanceWords);
        transpositionDistance(word, firstDistanceWords, secondDistanceWords);
        alternationDistance(word, firstDistanceWords, secondDistanceWords);
        insertionDistance(word, firstDistanceWords, secondDistanceWords);
      }

      similarWord=possibleSimilarWord(firstDistanceWords);
    }
    return similarWord;
  }

  //possibleSimilarWord
  private String possibleSimilarWord(HashSet<String> assumingWords) {
    if (assumingWords.isEmpty())
      return null;

    String bestWord = null;
    for (String currWord : assumingWords) {
      if (bestWord == null) {
        bestWord = currWord;
        continue;
      }

      INode bestNode = dictionary.find(bestWord);
      INode currNode = dictionary.find(currWord);

      if (bestNode.getValue() < currNode.getValue() ||
              (bestNode.getValue() == currNode.getValue() && bestWord.compareTo(currWord) > 0)) {
        bestWord = currWord;
      }
    }

    return bestWord;
  }

  //insertionDistance method
  private void insertionDistance(String inputWord, HashSet<String> firstDistanceWords, HashSet<String> secondDistanceWords) {
    String lowercase = inputWord.toLowerCase();
    StringBuilder sb = new StringBuilder(lowercase);

    for (int i = 0; i <= lowercase.length(); i++) {
      for (int j = 0; j < 26; j++) {
        sb.insert(i, (char) (j + 'a'));

        INode node = dictionary.find(sb.toString());

        if (node != null && node.getValue() != 0) {
          firstDistanceWords.add(sb.toString());
        }

        secondDistanceWords.add(sb.toString());

        sb = new StringBuilder(lowercase);
      }
    }
  }

  private void alternationDistance(String inputWord, HashSet<String> firstDistanceWords, HashSet<String> secondDistanceWords) {
    String lowercase = inputWord.toLowerCase();
    StringBuilder sb = new StringBuilder(lowercase);

    for (int i = 0; i < lowercase.length(); i++) {
      for (int j = 0; j < 26; j++) {
        sb.setCharAt(i, (char)(j + 'a'));

        INode node = dictionary.find(sb.toString());

        if (node != null && node.getValue() != 0) {
          firstDistanceWords.add(sb.toString());
        }

        secondDistanceWords.add(sb.toString());

        sb = new StringBuilder(lowercase);
      }
    }
  }

  private void transpositionDistance(String inputWord, HashSet<String> firstDistanceWords, HashSet<String> secondDistanceWords) {
    String lowercase = inputWord.toLowerCase();
    StringBuilder sb = new StringBuilder(lowercase);

    for (int i = 0; i < lowercase.length() - 1; i++) {
      StringBuilder temp = new StringBuilder(sb);

      char tempChar = temp.charAt(i + 1);
      temp.setCharAt(i + 1, temp.charAt(i));
      temp.setCharAt(i, tempChar);

      INode node = dictionary.find(temp.toString());

      if (node != null && node.getValue() != 0) {
        firstDistanceWords.add(temp.toString());
      }

      secondDistanceWords.add(temp.toString());
    }
  }

  private void deletionDistance(String inputWord, HashSet<String> firstDistanceWords, HashSet<String> secondDistanceWords) {
    String lowercase = inputWord.toLowerCase();
    StringBuilder sb = new StringBuilder(lowercase);

    for (int i = 0; i < lowercase.length(); i++) {
      sb.deleteCharAt(i);
      INode node = dictionary.find(sb.toString());

      if (node != null && node.getValue() != 0) {
        firstDistanceWords.add(sb.toString());
      }

      secondDistanceWords.add(sb.toString());

      sb = new StringBuilder(lowercase);
    }
  }

}

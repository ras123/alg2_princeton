import java.lang.IllegalArgumentException;
import java.util.HashMap;
import java.util.Map;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {

   private Map<String, Integer> nounToSynsetIdMap;
   private Map<Integer, String> synsetIdToSynsetMap;
   private SAP sap;

   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms) {
      this.nounToSynsetIdMap = new HashMap<>();
      this.synsetIdToSynsetMap = new HashMap<>();

      In in = new In(synsets);
      while (in.hasNextLine()) {
         String[] tokens = in.readLine().split(",");
         Integer synsetId = Integer.parseInt(tokens[0]);
         String synset = tokens[1];
         synsetIdToSynsetMap.put(synsetId, synset);

         String[] nouns = synset.split(" ");
         for (int i = 0; i < nouns.length; ++i) {
            nounToSynsetIdMap.put(nouns[i], synsetId);
         }
      }

      Digraph G = new Digraph(synsetIdToSynsetMap.size());
      in = new In(hypernyms);
      while (in.hasNextLine()) {
         String[] tokens = in.readLine().split(",");
         int v = Integer.parseInt(tokens[0]);
         for (int i = 1; i < tokens.length; ++i) {
            G.addEdge(v, Integer.parseInt(tokens[i]));
         }
      }

      this.sap = new SAP(G);
   }

   // returns all WordNet nouns
   public Iterable<String> nouns() {
      return nounToSynsetIdMap.keySet();
   }

   // is the word a WordNet noun?
   public boolean isNoun(String word) {
      return nounToSynsetIdMap.containsKey(word);
   }

   // distance between nounA and nounB (defined below)
   public int distance(String nounA, String nounB) {
      if (!isNoun(nounA) || !isNoun(nounB)) {
         throw new IllegalArgumentException();
      }

      return sap.length(nounToSynsetIdMap.get(nounA), nounToSynsetIdMap.get(nounB));
   }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
   public String sap(String nounA, String nounB) {
      if (!isNoun(nounA) || !isNoun(nounB)) {
         throw new IllegalArgumentException();
      }

      String synset = "";
      int ancestor = sap.ancestor(nounToSynsetIdMap.get(nounA), nounToSynsetIdMap.get(nounB));
      if (ancestor != -1) {
         synset = synsetIdToSynsetMap.get(ancestor);
      }

      return synset;
   }

   // do unit testing of this class
   public static void main(String[] args) {
      WordNet wn = new WordNet(args[0], args[1]);
      System.out.println(wn.sap("Actifed", "Coricidin"));  // Should return "antihistamine"
      System.out.println(wn.sap("Aegisthus", "antihistamine"));  // Should return ""
   }
}


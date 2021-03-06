import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {

    private Map<String, Set<Integer>> nounToSynsetIdsMap;
    private Map<Integer, String> synsetIdToSynsetMap;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        this.nounToSynsetIdsMap = new HashMap<>();
        this.synsetIdToSynsetMap = new HashMap<>();

        In in = new In(synsets);
        while (in.hasNextLine()) {
            String[] tokens = in.readLine().split(",");
            int synsetId = Integer.parseInt(tokens[0]);
            String synset = tokens[1];
            synsetIdToSynsetMap.put(synsetId, synset);

            String[] nouns = synset.split(" ");
            for (int i = 0; i < nouns.length; ++i) {
                if (!nounToSynsetIdsMap.containsKey(nouns[i])) {
                    nounToSynsetIdsMap.put(nouns[i], new HashSet<>());
                }

                nounToSynsetIdsMap.get(nouns[i]).add(synsetId);
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

        DAGValidator dagValidator = new DAGValidator(G);
        if (!dagValidator.hasSingleRoot() || dagValidator.hasCycle()) {
            throw new IllegalArgumentException();
        }

        this.sap = new SAP(G);
    }

    private static class DAGValidator {

        private Digraph G;
        private boolean[] visited;
        private boolean[] onStack;

        public DAGValidator(Digraph G) {
            this.G = G;
            this.visited = new boolean[G.V()];
            this.onStack = new boolean[G.V()];
        }

        public boolean hasSingleRoot() {
            // Root for the DAG has at least one incoming edge and no outgoing edges`
            boolean foundRoot = false;
            for (int v = 0; v < G.V(); ++v) {
                if (G.outdegree(v) == 0 && G.indegree(v) > 0) {
                    if (foundRoot) {
                        return false;
                    } else {
                        foundRoot = true;
                    }
                }
            }

            if (!foundRoot) {
                return false;
            }

            return true;
        }

        public boolean hasCycle() {
            for (int v = 0; v < G.V(); ++v) {
                if (!visited[v]) {
                    if (dfs(v)) {
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean dfs(int v) {
            visited[v] = true;
            onStack[v] = true;

            for (int w : G.adj(v)) {
                if (!visited[w]) {
                    if (dfs(w)) {
                        return true;
                    }
                } else if (onStack[w]) {
                    return true;
                }
            }

            onStack[v] = false;

            return false;
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToSynsetIdsMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new NullPointerException();
        }

        return nounToSynsetIdsMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        Iterable<Integer> nounASynsetIds = nounToSynsetIdsMap.get(nounA);
        Iterable<Integer> nounBSynsetIds = nounToSynsetIdsMap.get(nounB);

        return sap.length(nounASynsetIds, nounBSynsetIds);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        String synset = "";
        Iterable<Integer> nounASynsetIds = nounToSynsetIdsMap.get(nounA);
        Iterable<Integer> nounBSynsetIds = nounToSynsetIdsMap.get(nounB);
        int ancestor = sap.ancestor(nounASynsetIds, nounBSynsetIds);

        if (ancestor != -1) {
            synset = synsetIdToSynsetMap.get(ancestor);
        }

        return synset;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
        // System.out.println(wn.sap("Actifed", "Coricidin"));  // Should return "antihistamine"
        // System.out.println(wn.sap("Aegisthus", "antihistamine"));  // Should return ""
        System.out.println(wn.distance("horse", "zebra"));
        System.out.println(wn.sap("horse", "zebra"));

        System.out.println(wn.distance("horse", "table"));
        System.out.println(wn.sap("horse", "table"));
    }
}


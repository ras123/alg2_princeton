import java.util.Arrays;

import edu.princeton.cs.algs4.In;

public class Outcast {

    private WordNet wn;

    public Outcast(WordNet wordnet) {
        this.wn = wordnet;
    }

    private int[][] initializeCache(int size) {
        int[][] cache = new int[size][size];
        for (int i = 0; i < size; ++i) {
            Arrays.fill(cache[i], -1);
        }

        return cache;
    }

    public String outcast(String[] nouns) {  // given an array of WordNet nouns, return an outcast
        int maxDistance = Integer.MIN_VALUE;
        String outcast = "";
        int[][] cache = initializeCache(nouns.length);

        for (int i = 0; i < nouns.length; ++i) {
            int distance = 0;
            for (int j = 0; j < nouns.length; ++j) {
                if (i == j) {
                    continue;
                }

                if (cache[i][j] == -1) {
                    cache[i][j] = wn.distance(nouns[i], nouns[j]);
                    cache[j][i] = cache[i][j];
                }

                distance += cache[i][j];
            }

            if (distance > maxDistance) {
                maxDistance = distance;
                outcast = nouns[i];
            }
        }

        return outcast;
    }

    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wn);
        for (int i = 2; i < args.length; ++i) {
            In in = new In(args[i]);
            String[] nouns = in.readAllStrings();
            System.out.println(args[i] + ": " + outcast.outcast(nouns));
        }
    }
}
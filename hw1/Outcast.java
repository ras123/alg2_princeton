import edu.princeton.cs.algs4.In;

public class Outcast {

    private WordNet wn;

    public Outcast(WordNet wordnet) {
        this.wn = wordnet;
    }

    public String outcast(String[] nouns) {  // given an array of WordNet nouns, return an outcast
        int maxDistance = Integer.MIN_VALUE;
        String outcast = "";

        for (int i = 0; i < nouns.length; ++i) {
            int distance = 0;
            for (int j = 0; j < nouns.length; ++j) {
                if (i == j) {
                    continue;
                }

                // TODO: optimize by caching results instead of recomputing, distance(a, b) == distance(b, a)
                distance += wn.distance(nouns[i], nouns[j]);;
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
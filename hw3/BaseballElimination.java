import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {

    private Map<String, Team> teams;

    private static class Team {
        private String name;
        private int wins;
        private int losses;
        private int remaining;
        private Map<String, Integer> remainingAgainst;

        public Team(String name, int wins, int losses, int remaining) {
            this.name = name;
            this.wins = wins;
            this.losses = losses;
            this.remaining = remaining;
            this.remainingAgainst = new HashMap<>();
        }

        public int wins() {
            return wins;
        }

        public int losses() {
            return losses;
        }

        public int remaining() {
            return remaining;
        }

        public void setRemainingAgainst(String team, int games) {
            remainingAgainst.put(team, games);
        }

        public int getRemainingAgainst(String team) {
            return remainingAgainst.get(team);
        }
    }

    public BaseballElimination(String filename) {
        this.teams = new HashMap<>();

        In in = new In(filename);
        String[] teamNames = new String[in.readInt()];
        String[] remainingAgainst = new String[teamNames.length];
        int idx = 0;

        while (!in.isEmpty()) {
            teamNames[idx] = in.readString();
            Team team = new Team(teamNames[idx], in.readInt(), in.readInt(), in.readInt());
            teams.put(teamNames[idx], team);
            remainingAgainst[idx] = in.readLine();
            ++idx;
        }

        for (int i = 0; i < remainingAgainst.length; ++i) {
            String teamName = teamNames[i];
            StringTokenizer tokenizer = new StringTokenizer(remainingAgainst[i]);
            for (int j = 0; j < teamNames.length; ++j) {
                String otherTeamName = teamNames[j];
                int games = Integer.parseInt(tokenizer.nextToken());
                teams.get(teamName).setRemainingAgainst(otherTeamName, games);
            }
        }
    }

    public int numberOfTeams() {
        return teams.size();
    }

    public Iterable<String> teams() {
        return teams.keySet();
    }

    // Number of wins for given team
    public int wins(String team) {
        return teams.get(team).wins();
    }

    // Number of losses for given team
    public int losses(String team) {
        return teams.get(team).losses();
    }

    // Number of remaining games for given team
    public int remaining(String team) {
        return teams.get(team).remaining();
    }

    // Number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return teams.get(team1).getRemainingAgainst(team2);
    }

    public boolean isEliminated(String team) {
        return false;
    }

    // Subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}

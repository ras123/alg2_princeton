import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {

    private Map<String, Integer> teamMap;
    private Team[] teams;

    public BaseballElimination(String filename) {
        this.teamMap = new HashMap<>();

        In in = new In(filename);
        int teamCount = in.readInt();
        this.teams = new Team[teamCount + 1];

        int idx = 1;
        while (!in.isEmpty()) {
            Team.TeamBuilder teamBuilder = new Team.TeamBuilder()
                .setTeamCount(teamCount)
                .setName(in.readString())
                .setWins(in.readInt())
                .setLosses(in.readInt())
                .setRemaining(in.readInt());

            StringTokenizer tokenizer = new StringTokenizer(in.readLine());
            for (int i = 1; i <= teamCount; ++i) {
                int remainingGames = Integer.parseInt(tokenizer.nextToken());
                teamBuilder.setRemainingAgainst(i, remainingGames);
            }

            Team team = teamBuilder.build();
            teams[idx] = team;
            teamMap.put(team.name(), idx);
            ++idx;
        }
    }

    public int numberOfTeamMap() {
        return teamMap.size();
    }

    public Iterable<String> teams() {
        return teamMap.keySet();
    }

    // Number of wins for given team
    public int wins(String team) {
        return teams[teamMap.get(team)].wins();
    }

    // Number of losses for given team
    public int losses(String team) {
        return teams[teamMap.get(team)].losses();
    }

    // Number of remaining games for given team
    public int remaining(String team) {
        return teams[teamMap.get(team)].remaining();
    }

    // Number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return teams[teamMap.get(team1)].getRemainingAgainst(teamMap.get(team2));
    }

    public boolean isEliminated(String team) {
        return false;
    }

    // Subset R of teamMap that eliminates given team; null if not eliminated
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

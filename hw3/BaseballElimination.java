public class BaseballElimination {
    public BaseballElimination(String filename) {

    }

    public int numberOfTeams() {
        return -1;
    }

    public Iterable<String> teams() {
        return null;
    }

    // Number of wins for given team
    public int wins(String team) {
        return -1;
    }

    // Number of losses for given team
    public int losses(String team) {
        return -1;
    }

    // Number of remaining games for given team
    public int remaining(String team) {
        return -1;
    }

    // Number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return -1;
    }

    public boolean isEliminated(String team) {
        return false;
    }

    // Subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        return null;
    }
}

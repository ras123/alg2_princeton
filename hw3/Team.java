import java.util.Arrays;

public class Team {
    private final String name;
    private final int wins;
    private final int losses;
    private final int remaining;
    private final int[] remainingAgainst;

    private Team(TeamBuilder teamBuilder) {
        this.name = teamBuilder.name;
        this.wins = teamBuilder.wins;
        this.losses = teamBuilder.losses;
        this.remaining = teamBuilder.remaining;
        this.remainingAgainst = Arrays.copyOf(teamBuilder.remainingAgainst,
            teamBuilder.remainingAgainst.length);
    }

    public static class TeamBuilder {
        private String name;
        private int wins;
        private int losses; 
        private int remaining;
        private int[] remainingAgainst;

        public TeamBuilder setTeamCount(int teamCount) {
            this.remainingAgainst = new int[teamCount + 1];
            return this;
        }

        public TeamBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public TeamBuilder setWins(int wins) {
            this.wins = wins;
            return this;
        }

        public TeamBuilder setLosses(int losses) {
            this.losses = losses;
            return this;
        }

        public TeamBuilder setRemaining(int remaining) {
            this.remaining = remaining;
            return this;
        }

        public TeamBuilder setRemainingAgainst(int team, int games) {
            remainingAgainst[team] = games;
            return this;
        }

        public Team build() {
            return new Team(this);
        }
    }

    public String name() {
        return name;
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

    public int getRemainingAgainst(int teamIdx) {
        return remainingAgainst[teamIdx];
    }
}
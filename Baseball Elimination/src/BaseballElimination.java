/******************************************************************************
 * Author: Dennis Tran
 * Baseball Elimination implementation
 * Written: 11/12/17
 * For Coursera/Princeton Algorithms 2
 ******************************************************************************/

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private final SeparateChainingHashST<String, Integer> teams;
    private final String[] team_number;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] against;
    private FordFulkerson[] eliminated;

    // create a baseball division from given filename in format specified below
     public BaseballElimination(String filename) {
         In in = new In(filename);
         int size = in.readInt();

         teams = new SeparateChainingHashST<>();
         team_number = new String[size];
         wins = new int[size];
         losses = new int[size];
         remaining = new int[size];
         against = new int[size][size];
         eliminated = new FordFulkerson[size];

         for (int i = 0; i < size; i++) {
             String team = in.readString();
             teams.put(team, i);
             team_number[i] = team;
             wins[i] = in.readInt();
             losses[i] = in.readInt();
             remaining[i] = in.readInt();

             for (int j = 0; j < size; j++) {
                 against[i][j] = in.readInt();
             }
         }
    }

    // number of teams
    public int numberOfTeams() {
        return teams.size();
    }

    // all teams
    public Iterable<String> teams() {
        Queue<String> queue = new Queue<>();
        for (int i = 0; i < team_number.length; i++) {
            queue.enqueue(team_number[i]);
        }
        return queue;
    }

    // number of wins for given team
    public int wins(String team) {
        if (!teams.contains(team)) throw new java.lang.IllegalArgumentException("Invalid team");
        return wins[teams.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teams.contains(team)) throw new java.lang.IllegalArgumentException("Invalid team");
        return losses[teams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teams.contains(team)) throw new java.lang.IllegalArgumentException("Invalid team");
        return remaining[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teams.contains(team1) || !teams.contains(team2))
            throw new java.lang.IllegalArgumentException("Invalid team");
        return against[teams.get(team1)][teams.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!teams.contains(team)) throw new java.lang.IllegalArgumentException("Invalid team");
        if (eliminated[teams.get(team)] == null) {
            for (int i = 0; i < numberOfTeams(); i++) {
                int possible = wins[teams.get(team)] + remaining[teams.get(team)];
                if (possible < wins[i]) return true;
            }
            compute(teams.get(team));
        }
        int total = 0;
        for (int i = 0; i < numberOfTeams(); i++) {
            if (i == teams.get(team)) continue;
            for (int j = i + 1; j < numberOfTeams(); j++) {
                if (j == teams.get(team)) continue;
                total += against[i][j];
            }
        }
        return eliminated[teams.get(team)].value() != total;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.contains(team)) throw new java.lang.IllegalArgumentException("Invalid team");
        Queue<String> queue = new Queue<>();
        if (eliminated[teams.get(team)] == null) {
            for (int i = 0; i < numberOfTeams(); i++) {
                int possible = wins[teams.get(team)] + remaining[teams.get(team)];
                if (possible < wins[i]) {
                    queue.enqueue(team_number[i]);
                    return queue;
                }
            }
            compute(teams.get(team));
        }

        for (int i = 0; i < numberOfTeams() - 1; i++) {
            if (eliminated[teams.get(team)].inCut(i)) {
                int num = (i < teams.get(team)) ? i : i + 1;
                queue.enqueue(team_number[num]);
            }
        }
        if (queue.isEmpty()) return null;
        return queue;
    }

    private void compute(int team) {
         int vertices = numberOfTeams() + 1 + numberOfTeams() * (numberOfTeams() - 1) / 2;
         FlowNetwork network = new FlowNetwork(vertices);

         for (int i = 0; i < numberOfTeams() - 1; i++) {
             int num = (i < team) ? i : i + 1;
             int capacity = wins[team] + remaining[team] - wins[num];
             if (capacity < 0) capacity = 0;
             network.addEdge(new FlowEdge(i, vertices - 1, capacity));
         }

         int vertex = numberOfTeams();
         for (int i = 0; i < numberOfTeams(); i++) {
             if (i == team) continue;
             for (int j = i + 1; j < numberOfTeams(); j++) {
                 if (j == team) continue;
                 network.addEdge(new FlowEdge(vertices - 2, vertex, against[i][j]));
                 int num1 = (i < team) ? i : i - 1;
                 int num2 = (j < team) ? j : j - 1;
                 network.addEdge(new FlowEdge(vertex, num1, Double.POSITIVE_INFINITY));
                 network.addEdge(new FlowEdge(vertex, num2, Double.POSITIVE_INFINITY));
                 vertex++;
             }
         }
         eliminated[team] = new FordFulkerson(network, vertices - 2, vertices - 1);
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

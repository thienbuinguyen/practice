import java.util.ArrayList;

public class FourPatternCombinations {

    private static int dfs(ArrayList<Integer> moves) {
        if (moves.size() >= 4) return 1;
        else {
            int lastMove = moves.get(moves.size() - 1);
            int n = 0;

            for (int i = 1; i <= 9; i++) {
                if (!moves.contains(i)) {
                    boolean possible = true;

                    if (lastMove == 1) {
                        if (i == 3 && !moves.contains(2)) possible = false;
                        else if (i == 7 && !moves.contains(4)) possible = false;
                        else if (i == 9 && !moves.contains(5)) possible = false;
                    } else if (lastMove == 3) {
                        if (i == 1 && !moves.contains(2)) possible = false;
                        else if (i == 9 && !moves.contains(6)) possible = false;
                        else if (i == 7 && !moves.contains(5)) possible = false;
                    } else if (lastMove == 7) {
                        if (i == 1 && !moves.contains(4)) possible = false;
                        else if (i == 3 && !moves.contains(5)) possible = false;
                        else if (i == 9 && !moves.contains(8)) possible = false;
                    } else if (lastMove == 9) {
                        if (i == 1 && !moves.contains(5)) possible = false;
                        else if (i == 3 && !moves.contains(6)) possible = false;
                        else if (i == 7 && !moves.contains(8)) possible = false;
                    } else if (!moves.contains(5)) {
                        if (lastMove == 2 && i == 8 ||
                            lastMove == 4 && i == 6 ||
                            lastMove == 6 && i == 4 ||
                            lastMove == 8 && i == 2)
                        {
                            possible = false;
                        }
                    }

                    if (possible) {
                        moves.add(i);
                        n += dfs(moves);
                        moves.remove(moves.size() - 1);
                    }
                }
            }

            return n;
        }
    }

    private static int calculateFourPatternCombinations() {
        ArrayList<Integer> moves = new ArrayList<>();
        int n = 0;
        for (int i = 1; i <= 9; i++) {
            moves.add(i);
            n += dfs(moves);
            moves.remove(moves.size() - 1);
        }
        return n;
    }

    public static void main(String[] args) {
        System.out.println(calculateFourPatternCombinations());
    }
}

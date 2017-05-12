import core.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            sudokuabstractsolver sdks = sudokuabstractsolver.getNewSdkSolver(4, 16);
            sdks.setRelations(new int[][]{
                    {0, 1, 2, 3},
                    {4, 5, 6, 7},
                    {8, 9, 10, 11},
                    {12, 13, 14, 15},
                    {0, 4, 8, 12},
                    {1, 5, 9, 13},
                    {2, 6, 10, 14},
                    {3, 7, 11, 15},
                    {0, 1, 4, 5},
                    {2, 3, 6, 7},
                    {8, 9, 12, 13},
                    {10, 11, 14, 15}});
            sdks.setPuzzle(new int[]{
                    1, 0, 0, 0,
                    0, 0, 0, 0,
                    0, 0, 2, 0,
                    4, 2, 0, 0
            });
            List<int[]> res = sdks.calculate(2);
            int resnum = res.size();
            for (int n = 0; n < resnum; n++) {
                System.out.println(n);
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        System.out.print(res.get(n)[i * 4 + j] + " ");
                    }
                    System.out.println();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

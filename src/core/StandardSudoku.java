package core;

import java.util.List;

/**
 * based on sudokuabstractsolver
 * support standard sudoku such as
 * 4*4 StandardSudoku.getStandardSudoku(2);
 * 9*9 StandardSudoku.getStandardSudoku(3);
 * 16*16 StandardSudoku.getStandardSudoku(4);
 * 25*25 StandardSudoku.getStandardSudoku(5);
 */
public class StandardSudoku {
    private sudokuabstractsolver sdks;

    private StandardSudoku(int n) throws Exception {
        int nn = n * n;
        sdks = sudokuabstractsolver.getNewSdkSolver(nn, nn * nn);
        int[][] relations = new int[3 * nn][nn];
        int base = 0;
        int i, j;
        for (i = 0; i < nn; i++) {
            for (j = 0; j < nn; j++) {
                relations[base + i][j] = i * nn + j;
            }
        }
        base += nn;
        for (i = 0; i < nn; i++) {
            for (j = 0; j < nn; j++) {
                relations[base + i][j] = j * nn + i;
            }
        }
        base += nn;
        for (i = 0; i < nn; i++) {
            for (j = 0; j < nn; j++) {
                relations[base + i][j] = (i / n) * n * nn + (i % n) * n + (j / n) * nn + j % n;
            }
        }
        sdks.setRelations(relations);
    }

    public void setPuzzle(int[] puz) throws Exception {
        sdks.setPuzzle(puz);
    }

    public int[] solve() throws Exception {
        List<int[]> res = sdks.calculate(-1);
        if (res.size() == 1) {
            return res.get(0);
        } else if (res.size() == 0) {
            System.out.println("no result !? ");
            return new int[0];
        } else {
            System.out.println("result num = " + res.size());
            return res.get(0);
        }
    }

    public static StandardSudoku getStandardSudoku(int n) throws Exception {
        return new StandardSudoku(n);
    }
}

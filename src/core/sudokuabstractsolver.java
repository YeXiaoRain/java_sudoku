package core;

import java.util.ArrayList;
import java.util.List;

/**
 * based on sudokuabstract, this class is focus on how to solve
 * this class also record the relation between grids
 * e.g.
 * standard 4*4 sudoku is indexing as
 *  0  1 | 2  3
 *  4  5 | 6  7
 *  -----+-----
 *  8  9 |10 11
 * 12 13 |14 15
 *
 * so you need use
 * sudokuabstractsolver sdks = sudokuabstractsolver.getNewSdkSolver(4, 16);
 * sdks.setRelations(new int[][]{
 *      {0, 1, 2, 3},
 *      {4, 5, 6, 7},
 *      {8, 9, 10, 11},
 *      {12, 13, 14, 15},
 *
 *      {0, 4, 8, 12},
 *      {1, 5, 9, 13},
 *      {2, 6, 10, 14},
 *      {3, 7, 11, 15},
 *
 *      {0, 1, 4, 5},
 *      {2, 3, 6, 7},
 *      {8, 9, 12, 13},
 *      {10, 11, 14, 15}});
 * */
class sudokuabstractsolver {
    private sudokuabstract sdka;
    private int resultsize = 0;
    private List<int[]> res;
    private boolean isUpdate;
    private int numbernum;
    private int len;
    private List<int[]> relations;
    private List<List<Integer>> gridsrelation;

    private sudokuabstractsolver(int nn, int l) throws Exception {
        numbernum = nn;
        len = l;
        relations = new ArrayList<>();
        gridsrelation = new ArrayList<>();
        for (int i = 0; i < l; i++) {
            gridsrelation.add(new ArrayList<>());
        }
        sdka = sudokuabstract.getNewSudokuAbstract(nn, l);
    }

    private void initial(sudokuabstract s, int rs) throws Exception {
        if (s == sdka) {
            resultsize = rs;
        }
        for (int i = 0; i < len; i++) {
            s.getGrid(i).leftpossiblenum = numbernum;
            s.getGrid(i).impossibleValue = new boolean[numbernum + 1];
        }
        for (int i = 0; i < len; i++) {
            updateImpossible(s, i);
        }
    }

    private void gridonlyres(sudokuabstract s, int index) throws Exception {
        sudokuabstract.grid g = s.getGrid(index);
        if (g.value != sudokuabstract.EMPTY) {
            return;
        }
        if (g.leftpossiblenum == 1) {
            int v = 0;// never be zero or it will throw Exception
            for (int i = 1; i <= numbernum; i++) {
                if (!g.impossibleValue[i])
                    v = i;
            }
            s.setGrid(index, v);
            updateImpossible(s, index);
        } else if (g.leftpossiblenum == 0) {
            throw new sudokuException(sudokuException.ERR_BADSUDOKU, "BAD SODUKU: CONFLICT", index);
        }
    }

    private void relonlypos(sudokuabstract s, int index) throws Exception {
        int[] rel = relations.get(index);
        for (int v = 1; v <= numbernum; v++) {
            boolean[] impossibleindexsindex = new boolean[numbernum];
            int possibleposnum = numbernum;
            int i;
            for (i = 0; i < numbernum; i++) {
                int pos = rel[i];
                if (s.getGrid(pos).value == v)
                    break;
                if (s.getGrid(pos).value != sudokuabstract.EMPTY || s.getGrid(pos).impossibleValue[v]) {
                    possibleposnum--;
                    impossibleindexsindex[i] = true;
                }
            }
            if (i != numbernum)
                continue;
            if (possibleposnum == 1) {
                int pos = -1;// never be zero or it will throw Exception
                for (i = 0; i < numbernum; i++) {
                    if (!impossibleindexsindex[i])
                        pos = rel[i];
                }
                s.setGrid(pos, v);
                updateImpossible(s, index);
            } else if (possibleposnum == 0) {
                throw new sudokuException(sudokuException.ERR_BADSUDOKU, "BAD SODUKU: CONFLICT RELATION", index);
            }
        }
    }

    private void exclusionMethod(sudokuabstract s) throws Exception {
        int i;
        int rels = relations.size();
        do {
            isUpdate = false;
            for (i = 0; i < len; i++) {
                gridonlyres(s, i);
            }
            for (i = 0; i < rels; i++) {
                relonlypos(s, i);
            }
        } while (isUpdate);
    }

    private void updateImpossible(sudokuabstract s, int index) throws Exception {
        sudokuabstract.grid g = s.getGrid(index);
        if (g.value == sudokuabstract.EMPTY)
            return;
        isUpdate = true;
        int v = g.value;
        List<Integer> gridrel = gridsrelation.get(index);
        for (Integer rindex : gridrel) {
            int[] rels = relations.get(rindex);
            for (int j = 0; j < numbernum; j++) {
                int pos = rels[j];
                if (s.getGrid(pos).value != sudokuabstract.EMPTY)
                    continue;
                if (s.getGrid(pos).impossibleValue[v])
                    continue;
                s.getGrid(pos).impossibleValue[v] = true;
                s.getGrid(pos).leftpossiblenum--;
            }
        }
    }

    private List<int[]> enumerationMethod(sudokuabstract s, int maxres) throws Exception {
        sudokuabstract stmp = new sudokuabstract(1, 1);
        int i;
        for (i = 0; i < len; i++) {
            if (s.getGrid(i).value == sudokuabstract.EMPTY)
                break;
        }
        if (i == len)
            throw new sudokuException(sudokuException.ERR_ERROR, "fill all shouldn't go here");
        List<int[]> res = new ArrayList<>();
        sudokuabstract.grid g = s.getGrid(i);
        int v;
        for (v = 1; v <= numbernum; v++) {
            if (!g.impossibleValue[v]) {
                s.fork(stmp);
                stmp.getGrid(i).value = v;
                try {
                    List<int[]> ret = calculate(stmp, maxres);
                    if (ret.size() != 0) {
                        res.addAll(ret);
                    }
                    if (resultsize == 0)
                        break;
                } catch (sudokuException e) {
                    if (e.err != sudokuException.ERR_BADSUDOKU)
                        e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    private List<int[]> calculate(sudokuabstract s, int maxres) throws Exception {
        List<int[]> ret = new ArrayList<>();
        initial(s, maxres);
        exclusionMethod(s);
        if (s.fillcheck()) {
            resultsize--;
            s.getData(ret);
            return ret;
        }
        return enumerationMethod(s, maxres);
    }

    private void setPuzzle(sudokuabstract s, int[] puz) throws Exception {
        if (puz.length != len)
            throw new sudokuException(sudokuException.ERR_ERROR, "setPuzzle wrong length");
        for (int i = 0; i < len; i++) {
            s.setGrid(i, puz[i]);
        }
    }

    List<int[]> calculate(int maxres) throws Exception {
        return this.calculate(sdka, maxres);
    }

    void setRelations(int[][] rels) throws Exception {
        for (int[] r : rels) {
            if (r.length != numbernum)
                throw new sudokuException(sudokuException.ERR_ERROR, "wrong rel length");
            boolean[] conflictSol = new boolean[len];
            int i;
            for (i = 0; i < numbernum; i++) {
                if (r[i] < 0 || r[i] >= len)
                    throw new sudokuException(sudokuException.ERR_ERROR, "wrong rel index");
                if (conflictSol[r[i]])
                    throw new sudokuException(sudokuException.ERR_ERROR, "relation conflict");
                conflictSol[r[i]] = true;
            }
            int[] appendarr = new int[numbernum];
            System.arraycopy(r, 0, appendarr, 0, numbernum);
            relations.add(appendarr);
            int relationindex = relations.size() - 1;
            for (i = 0; i < numbernum; i++) {
                gridsrelation.get(r[i]).add(relationindex);
            }
        }
    }

    void setPuzzle(int[] puz) throws Exception {
        this.setPuzzle(sdka, puz);
    }

    static sudokuabstractsolver getNewSdkSolver(int nn, int l) throws Exception {
        return new sudokuabstractsolver(nn, l);
    }
}
/*
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
            System.out.println(res.size());
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {

                    System.out.print(res.get(0)[i * 4 + j] + " ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

*/

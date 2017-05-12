package core;

import java.util.ArrayList;
import java.util.List;

/**
 * based on sudokuabstract, this class is focus on how to solve
 */
public class sudokuabstractsolver {
    private sudokuabstract sdka;
    private int resultsize = 0;
    private List<int[]> res;
    private boolean isUpdate;
    private int numbernum;
    private int len;

    private sudokuabstractsolver(int nn, int l) throws Exception {
        numbernum = nn;
        len = l;
        sdka = sudokuabstract.getNewSudokuAbstract(nn, l);
    }

    private void initial(int rs) throws Exception {
        resultsize = rs;
        for(int i=0;i<len;i++){
            updateImpossible(i);
        }
    }

    private void gridonlyres(int index) throws Exception {
        sudokuabstract.grid g = sdka.getGrid(index);
        if (g.value != sudokuabstract.EMPTY) {
            return;
        }
        //g.impossibleValue = new boolean[leftpossiblenum + 1]; // 0 is take place ,1~numbernum corresponding to the value
        //List<int[]> relations = sdka.getRelations(); // simplify??
        //for (int i = 0, maxi = g.relationListIndex.size(); i < maxi; i++) {
        //    int[] thisrel = relations.get(g.relationListIndex.get(i));
        //    for (int j = 0; j < numbernum; j++) {
        //        if (thisrel[j] == index)
        //            continue;
        //        sudokuabstract.grid gj = sdka.getGrid(thisrel[j]);
        //        if (gj.value != sudokuabstract.EMPTY && !g.impossibleValue[gj.value]) {
        //            g.impossibleValue[gj.value] = true;
        //            g.leftpossiblenum--;
        //        }
        //    }
        //}
        if (g.leftpossiblenum == 1) {
            int v = 0;// never be zero or it will throw Exception
            for (int i = 1; i <= numbernum; i++) {
                if (!g.impossibleValue[i])
                    v = i;
            }
            sdka.setGrid(index, v);
            updateImpossible(index);
            isUpdate = true;
        } else if (g.leftpossiblenum == 0) {
            throw new sudokuException(sudokuException.ERR_BADSUDOKU, "BAD SODUKU: CONFLICT", index);
        }
    }

    private void relonlypos(int index) throws Exception {
        List<int[]> relations = sdka.getRelations();
        int[] rel = relations.get(index);
        for (int v = 1; v <= numbernum; v++) {
            boolean[] impossibleindexsindex = new boolean[numbernum];
            int possibleposnum = numbernum;
            int i;
            for (i = 0; i < numbernum; i++) {
                int pos = rel[i];
                if (sdka.getGrid(pos).value == v)
                    break;
                if (sdka.getGrid(pos).value != sudokuabstract.EMPTY || sdka.getGrid(pos).impossibleValue[v]) {
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
                sdka.setGrid(pos, v);
                updateImpossible(index);
                isUpdate = true;
            } else if (possibleposnum == 0) {
                throw new sudokuException(sudokuException.ERR_BADSUDOKU, "BAD SODUKU: CONFLICT RELATION", index);
            }
        }
    }

    private void enumerationMethod() throws Exception {
        int i;
        int rels = sdka.getRelations().size();
        do {
            isUpdate = false;
            for (i = 0; i < len; i++) {
                gridonlyres(i);
            }
            for (i = 0; i < rels; i++) {
                relonlypos(i);
            }
        } while (isUpdate);
    }
    private void updateImpossible(int index) throws Exception {
        sudokuabstract.grid g = sdka.getGrid(index);
        if(g.value==sudokuabstract.EMPTY)
            return;
        int v = g.value;
        List<int []> relations = sdka.getRelations();
        List<Integer> gridrel = g.relationListIndex;
        for (Integer rindex : gridrel) {
            int[] rels = relations.get(rindex);
            for (int j = 0; j < numbernum; j++) {
                int pos = rels[j];
                if (sdka.getGrid(pos).value != sudokuabstract.EMPTY)
                    continue;
                if (sdka.getGrid(pos).impossibleValue[v])
                    continue;
                sdka.getGrid(pos).impossibleValue[v] = true;
                sdka.getGrid(pos).leftpossiblenum--;
            }
        }
    }
    private void exclusionMethod() {

    }

    public List<int[]> calculate(int maxres) throws Exception {
        res = new ArrayList<int[]>();
        initial(maxres);
        enumerationMethod();
        if(sdka.fillcheck()) {
            sdka.getData(res);
            return res;
        }
        exclusionMethod();
        return res;
    }

    public void setRelations(int[][] rels) throws Exception {
        for (int[] r : rels) {
            sdka.setRelation(r);
        }
    }

    public void setPuzzle(int[] puz) throws Exception {
        if (puz.length != len)
            throw new sudokuException(sudokuException.ERR_ERROR, "setPuzzle wrong length");
        for (int i = 0; i < len; i++) {
            sdka.setGrid(i, puz[i]);
        }
    }

    public static sudokuabstractsolver getNewSdkSolver(int nn, int l) throws Exception {
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

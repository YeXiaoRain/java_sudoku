package core;

import java.util.ArrayList;
import java.util.List;

/**
 * this class only care about how to save the sudoku info
 */
public class sudokuabstract {
    static final int EMPTY = 0;

    static class grid {
        int value;
        List<Integer> relationListIndex;
        boolean[] impossibleValue;
        int leftpossiblenum;

        grid(int nn) {
            value = EMPTY;
            relationListIndex = new ArrayList<Integer>();
            leftpossiblenum = nn;
            impossibleValue = new boolean[nn + 1]; // 0 is take place ,1~numbernum corresponding to the value
        }
    }

    private int numbernum;
    private int len;
    private grid[] data;
    private List<int[]> relations;

    /*
    * numbernum and length
    * e.g. standard sudoku (numbernum,length)=(9,81)
    * */
    private sudokuabstract(int nn, int l) throws Exception {
        if (nn <= 0 || l % nn != 0)
            throw new sudokuException(sudokuException.ERR_ERROR, "sudokuabstract parameter error");
        numbernum = nn;
        len = l;
        data = new grid[len];
        int i;
        for (i = 0; i < len; i++) {
            data[i] = new grid(nn);
        }
        relations = new ArrayList<>();
    }

    private void validIndexCheck(int i) throws Exception {
        if (i < 0 || i >= len)
            throw new sudokuException(sudokuException.ERR_ERROR, "invalid index");
    }

    private void validValueCheck(int i) throws Exception {
        if (i < 0 || i > numbernum)
            throw new sudokuException(sudokuException.ERR_ERROR, "invalid value");
    }

    private void validRelationlenthCheck(int i) throws Exception {
        if (i != numbernum)
            throw new sudokuException(sudokuException.ERR_ERROR, "invalid relation lenth");
    }

    /*
    * display all relations
    * */
    public void showRelation() {
        int i, maxi = relations.size();
        for (i = 0; i < maxi; i++) {
            int j;
            for (j = 0; j < numbernum; j++)
                System.out.print(relations.get(i)[j] + " ");
            System.out.println();
        }
    }

    grid getGrid(int p) throws Exception {
        validIndexCheck(p);
        return data[p];
    }

    void setGrid(int p, int v) throws Exception {
        validIndexCheck(p);
        validValueCheck(v);
        data[p].value = v;
    }

    /*
    * index in v[0~numbernum-1] means those grid are in a group
    * */
    void setRelation(int[] v) throws Exception {
        validRelationlenthCheck(v.length);
        boolean[] conflictSol = new boolean[len];
        int i;
        for (i = 0; i < numbernum; i++) {
            validIndexCheck(v[i]);
            if (conflictSol[v[i]])
                throw new sudokuException(sudokuException.ERR_ERROR, "relation conflict");
            conflictSol[v[i]] = true;
        }
        int[] appendarr = new int[numbernum];
        System.arraycopy(v, 0, appendarr, 0, numbernum);
        relations.add(appendarr);
        int relationindex = relations.size() - 1;
        for (i = 0; i < numbernum; i++) {
            data[v[i]].relationListIndex.add(relationindex);
        }
    }

    List<int[]> getRelations() {
        return relations;
    }

    void getData(List<int[]> output) {
        int[] newres = new int[len];
        for (int i = 0; i < len; i++) {
            newres[i] = data[i].value;
        }
        output.add(newres);
    }

    boolean fillcheck() {
        for (int i = 0; i < len; i++) {
            if (data[i].value == EMPTY)
                return false;
        }
        return true;
    }

    static sudokuabstract getNewSudokuAbstract(int nn, int l) throws Exception {
        return new sudokuabstract(nn, l);
    }
}
/* Test
public class Main {
    public static void main(String[] args) {
        try {
            sudokuabstract sdka = sudokuabstract.getNewSudokuAbstract(4,16);
            sdka.setRelation(new int[]{0,1,2,3});
            sdka.setRelation(new int[]{4,5,6,7});
            sdka.setRelation(new int[]{8,9,10,11});
            sdka.setRelation(new int[]{12,13,14,15});
            sdka.setRelation(new int[]{0,4,8,12});
            sdka.setRelation(new int[]{1,5,9,13});
            sdka.setRelation(new int[]{2,6,10,14});
            sdka.setRelation(new int[]{3,7,11,15});
            sdka.setRelation(new int[]{0,1,4,5});
            sdka.setRelation(new int[]{2,3,6,7});
            sdka.setRelation(new int[]{8,9,12,13});
            sdka.setRelation(new int[]{10,11,14,15});
            sdka.showRelation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
* */
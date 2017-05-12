package core;

import java.util.List;

/**
 * this class only care about how to save the sudoku info
 */
class sudokuabstract {
    static final int EMPTY = 0;

    static class grid {
        int value;
        int leftpossiblenum;
        boolean[] impossibleValue;

        grid(int nn) {
            value = EMPTY;
            leftpossiblenum = nn;
            impossibleValue = new boolean[nn + 1]; // 0 is take place ,1~numbernum corresponding to the value
        }
    }

    private int numbernum;
    private int len;
    private grid[] data;

    /*
    * numbernum and length
    * e.g. standard sudoku (numbernum,length)=(9,81)
    * */
    sudokuabstract(int nn, int l) throws Exception {
        if (nn <= 0 || l < nn)
            throw new sudokuException(sudokuException.ERR_ERROR, "sudokuabstract parameter error");
        numbernum = nn;
        len = l;
        data = new grid[len];
        int i;
        for (i = 0; i < len; i++) {
            data[i] = new grid(nn);
        }
    }

    private void validIndexCheck(int i) throws Exception {
        if (i < 0 || i >= len)
            throw new sudokuException(sudokuException.ERR_ERROR, "invalid index");
    }

    private void validValueCheck(int i) throws Exception {
        if (i < 0 || i > numbernum)
            throw new sudokuException(sudokuException.ERR_ERROR, "invalid value");
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

    void fork(sudokuabstract stmp) {
        stmp.numbernum = numbernum;
        stmp.len = len;
        stmp.data = new grid[len];
        for (int i = 0; i < len; i++) {
            stmp.data[i] = new grid(numbernum);
            stmp.data[i].value = data[i].value;
            stmp.data[i].leftpossiblenum = data[i].leftpossiblenum;
            stmp.data[i].impossibleValue = new boolean[numbernum + 1];
            System.arraycopy(data[i].impossibleValue, 0, stmp.data[i].impossibleValue, 0, numbernum + 1);
        }
    }

    static sudokuabstract getNewSudokuAbstract(int nn, int l) throws Exception {
        return new sudokuabstract(nn, l);
    }
}

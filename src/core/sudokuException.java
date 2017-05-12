package core;

class sudokuException extends Exception {
    static final long ERR_ERROR = 1;
    static final long ERR_BADSUDOKU = 2;
    long err;
    private String message;
    private int val;

    sudokuException(long e, String m, int v) {
        err = e;
        message = m;
        val = v;
    }

    sudokuException(long e, String m) {
        this(e, m, 0);
    }

}

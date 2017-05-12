import core.*;


public class Main {

    public static void main(String[] args) {
        StandardSudoku soduku;
        try {
            int testnum = 3;
            soduku = StandardSudoku.getStandardSudoku(testnum);
            soduku.setPuzzle(new int[]{
                    0, 1, 0, 0, 0, 3, 4, 2, 7,
                    0, 0, 4, 0, 6, 2, 0, 5, 0,
                    0, 0, 2, 4, 0, 0, 3, 6, 0,
                    7, 0, 1, 0, 0, 0, 0, 0, 5,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    6, 9, 8, 5, 0, 0, 2, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 6, 9, 3, 5, 4, 7, 0, 2,
                    8, 0, 0, 0, 0, 0, 0, 0, 6
                    /*1, 2, 3, 4,
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    0, 0, 0, 0*/
            });
            int[] result = soduku.solve();
            int maxi = testnum * testnum, maxj = testnum * testnum;
            int maxn = maxi * maxj;
            if (result.length == maxn) {
                for (int i = 0; i < maxi; i++) {
                    for (int j = 0; j < maxj; j++) {
                        System.out.print(result[i * 4 + j] + " ");
                        if ((j + 1) % testnum == 0 && j + 1 != maxj)
                            System.out.print("| ");
                    }
                    System.out.println();
                    if ((i + 1) % testnum == 0 && i + 1 != maxi) {
                        for (int j = 0; j < maxj; j++) {
                            System.out.print("--");
                            if ((j + 1) % testnum == 0 && j + 1 != maxj)
                                System.out.print("+-");
                        }
                        System.out.println();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

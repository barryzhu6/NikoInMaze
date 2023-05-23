package edu.dsbbproj.nikoinmaze;
import java.util.*;

public class generateMaze {

    public static void generateInputCorner() { //confirm line + line is solvable, maybe not shortest, could be other solution

        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        int num = n * m;
        int e = (int) (0.5 * num) + (int) (num * Math.random());

        int p = (int) (0.2 * num * Math.random()); //magic < 1/5 of the number of maze grids
        int numBarrier = (int) (0.2 * num * Math.random());//initial barrier < 1/5 of the number of maze grids
        //p + numBarrier < 2/5 < 1/2 so that only 2 rows or 2 columns could be solvable

        int k = (int) ( Math.max(n, m) * Math.random() );
        int[] solvableMaze = new int[num];

        List barrier = getRandomBarrier(numBarrier, num-m, 1, m);
        for (int i = 0; i < (num - m); i ++)
            solvableMaze[i] = 0;
        for (Object o : barrier)
            solvableMaze[(int) o] = 1;

        List magics = getRandomBarrier(p, num-m, 1, m);
        List magicsTime = getRandomNumber(p, e-1, 1);

        List askTime = getRandomNumber(k, e-1, 1);

        //print
        System.out.println(n + " " + m + " " + e);
        printMaze(solvableMaze, m);
        printMagics(magics, magicsTime, m);
        System.out.println(k);
        for (Object o : askTime) System.out.println(o);
    }

    private static List getRandomBarrier(int len, int max, int min, int m){

        List list = new ArrayList();
        Random r = new Random();

        while(list.size() != len){
            int num = r.nextInt(max-min) + min;
            if (!list.contains(num) && (num % m != 0)) {
                list.add(num);
            }
        }
        return list;
    }

    private static List getRandomNumber (int len, int max, int min) {
        List list = new ArrayList();
        Random r = new Random();

        while(list.size() != len){
            int num = r.nextInt(max-min) + min;
            if (!list.contains(num)) {
                list.add(num);
            }
        }
        list.sort(Comparator.reverseOrder());
        return list;

    }

    private static void printMaze (int[] solvableMaze, int m) {
        for (int i = 0; i < solvableMaze.length; i++) {
            System.out.print( solvableMaze[i] + " ");
            if (i % m == m-1)
                System.out.println();
        }
    }

    private static void printMagics (List magics, List magicsTime, int m) {
        System.out.println(magicsTime.size());
        for (int i = 0; i < magicsTime.size(); i++)
            System.out.println (magicsTime.get(i) + " " + ((int)magics.get(i)) / m + " " + ((int)magics.get(i) % m + 1));
    }

    public static void main(String[] args) {
        generateInputCorner();
    }

}

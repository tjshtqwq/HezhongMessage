package sb.test;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long g = scanner.nextLong();
        long count = 1;
        double sum = 0.0;
        double pi = 0.0;
        double temp = 1.0;

        while (true) {
            if (count % 2 == 0) {
                sum -= 1.0 / temp;
            } else {
                sum += 1.0 / temp;
            }
            pi = 4.0 * sum;
            if (count % 100000000 == 0) {
                System.out.println("NOW PI: " + pi + " " + String.valueOf(count / g) + "%");
            }
            count++;
            temp += 2;
            if (count >= g) break;
        }
        System.out.println();
        System.out.println(pi);
        scanner.close();
    }
}

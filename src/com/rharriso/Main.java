package com.rharriso;

public class Main {
    public static void main(String[] args) {
        Integer boardCount = Integer.valueOf(args[0]);

        long startTime = System.nanoTime();

        for (int i = 0; i < boardCount; i++) {
            SudokuBoard board = new SudokuBoard(3);
            board.fill();
        }

        long endTime = System.nanoTime();

        double nanoToSecond = 1e-9;
        double seconds = (endTime - startTime) * nanoToSecond;
        System.out.println("Seconds: " + seconds);
        System.out.println("Board Count: " + boardCount);
        System.out.println("Boards per second: " + boardCount / seconds);
    }
}

package com.rharriso;

public class Main {
    public static void main(String[] args) {
        SudokuBoard board = new SudokuBoard(3);
        board.fill();
        System.out.println(board);
    }
}

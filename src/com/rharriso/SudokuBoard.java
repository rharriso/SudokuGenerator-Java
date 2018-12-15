package com.rharriso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SudokuBoard {
    private final int boardSize;
    private final int sizeSquared;

    private final List<Coord> cells = new ArrayList<>();
    private Map<Coord, Integer> cellValues = new HashMap<>();
    private Map<Coord, Set<Coord>> neighbors = new HashMap<>();
    private Set<Integer> validValues = new HashSet<>();

    SudokuBoard(int boardSize) {
        this.boardSize = boardSize;
        this.sizeSquared = boardSize * boardSize;

        List<Integer> valueRange = IntStream.range(1, sizeSquared + 1).boxed().collect(Collectors.toList());
        validValues.addAll(valueRange);

        for (int x = 0; x < sizeSquared; x++) {
            for (int y = 0; y < sizeSquared; y++) {
                Coord coord = new Coord(x, y);
                cells.add(coord);
                cellValues.put(coord, 0);
                neighbors.put(coord, findNeighbors(coord));
            }
        }
    }

    @Override
    public String toString() {
        return cells.stream()
            .map(c -> cellValues.get(c).toString())
            .collect(Collectors.joining("|"));
    }

    public void fill() {
        if (!doFill(new Coord(0, 0))){
            throw new RuntimeException("Unable to fill board: " + this);
        }
    }

    private Set<Coord> findNeighbors(Coord coord) {
        Set<Coord> result = new HashSet<>();

        // generate row neighbors
        for (int i = 0; i < boardSize; ++i) {
            result.add(new Coord(i, coord.y));
        }

        // generate col neighbors
        for (int j = 0; j < boardSize; ++j) {
            result.add(new Coord(coord.x, j));
        }

        int xFloor = (coord.x / boardSize) * boardSize;
        int yFloor = (coord.y / boardSize) * boardSize;

        // generate block neighbors
        for (int x = xFloor; x < xFloor + boardSize ; ++x) {
            for (int y = yFloor; y < yFloor + boardSize; ++y) {
                result.add(new Coord(x, y));
            }
        }

        return result;
    }

    private boolean doFill(Coord coord) {
        // get neighbor cellValues
        Set<Integer> neighborValues = neighbors.get(coord).stream()
            .map(c -> cellValues.get(c))
            .collect(Collectors.toSet());
        // filter valid cellValues
        HashSet<Integer> remainingValue = new HashSet<Integer>(validValues);
        remainingValue.removeAll(neighborValues);
        // shuffle remaining options
        List<Integer> remainingList = new ArrayList<>(remainingValue);
        Collections.shuffle(remainingList);

        for (Integer value : remainingList) {
            cellValues.put(coord, value);

            if (isLast(coord) || doFill(nextCell(coord))) {
                return true;
            }
        }

        // out of options backtrack
        cellValues.put(coord, 0);
        return false;
    }

    private Coord nextCell(Coord coord) {
        if (coord.y == sizeSquared - 1) {
            return new Coord(coord.x + 1, 0);
        }
        return new Coord(coord.x, coord.y + 1);
    }

    private boolean isLast(Coord coord) {
        return coord.x == sizeSquared - 1 &&
            coord.y == sizeSquared - 1;
    }
}

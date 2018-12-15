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
    private Map<Integer, Set<Coord>> neighbors = new HashMap<>();
    private Set<Integer> validValues = new HashSet<>();

    SudokuBoard(int boardSize) {
        this.boardSize = boardSize;
        this.sizeSquared = boardSize * boardSize;

        List<Integer> valueRange = IntStream.range(1, sizeSquared + 1).boxed().collect(Collectors.toList());
        validValues.addAll(valueRange);

        for (int x = 0; x < sizeSquared; x++) {
            for (int y = 0; y < sizeSquared; y++) {
                Coord coord = new Coord(x, y);
                neighbors.put(cells.size(), findNeighbors(coord));
                cells.add(coord);
                cellValues.put(coord, 0);
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
        if (!doFill(0)){
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

    private boolean doFill(int index) {
        Coord coord = cells.get(index);
        // get neighbor cellValues
        Set<Integer> neighborValues = new HashSet<>();

        for (Coord neighbor : neighbors.get(index)){
            neighborValues.add(cellValues.get(neighbor));
        }

        // filter valid cellValues
        HashSet<Integer> remainingValue = new HashSet<Integer>(validValues);
        remainingValue.removeAll(neighborValues);
        // shuffle remaining options
        List<Integer> remainingList = new ArrayList<>(remainingValue);
        Collections.shuffle(remainingList);

        for (Integer value : remainingList) {
            cellValues.put(coord, value);

            if (index == cells.size() - 1 || doFill(index + 1)) {
                return true;
            }
        }

        // out of options backtrack
        cellValues.put(coord, 0);
        return false;
    }
}

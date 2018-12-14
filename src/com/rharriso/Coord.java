package com.rharriso;

import java.util.Objects;

public class Coord {
    final int x;
    final int y;

    Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Coord)) return false;

        Coord otherCoord = (Coord) o;
        return otherCoord.x == x &&
            otherCoord.y == y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

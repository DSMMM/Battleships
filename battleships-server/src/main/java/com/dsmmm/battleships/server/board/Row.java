package com.dsmmm.battleships.server.board;

import java.util.Objects;

class Row {
    private final int value;

    Row(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Row row = (Row) o;
        return value == row.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    Row add(int y) {
        return new Row(value + y);
    }

    boolean inRange(Dimension dimension) {
        return value > 0 && dimension.greaterThanOrEqual(value);
    }
}
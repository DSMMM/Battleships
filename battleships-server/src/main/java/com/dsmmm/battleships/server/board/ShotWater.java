package com.dsmmm.battleships.server.board;

class ShotWater implements Water{

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass();
    }

    @Override
    public Field transform() {
        return this;
    }
}

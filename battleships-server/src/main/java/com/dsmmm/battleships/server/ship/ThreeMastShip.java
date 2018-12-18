package com.dsmmm.battleships.server.ship;

import com.dsmmm.battleships.server.board.Coordinates;
import com.dsmmm.battleships.server.board.Mast;
import com.dsmmm.battleships.server.board.NotShotMast;

import java.util.HashMap;
import java.util.Map;

class ThreeMastShip implements Ship {
    private Map<Coordinates, Mast> mapOfMasts;

    ThreeMastShip(Coordinates c1, Coordinates c2, Coordinates c3) {
        mapOfMasts = new HashMap<>();
        mapOfMasts = putMastsToShip(mapOfMasts, c1, c2, c3);

    }

    @Override
    public int countStandingMasts() {
        return (int) mapOfMasts.entrySet().stream().filter(s -> s.getValue() instanceof NotShotMast).count();
    }

    @Override
    public boolean checkIfShotOnShip(Coordinates c1) {
        return mapOfMasts.containsKey(c1) && mapOfMasts.get(c1).equals(new NotShotMast());
    }
}

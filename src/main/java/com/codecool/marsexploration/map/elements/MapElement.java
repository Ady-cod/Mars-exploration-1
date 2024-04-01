package com.codecool.marsexploration.map.elements;

import com.codecool.marsexploration.data.Coordinate;

public interface MapElement {

    boolean canBePlaced(String[][] mapGrid, Coordinate coordinate);

    void placeOnMap(String[][] mapGrid, Coordinate coordinate);

}

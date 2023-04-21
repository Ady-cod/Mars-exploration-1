package com.codecool.marsexploration.map.elements;

import com.codecool.marsexploration.data.Coordinate;
import com.codecool.marsexploration.util.MapUtil;


import java.util.*;

public abstract class ResourceElement implements MapElement {
    private final Coordinate coordinates;
    private final String symbol;

    public ResourceElement(Coordinate coordinates, String symbol) {
        this.coordinates = coordinates;
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    protected static Coordinate generateCoordinates(List<? extends TerrainElement> terrainElements, int mapDimension) {
        List<Coordinate> availableCoordinates = new ArrayList<>();

        for (TerrainElement terrainElement : terrainElements) {
            for (Coordinate coordinate : terrainElement.getCoordinates()) {
                List<Coordinate> adjacentCoordinates = MapUtil.getPossibleMoves(coordinate.x(), coordinate.y(),
                        new HashSet<>(), mapDimension);
                for (Coordinate adjacentCoordinate : adjacentCoordinates) {
                    if (!isCoordinateOccupied(adjacentCoordinate, terrainElements)) {
                        availableCoordinates.add(adjacentCoordinate);
                    }
                }
            }
        }

        if (availableCoordinates.isEmpty()) {
            throw new IllegalStateException("No available coordinates for placing the resource.");
        }

        Random random = new Random();
        return availableCoordinates.get(random.nextInt(availableCoordinates.size()));
    }

    private static boolean isCoordinateOccupied(Coordinate coordinate,
                                                List<? extends TerrainElement> terrainElements) {
        for (TerrainElement terrainElement : terrainElements) {
            if (terrainElement.getCoordinates().contains(coordinate)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canBePlaced(String[][] mapGrid, Coordinate coordinate) {
        // Check if the resource element can be placed at the given coordinate without overlapping other elements.
        return mapGrid[coordinate.x()][coordinate.y()] == null;
    }

    @Override
    public void placeOnMap(String[][] mapGrid, Coordinate coordinate) {
        // Place the resource element on the map grid at the given coordinate.
        // Update the map grid with the resource element's coordinate.
        mapGrid[coordinate.x()][coordinate.y()] = this.symbol;
    }



}

package com.codecool.marsexploration.map.elements;

import com.codecool.marsexploration.data.Coordinate;
import com.codecool.marsexploration.util.MapUtil;

import java.util.*;

public abstract class TerrainElement implements MapElement {
    private final Set<Coordinate> coordinates;
    private final int area;
    private final String symbol;

    public TerrainElement(Set<Coordinate> coordinates, int area, String symbol) {
        this.coordinates = coordinates;
        this.area = area;
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public Set<Coordinate> getCoordinates() {
        return coordinates;
    }

    public int getArea() {
        return area;
    }




    protected static Set<Coordinate> generateCoordinates(int area,int mapDimension) {
        Set<Coordinate> coordinates = new HashSet<>();
        Random random = new Random();

        int margin = mapDimension / 10;
        int x = margin + random.nextInt(mapDimension - 2 * margin);
        int y = margin + random.nextInt(mapDimension - 2 * margin);
        Coordinate startPoint = new Coordinate(x, y);
        coordinates.add(startPoint);


        int lastDirection = random.nextInt(4);
        int sameDirectionCount = 0;
        int maxSameDirectionCount = 2;

        for (int i = 1; i < area; i++) {
            List<Coordinate> possibleMoves = MapUtil.getAdjacentFreeSpots(x, y, coordinates, mapDimension);
            if (possibleMoves.isEmpty()) {
                break;
            }

            int direction = random.nextInt(possibleMoves.size());
            if (lastDirection == direction) {
                sameDirectionCount++;
            } else {
                sameDirectionCount = 0;
            }

            if (sameDirectionCount >= maxSameDirectionCount) {
                possibleMoves.remove(direction);
                if (possibleMoves.isEmpty()) {
                    break;
                }
                direction = random.nextInt(possibleMoves.size());
                sameDirectionCount = 0;
            }

            Coordinate nextMove = possibleMoves.get(direction);
            x = nextMove.x();
            y = nextMove.y();
            coordinates.add(nextMove);
            lastDirection = direction;
        }

        return coordinates;
    }

    @Override
    public boolean canBePlaced(String[][] mapGrid, Coordinate coordinate) {
        Set<Coordinate> elementCoordinates = this.getCoordinates();
        for (Coordinate elementCoordinate : elementCoordinates) {
            if (!isCoordinateValid(elementCoordinate, mapGrid.length)
                    || mapGrid[elementCoordinate.y()][elementCoordinate.x()] != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void placeOnMap(String[][] mapGrid, Coordinate coordinate) {
        Set<Coordinate> elementCoordinates = this.getCoordinates();
        for (Coordinate elementCoordinate : elementCoordinates) {
            mapGrid[elementCoordinate.y()][elementCoordinate.x()] = this.symbol;
        }
    }

    private boolean isCoordinateValid(Coordinate coordinate, int mapDimension) {
        int x = coordinate.x();
        int y = coordinate.y();
        return x >= 0 && x < mapDimension && y >= 0 && y < mapDimension;
    }




}

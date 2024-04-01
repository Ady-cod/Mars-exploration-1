package com.codecool.marsexploration.util;

import com.codecool.marsexploration.data.Coordinate;
import java.util.*;
public class MapUtil {

    public static List<Coordinate> getAdjacentFreeSpots(int x, int y, Set<Coordinate> currentCoordinates,
                                                        int mapDimension) {
        List<Coordinate> moves = new ArrayList<>();

        int[][] directions = {
                {-1, 0}, // left
                {1, 0},  // right
                {0, -1}, // up
                {0, 1}   // down
        };

        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];

            if (newX >= 0 && newX < mapDimension && newY >= 0 && newY < mapDimension) {
                Coordinate newCoordinate = new Coordinate(newX, newY);
                if (!currentCoordinates.contains(newCoordinate)) {
                    moves.add(newCoordinate);
                }
            }
        }

        return moves;
    }

}

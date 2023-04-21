package com.codecool.marsexploration.map;

import java.util.List;

public class MapConfiguration {
    public static final int COVERAGE_LIMIT = 5;
    private  String fileName;
    private final int mapWidth;
    private final int numberOfMinerals;
    private final int numberOfWaterPockets;
    private final List<Integer> minMountainAreas = List.of(10, 20, 30);
    private final List<Integer> minPitAreas = List.of(5, 15);

    public MapConfiguration(int mapWidth, String fileName) {
        this.fileName = fileName;
        this.mapWidth = mapWidth;
        this.numberOfMinerals = mapWidth/ COVERAGE_LIMIT; // coverageLimit
        this.numberOfWaterPockets = mapWidth/ COVERAGE_LIMIT; // coverageLimit
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getNumberOfMinerals() {
        return numberOfMinerals;
    }

    public int getNumberOfWaterPockets() {
        return numberOfWaterPockets;
    }


    public List<Integer> getMinMountainAreas() {
        return minMountainAreas;
    }

    public List<Integer> getMinPitAreas() {
        return minPitAreas;
    }

}

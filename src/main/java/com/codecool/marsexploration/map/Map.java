package com.codecool.marsexploration.map;

import com.codecool.marsexploration.data.Coordinate;
import com.codecool.marsexploration.map.elements.Mountain;
import com.codecool.marsexploration.map.elements.Pit;
import com.codecool.marsexploration.map.elements.ResourceElement;
import com.codecool.marsexploration.map.elements.TerrainElement;
import com.codecool.marsexploration.util.MapUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Map {
    private final MapConfiguration config;
    private final String[][] mapGrid;


    public Map(MapConfiguration config) {
        this.config = config;
        this.mapGrid = new String[config.getMapWidth()][config.getMapWidth()];
    }

    public void generateMapElements() {
        List<Mountain> mountains = placeMountains();
        List<Pit> pits = placePits();
        placeMinerals(mountains);
        placeWaterPockets(pits);

    }

    private List<Mountain> placeMountains() {
        List<Mountain> mountains = new ArrayList<>();
        List<Integer> mandatoryMountainAreas = config.getMinMountainAreas();
        int totalMapArea = config.getMapWidth() * config.getMapWidth();
        int targetCoverage = totalMapArea /2 ; // ~50% coverage
        int currentCoverage = 0;

        // Place mountains with mandatory areas
        for (int area : mandatoryMountainAreas) {
            Mountain mountain = new Mountain(area, config.getMapWidth(),"^");
            placeTerrainElement(mountains, mountain);
            currentCoverage += area;
        }

        // Place additional mountains with random areas until reaching the target coverage
        int attempts =0;
        while (currentCoverage < targetCoverage && attempts < 100) {
            int randomArea = generateMountainArea(config.getMapWidth());
            Mountain mountain = new Mountain(randomArea, config.getMapWidth(),"^");
            placeTerrainElement(mountains, mountain);
            currentCoverage += randomArea;
            attempts++;
        }

        return mountains;
    }

    private <T extends TerrainElement> void placeTerrainElement(List<T> terrainElements, T terrainElement) {
        boolean isPlaced = false;
        int attempts = 0;
        int maxAttempts = 100;

        while (!isPlaced && attempts < maxAttempts) {
            Coordinate randomCoordinate = getRandomCoordinate(config.getMapWidth());
            if (terrainElement.canBePlaced(mapGrid, randomCoordinate)) {
                terrainElement.placeOnMap(mapGrid, randomCoordinate);
                isPlaced = true;
                terrainElements.add( terrainElement);
            }
            attempts++;
        }
    }

    private static Coordinate getRandomCoordinate(int mapDimension) {
        Random random = new Random();
        int x = random.nextInt(mapDimension);
        int y = random.nextInt(mapDimension);
        return new Coordinate(x, y);
    }


    private int generateMountainArea(int mapDimension) {
        if (mapDimension < 50) {
            throw new IllegalArgumentException("The mapDimension should be at least 50.");
        }

        int maxArea = config.getMapWidth()* config.getMapWidth() / 50;
        int minArea = config.getMapWidth()* config.getMapWidth()/ 250;

        // Generate a random area within the allowed range (minArea to maxArea)
        Random random = new Random();
        return minArea + random.nextInt(maxArea - minArea + 1);
    }

    private List<Pit> placePits() {
        List<Pit> pits = new ArrayList<>();
        List<Integer> mandatoryPitAreas = config.getMinPitAreas();
        int totalMapArea = config.getMapWidth() * config.getMapWidth();
        int targetCoverage = totalMapArea / 4; // ~25% coverage
        int currentCoverage = 0;

        // Place pits with mandatory areas
        for (int area : mandatoryPitAreas) {
            Pit pit = new Pit(area, config.getMapWidth(),"#");
            placeTerrainElement(pits, pit);
            currentCoverage += area;
        }

        // Place additional pits with random areas until reaching the target coverage
        int attempts =0;
        int maxAttempts = 100;
        while (currentCoverage < targetCoverage && attempts < maxAttempts) {
            int randomArea = generatePitArea(config.getMapWidth());
            Pit pit = new Pit(randomArea, config.getMapWidth(),"#");
            placeTerrainElement(pits, pit);
            currentCoverage += randomArea;
            attempts++;
        }

        return pits;
    }

    private int generatePitArea(int mapDimension) {
        if (mapDimension < 50) {
            throw new IllegalArgumentException("The mapDimension should be at least 50.");
        }

        int maxArea = config.getMapWidth()* config.getMapWidth() / 100;
        int minArea = config.getMapWidth()* config.getMapWidth() / 500;

        // Generate a random area within the allowed range (minArea to maxArea)
        Random random = new Random();
        return minArea + random.nextInt(maxArea - minArea + 1);
    }

    private void placeMinerals(List<Mountain> mountains) {
        int numberOfMinerals = config.getNumberOfMinerals();
        placeResourceElement(mountains, numberOfMinerals, "*");
    }

    public void placeWaterPockets(List<Pit>pits) {
        int numberOfWaterPockets = config.getNumberOfWaterPockets();
        placeResourceElement(pits, numberOfWaterPockets, "~");
    }

    private void placeResourceElement(List<? extends TerrainElement> terrainElements, int numberOfResources, String resourceSymbol) {
        int placedResources = 0;

        while (placedResources < numberOfResources) {
            TerrainElement terrainElement = getRandomTerrainElement(terrainElements);
            List<Coordinate> adjacentCoordinates = new ArrayList<>();
            Set<Coordinate> terrainElementCoordinates = terrainElement.getCoordinates();
            for (Coordinate terrainElementCoordinate : terrainElementCoordinates) {
                List<Coordinate> adjacentFreeSpots = MapUtil.getAdjacentFreeSpots(terrainElementCoordinate.x(),
                        terrainElementCoordinate.y(), terrainElementCoordinates, config.getMapWidth());
                adjacentCoordinates.addAll(adjacentFreeSpots);
            }

            if (!adjacentCoordinates.isEmpty()) {
                Collections.shuffle(adjacentCoordinates);
                Coordinate randomAdjacentCoordinate = adjacentCoordinates.get(0);
                ResourceElement resource = new ResourceElement(randomAdjacentCoordinate, resourceSymbol) {
                };
                resource.placeOnMap(mapGrid, randomAdjacentCoordinate);
                placedResources++;
            }
        }
    }

    private TerrainElement getRandomTerrainElement(List<? extends TerrainElement> terrainElements) {
        Random random = new Random();
        int index = random.nextInt(terrainElements.size());
        return terrainElements.get(index);
    }


    public String mapToString() {
        StringBuilder mapString = new StringBuilder();

        for (int i = 0; i < config.getMapWidth(); i++) {
            for (int j = 0; j < config.getMapWidth(); j++) {
                String element = mapGrid[i][j];
                mapString.append(Objects.requireNonNullElse(element, " "));
            }
            mapString.append("\n");
        }

        return mapString.toString();
    }

    public void writeMapToFile() {
        String mapString = mapToString();
        String fileName = config.getFileName() + ".txt";
        Path outputPath = Paths.get("src","main", "resources", "generated_maps", fileName);

        try {
            Files.write(outputPath, mapString.getBytes());
            System.out.println("Map has been successfully generated and saved to the file: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing map to file: " + fileName);
            e.printStackTrace();
        }
    }

}

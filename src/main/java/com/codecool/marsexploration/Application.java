package com.codecool.marsexploration;

import com.codecool.marsexploration.map.Map;
import com.codecool.marsexploration.map.MapConfiguration;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int mapWidth = 0;

        // Prompt for map width with validation for number >= 50
        while (mapWidth < 50) {
            System.out.println("Please enter the width of the map (minimum 50):");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number for the width:");
                scanner.next(); // Consume the invalid input
            }
            mapWidth = scanner.nextInt();
            if (mapWidth < 50) {
                System.out.println("The width should be at least 50.");
            }
            scanner.nextLine(); // Consume newline left-over
        }

        // Prompt for a valid filename after confirming map width
        System.out.println("Please enter the name for the map:");
        String fileName = scanner.nextLine().trim();

        while (fileName.length() < 3 || !fileName.matches("^[a-zA-Z0-9._-]+$")) {
            System.out.println("Invalid filename. Please enter a valid name (at least 3 characters, no special characters except ., _, -):");
            fileName = scanner.nextLine().trim();
        }

        // Create a MapConfiguration instance and generate the map
        MapConfiguration config = new MapConfiguration(mapWidth, fileName);
        Map marsMap = new Map(config);
        marsMap.generateMapElements();

        // Write the map to a file
        marsMap.writeMapToFile();
    }

}

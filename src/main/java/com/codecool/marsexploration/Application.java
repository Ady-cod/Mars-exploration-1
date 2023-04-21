package com.codecool.marsexploration;

import com.codecool.marsexploration.map.Map;
import com.codecool.marsexploration.map.MapConfiguration;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
//        Random random = new Random();
//        System.out.println(random.nextInt(42));
//    }
        // Get the map width from the user
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the width of the map (minimum 50):");
        int mapWidth = scanner.nextInt();
        while (mapWidth < 50) {
            System.out.println("The width should be at least 50. Please enter a valid width:");
            mapWidth = scanner.nextInt();
        }

        System.out.println("Please enter the name for the map:");
        String fileName = scanner.nextLine().trim();
        while (true) {
            boolean result = false;
            if (fileName.length() >= 3) {
                result = fileName.matches("^[a-zA-Z0-9._-]+$");
            }
            if (result) break;
            System.out.println("Please type a name. Enter a valid name:");
            fileName = scanner.nextLine().trim();
        }




        // Create a MapConfiguration instance and generate the map
        MapConfiguration config = new MapConfiguration(mapWidth,fileName);
        Map marsMap = new Map(config);
        marsMap.generateMapElements();

        // Write the map to a file
        marsMap.writeMapToFile();
        System.out.println("Map has been successfully generated and saved to the file: " + config.getFileName());
    }

}

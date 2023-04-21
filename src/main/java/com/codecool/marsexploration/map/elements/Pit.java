package com.codecool.marsexploration.map.elements;


import java.util.*;

public class Pit extends TerrainElement {

    public Pit(int area,int mapDimension, String symbol) {
        super(generateCoordinates(area,mapDimension), area, symbol);
    }




}

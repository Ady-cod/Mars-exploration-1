package com.codecool.marsexploration.map.elements;


import java.util.*;

public class Mountain extends TerrainElement{

    public Mountain(int area, int mapDimension, String symbol){

        super(generateCoordinates(area,mapDimension),
                area, symbol);
    }

}

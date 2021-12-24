package de.g3sit.fastbridge.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class Locations {


    /**
     * checks if all locations are in the same world
     *
     * if no locations are given, true is returned
     * @param locs all location to check
     * @return true if all locations have the same world
     */
    public static boolean isSameWorld(Location... locs){
        return Arrays.stream(locs).map(Location::getWorld).distinct().count()<=1;
    }

    /**
     * creates a new location with the coordinate components which are smaller than from any other location
     *
     * yaw and pitch are omitted
     * @param locs all locations which have to be in the same world
     * @return a new location with the xyz coordinates and without yaw and pitch
     */
    public static Location minLocation(Location... locs) throws IllegalArgumentException{
        if(!isSameWorld(locs)){
            throw new IllegalArgumentException("Locations aren't in same world");
        }
        if(locs.length <= 0){
            throw new IllegalArgumentException("no locations given");
        }

        return Arrays.stream(locs).map(Location::toVector).reduce(Vector::getMinimum).orElse(new Vector()).toLocation(locs[0].getWorld());
    }

    /**
     * creates a new location with the coordinate components which are bigger than from any other location
     *
     * yaw and pitch are omitted
     * @param locs all locations which have to be in the same world
     * @return a new location with the xyz coordinates and without yaw and pitch
     */
    public static Location maxLocation(Location... locs) throws IllegalArgumentException{
        if(!isSameWorld(locs)){
            throw new IllegalArgumentException("Locations aren't in same world");
        }
        if(locs.length <= 0){
            throw new IllegalArgumentException("no locations given");
        }

        return Arrays.stream(locs).map(Location::toVector).reduce(Vector::getMaximum).orElse(new Vector()).toLocation(locs[0].getWorld());
    }
}

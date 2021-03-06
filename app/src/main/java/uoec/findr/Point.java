package uoec.findr;

import java.util.LinkedList;

/**
 * Created by tmetade on 2017-01-14.
 */
public class Point {
     Integer id;
     String name;
     int xCord;
     int yCord;
     int floor;
    LinkedList<Integer> neighbours;

    //Constuctor
    public Point(String name, int xCord, int yCord, int floor) {
        this.name = name;
        this.xCord = xCord;
        this.yCord = yCord;
        this.floor = floor;
        neighbours = new LinkedList<>();
    }

    //Setters and Getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getXCord() {
        return xCord;
    }

    public void setXCord(int xCord) {
        this.xCord = xCord;
    }

    public int getYCord() {
        return yCord;
    }

    public void setYCord(int yCord) {
        this.yCord = yCord;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void addNeighbour(int id) {
        neighbours.add(id);
    }

    public LinkedList<Integer> getNeighbours() {
        return neighbours;
    }

}

package ru.adel;

public class Node{

    private int vertex;
    private double weight;

    public Node(int v, double w)
    {
        vertex = v;
        weight = w;
    }

    public void setVertex(int vertex) {
        this.vertex = vertex;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getVertex() {
        return vertex;
    }

    public double getWeight() {
        return weight;
    }

}

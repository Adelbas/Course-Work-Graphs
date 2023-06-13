package ru.adel;


public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //Вычисление расстояния между двумя точками на плоскости
    public static double calculateDistance(Point p1, Point p2) {
        int x = Math.abs(p1.x-p2.x);
        int y = Math.abs(p1.y-p2.y);
        return Math.sqrt(x*x+y*y);
    }

    @Override
    public String toString() {
        return "( "+x+" "+y+" )";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return 31*x+y;
    }
}

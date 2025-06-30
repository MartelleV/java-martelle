package DesignPatterns;

interface Shape extends Cloneable {
    Shape clone();
    void draw();
}

class Circle implements Shape {
    public Shape clone() {
        return new Circle();
    }

    public void draw() {
        System.out.println("Drawing a Circle");
    }
}

public class Prototype {
    public static void main(String[] args) {
        Shape circle = new Circle();
        Shape clonedCircle = circle.clone();
        clonedCircle.draw(); // Outputs: Drawing a Circle
    }
}

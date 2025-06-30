package DesignPatterns;

interface Coffee {
    String getDescription();
    double getCost();
}

class SimpleCoffee implements Coffee {
    public String getDescription() {
        return "Simple Coffee";
    }

    public double getCost() {
        return 5.0;
    }
}

abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    public String getDescription() {
        return coffee.getDescription();
    }

    public double getCost() {
        return coffee.getCost();
    }
}

class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    public String getDescription() {
        return coffee.getDescription() + ", Milk";
    }

    public double getCost() {
        return coffee.getCost() + 1.5;
    }
}

public class Decorator {
    public static void main(String[] args) {
        Coffee coffee = new SimpleCoffee();
        Coffee milkCoffee = new MilkDecorator(coffee);
        System.out.println(milkCoffee.getDescription() + " costs $" + milkCoffee.getCost());
        // Outputs: Simple Coffee, Milk costs $6.5
    }
}

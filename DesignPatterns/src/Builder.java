package DesignPatterns;

class Meal {
    private String mainCourse;
    private String side;
    private String drink;

    public void setMainCourse(String mainCourse) {
        this.mainCourse = mainCourse;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public void showMeal() {
        System.out.println("Main Course: " + mainCourse);
        System.out.println("Side: " + side);
        System.out.println("Drink: " + drink);
    }
}

class MealBuilder {
    public Meal buildVegMeal() {
        Meal meal = new Meal();
        meal.setMainCourse("Veg Burger");
        meal.setSide("Fries");
        meal.setDrink("Coke");
        return meal;
    }
}

public class Builder {
    public static void main(String[] args) {
        MealBuilder builder = new MealBuilder();
        Meal vegMeal = builder.buildVegMeal();
        vegMeal.showMeal();
        // Outputs:
        // Main Course: Veg Burger
        // Side: Fries
        // Drink: Coke
    }
}

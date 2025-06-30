package DesignPatterns;

interface Animal {
    void speak();
}

class Dog implements Animal {
    public void speak() {
        System.out.println("Woof!");
    }
}

class Cat implements Animal {
    public void speak() {
        System.out.println("Meow!");
    }
}

class AnimalFactory {
    public Animal createAnimal(String type) {
        if (type.equalsIgnoreCase("Dog")) {
            return new Dog();
        } else if (type.equalsIgnoreCase("Cat")) {
            return new Cat();
        }
        return null;
    }
}

public class Factory {
    public static void main(String[] args) {
        AnimalFactory factory = new AnimalFactory();
        Animal animal = factory.createAnimal("Dog");
        animal.speak(); // Outputs: Woof!
    }
}

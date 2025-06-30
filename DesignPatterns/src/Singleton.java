package DesignPatterns;

public class Singleton {
    // The single instance
    private static Singleton instance;

    // Private constructor to prevent instantiation
    private Singleton() {}

    // Public method to access the instance
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    // Method to demonstrate functionality
    public void showMessage() {
        System.out.println("Hello from Singleton!");
    }

    public static void main(String[] args) {
        Singleton singleton = Singleton.getInstance();
        singleton.showMessage(); // Outputs: Hello from Singleton!
    }
}

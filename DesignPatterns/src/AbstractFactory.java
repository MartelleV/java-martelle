package DesignPatterns;

interface Button {
    void paint();
}

class WindowsButton implements Button {
    public void paint() {
        System.out.println("Painting a Windows button");
    }
}

class MacButton implements Button {
    public void paint() {
        System.out.println("Painting a Mac button");
    }
}

interface GUIFactory {
    Button createButton();
}

class WindowsFactory implements GUIFactory {
    public Button createButton() {
        return new WindowsButton();
    }
}

class MacFactory implements GUIFactory {
    public Button createButton() {
        return new MacButton();
    }
}

public class AbstractFactory {
    public static void main(String[] args) {
        GUIFactory windowsFactory = new WindowsFactory();
        Button button = windowsFactory.createButton();
        button.paint(); // Outputs: Painting a Windows button

        GUIFactory macFactory = new MacFactory();
        Button button2 = macFactory.createButton();
        button2.paint();
    }
}

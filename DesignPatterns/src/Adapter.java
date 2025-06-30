package DesignPatterns;

interface NewSystem {
    void newRequest();
}

class LegacySystem {
    public void oldRequest() {
        System.out.println("Legacy System Request");
    }
}

class AdapterClass implements NewSystem {
    private final LegacySystem legacySystem;

    public AdapterClass(LegacySystem legacySystem) {
        this.legacySystem = legacySystem;
    }

    public void newRequest() {
        legacySystem.oldRequest();
    }
}

public class Adapter {
    public static void main(String[] args) {
        LegacySystem legacy = new LegacySystem();
        NewSystem adapter = new AdapterClass(legacy);
        adapter.newRequest(); // Outputs: Legacy System Request
    }
}
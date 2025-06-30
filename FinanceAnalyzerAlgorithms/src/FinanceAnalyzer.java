import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FinanceAnalyzer {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    // Main CLI interface
    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = getUserChoice();
            if (choice == 0) break;

            switch (choice) {
                case 1 -> runMovingAverageCrossover();
                case 2 -> runMonteCarloSimulation();
                case 3 -> runBudgetAllocation();
                case 4 -> runExpenseClustering();
                case 5 -> runDebtRepaymentOptimization();
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n=== Finance Analyzer ===");
        System.out.println("1. Moving Average Crossover (Stock Analysis)");
        System.out.println("2. Monte Carlo Savings Simulation");
        System.out.println("3. Budget Allocation (Graph-based)");
        System.out.println("4. Expense Clustering (K-Means)");
        System.out.println("5. Debt Repayment Optimization");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }

    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Algorithm 1: Moving Average Crossover for Stock Analysis
    private static void runMovingAverageCrossover() {
        while (true) {
            try {
                List<Double> prices = readStockPrices("stock_prices.csv");

                System.out.print("Enter short-term MA period (e.g., 10): ");
                int shortPeriod = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter long-term MA period (e.g., 50): ");
                int longPeriod = Integer.parseInt(scanner.nextLine());

                if (shortPeriod <= 0 || longPeriod <= 0) {
                    System.out.println("Periods must be positive integers. Try again.\n");
                    continue;
                }
                if (shortPeriod >= longPeriod) {
                    System.out.println("Short-term period must be less than long-term period. Try again.\n");
                    continue;
                }
                if (longPeriod > prices.size()) {
                    System.out.println("Long-term period exceeds number of data points (" + prices.size() + "). Try again.\n");
                    continue;
                }

                List<Double> shortMA = calculateMovingAverage(prices, shortPeriod);
                List<Double> longMA = calculateMovingAverage(prices, longPeriod);

                // Align both lists to the same size (cut from the end of the longer one)
                int minSize = Math.min(shortMA.size(), longMA.size());
                shortMA = shortMA.subList(shortMA.size() - minSize, shortMA.size());
                longMA = longMA.subList(longMA.size() - minSize, longMA.size());

                List<String> signals = generateTradingSignals(shortMA, longMA);
                saveSignalsToFile(signals, "trading_signals.csv");

                System.out.println("Trading signals generated and saved to trading_signals.csv");
                break; // Exit loop on success

            } catch (IOException e) {
                System.out.println("Error reading stock prices: " + e.getMessage());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter numeric values for periods.");
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    private static List<Double> readStockPrices(String fileName) throws IOException {
        List<Double> prices = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                prices.add(Double.parseDouble(line));
            }
        }
        return prices;
    }

    private static List<Double> calculateMovingAverage(List<Double> prices, int period) {
        List<Double> ma = new ArrayList<>();
        for (int i = 0; i <= prices.size() - period; i++) {
            double sum = 0;
            for (int j = i; j < i + period; j++) {
                sum += prices.get(j);
            }
            ma.add(sum / period);
        }
        return ma;
    }

    private static List<String> generateTradingSignals(List<Double> shortMA, List<Double> longMA) {
        List<String> signals = new ArrayList<>();
        for (int i = 1; i < shortMA.size(); i++) {
            if (shortMA.get(i - 1) < longMA.get(i - 1) && shortMA.get(i) > longMA.get(i)) {
                signals.add("Day " + (i + 1) + ": BUY");
            } else if (shortMA.get(i - 1) > longMA.get(i - 1) && shortMA.get(i) < longMA.get(i)) {
                signals.add("Day " + (i + 1) + ": SELL");
            } else {
                signals.add("Day " + (i + 1) + ": HOLD");
            }
        }
        return signals;
    }

    private static void saveSignalsToFile(List<String> signals, String fileName) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
            for (String signal : signals) {
                writer.write(signal);
                writer.newLine();
            }
        }
    }

    // Algorithm 2: Monte Carlo Simulation for Savings
    private static void runMonteCarloSimulation() {
        System.out.print("Enter initial investment ($): ");
        double initial = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter years to simulate: ");
        int years = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter number of simulations: ");
        int simulations = Integer.parseInt(scanner.nextLine());

        List<Double> finalBalances = new ArrayList<>();
        for (int i = 0; i < simulations; i++) {
            finalBalances.add(monteCarloSimulation(initial, years));
        }

        double average = finalBalances.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double stdDev = calculateStandardDeviation(finalBalances, average);

        System.out.printf("Average final balance: $%.2f%n", average);
        System.out.printf("Standard deviation: $%.2f%n", stdDev);
        try {
            saveSimulationResults(finalBalances, "monte_carlo_results.csv");
            System.out.println("Simulation results saved to monte_carlo_results.csv");
        } catch (IOException e) {
            System.out.println("Error saving results: " + e.getMessage());
        }
    }

    private static double monteCarloSimulation(double initial, int years) {
        double balance = initial;
        for (int i = 0; i < years; i++) {
            double annualReturn = random.nextGaussian() * 0.05 + 0.07; // Mean 7%, SD 5%
            balance *= (1 + annualReturn);
        }
        return balance;
    }

    private static double calculateStandardDeviation(List<Double> values, double mean) {
        double sum = values.stream().mapToDouble(v -> Math.pow(v - mean, 2)).sum();
        return Math.sqrt(sum / values.size());
    }

    private static void saveSimulationResults(List<Double> balances, String fileName) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
            for (Double balance : balances) {
                writer.write(String.format("%.2f", balance));
                writer.newLine();
            }
        }
    }

    // Algorithm 3: Budget Allocation (Graph-based)
    private static void runBudgetAllocation() {
        System.out.print("Enter total budget ($): ");
        double totalBudget = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter number of categories: ");
        int numCategories = Integer.parseInt(scanner.nextLine());

        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < numCategories; i++) {
            System.out.print("Enter name for category " + (i + 1) + ": ");
            String name = scanner.nextLine();
            System.out.print("Enter priority (1-10) for " + name + ": ");
            int priority = Integer.parseInt(scanner.nextLine());
            categories.add(new Category(name, priority));
        }

        List<Double> allocations = allocateBudget(categories, totalBudget);
        System.out.println("\nBudget Allocation:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.printf("%s: $%.2f%n", categories.get(i).name, allocations.get(i));
        }
    }

    private static class Category {
        String name;
        int priority;

        Category(String name, int priority) {
            this.name = name;
            this.priority = Math.max(1, Math.min(10, priority));
        }
    }

    private static List<Double> allocateBudget(List<Category> categories, double totalBudget) {
        double totalPriority = categories.stream().mapToInt(c -> c.priority).sum();
        List<Double> allocations = new ArrayList<>();
        for (Category category : categories) {
            double allocation = (category.priority / totalPriority) * totalBudget;
            allocations.add(allocation);
        }
        return allocations;
    }

    // Algorithm 4: Expense Clustering (K-Means)
    private static void runExpenseClustering() {
        try {
            List<Expense> expenses = readExpenses("expenses.csv");
            System.out.print("Enter number of clusters (k): ");
            int k = Integer.parseInt(scanner.nextLine());

            List<List<Expense>> clusters = kMeansClustering(expenses, k);
            saveClustersToFile(clusters, "expense_clusters.csv");
            System.out.println("Expense clusters saved to expense_clusters.csv");
        } catch (IOException e) {
            System.out.println("Error reading expenses: " + e.getMessage());
        }
    }

    private static class Expense {
        double amount;
        double date;

        Expense(double amount, double date) {
            this.amount = amount;
            this.date = date;
        }

        double distance(Expense other) {
            return Math.sqrt(Math.pow(this.amount - other.amount, 2) + Math.pow(this.date - other.date, 2));
        }
    }

    private static List<Expense> readExpenses(String fileName) throws IOException {
        List<Expense> expenses = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                expenses.add(new Expense(Double.parseDouble(parts[0]), Double.parseDouble(parts[1])));
            }
        }
        return expenses;
    }

    private static List<List<Expense>> kMeansClustering(List<Expense> expenses, int k) {
        List<Expense> centroids = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            centroids.add(expenses.get(random.nextInt(expenses.size())));
        }

        List<List<Expense>> clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            clusters.add(new ArrayList<>());
        }

        boolean changed = true;
        while (changed) {
            for (List<Expense> cluster : clusters) {
                cluster.clear();
            }

            for (Expense expense : expenses) {
                int closest = 0;
                double minDistance = expense.distance(centroids.getFirst());
                for (int i = 1; i < centroids.size(); i++) {
                    double distance = expense.distance(centroids.get(i));
                    if (distance < minDistance) {
                        minDistance = distance;
                        closest = i;
                    }
                }
                clusters.get(closest).add(expense);
            }

            changed = false;
            for (int i = 0; i < k; i++) {
                if (clusters.get(i).isEmpty()) continue;
                double avgAmount = clusters.get(i).stream().mapToDouble(e -> e.amount).average().orElse(0);
                double avgDate = clusters.get(i).stream().mapToDouble(e -> e.date).average().orElse(0);
                Expense newCentroid = new Expense(avgAmount, avgDate);
                if (newCentroid.distance(centroids.get(i)) > 0.001) {
                    centroids.set(i, newCentroid);
                    changed = true;
                }
            }
        }
        return clusters;
    }

    private static void saveClustersToFile(List<List<Expense>> clusters, String fileName) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
            for (int i = 0; i < clusters.size(); i++) {
                writer.write("Cluster " + (i + 1) + ":");
                writer.newLine();
                for (Expense expense : clusters.get(i)) {
                    writer.write(String.format("%.2f,%.2f", expense.amount, expense.date));
                    writer.newLine();
                }
            }
        }
    }

    // Algorithm 5: Debt Repayment Optimization
    private static void runDebtRepaymentOptimization() {
        System.out.print("Enter number of debts: ");
        int numDebts = Integer.parseInt(scanner.nextLine());
        List<Debt> debts = new ArrayList<>();

        for (int i = 0; i < numDebts; i++) {
            System.out.print("Enter balance for debt " + (i + 1) + ": ");
            double balance = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter interest rate for debt " + (i + 1) + " (%): ");
            double rate = Double.parseDouble(scanner.nextLine());
            debts.add(new Debt(balance, rate / 100));
        }

        System.out.print("Enter monthly payment amount: ");
        double monthlyPayment = Double.parseDouble(scanner.nextLine());

        List<String> plan = optimizeDebtRepayment(debts, monthlyPayment);
        System.out.println("\nDebt Repayment Plan:");
        plan.forEach(System.out::println);
    }

    private static class Debt {
        double balance;
        double interestRate;

        Debt(double balance, double interestRate) {
            this.balance = balance;
            this.interestRate = interestRate;
        }
    }

    private static List<String> optimizeDebtRepayment(List<Debt> debts, double monthlyPayment) {
        List<String> plan = new ArrayList<>();
        while (debts.stream().anyMatch(d -> d.balance > 0)) {
            debts.forEach(d -> d.balance *= (1 + d.interestRate / 12)); // Monthly interest
            Debt highestInterest = debts.stream()
                    .filter(d -> d.balance > 0)
                    .max(Comparator.comparingDouble(d -> d.interestRate))
                    .orElse(null);
            if (highestInterest == null) break;

            double payment = Math.min(monthlyPayment, highestInterest.balance);
            highestInterest.balance -= payment;
            plan.add(String.format("Pay $%.2f to debt with %.2f%% interest, remaining: $%.2f",
                    payment, highestInterest.interestRate * 100, highestInterest.balance));
        }
        return plan;
    }
}
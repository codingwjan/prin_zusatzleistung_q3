package fridge;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final FridgeRepository repo = new FridgeRepository();

    public static void main(String[] args) {
        // Sicherstellen, dass DB da ist
        Db.ensureDatabase();

        System.out.println("Willkommen in Herr Rohdes Kühlschrank!");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addItem();
                    break;
                case "2":
                    removeItem();
                    break;
                case "3":
                    showItems();
                    break;
                case "4":
                    repo.reset();
                    break;
                case "0":
                    System.out.println("Auf Wiedersehen!");
                    running = false;
                    break;
                default:
                    System.out.println("Ungültige Eingabe. Bitte wählen Sie 0-4.");
            }
            System.out.println(); // Leerzeile für bessere Lesbarkeit
        }
    }

    private static void printMenu() {
        System.out.println("--- MENÜ ---");
        System.out.println("1) Item hinzufügen");
        System.out.println("2) Item entfernen");
        System.out.println("3) Inhalt anzeigen");
        System.out.println("4) Reset (Alles löschen)");
        System.out.println("0) Beenden");
        System.out.print("Ihre Wahl: ");
    }

    private static void addItem() {
        String name = readName("Name des Items: ");
        int amount = readPositiveInt("Menge hinzufügen: ");
        repo.add(name, amount);
    }

    private static void removeItem() {
        String name = readName("Welches Item entfernen? ");
        int amount = readPositiveInt("Menge entfernen: ");
        repo.remove(name, amount);
    }

    private static void showItems() {
        List<Item> items = repo.listAll();
        if (items.isEmpty()) {
            System.out.println("Der Kühlschrank ist leer.");
        } else {
            System.out.println("--- Inhalt ---");
            for (Item item : items) {
                System.out.println(item);
            }
        }
    }

    // Hilfsmethode für Texteingabe
    private static String readName(String prompt) {
        String input = "";
        while (input.isEmpty()) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Name darf nicht leer sein.");
            }
        }
        return input;
    }

    // Hilfsmethode für Zahleneingabe
    private static int readPositiveInt(String prompt) {
        int number = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                number = Integer.parseInt(raw);
                if (number > 0) {
                    valid = true;
                } else {
                    System.out.println("Bitte eine positive Zahl (>0) eingeben.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Das war keine gültige Zahl.");
            }
        }
        return number;
    }
}

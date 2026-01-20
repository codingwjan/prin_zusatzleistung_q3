package fridge;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FridgeRepository {

    public void add(String name, int amount) {
        // Erst prüfen ob Item existiert
        String selectSql = "SELECT quantity FROM items WHERE name = ?";
        String insertSql = "INSERT INTO items(name, quantity) VALUES(?, ?)";
        String updateSql = "UPDATE items SET quantity = ? WHERE name = ?";

        try (Connection conn = Db.getConnection()) {
            // Prüfen ob existiert
            try (PreparedStatement checkStmt = conn.prepareStatement(selectSql)) {
                checkStmt.setString(1, name);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    // Aktualisieren
                    int current = rs.getInt("quantity");
                    int newQty = current + amount;
                    try (PreparedStatement upStmt = conn.prepareStatement(updateSql)) {
                        upStmt.setInt(1, newQty);
                        upStmt.setString(2, name);
                        upStmt.executeUpdate();
                    }
                    System.out.println("Item aktualisiert: " + name + " (Neuer Bestand: " + newQty + ")");
                } else {
                    // Einfügen
                    try (PreparedStatement inStmt = conn.prepareStatement(insertSql)) {
                        inStmt.setString(1, name);
                        inStmt.setInt(2, amount);
                        inStmt.executeUpdate();
                    }
                    System.out.println("Neues Item angelegt: " + name + " (Bestand: " + amount + ")");
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Hinzufügen: " + e.getMessage());
        }
    }

    public void remove(String name, int amount) {
        String selectSql = "SELECT quantity FROM items WHERE name = ?";
        String updateSql = "UPDATE items SET quantity = ? WHERE name = ?";
        String deleteSql = "DELETE FROM items WHERE name = ?";

        try (Connection conn = Db.getConnection()) {
            try (PreparedStatement checkStmt = conn.prepareStatement(selectSql)) {
                checkStmt.setString(1, name);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    int current = rs.getInt("quantity");

                    if (current < amount) {
                        System.out.println("Fehler: Nicht genügend " + name + " vorhanden (Aktuell: " + current + ").");
                        return;
                    }

                    int newQty = current - amount;
                    if (newQty == 0) {
                        // Löschen
                        try (PreparedStatement delStmt = conn.prepareStatement(deleteSql)) {
                            delStmt.setString(1, name);
                            delStmt.executeUpdate();
                        }
                        System.out.println("Item aufgebraucht und gelöscht: " + name);
                    } else {
                        // Aktualisieren
                        try (PreparedStatement upStmt = conn.prepareStatement(updateSql)) {
                            upStmt.setInt(1, newQty);
                            upStmt.setString(2, name);
                            upStmt.executeUpdate();
                        }
                        System.out.println(amount + "x " + name + " entfernt. Rest: " + newQty);
                    }
                } else {
                    System.out.println("Item nicht gefunden: " + name);
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Entfernen: " + e.getMessage());
        }
    }

    public List<Item> listAll() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT name, quantity FROM items ORDER BY name";

        try (Connection conn = Db.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(new Item(rs.getString("name"), rs.getInt("quantity")));
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Laden: " + e.getMessage());
        }
        return items;
    }

    public void reset() {
        try (Connection conn = Db.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM items");
            System.out.println("Kühlschrank wurde komplett geleert.");
        } catch (SQLException e) {
            System.err.println("Reset fehlgeschlagen: " + e.getMessage());
        }
    }
}

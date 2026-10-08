package battleship;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Scoreboard {

    private static final String DB_URL = "jdbc:sqlite:battleship.db";

    public Scoreboard() {
        createTable();
    }

    private void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS games (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    player TEXT NOT NULL,
                    result TEXT NOT NULL,
                    shots INTEGER NOT NULL,
                    hits INTEGER NOT NULL,
                    sunk_ships INTEGER NOT NULL,
                    played_at TEXT NOT NULL
                )
                """;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar a tabela de jogos.", e);
        }
    }

    public void saveGame(Game game, String player, String result) {

        String sql = """
                INSERT INTO games
                (player, result, shots, hits, sunk_ships, played_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        int shots = game.getMyMoves().stream()
                .mapToInt(move -> move.getShots().size())
                .sum();

        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, player);
            statement.setString(2, result);
            statement.setInt(3, shots);
            statement.setInt(4, game.getHits());
            statement.setInt(5, game.getSunkShips());
            statement.setString(6, date);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao guardar o jogo.", e);
        }
    }

    public void showScores() {

        String sql = """
                SELECT player, result, shots, hits, sunk_ships, played_at
                FROM games
                ORDER BY id DESC
                """;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println();
            System.out.println("================ SCOREBOARD ================");
            System.out.printf("%-15s %-10s %-8s %-8s %-8s %-20s%n",
                    "Jogador", "Resultado", "Tiros", "Acertos", "Afundados", "Data");

            boolean hasGames = false;

            while (resultSet.next()) {

                hasGames = true;

                System.out.printf("%-15s %-10s %-8d %-8d %-8d %-20s%n",
                        resultSet.getString("player"),
                        resultSet.getString("result"),
                        resultSet.getInt("shots"),
                        resultSet.getInt("hits"),
                        resultSet.getInt("sunk_ships"),
                        resultSet.getString("played_at"));
            }

            if (!hasGames) {
                System.out.println("Ainda não existem jogos registados.");
            }

            System.out.println("============================================");
            System.out.println();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar o scoreboard.", e);
        }
    }
}
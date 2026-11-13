package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Scoreboard.
 * Author: ${user.name}
 * Date: 2026-11-10 19:15
 *
 * Cyclomatic Complexity:
 * - Scoreboard(): 1
 * - createTable(): 2
 * - saveGame(Game, String, String): 2
 * - showScores(): 4
 */
public class ScoreboardTest {

    private Scoreboard scoreboard;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        scoreboard = new Scoreboard();
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        scoreboard = null;
    }

    // Helper para criar uma instância válida de Game
    private Game createSampleGame() {
        Fleet fleet = new Fleet();
        return new Game(fleet);
    }

    // --- Scoreboard() / createTable() Tests (CC = 1 e CC = 2) ---

    @Test
    @DisplayName("scoreboard: Verify constructor and database table initialization")
    public void scoreboard() {
        assertNotNull(scoreboard, "Error: Scoreboard instance should not be null after initialization.");
    }

    @Test
    @DisplayName("createTable1: Verify idempotent table creation without exception")
    public void createTable1() {
        assertDoesNotThrow(() -> new Scoreboard(),
                "Error: Instantiating Scoreboard multiple times should not throw an exception.");
    }

    @Test
    @DisplayName("createTable2: Verify table availability by running showScores on fresh instance")
    public void createTable2() {
        Scoreboard freshScoreboard = new Scoreboard();
        assertDoesNotThrow(freshScoreboard::showScores,
                "Error: Querying table right after creation should succeed.");
    }

    // --- saveGame() Tests (CC = 2) ---

    @Test
    @DisplayName("saveGame1: Verify saving game successfully with valid game instance")
    public void saveGame1() {
        Game game = createSampleGame();
        assertDoesNotThrow(() -> scoreboard.saveGame(game, "Player1", "VITÓRIA"),
                "Error: saveGame should execute without throwing exceptions.");
    }

    @Test
    @DisplayName("saveGame2: Verify saved game reflects in database output")
    public void saveGame2() {
        Game game = createSampleGame();
        scoreboard.saveGame(game, "TestUser", "DERROTA");
        scoreboard.showScores();

        String output = outputStreamCaptor.toString();
        assertAll("Verifying saved game details in scoreboard output",
                () -> assertTrue(output.contains("TestUser"), "Error: Scoreboard output should contain 'TestUser'."),
                () -> assertTrue(output.contains("DERROTA"), "Error: Scoreboard output should contain 'DERROTA'.")
        );
    }

    // --- showScores() Tests (CC = 4) ---

    @Test
    @DisplayName("showScores1: Verify output when no games exist in database")
    public void showScores1() {
        scoreboard.showScores();
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("SCOREBOARD"), "Error: Output should contain header banner.");
    }

    @Test
    @DisplayName("showScores2: Verify output branch when games are present")
    public void showScores2() {
        Game game = createSampleGame();
        scoreboard.saveGame(game, "Hero", "VITÓRIA");

        outputStreamCaptor.reset();
        scoreboard.showScores();
        String output = outputStreamCaptor.toString();

        assertTrue(output.contains("Hero"), "Error: Output should list saved player 'Hero'.");
    }

    @Test
    @DisplayName("showScores3: Verify formatting and table headers")
    public void showScores3() {
        scoreboard.showScores();
        String output = outputStreamCaptor.toString();

        assertAll("Verifying header structure",
                () -> assertTrue(output.contains("Jogador"), "Error: Output missing column 'Jogador'."),
                () -> assertTrue(output.contains("Resultado"), "Error: Output missing column 'Resultado'."),
                () -> assertTrue(output.contains("Tiros"), "Error: Output missing column 'Tiros'.")
        );
    }

    @Test
    @DisplayName("showScores4: Verify output listing multiple saved games")
    public void showScores4() {
        Game game1 = createSampleGame();
        Game game2 = createSampleGame();

        scoreboard.saveGame(game1, "PlayerAlpha", "VITÓRIA");
        scoreboard.saveGame(game2, "PlayerBeta", "DERROTA");

        outputStreamCaptor.reset();
        scoreboard.showScores();
        String output = outputStreamCaptor.toString();

        assertAll("Verifying multiple entries listed",
                () -> assertTrue(output.contains("PlayerAlpha"), "Error: Missing 'PlayerAlpha' in output."),
                () -> assertTrue(output.contains("PlayerBeta"), "Error: Missing 'PlayerBeta' in output.")
        );
    }
}
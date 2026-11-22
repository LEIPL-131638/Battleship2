
package battleship;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {

    private record Execution(String json, String console) {
    }

    private List<IPosition> createShots() {
        return List.of(
                new Position(1, 1),
                new Position(2, 2),
                new Position(3, 3)
        );
    }

    private Execution execute(Move move, boolean verbose) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String json;

        try (PrintStream captured = new PrintStream(
                output, true, StandardCharsets.UTF_8)) {

            System.setOut(captured);
            json = move.processEnemyFire(verbose);

        } finally {
            System.setOut(originalOut);
        }

        return new Execution(
                json,
                output.toString(StandardCharsets.UTF_8)
        );
    }

    private JsonNode findBoat(JsonNode boats, String type) {
        for (JsonNode boat : boats) {
            if (boat.get("type").asText().equals(type)) {
                return boat;
            }
        }
        return null;
    }

    @Test
    @DisplayName("Deve criar um movimento e devolver os seus dados")
    void testConstructorAndGetters() {
        List<IPosition> shots = createShots();

        List<IGame.ShotResult> results = List.of(
                new IGame.ShotResult(false, false, null, false),
                new IGame.ShotResult(false, false, null, false),
                new IGame.ShotResult(false, false, null, false)
        );

        Move move = new Move(7, shots, results);

        assertEquals(7, move.getNumber());
        assertEquals(shots, move.getShots());
        assertEquals(results, move.getShotResults());

        assertEquals(
                "Move{number=7, shots=3, results=3}",
                move.toString()
        );
    }

    @Test
    @DisplayName("Deve contar tiros válidos, tiros na água, acertos e tiros exteriores")
    void testProcessEnemyFireWithMissesAndHits() throws Exception {
        Ship ship = new Barge(
                Compass.NORTH,
                new Position(5, 5)
        );

        List<IGame.ShotResult> results = List.of(
                new IGame.ShotResult(true, false, null, false),
                new IGame.ShotResult(true, false, ship, false),
                new IGame.ShotResult(false, false, null, false)
        );

        Move move = new Move(1, createShots(), results);

        Execution execution = execute(move, false);
        JsonNode json = new ObjectMapper().readTree(execution.json());

        assertEquals(2, json.get("validShots").asInt());
        assertEquals(1, json.get("outsideShots").asInt());
        assertEquals(0, json.get("repeatedShots").asInt());
        assertEquals(1, json.get("missedShots").asInt());

        assertEquals(0, json.get("sunkBoats").size());

        assertEquals(1, json.get("hitsOnBoats").size());
        assertEquals(
                "Barca",
                json.get("hitsOnBoats").get(0).get("type").asText()
        );
        assertEquals(
                1,
                json.get("hitsOnBoats").get(0).get("hits").asInt()
        );

        assertFalse(execution.console().contains("Jogada nº"));
    }

    @Test
    @DisplayName("Deve contar corretamente os tiros repetidos")
    void testProcessEnemyFireWithRepeatedShots() throws Exception {
        List<IGame.ShotResult> results = List.of(
                new IGame.ShotResult(true, true, null, false),
                new IGame.ShotResult(true, true, null, false),
                new IGame.ShotResult(false, false, null, false)
        );

        Move move = new Move(2, createShots(), results);

        Execution execution = execute(move, true);
        JsonNode json = new ObjectMapper().readTree(execution.json());

        assertEquals(0, json.get("validShots").asInt());
        assertEquals(1, json.get("outsideShots").asInt());
        assertEquals(2, json.get("repeatedShots").asInt());
        assertEquals(0, json.get("missedShots").asInt());

        assertTrue(execution.console().contains("2 tiros repetidos"));
        assertTrue(execution.console().contains("1 tiro exterior"));
    }

    @Test
    @DisplayName("Deve contar um único tiro repetido")
    void testProcessEnemyFireWithSingleRepeatedShot() throws Exception {
        List<IGame.ShotResult> results = List.of(
                new IGame.ShotResult(true, true, null, false),
                new IGame.ShotResult(false, false, null, false),
                new IGame.ShotResult(false, false, null, false)
        );

        Move move = new Move(3, createShots(), results);

        Execution execution = execute(move, true);
        JsonNode json = new ObjectMapper().readTree(execution.json());

        assertEquals(0, json.get("validShots").asInt());
        assertEquals(2, json.get("outsideShots").asInt());
        assertEquals(1, json.get("repeatedShots").asInt());
        assertEquals(0, json.get("missedShots").asInt());

        assertTrue(execution.console().contains("1 tiro repetido"));
        assertTrue(execution.console().contains("2 tiros exteriores"));
    }

    @Test
    @DisplayName("Deve contar navios afundados e excluir os seus acertos da lista de navios não afundados")
    void testProcessEnemyFireWithSunkShip() throws Exception {
        Ship ship = new Barge(
                Compass.NORTH,
                new Position(5, 5)
        );

        List<IGame.ShotResult> results = List.of(
                new IGame.ShotResult(true, false, ship, true),
                new IGame.ShotResult(true, true, null, false),
                new IGame.ShotResult(false, false, null, false)
        );

        Move move = new Move(4, createShots(), results);

        Execution execution = execute(move, true);
        JsonNode json = new ObjectMapper().readTree(execution.json());

        assertEquals(1, json.get("validShots").asInt());
        assertEquals(1, json.get("outsideShots").asInt());
        assertEquals(1, json.get("repeatedShots").asInt());
        assertEquals(0, json.get("missedShots").asInt());

        assertEquals(1, json.get("sunkBoats").size());
        assertEquals(
                "Barca",
                json.get("sunkBoats").get(0).get("type").asText()
        );
        assertEquals(
                1,
                json.get("sunkBoats").get(0).get("count").asInt()
        );

        assertEquals(0, json.get("hitsOnBoats").size());

        assertTrue(execution.console().contains("1 tiro válido"));
        assertTrue(execution.console().contains("1 tiro repetido"));
        assertTrue(execution.console().contains("1 tiro exterior"));
    }

    @Test
    @DisplayName("Deve contar vários tiros num navio que continua a flutuar")
    void testProcessEnemyFireWithMultipleHits() throws Exception {
        Ship ship = new Barge(
                Compass.NORTH,
                new Position(5, 5)
        );

        List<IGame.ShotResult> results = List.of(
                new IGame.ShotResult(true, false, ship, false),
                new IGame.ShotResult(true, false, ship, false),
                new IGame.ShotResult(true, false, null, false)
        );

        Move move = new Move(6, createShots(), results);

        Execution execution = execute(move, true);
        JsonNode json = new ObjectMapper().readTree(execution.json());

        assertEquals(3, json.get("validShots").asInt());
        assertEquals(0, json.get("outsideShots").asInt());
        assertEquals(0, json.get("repeatedShots").asInt());
        assertEquals(1, json.get("missedShots").asInt());

        assertEquals(0, json.get("sunkBoats").size());
        assertEquals(1, json.get("hitsOnBoats").size());

        assertEquals(
                2,
                json.get("hitsOnBoats").get(0).get("hits").asInt()
        );

        assertTrue(execution.console().contains("3 tiros válidos"));
        assertTrue(execution.console().contains("2 tiros num(a) Barca"));
        assertTrue(execution.console().contains("1 tiro na água"));
    }

    @Test
    @DisplayName("Deve identificar três tiros fora do tabuleiro")
    void testProcessEnemyFireWithInvalidShots() throws Exception {
        List<IGame.ShotResult> results = List.of(
                new IGame.ShotResult(false, false, null, false),
                new IGame.ShotResult(false, false, null, false),
                new IGame.ShotResult(false, false, null, false)
        );

        Move move = new Move(8, createShots(), results);

        Execution execution = execute(move, true);
        JsonNode json = new ObjectMapper().readTree(execution.json());

        assertEquals(0, json.get("validShots").asInt());
        assertEquals(3, json.get("outsideShots").asInt());
        assertEquals(0, json.get("repeatedShots").asInt());
        assertEquals(0, json.get("missedShots").asInt());

        assertEquals(0, json.get("sunkBoats").size());
        assertEquals(0, json.get("hitsOnBoats").size());

        assertTrue(execution.console().contains("3 tiros exteriores"));
    }

    @Test
    @DisplayName("Deve processar vários navios atingidos sem tiros na água")
    void testProcessEnemyFireWithHitsAndNoMisses() throws Exception {
        Ship barge = new Barge(
                Compass.NORTH,
                new Position(5, 5)
        );

        Ship frigate = new Frigate(
                Compass.NORTH,
                new Position(1, 1)
        );

        Ship caravel = new Caravel(
                Compass.NORTH,
                new Position(7, 7)
        );

        List<IGame.ShotResult> results = List.of(
                new IGame.ShotResult(true, false, barge, false),
                new IGame.ShotResult(true, false, frigate, false),
                new IGame.ShotResult(true, false, caravel, false)
        );

        Move move = new Move(9, createShots(), results);

        Execution execution = execute(move, true);
        JsonNode json = new ObjectMapper().readTree(execution.json());

        assertEquals(3, json.get("validShots").asInt());
        assertEquals(0, json.get("outsideShots").asInt());
        assertEquals(0, json.get("repeatedShots").asInt());
        assertEquals(0, json.get("missedShots").asInt());

        assertEquals(0, json.get("sunkBoats").size());
        assertEquals(3, json.get("hitsOnBoats").size());

        assertNotNull(findBoat(json.get("hitsOnBoats"), "Barca"));
        assertNotNull(findBoat(json.get("hitsOnBoats"), "Fragata"));
        assertNotNull(findBoat(json.get("hitsOnBoats"), "Caravela"));

        assertTrue(execution.console().contains("ao fundo") == false);
        assertTrue(execution.console().contains("1 tiro num(a)"));
    }

    @Test
    @DisplayName("Deve contar vários tipos de navios afundados")
    void testProcessEnemyFireWithMultipleSunkShipTypes() throws Exception {
        Ship barge = new Barge(
                Compass.NORTH,
                new Position(5, 5)
        );

        Ship frigate = new Frigate(
                Compass.NORTH,
                new Position(1, 1)
        );

        Ship caravel = new Caravel(
                Compass.NORTH,
                new Position(7, 7)
        );

        List<IGame.ShotResult> results = List.of(
                new IGame.ShotResult(true, false, barge, true),
                new IGame.ShotResult(true, false, frigate, true),
                new IGame.ShotResult(true, false, caravel, true)
        );

        Move move = new Move(10, createShots(), results);

        Execution execution = execute(move, true);
        JsonNode json = new ObjectMapper().readTree(execution.json());

        assertEquals(3, json.get("validShots").asInt());
        assertEquals(0, json.get("outsideShots").asInt());
        assertEquals(0, json.get("repeatedShots").asInt());
        assertEquals(0, json.get("missedShots").asInt());

        assertEquals(3, json.get("sunkBoats").size());
        assertEquals(0, json.get("hitsOnBoats").size());

        assertEquals(1, findBoat(json.get("sunkBoats"), "Barca").get("count").asInt());
        assertEquals(1, findBoat(json.get("sunkBoats"), "Fragata").get("count").asInt());
        assertEquals(1, findBoat(json.get("sunkBoats"), "Caravela").get("count").asInt());

        assertTrue(execution.console().contains("Barca ao fundo"));
        assertTrue(execution.console().contains("Fragata ao fundo"));
        assertTrue(execution.console().contains("Caravela ao fundo"));
    }
}

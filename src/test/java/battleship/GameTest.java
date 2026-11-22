package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Game.
 * Author: Miguel Assunção (130501)
 * Date: 2026-11-22
 *
 * Cyclomatic Complexity:
 * - printBoard(IFleet, List<IMove>, boolean, boolean): 5
 * - printShips(IFleet, char[][]): 4
 * - printBoardShots(List<IMove>, char[][]): 7
 * - printBoardFrame(char[][]): 5
 * - printHeaderColumns(): 2
 * - jsonShots(List<IPosition>): 3
 * - Game(IFleet): 1
 * - getMyFleet(): 1
 * - getAlienFleet(): 1
 * - getMyMoves(): 1
 * - getAlienMoves(): 1
 * - randomEnemyFire(): 11
 * - readEnemyFire(Scanner): 7
 * - fireShots(List<IPosition>): 3
 * - fireSingleShot(IPosition, boolean): 6
 * - getRepeatedShots(): 1
 * - getInvalidShots(): 1
 * - getHits(): 1
 * - getSunkShips(): 1
 * - getRemainingShips(): 1
 * - repeatedShot(IPosition): 3
 * - printMyBoard(boolean, boolean): 1
 * - printAlienBoard(boolean, boolean): 1
 * - over(): 1
 */
@DisplayName("Tests for the Game class")
public class GameTest {

	private Game game;
	private Fleet fleet;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;

	@BeforeEach
	void setUp() {
		System.setOut(new PrintStream(outputStreamCaptor));
		fleet = new Fleet();
		game = new Game(fleet);
	}

	@AfterEach
	void tearDown() {
		System.setOut(originalOut);
		game = null;
		fleet = null;
	}

	@Test
	@DisplayName("constructor: Verify initial state of Game instance and getters")
	void constructor() {
		assertAll("Verifying initial state of Game",
				() -> assertNotNull(game, "Game instance should not be null after construction."),
				() -> assertEquals(fleet, game.getMyFleet(), "MyFleet should match the fleet passed to constructor."),
				() -> assertNotNull(game.getAlienFleet(), "AlienFleet should not be null."),
				() -> assertNotNull(game.getMyMoves(), "MyMoves list should not be null."),
				() -> assertTrue(game.getMyMoves().isEmpty(), "MyMoves list should be empty initially."),
				() -> assertNotNull(game.getAlienMoves(), "AlienMoves list should not be null."),
				() -> assertTrue(game.getAlienMoves().isEmpty(), "AlienMoves list should be empty initially."),
				() -> assertEquals(0, game.getInvalidShots(), "Invalid shots count should be zero upon initialization."),
				() -> assertEquals(0, game.getRepeatedShots(), "Repeated shots count should be zero upon initialization."),
				() -> assertEquals(0, game.getHits(), "Hits count should be zero upon initialization."),
				() -> assertEquals(0, game.getSunkShips(), "Sunk ships count should be zero upon initialization.")
		);
	}

	@Test
	@DisplayName("printBoard1: Print board with floating and sunk ships, shots, and legend")
	void testPrintBoardWithShotsAndLegend() {
		Barge sunkBarge = new Barge(Compass.NORTH, new Position(1, 1));
		Caravel floatingCaravel = new Caravel(Compass.NORTH, new Position(4, 4));
		fleet.addShip(sunkBarge);
		fleet.addShip(floatingCaravel);

		// Sink the barge, hit the caravel, shoot adjacent to sunk barge, shoot water, and shoot outside
		List<IPosition> move1Shots = List.of(
				new Position(1, 1), // hits and sinks barge
				new Position(1, 2), // adjacent to sunk barge ('-' -> 'o')
				new Position(8, 8)  // empty water ('.' -> 'o')
		);
		game.fireShots(move1Shots);

		List<IPosition> move2Shots = List.of(
				new Position(1, 1), // repeated shot on already hit ship ('*')
				new Position(8, 8), // repeated shot on water ('o')
				new Position(-1, 0) // outside board
		);
		game.fireShots(move2Shots);

		outputStreamCaptor.reset();
		Game.printBoard(fleet, game.getAlienMoves(), true, true);
		String output = outputStreamCaptor.toString();

		assertAll("Verifying board rendering with shots and legend",
				() -> assertTrue(output.contains("LEGENDA"), "Output should include legend when showLegend is true."),
				() -> assertTrue(output.contains("*"), "Output should include hit marker '*'."),
				() -> assertTrue(output.contains("o"), "Output should include water shot marker 'o'."),
				() -> assertTrue(output.contains("#"), "Output should include intact ship marker '#'."),
				() -> assertTrue(output.contains("-"), "Output should include adjacent marker '-'.")
		);
	}

	@Test
	@DisplayName("printBoard2: Print board without shots or legend and check null assertions")
	void testPrintBoardWithoutShotsOrLegend() {
		Game.printBoard(fleet, game.getAlienMoves(), false, false);
		String output = outputStreamCaptor.toString();
		assertFalse(output.contains("LEGENDA"), "Output should not include legend when showLegend is false.");

		assertThrows(AssertionError.class, () -> Game.printBoard(null, List.of(), false, false),
				"Null fleet should throw AssertionError.");
		assertThrows(AssertionError.class, () -> Game.printBoard(fleet, null, false, false),
				"Null moves should throw AssertionError.");
	}

	@Test
	@DisplayName("jsonShots: Serialize shots to JSON and verify null assertion")
	void testJsonShots() {
		List<IPosition> shots = List.of(new Position(0, 0), new Position(2, 3));
		String json = Game.jsonShots(shots);

		assertAll("Verifying JSON serialization",
				() -> assertNotNull(json, "JSON string should not be null."),
				() -> assertTrue(json.contains("\"row\" : \"A\""), "JSON should contain row A."),
				() -> assertTrue(json.contains("\"column\" : 1"), "JSON should contain column 1."),
				() -> assertTrue(json.contains("\"row\" : \"C\""), "JSON should contain row C."),
				() -> assertTrue(json.contains("\"column\" : 4"), "JSON should contain column 4.")
		);

		assertThrows(AssertionError.class, () -> Game.jsonShots(null),
				"Null shots list should throw AssertionError.");
	}

	@Test
	@DisplayName("randomEnemyFire1: Generate 3 random shots when board has many available positions")
	void testRandomEnemyFireNormal() {
		Barge sunkBarge = new Barge(Compass.NORTH, new Position(0, 0));
		fleet.addShip(sunkBarge);
		sunkBarge.sink();

		// Leave exactly 3 candidate positions to exercise duplicate check in while loop
		List<IPosition> previouslyShot = new ArrayList<>();
		for (int r = 0; r < Game.BOARD_SIZE; r++) {
			for (int c = 0; c < Game.BOARD_SIZE; c++) {
				Position p = new Position(r, c);
				if (!(r == 9 && c >= 7)) {
					previouslyShot.add(p);
				}
			}
		}
		game.getAlienMoves().add(new Move(1, previouslyShot, List.of()));

		String json = game.randomEnemyFire();
		assertNotNull(json, "randomEnemyFire should return non-null JSON.");
		assertEquals(2, game.getAlienMoves().size(), "A new move should be added to alienMoves.");
	}

	@Test
	@DisplayName("randomEnemyFire2: Handle fewer than NUMBER_SHOTS candidate positions remaining")
	void testRandomEnemyFireFewCandidates() {
		// Mark 98 positions as already shot, leaving only 2 candidate positions (< NUMBER_SHOTS)
		List<IPosition> previouslyShot = new ArrayList<>();
		for (int r = 0; r < Game.BOARD_SIZE; r++) {
			for (int c = 0; c < Game.BOARD_SIZE; c++) {
				if (!(r == 9 && c >= 8)) {
					previouslyShot.add(new Position(r, c));
				}
			}
		}
		game.getAlienMoves().add(new Move(1, previouslyShot, List.of()));

		String json = game.randomEnemyFire();
		assertNotNull(json, "randomEnemyFire should return non-null JSON even with < 3 candidates.");
		assertEquals(2, game.getAlienMoves().size(), "A new move should be recorded.");
		assertEquals(Game.NUMBER_SHOTS, game.getAlienMoves().get(1).getShots().size(),
				"Move should still contain NUMBER_SHOTS shots.");
	}

	@Test
	@DisplayName("readEnemyFire1: Read compact and separated valid coordinates")
	void testReadEnemyFireValidFormats() {
		Scanner compactScanner = new Scanner("A1 B2 C3 D4\n");
		String json1 = game.readEnemyFire(compactScanner);
		assertNotNull(json1, "readEnemyFire should return JSON for compact format.");
		assertEquals(1, game.getAlienMoves().size(), "One move should be recorded.");

		Scanner separatedScanner = new Scanner("D 4 E 5 F 6\n");
		String json2 = game.readEnemyFire(separatedScanner);
		assertNotNull(json2, "readEnemyFire should return JSON for separated format.");
		assertEquals(2, game.getAlienMoves().size(), "Two moves should be recorded.");
	}

	@Test
	@DisplayName("readEnemyFire2: Throw IllegalArgumentException on incomplete or insufficient positions and assert non-null")
	void testReadEnemyFireInvalidInputs() {
		Scanner incompleteScanner = new Scanner("A B C\n");
		IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
				() -> game.readEnemyFire(incompleteScanner),
				"Column letter without row number should throw IllegalArgumentException.");
		assertTrue(ex1.getMessage().contains("Posição incompleta"));

		Scanner tooFewScanner = new Scanner("A1 B2\n");
		IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
				() -> game.readEnemyFire(tooFewScanner),
				"Fewer than 3 positions should throw IllegalArgumentException.");
		assertTrue(ex2.getMessage().contains("exatamente"));

		assertThrows(AssertionError.class, () -> game.readEnemyFire(null),
				"Null scanner should throw AssertionError.");
	}

	@Test
	@DisplayName("fireShots: Validate shot count, duplicate detection within same move, and null assertion")
	void testFireShots() {
		assertThrows(AssertionError.class, () -> game.fireShots(null),
				"Null shots list should throw AssertionError.");

		assertThrows(IllegalArgumentException.class,
				() -> game.fireShots(List.of(new Position(0, 0), new Position(0, 1))),
				"Firing fewer than NUMBER_SHOTS should throw IllegalArgumentException.");

		List<IPosition> duplicateShots = List.of(new Position(2, 2), new Position(2, 2), new Position(2, 3));
		game.fireShots(duplicateShots);
		assertEquals(1, game.getRepeatedShots(), "Duplicate shot in the same move should increment repeatedShots.");
	}

	@Test
	@DisplayName("fireSingleShot: Cover invalid, repeated, miss, partial hit, and sinking shot")
	void testFireSingleShotAllBranches() {
		assertThrows(AssertionError.class, () -> game.fireSingleShot(null, false),
				"Null position should throw AssertionError.");

		// 1. Invalid position (outside board)
		IGame.ShotResult invalidRes = game.fireSingleShot(new Position(-1, 5), false);
		assertFalse(invalidRes.valid(), "Shot outside board should be invalid.");
		assertEquals(1, game.getInvalidShots(), "Invalid shots counter should be 1.");

		// 2. Repeated shot via isRepeated flag
		IGame.ShotResult repeatedFlagRes = game.fireSingleShot(new Position(2, 3), true);
		assertTrue(repeatedFlagRes.valid() && repeatedFlagRes.repeated(), "Shot with isRepeated=true should be marked repeated.");
		assertEquals(1, game.getRepeatedShots(), "Repeated shots counter should be 1.");

		// 3. Repeated shot via previous move history
		game.fireShots(List.of(new Position(0, 0), new Position(0, 1), new Position(0, 2)));
		IGame.ShotResult repeatedHistoryRes = game.fireSingleShot(new Position(0, 0), false);
		assertTrue(repeatedHistoryRes.repeated(), "Shot on previously fired position should be marked repeated.");
		assertEquals(2, game.getRepeatedShots(), "Repeated shots counter should be 2.");

		// 4. Miss (water)
		IGame.ShotResult missRes = game.fireSingleShot(new Position(9, 9), false);
		assertTrue(missRes.valid(), "Water shot should be valid.");
		assertFalse(missRes.repeated(), "Water shot should not be repeated.");
		assertNull(missRes.ship(), "Water shot should not hit any ship.");

		// 5. Partial hit (ship still floating) and 6. Sinking hit
		Caravel caravel = new Caravel(Compass.NORTH, new Position(5, 5));
		fleet.addShip(caravel);

		IGame.ShotResult hitRes = game.fireSingleShot(new Position(5, 5), false);
		assertEquals(caravel, hitRes.ship(), "Shot on (5,5) should hit the Caravel.");
		assertFalse(hitRes.sunk(), "Caravel should not be sunk after 1 hit.");
		assertEquals(1, game.getHits(), "Hits counter should be 1.");
		assertEquals(0, game.getSunkShips(), "Sunk ships counter should still be 0.");

		IGame.ShotResult sinkRes = game.fireSingleShot(new Position(6, 5), false);
		assertEquals(caravel, sinkRes.ship(), "Shot on (6,5) should hit the Caravel.");
		assertTrue(sinkRes.sunk(), "Caravel should be sunk after 2nd hit.");
		assertEquals(2, game.getHits(), "Hits counter should be 2.");
		assertEquals(1, game.getSunkShips(), "Sunk ships counter should be 1.");
	}

	@Test
	@DisplayName("repeatedShot: Identify previously fired positions and check null assertion")
	void testRepeatedShot() {
		Position pos = new Position(2, 3);
		assertFalse(game.repeatedShot(pos), "Position should not be repeated before any moves.");

		game.fireShots(List.of(pos, new Position(2, 4), new Position(2, 5)));
		assertTrue(game.repeatedShot(pos), "Position should be marked repeated after firing.");
		assertFalse(game.repeatedShot(new Position(7, 7)), "Unfired position should not be marked repeated.");

		assertThrows(AssertionError.class, () -> game.repeatedShot(null),
				"Null position should throw AssertionError.");
	}

	@Test
	@DisplayName("getRemainingShips: Count floating ships before and after sinking")
	void testGetRemainingShips() {
		Ship ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		Ship ship2 = new Frigate(Compass.EAST, new Position(5, 5));

		fleet.addShip(ship1);
		assertEquals(1, game.getRemainingShips(), "One ship should be floating.");
		fleet.addShip(ship2);
		assertEquals(2, game.getRemainingShips(), "Two ships should be floating.");
		ship2.sink();
		assertEquals(1, game.getRemainingShips(), "One ship should remain floating after sinking ship2.");
	}

	@Test
	@DisplayName("printMyBoard, printAlienBoard, and over: Execute console printing methods")
	void testBoardPrintingAndOver() {
		game.printMyBoard(false, true);
		assertTrue(outputStreamCaptor.toString().contains("LEGENDA"), "printMyBoard should print board with legend.");

		outputStreamCaptor.reset();
		game.printAlienBoard(true, false);
		assertTrue(outputStreamCaptor.toString().contains("1 2 3 4 5 6 7 8 9 10"), "printAlienBoard should print board header.");

		outputStreamCaptor.reset();
		game.over();
		assertTrue(outputStreamCaptor.toString().contains("Java Sparrow"), "over() should print end-of-game message.");
	}
}

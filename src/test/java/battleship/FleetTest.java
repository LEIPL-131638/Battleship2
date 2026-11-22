package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Fleet.
 * Author: Miguel Assunção (130501)
 * Date: 2026-11-22
 *
 * Cyclomatic Complexity:
 * - Fleet(): 1
 * - createRandom(): 3
 * - getShips(): 1
 * - addShip(IShip): 4
 * - getShipsLike(String): 3
 * - getFloatingShips(): 1
 * - filterFloatingShips(List<IShip>): 3
 * - getSunkShips(): 3
 * - shipAt(IPosition): 3
 * - colisionRisk(IShip): 3
 * - printShips(List<IShip>): 2
 * - printStatus(): 1
 * - printShipsByCategory(String): 1
 * - printFloatingShips(): 1
 * - printAllShips(): 1
 */
@DisplayName("Tests for the Fleet class")
public class FleetTest {

	private Fleet fleet;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;

	@BeforeEach
	void setUp() {
		System.setOut(new PrintStream(outputStreamCaptor));
		fleet = new Fleet();
	}

	@AfterEach
	void tearDown() {
		System.setOut(originalOut);
		fleet = null;
	}

	@Test
	@DisplayName("constructor: Verify Fleet initializes with an empty ships list")
	void testConstructor() {
		assertNotNull(fleet, "Error: Instance of Fleet should not be null.");
		assertTrue(fleet.getShips().isEmpty(), "Error: Fleet should be initialized with empty ships list.");
	}

	@Test
	@DisplayName("createRandom: Verify random fleet generation with expected size")
	void testCreateRandom() {
		IFleet randomFleet = Fleet.createRandom();
		assertNotNull(randomFleet, "Error: createRandom should return a non-null fleet instance.");
		assertEquals(IFleet.FLEET_SIZE, randomFleet.getShips().size(),
				"Error: createRandom should create a fleet with the correct number of ships.");
	}

	@Test
	@DisplayName("getShips: Verify retrieval of ships list")
	void testGetShips() {
		assertTrue(fleet.getShips().isEmpty(), "Error: Fleet's ships list should initially be empty.");
		IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship);
		assertEquals(1, fleet.getShips().size(), "Error: Fleet should have size 1 after adding a ship.");
		assertEquals(ship, fleet.getShips().get(0), "Error: Fleet's first ship should match the added ship.");
	}

	@Test
	@DisplayName("addShip1: Valid ship inside board without collision should be added")
	void testAddShip1() {
		IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
		assertTrue(fleet.addShip(ship), "Error: Valid ship should be added successfully.");
		assertEquals(1, fleet.getShips().size(), "Error: Fleet should contain one ship after addition.");
	}

	@Test
	@DisplayName("addShip2: Should reject ship when fleet size exceeds FLEET_SIZE")
	void testAddShip2() {
		for (int i = 0; i <= IFleet.FLEET_SIZE; i++) {
			fleet.getShips().add(new Barge(Compass.NORTH, new Position(0, 0)));
		}
		IShip extraShip = new Barge(Compass.NORTH, new Position(9, 9));
		assertFalse(fleet.addShip(extraShip), "Error: Should not add ship when fleet size limit is exceeded.");
	}

	@Test
	@DisplayName("addShip3: Should reject ship positioned outside the board")
	void testAddShip3() {
		IShip shipOutside = new Barge(Compass.NORTH, new Position(99, 99));
		assertFalse(fleet.addShip(shipOutside), "Error: Should not add ship outside the board.");
	}

	@Test
	@DisplayName("addShip4: Should reject ship with collision risk")
	void testAddShip4() {
		IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		IShip ship2 = new Barge(Compass.NORTH, new Position(1, 2));
		fleet.addShip(ship1);
		assertFalse(fleet.addShip(ship2), "Error: Should not add ship with a collision risk.");
	}

	@Test
	@DisplayName("addShip5: Null ship should trigger AssertionError")
	void testAddShipNull() {
		assertThrows(AssertionError.class, () -> fleet.addShip(null),
				"Error: Passing null to addShip should throw AssertionError.");
	}

	@Test
	@DisplayName("getShipsLike: Filter ships by category and handle null assertion")
	void testGetShipsLike() {
		IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		IShip ship2 = new Caravel(Compass.NORTH, new Position(4, 4));
		fleet.addShip(ship1);
		fleet.addShip(ship2);

		List<IShip> barges = fleet.getShipsLike("Barca");
		assertEquals(1, barges.size(), "Error: There should be exactly one ship of category 'Barca'.");
		assertEquals(ship1, barges.get(0), "Error: The ship of category 'Barca' does not match.");
		assertTrue(fleet.getShipsLike("Galeao").isEmpty(), "Error: Category not present should return empty list.");
		assertThrows(AssertionError.class, () -> fleet.getShipsLike(null),
				"Error: Passing null to getShipsLike should throw AssertionError.");
	}

	@Test
	@DisplayName("getFloatingShips: Return only ships that are still floating")
	void testGetFloatingShips() {
		IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		IShip ship2 = new Caravel(Compass.NORTH, new Position(4, 4));
		fleet.addShip(ship1);
		fleet.addShip(ship2);

		List<IShip> floatingShips = fleet.getFloatingShips();
		assertEquals(2, floatingShips.size(), "Error: All ships should be floating initially.");

		ship1.sink();
		floatingShips = fleet.getFloatingShips();
		assertEquals(1, floatingShips.size(), "Error: Only one ship should be floating after sinking one.");
		assertEquals(ship2, floatingShips.get(0), "Error: The floating ship should match the expected result.");
	}

	@Test
	@DisplayName("getSunkShips: Return only ships that have been sunk")
	void testGetSunkShips() {
		IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		IShip ship2 = new Caravel(Compass.NORTH, new Position(4, 4));
		fleet.addShip(ship1);
		fleet.addShip(ship2);

		assertTrue(fleet.getSunkShips().isEmpty(), "Error: No ships should be sunk initially.");

		ship1.sink();
		List<IShip> sunkShips = fleet.getSunkShips();
		assertEquals(1, sunkShips.size(), "Error: Exactly one ship should be sunk.");
		assertEquals(ship1, sunkShips.get(0), "Error: Sunk ship should match ship1.");
	}

	@Test
	@DisplayName("shipAt: Find ship at occupied position, return null for empty, and assert non-null")
	void testShipAt() {
		IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship);

		assertEquals(ship, fleet.shipAt(new Position(1, 1)), "Error: Should return the correct ship at the position.");
		assertNull(fleet.shipAt(new Position(5, 5)), "Error: Should return null for empty positions in the fleet.");
		assertThrows(AssertionError.class, () -> fleet.shipAt(null),
				"Error: Passing null to shipAt should throw AssertionError.");
	}

	@Test
	@DisplayName("colisionRisk: Detect adjacent/overlapping ships and assert non-null")
	void testColisionRisk() throws Exception {
		var method = Fleet.class.getDeclaredMethod("colisionRisk", IShip.class);
		method.setAccessible(true);

		IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		IShip ship2 = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship1);

		assertTrue((Boolean) method.invoke(fleet, ship2), "Error: Overlapping ships should be at collision risk.");
		assertFalse((Boolean) method.invoke(fleet, new Barge(Compass.NORTH, new Position(5, 5))),
				"Error: Ships at non-overlapping positions should not have a collision risk.");

		InvocationTargetException ex = assertThrows(InvocationTargetException.class,
				() -> method.invoke(fleet, new Object[]{null}),
				"Error: Passing null to colisionRisk should throw AssertionError.");
		assertInstanceOf(AssertionError.class, ex.getCause());
	}

	@Test
	@DisplayName("printShips: Print provided list of ships and assert non-null")
	void testPrintShips() {
		IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.printShips(List.of(ship));
		assertTrue(outputStreamCaptor.toString().contains("Barca"),
				"Error: printShips output should contain ship representation.");
		assertThrows(AssertionError.class, () -> fleet.printShips(null),
				"Error: Passing null to printShips should throw AssertionError.");
	}

	@Test
	@DisplayName("printStatus: Print fleet status summary")
	void testPrintStatus() {
		IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		IShip ship2 = new Caravel(Compass.NORTH, new Position(4, 4));
		fleet.addShip(ship1);
		fleet.addShip(ship2);
		ship1.sink();

		fleet.printStatus();
		String output = outputStreamCaptor.toString();
		assertTrue(output.contains("Estado da Frota: 1 a flutuar, 1 afundados!"),
				"Error: printStatus should print accurate floating and sunk counts.");
	}

	@Test
	@DisplayName("printShipsByCategory: Print ships matching category and assert non-null")
	void testPrintShipsByCategory() {
		IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship);

		fleet.printShipsByCategory("Barca");
		assertTrue(outputStreamCaptor.toString().contains("Barca"),
				"Error: printShipsByCategory should print ships of given category.");
		assertThrows(AssertionError.class, () -> fleet.printShipsByCategory(null),
				"Error: Passing null to printShipsByCategory should throw AssertionError.");
	}

	@Test
	@DisplayName("printFloatingShips: Print all currently floating ships")
	void testPrintFloatingShips() {
		IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship);

		fleet.printFloatingShips();
		assertTrue(outputStreamCaptor.toString().contains("Barca"),
				"Error: printFloatingShips should print floating ships.");
	}

	@Test
	@DisplayName("printAllShips: Print all ships in the fleet")
	void testPrintAllShips() {
		IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship);

		fleet.printAllShips();
		assertTrue(outputStreamCaptor.toString().contains("Barca"),
				"Error: printAllShips should print all ships in the fleet.");
	}
}

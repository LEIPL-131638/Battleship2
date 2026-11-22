package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Caravel.
 * Author: Miguel Assunção (130501)
 * Date: 2026-11-22
 *
 * Cyclomatic Complexity:
 * - Caravel(Compass, IPosition): 5
 * - getSize(): 1
 * - stillFloating(): 2
 * - getPositions(): 1
 * - getTopMostPos(): 1
 * - getBottomMostPos(): 1
 * - getLeftMostPos(): 1
 * - getRightMostPos(): 1
 */
@DisplayName("Tests for the Caravel class")
public class CaravelTest {

	private Caravel caravelN;
	private Caravel caravelS;
	private Caravel caravelE;
	private Caravel caravelW;

	@BeforeEach
	void setUp() {
		caravelN = new Caravel(Compass.NORTH, new Position(5, 5));
		caravelS = new Caravel(Compass.SOUTH, new Position(5, 5));
		caravelE = new Caravel(Compass.EAST, new Position(5, 5));
		caravelW = new Caravel(Compass.WEST, new Position(5, 5));
	}

	@AfterEach
	void tearDown() {
		caravelN = null;
		caravelS = null;
		caravelE = null;
		caravelW = null;
	}

	@Test
	@DisplayName("caravel1: Verify initialization with NORTH bearing")
	void caravel1() {
		List<IPosition> positions = caravelN.getPositions();
		assertAll("Verifying NORTH bearing initialization",
				() -> assertNotNull(caravelN, "Error: The Caravel should not be null."),
				() -> assertEquals("Caravela", caravelN.getCategory(), "Error: Caravel category should be 'Caravela'."),
				() -> assertEquals(Compass.NORTH, caravelN.getBearing(), "Error: The Caravel's direction should be NORTH."),
				() -> assertEquals(2, caravelN.getSize(), "Error: The size of the Caravel should be 2."),
				() -> assertEquals(2, positions.size(), "Error: Caravel should have 2 positions."),
				() -> assertEquals(new Position(5, 5), positions.get(0), "Error: First position incorrect for NORTH."),
				() -> assertEquals(new Position(6, 5), positions.get(1), "Error: Second position incorrect for NORTH."),
				() -> assertEquals(5, caravelN.getTopMostPos(), "Error: The topmost position should be 5."),
				() -> assertEquals(6, caravelN.getBottomMostPos(), "Error: The bottommost position should be 6.")
		);
	}

	@Test
	@DisplayName("caravel2: Verify initialization with SOUTH bearing")
	void caravel2() {
		List<IPosition> positions = caravelS.getPositions();
		assertAll("Verifying SOUTH bearing initialization",
				() -> assertNotNull(caravelS, "Error: The Caravel should not be null."),
				() -> assertEquals(Compass.SOUTH, caravelS.getBearing(), "Error: The Caravel's direction should be SOUTH."),
				() -> assertEquals(2, positions.size(), "Error: Caravel should have 2 positions."),
				() -> assertEquals(new Position(5, 5), positions.get(0), "Error: First position incorrect for SOUTH."),
				() -> assertEquals(new Position(6, 5), positions.get(1), "Error: Second position incorrect for SOUTH."),
				() -> assertEquals(6, caravelS.getBottomMostPos(), "Error: The bottommost position should be 6.")
		);
	}

	@Test
	@DisplayName("caravel3: Verify initialization with EAST bearing")
	void caravel3() {
		List<IPosition> positions = caravelE.getPositions();
		assertAll("Verifying EAST bearing initialization",
				() -> assertNotNull(caravelE, "Error: The Caravel should not be null."),
				() -> assertEquals(Compass.EAST, caravelE.getBearing(), "Error: The Caravel's direction should be EAST."),
				() -> assertEquals(2, positions.size(), "Error: Caravel should have 2 positions."),
				() -> assertEquals(new Position(5, 5), positions.get(0), "Error: First position incorrect for EAST."),
				() -> assertEquals(new Position(5, 6), positions.get(1), "Error: Second position incorrect for EAST."),
				() -> assertEquals(6, caravelE.getRightMostPos(), "Error: The rightmost position should be 6.")
		);
	}

	@Test
	@DisplayName("caravel4: Verify initialization with WEST bearing")
	void caravel4() {
		List<IPosition> positions = caravelW.getPositions();
		assertAll("Verifying WEST bearing initialization",
				() -> assertNotNull(caravelW, "Error: The Caravel should not be null."),
				() -> assertEquals(Compass.WEST, caravelW.getBearing(), "Error: The Caravel's direction should be WEST."),
				() -> assertEquals(2, positions.size(), "Error: Caravel should have 2 positions."),
				() -> assertEquals(new Position(5, 5), positions.get(0), "Error: First position incorrect for WEST."),
				() -> assertEquals(new Position(5, 6), positions.get(1), "Error: Second position incorrect for WEST."),
				() -> assertEquals(5, caravelW.getLeftMostPos(), "Error: The leftmost position should be 5.")
		);
	}

	@Test
	@DisplayName("caravel5: Verify exception handling when bearing or position is null")
	void caravel5() {
		Exception exception = assertThrows(NullPointerException.class,
				() -> new Caravel(null, new Position(0, 0)),
				"Error: A NullPointerException should have been thrown for a null direction.");
		assertEquals("Ship's bearing must not be null", exception.getMessage(),
				"Error: The exception message does not match the expected value.");

		Exception exception2 = assertThrows(NullPointerException.class,
				() -> new Caravel(Compass.NORTH, null),
				"Error: A NullPointerException should have been thrown for a null position.");
		assertEquals("Ship's position must not be null", exception2.getMessage(),
				"Error: The exception message does not match the expected value.");

		Exception exception3 = assertThrows(NullPointerException.class,
				() -> new Caravel(null, null),
				"Error: A NullPointerException should have been thrown for null direction and position.");
		assertEquals("Ship's bearing must not be null", exception3.getMessage(),
				"Error: The exception message does not match the expected value.");
	}

	@Test
	@DisplayName("getSize: Verify Caravel size is 2")
	void getSize() {
		assertEquals(2, caravelN.getSize(), "Error: The size of the Caravel should be 2.");
	}

	@Test
	@DisplayName("stillFloating1: Verify Caravel floats when intact or partially hit")
	void stillFloating1() {
		assertTrue(caravelN.stillFloating(), "Error: Caravel should float when intact.");
		caravelN.getPositions().get(0).shoot();
		assertTrue(caravelN.stillFloating(), "Error: Caravel should still float when only one position is hit.");
	}

	@Test
	@DisplayName("stillFloating2: Verify Caravel sinks when all positions are hit")
	void stillFloating2() {
		caravelN.getPositions().forEach(IPosition::shoot);
		assertFalse(caravelN.stillFloating(), "Error: Caravel should not float when all positions are hit.");
	}
}

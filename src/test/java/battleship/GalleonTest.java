package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;


/**
 * Test class for class Galleon.
 * Author: ${user.name}
 * Date: 2026-11-20 20:07
 * Cyclomatic Complexity:
 * - Galleon(Compass, IPosition): 5
 * - fillNorth(IPosition): 2
 * - fillSouth(IPosition): 3
 * - fillEast(IPosition): 2
 * - fillWest(IPosition): 2
 * Total CC: 14
 */
class GalleonTest {

	private Galleon galleon;

	private Position initialPosition;

	@BeforeEach
	void setUp() {
		initialPosition = new Position(5, 5);
		galleon = new Galleon(
				Compass.NORTH,
				initialPosition
		);
	}

	@AfterEach
	void tearDown() {
		galleon = null;
		initialPosition = null;
	}

	@Test
	@DisplayName("constructor1: Create Galleon facing NORTH")
	void constructor1() {

		List<IPosition> positions = galleon.getPositions();

		assertNotNull(
				galleon,
				"Error: Galleon instance should not be null."
		);

		assertEquals(
				5,
				positions.size(),
				"Error: NORTH Galleon should contain exactly 5 positions."
		);

		assertAll(
				"NORTH orientation positions",
				() -> assertEquals(
						new Position(5, 5),
						positions.get(0),
						"Error: NORTH position 1 should be (5,5)."
				),
				() -> assertEquals(
						new Position(5, 6),
						positions.get(1),
						"Error: NORTH position 2 should be (5,6)."
				),
				() -> assertEquals(
						new Position(5, 7),
						positions.get(2),
						"Error: NORTH position 3 should be (5,7)."
				),
				() -> assertEquals(
						new Position(6, 6),
						positions.get(3),
						"Error: NORTH position 4 should be (6,6)."
				),
				() -> assertEquals(
						new Position(7, 6),
						positions.get(4),
						"Error: NORTH position 5 should be (7,6)."
				)
		);
	}

	@Test
	@DisplayName("constructor2: Create Galleon facing EAST")
	void constructor2() {

		Galleon eastGalleon = new Galleon(
				Compass.EAST,
				new Position(5, 5)
		);

		List<IPosition> positions = eastGalleon.getPositions();

		assertNotNull(
				eastGalleon,
				"Error: EAST Galleon should not be null."
		);

		assertEquals(
				5,
				positions.size(),
				"Error: EAST Galleon should contain exactly 5 positions."
		);

		assertAll(
				"EAST orientation positions",
				() -> assertEquals(
						new Position(5, 5),
						positions.get(0),
						"Error: EAST position 1 should be (5,5)."
				),
				() -> assertEquals(
						new Position(6, 3),
						positions.get(1),
						"Error: EAST position 2 should be (6,3)."
				),
				() -> assertEquals(
						new Position(6, 4),
						positions.get(2),
						"Error: EAST position 3 should be (6,4)."
				),
				() -> assertEquals(
						new Position(6, 5),
						positions.get(3),
						"Error: EAST position 4 should be (6,5)."
				),
				() -> assertEquals(
						new Position(7, 5),
						positions.get(4),
						"Error: EAST position 5 should be (7,5)."
				)
		);
	}

	@Test
	@DisplayName("constructor3: Create Galleon facing SOUTH")
	void constructor3() {

		Galleon southGalleon = new Galleon(
				Compass.SOUTH,
				new Position(5, 5)
		);

		List<IPosition> positions = southGalleon.getPositions();

		assertNotNull(
				southGalleon,
				"Error: SOUTH Galleon should not be null."
		);

		assertEquals(
				5,
				positions.size(),
				"Error: SOUTH Galleon should contain exactly 5 positions."
		);

		assertAll(
				"SOUTH orientation positions",
				() -> assertEquals(
						new Position(5, 5),
						positions.get(0),
						"Error: SOUTH position 1 should be (5,5)."
				),
				() -> assertEquals(
						new Position(6, 5),
						positions.get(1),
						"Error: SOUTH position 2 should be (6,5)."
				),
				() -> assertEquals(
						new Position(7, 4),
						positions.get(2),
						"Error: SOUTH position 3 should be (7,4)."
				),
				() -> assertEquals(
						new Position(7, 5),
						positions.get(3),
						"Error: SOUTH position 4 should be (7,5)."
				),
				() -> assertEquals(
						new Position(7, 6),
						positions.get(4),
						"Error: SOUTH position 5 should be (7,6)."
				)
		);
	}

	@Test
	@DisplayName("constructor4: Create Galleon facing WEST")
	void constructor4() {

		Galleon westGalleon = new Galleon(
				Compass.WEST,
				new Position(5, 5)
		);

		List<IPosition> positions = westGalleon.getPositions();

		assertNotNull(
				westGalleon,
				"Error: WEST Galleon should not be null."
		);

		assertEquals(
				5,
				positions.size(),
				"Error: WEST Galleon should contain exactly 5 positions."
		);

		assertAll(
				"WEST orientation positions",
				() -> assertEquals(
						new Position(5, 5),
						positions.get(0),
						"Error: WEST position 1 should be (5,5)."
				),
				() -> assertEquals(
						new Position(6, 5),
						positions.get(1),
						"Error: WEST position 2 should be (6,5)."
				),
				() -> assertEquals(
						new Position(6, 6),
						positions.get(2),
						"Error: WEST position 3 should be (6,6)."
				),
				() -> assertEquals(
						new Position(6, 7),
						positions.get(3),
						"Error: WEST position 4 should be (6,7)."
				),
				() -> assertEquals(
						new Position(7, 5),
						positions.get(4),
						"Error: WEST position 5 should be (7,5)."
				)
		);
	}

	@Test
	@DisplayName("constructor5: Null bearing should throw NullPointerException")
	void constructor5() {

		assertThrows(
				NullPointerException.class,
				() -> new Galleon(null, new Position(5, 5)),
				"Error: Null bearing should throw NullPointerException."
		);
	}

	@Test
	@DisplayName("constructor: Null position should throw NullPointerException")
	void constructorWithNullPosition() {

		assertThrows(
				NullPointerException.class,
				() -> new Galleon(Compass.NORTH, null),
				"Error: Null position should throw NullPointerException."
		);
	}

	@Test
	@DisplayName("stillFloating1: Galleon should float when no positions are hit")
	void stillFloating1() {

		assertTrue(
				galleon.stillFloating(),
				"Error: Galleon should initially be floating."
		);
	}

	@Test
	@DisplayName("stillFloating2: Galleon should sink when all positions are hit")
	void stillFloating2() {

		galleon.getPositions().forEach(
				IPosition::shoot
		);

		assertFalse(
				galleon.stillFloating(),
				"Error: Galleon should not float when all positions are hit."
		);
	}

	@Test
	@DisplayName("getTopMostPos: Return the topmost row")
	void getTopMostPos() {

		assertEquals(
				5,
				galleon.getTopMostPos(),
				"Error: Expected topmost row 5."
		);
	}

	@Test
	@DisplayName("getBottomMostPos: Return the bottommost row")
	void getBottomMostPos() {

		assertEquals(
				7,
				galleon.getBottomMostPos(),
				"Error: Expected bottommost row 7."
		);
	}

	@Test
	@DisplayName("getLeftMostPos: Return the leftmost column")
	void getLeftMostPos() {

		assertEquals(
				5,
				galleon.getLeftMostPos(),
				"Error: Expected leftmost column 5."
		);
	}

	@Test
	@DisplayName("getRightMostPos: Return the rightmost column")
	void getRightMostPos() {

		assertEquals(
				7,
				galleon.getRightMostPos(),
				"Error: Expected rightmost column 7."
		);
	}
}
package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Compass.
 * Author: britoeabreu
 * Date: 2023-10-10
 * Time: 15:30
 * Cyclomatic Complexity for each method:
 * - Constructor: 1
 * - getDirection: 1
 * - toString: 1
 * - charToCompass: 4
 */

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for enum Compass.
 *
 * Author: ${user.name}
 * Date: 2026-11-20 20:07
 *
 * Cyclomatic Complexity:
 * - Constructor: 1
 * - randomBearing(): 1
 * - getDirection(): 1
 * - toString(): 1
 * - charToCompass(char): 5
 */
class CompassTest {

	private Compass compass;

	@BeforeEach
	void setUp() {
		compass = Compass.NORTH;
	}

	@AfterEach
	void tearDown() {
		compass = null;
	}

	/**
	 * Test for the Compass enum constructor.
	 * Cyclomatic Complexity: 1
	 */
	@Test
	@DisplayName("Constructor: Compass instance should be initialized")
	void constructor() {
		assertNotNull(
				compass,
				"Error: Compass instance should not be null."
		);
	}

	/**
	 * Test for randomBearing().
	 * Cyclomatic Complexity: 1
	 */
	@Test
	@DisplayName("randomBearing: Should return a valid compass direction")
	void randomBearing() {
		Compass result = Compass.randomBearing();

		assertNotNull(
				result,
				"Error: Random bearing should not be null."
		);

		assertTrue(
				result == Compass.NORTH ||
						result == Compass.SOUTH ||
						result == Compass.EAST ||
						result == Compass.WEST,
				"Error: Random bearing should be one of the four compass directions."
		);
	}

	/**
	 * Test for getDirection().
	 * Cyclomatic Complexity: 1
	 */
	@Test
	@DisplayName("getDirection: Should return the correct direction character")
	void getDirection() {
		assertAll(
				"Compass direction characters",
				() -> assertEquals(
						'n',
						Compass.NORTH.getDirection(),
						"Error: NORTH should return 'n'."
				),
				() -> assertEquals(
						's',
						Compass.SOUTH.getDirection(),
						"Error: SOUTH should return 's'."
				),
				() -> assertEquals(
						'e',
						Compass.EAST.getDirection(),
						"Error: EAST should return 'e'."
				),
				() -> assertEquals(
						'o',
						Compass.WEST.getDirection(),
						"Error: WEST should return 'o'."
				)
		);
	}

	/**
	 * Test for toString().
	 * Cyclomatic Complexity: 1
	 */
	@Test
	@DisplayName("toString: Should return the direction character as a String")
	void toStringTest() {
		assertAll(
				"Compass string representations",
				() -> assertEquals(
						"n",
						Compass.NORTH.toString(),
						"Error: NORTH should return \"n\"."
				),
				() -> assertEquals(
						"s",
						Compass.SOUTH.toString(),
						"Error: SOUTH should return \"s\"."
				),
				() -> assertEquals(
						"e",
						Compass.EAST.toString(),
						"Error: EAST should return \"e\"."
				),
				() -> assertEquals(
						"o",
						Compass.WEST.toString(),
						"Error: WEST should return \"o\"."
				)
		);
	}

	/**
	 * Test for charToCompass().
	 * Cyclomatic Complexity: 5
	 *
	 * Path 1: Input 'n'.
	 */
	@Test
	@DisplayName("charToCompass1: Character 'n' should return NORTH")
	void charToCompass1() {
		assertEquals(
				Compass.NORTH,
				Compass.charToCompass('n'),
				"Error: Character 'n' should map to Compass.NORTH."
		);
	}

	/**
	 * Path 2: Input 's'.
	 */
	@Test
	@DisplayName("charToCompass2: Character 's' should return SOUTH")
	void charToCompass2() {
		assertEquals(
				Compass.SOUTH,
				Compass.charToCompass('s'),
				"Error: Character 's' should map to Compass.SOUTH."
		);
	}

	/**
	 * Path 3: Input 'e'.
	 */
	@Test
	@DisplayName("charToCompass3: Character 'e' should return EAST")
	void charToCompass3() {
		assertEquals(
				Compass.EAST,
				Compass.charToCompass('e'),
				"Error: Character 'e' should map to Compass.EAST."
		);
	}

	/**
	 * Path 4: Input 'o'.
	 */
	@Test
	@DisplayName("charToCompass4: Character 'o' should return WEST")
	void charToCompass4() {
		assertEquals(
				Compass.WEST,
				Compass.charToCompass('o'),
				"Error: Character 'o' should map to Compass.WEST."
		);
	}

	/**
	 * Path 5: Default switch case.
	 */
	@Test
	@DisplayName("charToCompass5: Invalid character should return null")
	void charToCompass5() {
		assertAll(
				"Invalid compass characters",
				() -> assertNull(
						Compass.charToCompass('x'),
						"Error: Invalid character 'x' should return null."
				),
				() -> assertNull(
						Compass.charToCompass('\0'),
						"Error: Null character should return null."
				),
				() -> assertNull(
						Compass.charToCompass('N'),
						"Error: Uppercase character 'N' should return null."
				)
		);
	}
}

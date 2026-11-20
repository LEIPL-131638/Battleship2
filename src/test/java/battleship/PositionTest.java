package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

/**
 * Test class for class Position.
 * Author: ${user.name}
 * Date: 2026-11-20 20:07
 * Cyclomatic Complexity:
 * - Position(char, int): 1
 * - Position(int, int): 1
 * - randomPosition(): 1
 * - getRow(): 1
 * - getColumn(): 1
 * - getClassicRow(): 1
 * - getClassicColumn(): 1
 * - isInside(): 4
 * - isAdjacentTo(): 2
 * - adjacentPositions(): 10
 * - isOccupied(): 1
 * - isHit(): 1
 * - occupy(): 1
 * - shoot(): 1
 * - equals(): 4
 * - hashCode(): 1
 * - toString(): 1
 */
class PositionTest {

	private Position position;

	@BeforeEach
	void setUp() {
		position = new Position(2, 3);
	}

	@AfterEach
	void tearDown() {
		position = null;
	}

	@Test
	@DisplayName("Constructor 1: Initialize position using integer coordinates")
	void constructor() {
		Position pos = new Position(1, 1);

		assertAll(
				"Integer constructor",
				() -> assertNotNull(
						pos,
						"Error: Position instance should not be null."
				),
				() -> assertEquals(
						1, pos.getRow(),
						"Error: Expected row 1 but got " + pos.getRow()
				),
				() -> assertEquals(
						1, pos.getColumn(),
						"Error: Expected column 1 but got " + pos.getColumn()
				),
				() -> assertFalse(
						pos.isOccupied(),
						"Error: New position should not be occupied."
				),
				() -> assertFalse(
						pos.isHit(),
						"Error: New position should not be hit."
				)
		);
	}

	@Test
	@DisplayName("Constructor 2: Initialize position using classic coordinates")
	void constructorClassic() {
		Position pos = new Position('C', 4);

		assertAll(
				"Classic constructor",
				() -> assertEquals(
						2, pos.getRow(),
						"Error: Expected row 2 but got " + pos.getRow()
				),
				() -> assertEquals(
						3, pos.getColumn(),
						"Error: Expected column 3 but got " + pos.getColumn()
				),
				() -> assertEquals(
						'C', pos.getClassicRow(),
						"Error: Expected classic row C."
				),
				() -> assertEquals(
						4, pos.getClassicColumn(),
						"Error: Expected classic column 4."
				)
		);
	}

	@Test
	@DisplayName("randomPosition: Generate a valid random position")
	void randomPosition() {
		Position result = Position.randomPosition();

		assertNotNull(
				result,
				"Error: Random position should not be null."
		);

		assertTrue(
				result.isInside(),
				"Error: Random position should be inside the game board."
		);
	}

	@Test
	@DisplayName("getRow: Return the correct row")
	void getRow() {
		assertEquals(
				2, position.getRow(),
				"Error: Expected row 2 but got " + position.getRow()
		);
	}

	@Test
	@DisplayName("getColumn: Return the correct column")
	void getColumn() {
		assertEquals(
				3, position.getColumn(),
				"Error: Expected column 3 but got " + position.getColumn()
		);
	}

	@Test
	@DisplayName("getClassicRow: Return the traditional row character")
	void getClassicRow() {
		assertEquals(
				'C', position.getClassicRow(),
				"Error: Expected classic row C but got "
						+ position.getClassicRow()
		);
	}

	@Test
	@DisplayName("getClassicColumn: Return the traditional column")
	void getClassicColumn() {
		assertEquals(
				4, position.getClassicColumn(),
				"Error: Expected classic column 4 but got "
						+ position.getClassicColumn()
		);
	}

	@Test
	@DisplayName("isInside1: Row and column are within board boundaries")
	void isInside1() {
		position = new Position(0, 0);

		assertTrue(
				position.isInside(),
				"Error: Position (0,0) should be inside the board."
		);
	}

	@Test
	@DisplayName("isInside2: Negative row should return false")
	void isInside2() {
		position = new Position(-1, 5);

		assertFalse(
				position.isInside(),
				"Error: Negative row should make position invalid."
		);
	}

	@Test
	@DisplayName("isInside3: Negative column should return false")
	void isInside3() {
		position = new Position(5, -1);

		assertFalse(
				position.isInside(),
				"Error: Negative column should make position invalid."
		);
	}

	@Test
	@DisplayName("isInside4: Row or column exceeding board size should return false")
	void isInside4() {
		assertAll(
				"Upper board boundaries",
				() -> assertFalse(
						new Position(Game.BOARD_SIZE, 5).isInside(),
						"Error: Row equal to BOARD_SIZE should be invalid."
				),
				() -> assertFalse(
						new Position(5, Game.BOARD_SIZE).isInside(),
						"Error: Column equal to BOARD_SIZE should be invalid."
				)
		);
	}

	@Test
	@DisplayName("isAdjacentTo1: Positions within one cell should be adjacent")
	void isAdjacentTo1() {
		Position other = new Position(3, 4);

		assertTrue(
				position.isAdjacentTo(other),
				"Error: Diagonally adjacent position should return true."
		);
	}

	@Test
	@DisplayName("isAdjacentTo2: Positions outside adjacency range should return false")
	void isAdjacentTo2() {
		Position other = new Position(4, 5);

		assertFalse(
				position.isAdjacentTo(other),
				"Error: Non-adjacent position should return false."
		);
	}

	@Test
	@DisplayName("isAdjacentTo: Null argument should throw NullPointerException")
	void isAdjacentToWithNull() {
		assertThrows(
				NullPointerException.class,
				() -> position.isAdjacentTo(null),
				"Error: Null argument should cause NullPointerException."
		);
	}

	@Test
	@DisplayName("adjacentPositions1: Center position should have eight adjacent positions")
	void adjacentPositions1() {
		position = new Position(5, 5);

		List<IPosition> result = position.adjacentPositions();

		assertEquals(
				8, result.size(),
				"Error: Center position should have 8 adjacent positions."
		);
	}

	@Test
	@DisplayName("adjacentPositions2: Upper-left corner should have three adjacent positions")
	void adjacentPositions2() {
		position = new Position(0, 0);

		List<IPosition> result = position.adjacentPositions();

		assertEquals(
				3, result.size(),
				"Error: Upper-left corner should have 3 adjacent positions."
		);
	}

	@Test
	@DisplayName("adjacentPositions3: Upper-right corner should have three adjacent positions")
	void adjacentPositions3() {
		position = new Position(0, Game.BOARD_SIZE - 1);

		List<IPosition> result = position.adjacentPositions();

		assertEquals(
				3, result.size(),
				"Error: Upper-right corner should have 3 adjacent positions."
		);
	}

	@Test
	@DisplayName("adjacentPositions4: Lower-left corner should have three adjacent positions")
	void adjacentPositions4() {
		position = new Position(Game.BOARD_SIZE - 1, 0);

		List<IPosition> result = position.adjacentPositions();

		assertEquals(
				3, result.size(),
				"Error: Lower-left corner should have 3 adjacent positions."
		);
	}

	@Test
	@DisplayName("adjacentPositions5: Lower-right corner should have three adjacent positions")
	void adjacentPositions5() {
		position = new Position(
				Game.BOARD_SIZE - 1,
				Game.BOARD_SIZE - 1
		);

		List<IPosition> result = position.adjacentPositions();

		assertEquals(
				3, result.size(),
				"Error: Lower-right corner should have 3 adjacent positions."
		);
	}

	@Test
	@DisplayName("adjacentPositions6: Top edge should have five adjacent positions")
	void adjacentPositions6() {
		position = new Position(0, 5);

		assertEquals(
				5, position.adjacentPositions().size(),
				"Error: Top edge should have 5 adjacent positions."
		);
	}

	@Test
	@DisplayName("adjacentPositions7: Bottom edge should have five adjacent positions")
	void adjacentPositions7() {
		position = new Position(Game.BOARD_SIZE - 1, 5);

		assertEquals(
				5, position.adjacentPositions().size(),
				"Error: Bottom edge should have 5 adjacent positions."
		);
	}

	@Test
	@DisplayName("adjacentPositions8: Left edge should have five adjacent positions")
	void adjacentPositions8() {
		position = new Position(5, 0);

		assertEquals(
				5, position.adjacentPositions().size(),
				"Error: Left edge should have 5 adjacent positions."
		);
	}

	@Test
	@DisplayName("adjacentPositions9: Right edge should have five adjacent positions")
	void adjacentPositions9() {
		position = new Position(5, Game.BOARD_SIZE - 1);

		assertEquals(
				5, position.adjacentPositions().size(),
				"Error: Right edge should have 5 adjacent positions."
		);
	}

	@Test
	@DisplayName("isOccupied: Check initial and occupied states")
	void isOccupied() {
		assertFalse(
				position.isOccupied(),
				"Error: New position should not be occupied."
		);

		position.occupy();

		assertTrue(
				position.isOccupied(),
				"Error: Position should be occupied after occupy()."
		);
	}

	@Test
	@DisplayName("isHit: Check initial and hit states")
	void isHit() {
		assertFalse(
				position.isHit(),
				"Error: New position should not be hit."
		);

		position.shoot();

		assertTrue(
				position.isHit(),
				"Error: Position should be hit after shoot()."
		);
	}

	@Test
	@DisplayName("occupy: Mark position as occupied")
	void occupy() {
		position.occupy();

		assertTrue(
				position.isOccupied(),
				"Error: occupy() should set occupied state to true."
		);
	}

	@Test
	@DisplayName("shoot: Mark position as hit")
	void shoot() {
		position.shoot();

		assertTrue(
				position.isHit(),
				"Error: shoot() should set hit state to true."
		);
	}

	@Test
	@DisplayName("equals1: Same object should return true")
	void equals1() {
		assertTrue(
				position.equals(position),
				"Error: An object should be equal to itself."
		);
	}

	@Test
	@DisplayName("equals2: Different IPosition with identical coordinates should return true")
	void equals2() {
		IPosition other = new Position(2, 3);

		assertTrue(
				position.equals(other),
				"Error: Positions with identical coordinates should be equal."
		);
	}

	@Test
	@DisplayName("equals3: Object with different coordinates should return false")
	void equals3() {
		IPosition other = new Position(2, 4);

		assertFalse(
				position.equals(other),
				"Error: Positions with different coordinates should not be equal."
		);
	}

	@Test
	@DisplayName("equals4: Null or unrelated object should return false")
	void equals4() {
		assertAll(
				"Invalid equality comparisons",
				() -> assertFalse(
						position.equals(null),
						"Error: Position should not equal null."
				),
				() -> assertFalse(
						position.equals(new Object()),
						"Error: Position should not equal an unrelated object."
				)
		);
	}

	@Test
	@DisplayName("hashCode: Equal positions should have equal hash codes")
	void hashCodeConsistency() {
		Position same = new Position(2, 3);

		assertEquals(
				position.hashCode(),
				same.hashCode(),
				"Error: Equal positions should have identical hash codes."
		);
	}

	@Test
	@DisplayName("hashCode: Changing occupied or hit state should affect hash code")
	void hashCodeStateChanges() {
		int initialHash = position.hashCode();

		position.occupy();

		int occupiedHash = position.hashCode();

		position.shoot();

		int hitHash = position.hashCode();

		assertNotEquals(
				initialHash,
				occupiedHash,
				"Error: Occupied state should change the hash code for these coordinates."
		);

		assertNotEquals(
				occupiedHash,
				hitHash,
				"Error: Hit state should change the hash code for these coordinates."
		);
	}

	@Test
	@DisplayName("toString: Return classic coordinate representation")
	void toStringTest() {
		String expected = "C4";

		assertEquals(
				expected,
				position.toString(),
				"Error: Expected string '" + expected
						+ "' but got '" + position.toString() + "'."
		);
	}
}

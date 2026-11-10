package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Frigate.
 * Author: ${user.name}
 * Date: 2026-11-10 19:07
 *
 * Cyclomatic Complexity:
 * - Frigate(Compass, IPosition): 9
 */
public class FrigateTest {

	private Frigate frigate;

	@BeforeEach
	public void setUp() {
		frigate = new Frigate(Compass.NORTH, new Position(2, 2));
	}

	@AfterEach
	public void tearDown() {
		frigate = null;
	}

	@Test
	@DisplayName("frigate1: Verify initialization attributes (name, size, bearing, position)")
	public void frigate1() {
		assertAll("Verifying initial attributes of Frigate",
				() -> assertEquals("Fragata", frigate.getCategory(), "Error: expected ship category 'Fragata' but got " + frigate.getCategory()),
				() -> assertEquals(4, frigate.getSize(), "Error: expected ship size 4 but got " + frigate.getSize()),
				() -> assertEquals(Compass.NORTH, frigate.getBearing(), "Error: expected bearing NORTH but got " + frigate.getBearing()),
				() -> assertEquals(new Position(2, 2), frigate.getPosition(), "Error: expected position (2,2) but got " + frigate.getPosition())
		);
	}

	@Test
	@DisplayName("frigate2: Verify NORTH bearing path - total positions generated")
	public void frigate2() {
		Frigate northFrigate = new Frigate(Compass.NORTH, new Position(1, 1));
		assertEquals(4, northFrigate.getPositions().size(),
				"Error: expected 4 positions for NORTH bearing but got " + northFrigate.getPositions().size());
	}

	@Test
	@DisplayName("frigate3: Verify NORTH bearing path - exact coordinate points")
	public void frigate3() {
		Frigate northFrigate = new Frigate(Compass.NORTH, new Position(1, 1));
		List<IPosition> positions = northFrigate.getPositions();

		assertAll("Verifying NORTH path positions",
				() -> assertEquals(new Position(1, 1), positions.get(0), "Error: expected position (1,1) at index 0 but got " + positions.get(0)),
				() -> assertEquals(new Position(2, 1), positions.get(1), "Error: expected position (2,1) at index 1 but got " + positions.get(1)),
				() -> assertEquals(new Position(3, 1), positions.get(2), "Error: expected position (3,1) at index 2 but got " + positions.get(2)),
				() -> assertEquals(new Position(4, 1), positions.get(3), "Error: expected position (4,1) at index 3 but got " + positions.get(3))
		);
	}

	@Test
	@DisplayName("frigate4: Verify SOUTH bearing path - total positions generated")
	public void frigate4() {
		Frigate southFrigate = new Frigate(Compass.SOUTH, new Position(0, 0));
		assertEquals(4, southFrigate.getPositions().size(),
				"Error: expected 4 positions for SOUTH bearing but got " + southFrigate.getPositions().size());
	}

	@Test
	@DisplayName("frigate5: Verify SOUTH bearing path - exact coordinate points")
	public void frigate5() {
		Frigate southFrigate = new Frigate(Compass.SOUTH, new Position(0, 0));
		List<IPosition> positions = southFrigate.getPositions();

		assertAll("Verifying SOUTH path positions",
				() -> assertEquals(new Position(0, 0), positions.get(0), "Error: expected position (0,0) at index 0 but got " + positions.get(0)),
				() -> assertEquals(new Position(1, 0), positions.get(1), "Error: expected position (1,0) at index 1 but got " + positions.get(1)),
				() -> assertEquals(new Position(2, 0), positions.get(2), "Error: expected position (2,0) at index 2 but got " + positions.get(2)),
				() -> assertEquals(new Position(3, 0), positions.get(3), "Error: expected position (3,0) at index 3 but got " + positions.get(3))
		);
	}

	@Test
	@DisplayName("frigate6: Verify EAST bearing path - total positions generated")
	public void frigate6() {
		Frigate eastFrigate = new Frigate(Compass.EAST, new Position(3, 3));
		assertEquals(4, eastFrigate.getPositions().size(),
				"Error: expected 4 positions for EAST bearing but got " + eastFrigate.getPositions().size());
	}

	@Test
	@DisplayName("frigate7: Verify EAST bearing path - exact coordinate points")
	public void frigate7() {
		Frigate eastFrigate = new Frigate(Compass.EAST, new Position(3, 3));
		List<IPosition> positions = eastFrigate.getPositions();

		assertAll("Verifying EAST path positions",
				() -> assertEquals(new Position(3, 3), positions.get(0), "Error: expected position (3,3) at index 0 but got " + positions.get(0)),
				() -> assertEquals(new Position(3, 4), positions.get(1), "Error: expected position (3,4) at index 1 but got " + positions.get(1)),
				() -> assertEquals(new Position(3, 5), positions.get(2), "Error: expected position (3,5) at index 2 but got " + positions.get(2)),
				() -> assertEquals(new Position(3, 6), positions.get(3), "Error: expected position (3,6) at index 3 but got " + positions.get(3))
		);
	}

	@Test
	@DisplayName("frigate8: Verify WEST bearing path - total positions generated")
	public void frigate8() {
		Frigate westFrigate = new Frigate(Compass.WEST, new Position(5, 5));
		assertEquals(4, westFrigate.getPositions().size(),
				"Error: expected 4 positions for WEST bearing but got " + westFrigate.getPositions().size());
	}

	@Test
	@DisplayName("frigate9: Verify WEST bearing path - exact coordinate points")
	public void frigate9() {
		Frigate westFrigate = new Frigate(Compass.WEST, new Position(5, 5));
		List<IPosition> positions = westFrigate.getPositions();

		assertAll("Verifying WEST path positions",
				() -> assertEquals(new Position(5, 5), positions.get(0), "Error: expected position (5,5) at index 0 but got " + positions.get(0)),
				() -> assertEquals(new Position(5, 6), positions.get(1), "Error: expected position (5,6) at index 1 but got " + positions.get(1)),
				() -> assertEquals(new Position(5, 7), positions.get(2), "Error: expected position (5,7) at index 2 but got " + positions.get(2)),
				() -> assertEquals(new Position(5, 8), positions.get(3), "Error: expected position (5,8) at index 3 but got " + positions.get(3))
		);
	}
}
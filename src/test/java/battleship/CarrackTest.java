package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Carrack.
 * Author: britoeabreu
 * Date: 2026-11-10 18:55
 *
 * Cyclomatic Complexity:
 * - Carrack(Compass, IPosition): 5
 * - getSize(): 1
 * - stillFloating(): 2
 * - getPositions(): 1
 * - getTopMostPos(): 1
 * - getBottomMostPos(): 1
 * - getLeftMostPos(): 1
 * - getRightMostPos(): 1
 */
public class CarrackTest {

    private Carrack carrack;

    @BeforeEach
    public void setUp() {
        carrack = new Carrack(Compass.NORTH, new Position(5, 5));
    }

    @AfterEach
    public void tearDown() {
        carrack = null;
    }

    // --- Constructor Tests (CC = 5: North, South, East, West, Null Exception) ---

    @Test
    @DisplayName("carrack1: Verify initialization with NORTH bearing")
    public void carrack1() {
        List<IPosition> positions = carrack.getPositions();
        assertAll("Verifying NORTH bearing initialization",
                () -> assertEquals("Nau", carrack.getCategory(), "Error: expected category 'Nau' but got " + carrack.getCategory()),
                () -> assertEquals(Compass.NORTH, carrack.getBearing(), "Error: expected bearing NORTH but got " + carrack.getBearing()),
                () -> assertEquals(3, carrack.getSize(), "Error: expected size 3 but got " + carrack.getSize()),
                () -> assertEquals(new Position(5, 5), positions.get(0), "Error: first position incorrect for NORTH"),
                () -> assertEquals(new Position(6, 5), positions.get(1), "Error: second position incorrect for NORTH"),
                () -> assertEquals(new Position(7, 5), positions.get(2), "Error: third position incorrect for NORTH")
        );
    }

    @Test
    @DisplayName("carrack2: Verify initialization with SOUTH bearing")
    public void carrack2() {
        Carrack southCarrack = new Carrack(Compass.SOUTH, new Position(5, 5));
        List<IPosition> positions = southCarrack.getPositions();
        assertAll("Verifying SOUTH bearing initialization",
                () -> assertEquals(new Position(5, 5), positions.get(0), "Error: first position incorrect for SOUTH"),
                () -> assertEquals(new Position(6, 5), positions.get(1), "Error: second position incorrect for SOUTH"),
                () -> assertEquals(new Position(7, 5), positions.get(2), "Error: third position incorrect for SOUTH")
        );
    }

    @Test
    @DisplayName("carrack3: Verify initialization with EAST bearing")
    public void carrack3() {
        Carrack eastCarrack = new Carrack(Compass.EAST, new Position(5, 5));
        List<IPosition> positions = eastCarrack.getPositions();
        assertAll("Verifying EAST bearing initialization",
                () -> assertEquals(new Position(5, 5), positions.get(0), "Error: first position incorrect for EAST"),
                () -> assertEquals(new Position(5, 6), positions.get(1), "Error: second position incorrect for EAST"),
                () -> assertEquals(new Position(5, 7), positions.get(2), "Error: third position incorrect for EAST")
        );
    }

    @Test
    @DisplayName("carrack4: Verify initialization with WEST bearing")
    public void carrack4() {
        Carrack westCarrack = new Carrack(Compass.WEST, new Position(5, 5));
        List<IPosition> positions = westCarrack.getPositions();
        assertAll("Verifying WEST bearing initialization",
                () -> assertEquals(new Position(5, 5), positions.get(0), "Error: first position incorrect for WEST"),
                () -> assertEquals(new Position(5, 6), positions.get(1), "Error: second position incorrect for WEST"),
                () -> assertEquals(new Position(5, 7), positions.get(2), "Error: third position incorrect for WEST")
        );
    }

    @Test
    @DisplayName("carrack5: Verify exception handling on null inputs")
    public void carrack5() {
        assertAll("Verifying null pointer exceptions",
                () -> assertThrows(NullPointerException.class, () -> new Carrack(null, new Position(5, 5)), "Error: expected NPE for null bearing"),
                () -> assertThrows(NullPointerException.class, () -> new Carrack(Compass.NORTH, null), "Error: expected NPE for null position")
        );
    }

    // --- Simple Getter Tests (CC = 1) ---

    @Test
    @DisplayName("getSize: Verify size retrieval")
    public void getSize() {
        assertEquals(3, carrack.getSize(), "Error: expected size 3 but got " + carrack.getSize());
    }

    @Test
    @DisplayName("getPositions: Verify positions list size")
    public void getPositions() {
        assertEquals(3, carrack.getPositions().size(), "Error: expected 3 positions but got " + carrack.getPositions().size());
    }

    @Test
    @DisplayName("getTopMostPos: Verify topmost position calculation")
    public void getTopMostPos() {
        assertEquals(5, carrack.getTopMostPos(), "Error: expected top position 5 but got " + carrack.getTopMostPos());
    }

    @Test
    @DisplayName("getBottomMostPos: Verify bottommost position calculation")
    public void getBottomMostPos() {
        assertEquals(7, carrack.getBottomMostPos(), "Error: expected bottom position 7 but got " + carrack.getBottomMostPos());
    }

    @Test
    @DisplayName("getLeftMostPos: Verify leftmost position calculation")
    public void getLeftMostPos() {
        assertEquals(5, carrack.getLeftMostPos(), "Error: expected left position 5 but got " + carrack.getLeftMostPos());
    }

    @Test
    @DisplayName("getRightMostPos: Verify rightmost position calculation")
    public void getRightMostPos() {
        assertEquals(5, carrack.getRightMostPos(), "Error: expected right position 5 but got " + carrack.getRightMostPos());
    }

    // --- stillFloating Tests (CC = 2) ---

    @Test
    @DisplayName("stillFloating1: Verify status when intact or partially hit")
    public void stillFloating1() {
        assertTrue(carrack.stillFloating(), "Error: ship should float when intact");
        carrack.getPositions().get(0).shoot();
        assertTrue(carrack.stillFloating(), "Error: ship should float when partially hit");
    }

    @Test
    @DisplayName("stillFloating2: Verify status when fully hit")
    public void stillFloating2() {
        carrack.getPositions().forEach(IPosition::shoot);
        assertFalse(carrack.stillFloating(), "Error: ship should not float when all positions are hit");
    }
}
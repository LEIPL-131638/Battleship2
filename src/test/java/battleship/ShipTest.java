
package battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    /**
     * Implementação auxiliar para testar navios com posições personalizadas.
     */
    private static class TestShip extends Ship {

        TestShip(String category, Compass bearing, IPosition origin,
                 IPosition... occupiedPositions) {

            super(category, bearing, origin, occupiedPositions.length);

            getPositions().addAll(List.of(occupiedPositions));
        }
    }

    @Test
    @DisplayName("Deve construir um navio e devolver os seus atributos")
    void testConstructorAndGetters() {
        Position origin = new Position(5, 5);

        Ship ship = new Barge(Compass.NORTH, origin);

        assertNotNull(ship);
        assertEquals("Barca", ship.getCategory());
        assertEquals(Compass.NORTH, ship.getBearing());
        assertEquals(origin, ship.getPosition());
        assertEquals(1, ship.getSize());

        assertNotNull(ship.getPositions());
        assertEquals(1, ship.getPositions().size());
        assertEquals(new Position(5, 5), ship.getPositions().get(0));
    }

    @Test
    @DisplayName("Deve rejeitar argumentos nulos no construtor")
    void testConstructorRejectsNullArguments() {
        Position position = new Position(5, 5);

        assertThrows(
                NullPointerException.class,
                () -> new TestShip(
                        null,
                        Compass.NORTH,
                        position,
                        position
                )
        );

        assertThrows(
                NullPointerException.class,
                () -> new TestShip(
                        "Teste",
                        null,
                        position,
                        position
                )
        );

        assertThrows(
                NullPointerException.class,
                () -> new TestShip(
                        "Teste",
                        Compass.NORTH,
                        null,
                        position
                )
        );
    }

    @Test
    @DisplayName("Deve construir corretamente todos os tipos de navios")
    void testBuildShip() {
        Position position = new Position(1, 1);

        assertInstanceOf(
                Barge.class,
                Ship.buildShip("barca", Compass.NORTH, position)
        );

        assertInstanceOf(
                Caravel.class,
                Ship.buildShip("caravela", Compass.NORTH, position)
        );

        assertInstanceOf(
                Carrack.class,
                Ship.buildShip("nau", Compass.NORTH, position)
        );

        assertInstanceOf(
                Frigate.class,
                Ship.buildShip("fragata", Compass.NORTH, position)
        );

        assertInstanceOf(
                Galleon.class,
                Ship.buildShip("galeao", Compass.NORTH, position)
        );

        assertNull(
                Ship.buildShip("desconhecido", Compass.NORTH, position)
        );
    }

    @Test
    @DisplayName("Deve identificar corretamente as posições adjacentes sem duplicados")
    void testGetAdjacentPositions() {
        TestShip ship = new TestShip(
                "Teste",
                Compass.EAST,
                new Position(5, 5),
                new Position(5, 5),
                new Position(5, 6),
                new Position(5, 7)
        );

        List<IPosition> adjacent = ship.getAdjacentPositions();

        assertEquals(12, adjacent.size());

        Set<IPosition> uniquePositions = new HashSet<>(adjacent);

        assertEquals(
                adjacent.size(),
                uniquePositions.size(),
                "A lista não deve conter posições duplicadas"
        );

        assertFalse(adjacent.contains(new Position(5, 5)));
        assertFalse(adjacent.contains(new Position(5, 6)));
        assertFalse(adjacent.contains(new Position(5, 7)));

        assertTrue(adjacent.contains(new Position(4, 5)));
        assertTrue(adjacent.contains(new Position(6, 7)));
    }

    @Test
    @DisplayName("Deve identificar corretamente um navio que ainda está a flutuar")
    void testStillFloating() {
        TestShip ship = new TestShip(
                "Teste",
                Compass.EAST,
                new Position(5, 5),
                new Position(5, 5),
                new Position(5, 6),
                new Position(5, 7)
        );

        assertTrue(ship.stillFloating());

        ship.getPositions().get(0).shoot();
        ship.getPositions().get(1).shoot();

        assertTrue(ship.stillFloating());

        ship.getPositions().get(2).shoot();

        assertFalse(ship.stillFloating());
    }

    @Test
    @DisplayName("Deve calcular corretamente as posições extremas de um navio")
    void testExtremePositions() {
        TestShip ship = new TestShip(
                "Teste",
                Compass.NORTH,
                new Position(5, 5),
                new Position(5, 5),
                new Position(2, 8),
                new Position(7, 1)
        );

        assertEquals(2, ship.getTopMostPos());
        assertEquals(7, ship.getBottomMostPos());
        assertEquals(1, ship.getLeftMostPos());
        assertEquals(8, ship.getRightMostPos());
    }

    @Test
    @DisplayName("Deve identificar uma posição ocupada e uma posição livre")
    void testOccupies() {
        TestShip ship = new TestShip(
                "Teste",
                Compass.EAST,
                new Position(5, 5),
                new Position(5, 5),
                new Position(5, 6),
                new Position(5, 7)
        );

        assertTrue(ship.occupies(new Position(5, 7)));
        assertFalse(ship.occupies(new Position(8, 8)));
    }

    @Test
    @DisplayName("Deve identificar posições demasiado próximas")
    void testTooCloseToPosition() {
        TestShip ship = new TestShip(
                "Teste",
                Compass.EAST,
                new Position(5, 5),
                new Position(5, 5),
                new Position(5, 6),
                new Position(5, 7)
        );

        assertTrue(ship.tooCloseTo(new Position(5, 8)));
        assertFalse(ship.tooCloseTo(new Position(8, 8)));
    }

    @Test
    @DisplayName("Deve identificar navios demasiado próximos")
    void testTooCloseToShip() {
        Ship ship = new Barge(
                Compass.NORTH,
                new Position(5, 5)
        );

        TestShip nearbyShip = new TestShip(
                "Teste",
                Compass.NORTH,
                new Position(0, 0),
                new Position(0, 0),
                new Position(5, 6)
        );

        TestShip farShip = new TestShip(
                "Teste",
                Compass.NORTH,
                new Position(0, 0),
                new Position(0, 0),
                new Position(9, 9)
        );

        assertTrue(ship.tooCloseTo(nearbyShip));
        assertFalse(ship.tooCloseTo(farShip));
    }

    @Test
    @DisplayName("Deve reconhecer navios dentro dos limites do tabuleiro")
    void testIsInsideBoard() {
        Ship validShip = new Barge(
                Compass.NORTH,
                new Position(5, 5)
        );

        assertTrue(validShip.isInsideBoard());

        Ship outsideLeft = new Barge(
                Compass.NORTH,
                new Position(5, -1)
        );

        Ship outsideRight = new Barge(
                Compass.NORTH,
                new Position(5, 10)
        );

        Ship outsideTop = new Barge(
                Compass.NORTH,
                new Position(-1, 5)
        );

        Ship outsideBottom = new Barge(
                Compass.NORTH,
                new Position(10, 5)
        );

        assertFalse(outsideLeft.isInsideBoard());
        assertFalse(outsideRight.isInsideBoard());
        assertFalse(outsideTop.isInsideBoard());
        assertFalse(outsideBottom.isInsideBoard());
    }

    @Test
    @DisplayName("Deve disparar apenas na posição correspondente do navio")
    void testShoot() {
        TestShip ship = new TestShip(
                "Teste",
                Compass.EAST,
                new Position(5, 5),
                new Position(5, 5),
                new Position(5, 6),
                new Position(5, 7)
        );

        ship.shoot(new Position(5, 6));

        assertFalse(ship.getPositions().get(0).isHit());
        assertTrue(ship.getPositions().get(1).isHit());
        assertFalse(ship.getPositions().get(2).isHit());

        ship.shoot(new Position(4, 4));

        assertFalse(ship.getPositions().get(0).isHit());
        assertTrue(ship.getPositions().get(1).isHit());
        assertFalse(ship.getPositions().get(2).isHit());
    }

    @Test
    @DisplayName("Deve afundar todas as posições do navio")
    void testSink() {
        TestShip ship = new TestShip(
                "Teste",
                Compass.EAST,
                new Position(5, 5),
                new Position(5, 5),
                new Position(5, 6),
                new Position(5, 7)
        );

        ship.sink();

        for (IPosition position : ship.getPositions()) {
            assertTrue(position.isHit());
        }

        assertFalse(ship.stillFloating());
    }

    @Test
    @DisplayName("Deve representar corretamente o navio em texto")
    void testToString() {
        Ship ship = new Barge(
                Compass.NORTH,
                new Position(5, 5)
        );

        assertEquals(
                "[Barca n F6]",
                ship.toString()
        );
    }
}

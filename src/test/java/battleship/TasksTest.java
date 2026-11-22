
package battleship;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TasksTest {

	@Test
	@DisplayName("Deve apresentar a lista de comandos disponíveis")
	void menuHelp() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;

		try (PrintStream captured = new PrintStream(
				output, true, StandardCharsets.UTF_8)) {

			System.setOut(captured);
			Tasks.menuHelp();

		} finally {
			System.setOut(originalOut);
		}

		String result = output.toString(StandardCharsets.UTF_8);

		assertTrue(result.contains("AJUDA DO MENU"));
		assertTrue(result.contains("gerafrota"));
		assertTrue(result.contains("desisto"));
	}

	@Test
	@DisplayName("Deve processar comandos sem frota e terminar corretamente")
	void menu() {
		InputStream originalIn = System.in;
		PrintStream originalOut = System.out;
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		String commands = String.join(" ",
				"comandoInvalido",
				"estado",
				"mapa",
				"rajada",
				"simula",
				"exportapdf",
				"grafico",
				"scoreboard",
				"ajuda",
				"desisto"
		);

		try (PrintStream captured = new PrintStream(
				output, true, StandardCharsets.UTF_8)) {

			System.setIn(new ByteArrayInputStream(
					commands.getBytes(StandardCharsets.UTF_8)
			));

			System.setOut(captured);

			Tasks.menu();

		} finally {
			System.setIn(originalIn);
			System.setOut(originalOut);
		}

		String result = output.toString(StandardCharsets.UTF_8);

		assertTrue(result.contains("Que comando é esse??? Repete ..."));
		assertTrue(result.contains("Primeiro tem de gerar ou ler uma frota."));
		assertTrue(result.contains("AJUDA DO MENU"));
		assertTrue(result.contains("SCOREBOARD"));
		assertTrue(result.contains("Jogador"));
		assertTrue(result.contains("Bons ventos!"));
	}

	@Test
	@DisplayName("Deve gerar uma frota aleatória e apresentar o seu estado")
	void menuWithRandomFleet() {
		InputStream originalIn = System.in;
		PrintStream originalOut = System.out;
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		String commands = String.join(" ",
				"gerafrota",
				"estado",
				"mapa",
				"desisto"
		);

		try (PrintStream captured = new PrintStream(
				output, true, StandardCharsets.UTF_8)) {

			System.setIn(new ByteArrayInputStream(
					commands.getBytes(StandardCharsets.UTF_8)
			));

			System.setOut(captured);

			Tasks.menu();

		} finally {
			System.setIn(originalIn);
			System.setOut(originalOut);
		}

		String result = output.toString(StandardCharsets.UTF_8);

		assertTrue(result.contains("Estado da Frota: 11 a flutuar, 0 afundados!"));
		assertTrue(result.contains("LEGENDA"));
		assertTrue(result.contains("Bons ventos!"));
	}

	@Test
	@DisplayName("Deve processar comandos com frota e realizar uma rajada")
	void menuWithFleet() {
		InputStream originalIn = System.in;
		PrintStream originalOut = System.out;
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		String ships = String.join(" ",
				"barca 0 0 n",
				"barca 0 2 n",
				"barca 0 4 n",
				"barca 0 6 n",
				"barca 0 8 n",
				"barca 2 0 n",
				"barca 2 2 n",
				"barca 2 4 n",
				"barca 2 6 n",
				"barca 2 8 n",
				"barca 4 0 n"
		);

		String commands = String.join("\n",
				"lefrota",
				ships,
				"estado",
				"mapa",
				"rajada J1 J3 J5",
				"desisto"
		);

		try (PrintStream captured = new PrintStream(
				output, true, StandardCharsets.UTF_8)) {

			System.setIn(new ByteArrayInputStream(
					commands.getBytes(StandardCharsets.UTF_8)
			));

			System.setOut(captured);

			Tasks.menu();

		} finally {
			System.setIn(originalIn);
			System.setOut(originalOut);
		}

		String result = output.toString(StandardCharsets.UTF_8);

		assertTrue(result.contains("Estado da Frota: 11 a flutuar, 0 afundados!"));
		assertTrue(result.contains("LEGENDA"));
		assertTrue(result.contains("A jogada demorou"));
		assertTrue(result.contains("Bons ventos!"));
	}

	@Test
	@DisplayName("Deve construir uma frota válida com 11 navios")
	void buildFleet() {

		String inputData = String.join(" ",
				"navio 0 0 n",
				"barca 0 0 n",
				"barca 0 0 s",
				"barca 0 2 n",
				"barca 0 4 n",
				"barca 0 6 n",
				"barca 0 8 n",
				"barca 2 0 n",
				"barca 2 2 n",
				"barca 2 4 n",
				"barca 2 6 n",
				"barca 2 8 n",
				"barca 4 0 n"
		);

		try (Scanner input = new Scanner(inputData)) {
			Fleet fleet = Tasks.buildFleet(input);

			assertNotNull(fleet);
			assertEquals(11, fleet.getShips().size());

			for (IShip ship : fleet.getShips()) {
				assertEquals("Barca", ship.getCategory());
			}

			for (int i = 0; i < fleet.getShips().size(); i++) {
				for (int j = i + 1; j < fleet.getShips().size(); j++) {
					assertFalse(
							fleet.getShips().get(i).tooCloseTo(fleet.getShips().get(j)),
							"Dois navios não podem estar demasiado próximos"
					);
				}
			}
		}
	}

	@Test
	@DisplayName("Deve criar um navio válido e rejeitar um tipo desconhecido")
	void readShip() {

		try (Scanner input = new Scanner("barca 3 7 n")) {
			Ship ship = Tasks.readShip(input);

			assertNotNull(ship);
			assertInstanceOf(Barge.class, ship);
			assertEquals("Barca", ship.getCategory());
			assertEquals(3, ship.getPosition().getRow());
			assertEquals(7, ship.getPosition().getColumn());
			assertEquals(Compass.NORTH, ship.getBearing());
		}

		try (Scanner input = new Scanner("navio 3 7 n")) {
			Ship ship = Tasks.readShip(input);

			assertNull(ship);
		}
	}

	@Test
	@DisplayName("Deve ler corretamente uma posição")
	void readPosition() {

		try (Scanner input = new Scanner("3 7")) {
			Position position = Tasks.readPosition(input);

			assertNotNull(position);
			assertEquals(3, position.getRow());
			assertEquals(7, position.getColumn());
		}
	}

	@Test
	@DisplayName("Deve ler coordenadas clássicas e rejeitar formatos inválidos")
	void readClassicPosition() {

		try (Scanner input = new Scanner("A3")) {
			IPosition position = Tasks.readClassicPosition(input);

			assertEquals(0, position.getRow());
			assertEquals(2, position.getColumn());
		}

		try (Scanner input = new Scanner("A 3")) {
			IPosition position = Tasks.readClassicPosition(input);

			assertEquals(0, position.getRow());
			assertEquals(2, position.getColumn());
		}

		try (Scanner input = new Scanner("a3")) {
			IPosition position = Tasks.readClassicPosition(input);

			assertEquals(0, position.getRow());
			assertEquals(2, position.getColumn());
		}

		try (Scanner input = new Scanner("AB3")) {
			assertThrows(
					IllegalArgumentException.class,
					() -> Tasks.readClassicPosition(input)
			);
		}

		try (Scanner input = new Scanner("")) {
			assertThrows(
					IllegalArgumentException.class,
					() -> Tasks.readClassicPosition(input)
			);
		}
	}
}

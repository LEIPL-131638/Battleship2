package battleship;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Exporta para PDF o histórico das rajadas de um jogo e o estado final do tabuleiro,
 * usando a biblioteca Apache PDFBox.
 *
 * @author Dinis Fernandes (130478)
 */
public class PdfExporter {

    private static final float MARGIN = 50;
    private static final float FONT_SIZE = 11;
    private static final float LEADING = 15;

    private final PDType1Font font = new PDType1Font(Standard14Fonts.FontName.COURIER);
    private final PDType1Font boldFont = new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD);

    /**
     * Gera um ficheiro PDF com o histórico das rajadas e o tabuleiro final do jogo.
     *
     * @param game     o jogo cujo histórico vai ser exportado
     * @param fileName o caminho do ficheiro PDF a criar
     * @return o ficheiro PDF criado
     * @throws IOException se não for possível escrever o ficheiro
     */
    public File export(IGame game, String fileName) throws IOException {
        assert game != null;

        List<String> lines = buildLines(game);
        File file = new File(fileName);

        try (PDDocument document = new PDDocument()) {
            writeLines(document, lines);
            document.save(file);
        }
        return file;
    }

    /**
     * Constrói as linhas de texto do relatório: cabeçalho, rajadas, resumo e tabuleiro.
     *
     * @param game o jogo a exportar
     * @return a lista de linhas a escrever no PDF
     */
    List<String> buildLines(IGame game) {
        List<String> lines = new ArrayList<>();
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        lines.add("# Batalha Naval - Histórico das jogadas");
        lines.add("Exportado em " + date);
        lines.add("");

        List<IMove> moves = game.getAlienMoves();
        if (moves.isEmpty())
            lines.add("Ainda não foi disparada nenhuma rajada.");

        for (IMove move : moves) {
            lines.add("# Rajada " + move.getNumber());
            List<IPosition> shots = move.getShots();
            List<IGame.ShotResult> results = move.getShotResults();
            for (int i = 0; i < shots.size(); i++)
                lines.add("   " + shots.get(i) + "  ->  " + describe(results.get(i)));
        }

        lines.add("");
        lines.add("# Resumo");
        lines.add("Navios atingidos (tiros certeiros): " + game.getHits());
        lines.add("Navios afundados: " + game.getSunkShips());
        lines.add("Navios a flutuar: " + game.getRemainingShips());
        lines.add("Tiros repetidos: " + game.getRepeatedShots());
        lines.add("Tiros fora do tabuleiro: " + game.getInvalidShots());
        lines.add("");
        lines.add("# Tabuleiro final");
        lines.addAll(boardLines(game.getMyFleet(), moves));
        lines.add("'#' navio  '*' tiro certeiro  'o' tiro na água  '-' junto a navio afundado");
        return lines;
    }

    /**
     * Descreve em texto o resultado de um tiro.
     *
     * @param result o resultado do tiro
     * @return a descrição do resultado
     */
    static String describe(IGame.ShotResult result) {
        if (!result.valid())
            return "fora do tabuleiro";
        if (result.repeated())
            return "tiro repetido";
        if (result.ship() == null)
            return "água";
        if (result.sunk())
            return "navio afundado (" + result.ship().getCategory() + ")";
        return "navio atingido (" + result.ship().getCategory() + ")";
    }

    /**
     * Desenha o tabuleiro em texto, com os navios e os tiros recebidos.
     *
     * @param fleet a frota do jogador
     * @param moves as rajadas recebidas
     * @return as linhas de texto do tabuleiro
     */
    static List<String> boardLines(IFleet fleet, List<IMove> moves) {
        char[][] map = new char[Game.BOARD_SIZE][Game.BOARD_SIZE];
        for (char[] row : map)
            java.util.Arrays.fill(row, '.');

        for (IShip ship : fleet.getShips()) {
            for (IPosition pos : ship.getPositions())
                map[pos.getRow()][pos.getColumn()] = '#';
            if (!ship.stillFloating())
                for (IPosition pos : ship.getAdjacentPositions())
                    if (pos.isInside() && map[pos.getRow()][pos.getColumn()] == '.')
                        map[pos.getRow()][pos.getColumn()] = '-';
        }

        for (IMove move : moves)
            for (IPosition shot : move.getShots())
                if (shot.isInside()) {
                    char c = map[shot.getRow()][shot.getColumn()];
                    map[shot.getRow()][shot.getColumn()] = (c == '#' || c == '*') ? '*' : 'o';
                }

        List<String> lines = new ArrayList<>();
        StringBuilder header = new StringBuilder("    ");
        for (int col = 1; col <= Game.BOARD_SIZE; col++)
            header.append(String.format("%2d", col));
        lines.add(header.toString());
        for (int row = 0; row < Game.BOARD_SIZE; row++) {
            StringBuilder line = new StringBuilder(" " + (char) ('A' + row) + "  ");
            for (int col = 0; col < Game.BOARD_SIZE; col++)
                line.append(' ').append(map[row][col]);
            lines.add(line.toString());
        }
        return lines;
    }

    /**
     * Escreve as linhas no documento, criando páginas novas quando necessário.
     * As linhas que começam por "# " são escritas a negrito como títulos.
     *
     * @param document o documento PDF
     * @param lines    as linhas a escrever
     * @throws IOException se houver um erro ao escrever no documento
     */
    private void writeLines(PDDocument document, List<String> lines) throws IOException {
        PDPageContentStream content = null;
        float y = 0;
        try {
            for (String line : lines) {
                if (content == null || y < MARGIN) {
                    if (content != null) {
                        content.endText();
                        content.close();
                    }
                    PDPage page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    y = page.getMediaBox().getHeight() - MARGIN;
                    content.beginText();
                    content.newLineAtOffset(MARGIN, y);
                }
                boolean title = line.startsWith("# ");
                content.setFont(title ? boldFont : font, title ? FONT_SIZE + 2 : FONT_SIZE);
                content.showText(title ? line.substring(2) : line);
                content.newLineAtOffset(0, -LEADING);
                y -= LEADING;
            }
        } finally {
            if (content != null) {
                content.endText();
                content.close();
            }
        }
    }
}
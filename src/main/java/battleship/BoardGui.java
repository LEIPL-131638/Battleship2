package battleship;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class BoardGui {

    public static void showBoard(Game game) {
        FlatLightLaf.setup();

        char[][] map = new char[Game.BOARD_SIZE][Game.BOARD_SIZE];
        for (int r = 0; r < Game.BOARD_SIZE; r++)
            for (int c = 0; c < Game.BOARD_SIZE; c++)
                map[r][c] = '.';

        for (IShip ship : game.getMyFleet().getShips()) {
            for (IPosition pos : ship.getPositions())
                map[pos.getRow()][pos.getColumn()] = '#';
            if (!ship.stillFloating())
                for (IPosition adj : ship.getAdjacentPositions())
                    if (adj.isInside() && map[adj.getRow()][adj.getColumn()] == '.')
                        map[adj.getRow()][adj.getColumn()] = '-';
        }

        for (IMove move : game.getAlienMoves()) {
            for (IPosition shot : move.getShots()) {
                if (shot.isInside()) {
                    int r = shot.getRow();
                    int c = shot.getColumn();
                    if (map[r][c] == '#' || map[r][c] == '*')
                        map[r][c] = '*';
                    else
                        map[r][c] = 'o';
                }
            }
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Batalha Naval - Tabuleiro");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(500, 500);
            frame.setLocationRelativeTo(null);

            JPanel grid = new JPanel(new GridLayout(Game.BOARD_SIZE + 1, Game.BOARD_SIZE + 1, 2, 2));
            grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            grid.add(new JLabel("", SwingConstants.CENTER));
            for (int c = 1; c <= Game.BOARD_SIZE; c++) {
                grid.add(new JLabel(String.valueOf(c), SwingConstants.CENTER));
            }

            for (int r = 0; r < Game.BOARD_SIZE; r++) {
                grid.add(new JLabel(String.valueOf((char) ('A' + r)), SwingConstants.CENTER));
                for (int c = 0; c < Game.BOARD_SIZE; c++) {
                    char symbol = map[r][c];
                    JLabel cell = new JLabel(String.valueOf(symbol), SwingConstants.CENTER);
                    cell.setOpaque(true);
                    cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                    if (symbol == '#') {
                        cell.setBackground(new Color(160, 160, 160));
                    } else if (symbol == '*') {
                        cell.setBackground(new Color(220, 80, 80));
                        cell.setForeground(Color.WHITE);
                    } else if (symbol == 'o') {
                        cell.setBackground(new Color(100, 160, 210));
                    } else if (symbol == '-') {
                        cell.setBackground(new Color(210, 210, 210));
                    } else {
                        cell.setBackground(new Color(200, 230, 255));
                    }
                    grid.add(cell);
                }
            }

            frame.add(grid);
            frame.setVisible(true);
        });
    }
}

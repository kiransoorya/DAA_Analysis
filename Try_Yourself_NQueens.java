import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Try_Yourself_NQueens extends JFrame {
    private JButton[][] buttons;
    private List<Point> queens = new ArrayList<>();

    public Try_Yourself_NQueens(int size) {
        // Set up the JFrame
        super("N-Queens Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new GridLayout(size, size));

        // Initialize the 2D array of buttons
        buttons = new JButton[size][size];

        // Create buttons and add them to the JFrame
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
                buttons[i][j].addActionListener(new ButtonClickListener());
                add(buttons[i][j]);
            }
        }

        // Set up JFrame properties
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            int clickedRow = -1;
            int clickedCol = -1;

            // Find the location of the clicked button
            for (int i = 0; i < buttons.length; i++) {
                for (int j = 0; j < buttons[i].length; j++) {
                    if (buttons[i][j] == source) {
                        clickedRow = i;
                        clickedCol = j;
                        break;
                    }
                }
            }

            if (isQueenAt(clickedRow, clickedCol)) {
                // If there is a queen at the clicked position and it's on a yellow background, remove it
                if (buttons[clickedRow][clickedCol].getBackground() == Color.YELLOW) {
                    queens.remove(new Point(clickedRow, clickedCol));
                    clearMarkings();
                    updateMarkings();
                }
            } else {
                // If the background is not yellow, place a queen and update markings
                if (buttons[clickedRow][clickedCol].getBackground() != Color.YELLOW) {
                    queens.add(new Point(clickedRow, clickedCol));
                    clearMarkings();
                    updateMarkings();
                }
            }
        }

        private boolean isQueenAt(int row, int col) {
            // Check if there is a queen at the specified position
            return queens.contains(new Point(row, col));
        }
    }

    private void updateMarkings() {
        for (Point queen : queens) {
            int row = queen.x;
            int col = queen.y;

            // Check if the background is not yellow before updating markings
            if (buttons[row][col].getBackground() != Color.YELLOW) {
                // Display "Q" on the board
                buttons[row][col].setText("Q");

                // Change color horizontally and vertically
                for (int i = 0; i < buttons.length; i++) {
                    buttons[i][col].setBackground(Color.YELLOW);
                    buttons[row][i].setBackground(Color.YELLOW);
                }

                // Change color on the main diagonal
                for (int i = 0; i < buttons.length; i++) {
                    int c = col + (i - row);
                    if (c >= 0 && c < buttons.length) {
                        buttons[i][c].setBackground(Color.YELLOW);
                    }
                }

                // Change color on the secondary diagonal
                for (int i = 0; i < buttons.length; i++) {
                    int c = col - (i - row);
                    if (c >= 0 && c < buttons.length) {
                        buttons[i][c].setBackground(Color.YELLOW);
                    }
                }
            }
        }
    }

    private void clearMarkings() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setBackground(null);
                buttons[i][j].setText(""); // Clear the displayed text
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String sizeInput = JOptionPane.showInputDialog("Enter the size of the board:");

            // Check if the user clicked Cancel or closed the dialog
            if (sizeInput != null) {
                try {
                    int size = Integer.parseInt(sizeInput);
                    new Try_Yourself_NQueens(size);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer.");
                }
            }
        });
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class NQueensGUI {
    private JFrame frame;
    private JPanel boardPanel;
    private List<int[][]> solutions;
    private int currentSolutionIndex = -1; // Initialize to -1

    private JButton prevButton;
    private JButton currentButton;
    private JButton nextButton;

    public NQueensGUI() {
        frame = new JFrame("N-Queens Solver");
        frame.setSize(700, 700); // Adjusted size for better layout
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        boardPanel = new JPanel();
        mainPanel.add(boardPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        prevButton = new JButton("Previous Solution");
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPreviousSolution();
            }
        });
        buttonPanel.add(prevButton);

        currentButton = new JButton("Current Solution");
        currentButton.setEnabled(false); // Disabled by default
        buttonPanel.add(currentButton);

        nextButton = new JButton("Next Solution");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextSolution();
            }
        });
        buttonPanel.add(nextButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);

        // Prompt user for board size
        int boardSize = getBoardSizeFromUser();
        if (boardSize > 0) {
            solveNQueens(boardSize);
            showNextSolution();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid board size. Exiting.");
            System.exit(0);
        }
    }

    private int getBoardSizeFromUser() {
        int boardSize = 0;
        boolean isValidInput = false;
        while (!isValidInput) {
            try {
                String input = JOptionPane.showInputDialog(frame, "Enter the board size (e.g., 8):");
                if (input == null) {
                    // Handle cancellation
                    return -1;
                }
                boardSize = Integer.parseInt(input);
                if (boardSize > 0) {
                    isValidInput = true;
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a positive integer.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid integer.");
            }
        }
        return boardSize;
    }

    private void solveNQueens(int n) {
        solutions = new ArrayList<>();
        int[] queens = new int[n];
        placeQueens(queens, 0, n);
    }

    private void placeQueens(int[] queens, int row, int n) {
        if (row == n) {
            solutions.add(generateBoard(queens, n));
            return;
        }

        for (int col = 0; col < n; col++) {
            if (isValid(queens, row, col)) {
                queens[row] = col;
                placeQueens(queens, row + 1, n);
            }
        }
    }

    private boolean isValid(int[] queens, int row, int col) {
        for (int i = 0; i < row; i++) {
            if (queens[i] == col || queens[i] - i == col - row || queens[i] + i == col + row) {
                return false;
            }
        }
        return true;
    }

    private int[][] generateBoard(int[] queens, int n) {
        int[][] board = new int[n][n];
        for (int i = 0; i < n; i++) {
            board[i][queens[i]] = 1;
        }
        return board;
    }

    private void showNextSolution() {
        if (solutions != null && !solutions.isEmpty()) {
            currentSolutionIndex = (currentSolutionIndex + 1) % solutions.size();
            drawBoard();
            updateButtonState();
        }
    }

    private void showPreviousSolution() {
        if (solutions != null && !solutions.isEmpty()) {
            currentSolutionIndex = (currentSolutionIndex - 1 + solutions.size()) % solutions.size();
            drawBoard();
            updateButtonState();
        }
    }

    private void drawBoard() {
        boardPanel.setVisible(false);
        boardPanel.removeAll();

        if (solutions != null && !solutions.isEmpty()) {
            int[][] currentSolution = solutions.get(currentSolutionIndex);

            JPanel centeringPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbcCentering = new GridBagConstraints();
            gbcCentering.gridx = 0;
            gbcCentering.gridy = 0;

            JPanel boardWithLabels = new JPanel(new BorderLayout());

            // Create a panel for row numbers on the right with margin
            JPanel rowNumberPanel = new JPanel(new GridLayout(currentSolution.length, 1));
            rowNumberPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Add right margin
            rowNumberPanel.setBackground(Color.YELLOW); // Yellow background
            for (int i = 0; i < currentSolution.length; i++) {
                JLabel rowLabel = new JLabel(Integer.toString(i + 1));
                rowLabel.setHorizontalAlignment(JLabel.CENTER);
                rowNumberPanel.add(rowLabel);
            }

            // Create a panel for the chessboard
            JPanel chessboardPanel = new JPanel(new GridLayout(currentSolution.length, currentSolution.length, 10, 10));
            Dimension buttonSize = new Dimension(50, 50);

            // Add column numbers on the top
            JPanel columnNumberPanel = new JPanel(new GridLayout(1, currentSolution.length));
            columnNumberPanel.setBackground(Color.YELLOW); // Yellow background
            for (int i = 0; i < currentSolution.length; i++) {
                JLabel colLabel = new JLabel(Integer.toString(i + 1));
                colLabel.setHorizontalAlignment(JLabel.CENTER);
                columnNumberPanel.add(colLabel);
            }

            // Add cells to the chessboard panel
            for (int i = 0; i < currentSolution.length; i++) {
                for (int j = 0; j < currentSolution.length; j++) {
                    JButton button = new JButton();
                    button.setEnabled(false);
                    if (currentSolution[i][j] == 1) {
                        button.setText("Q");
                    }
                    button.setPreferredSize(buttonSize);
                    chessboardPanel.add(button);
                }
            }

            // Add row numbers on the right and chessboard in the center
            boardWithLabels.add(rowNumberPanel, BorderLayout.EAST);
            boardWithLabels.add(chessboardPanel, BorderLayout.CENTER);

            // Add column numbers at the bottom
            boardWithLabels.add(columnNumberPanel, BorderLayout.SOUTH);

            centeringPanel.add(boardWithLabels, gbcCentering);
            boardPanel.add(centeringPanel, BorderLayout.CENTER);
        } else {
            JLabel infoLabel = new JLabel("No solution found.");
            boardPanel.add(infoLabel, BorderLayout.CENTER);
        }

        boardPanel.setVisible(true);

        // Disable the "Next Solution" and "Previous Solution" buttons after reaching the first/last solution
        updateButtonState();
        frame.getRootPane().setDefaultButton(null);
        frame.revalidate();
        frame.repaint();
    }

    private void updateButtonState() {
        // Disable the "Next Solution" button after reaching the last solution
        nextButton.setEnabled(currentSolutionIndex < solutions.size() - 1);

        // Disable the "Previous Solution" button when viewing the first solution
        prevButton.setEnabled(currentSolutionIndex > 0);

        // Update the text on the "Next Solution" button with solution info
        if (currentSolutionIndex == solutions.size() - 1) {
            nextButton.setText("No More Solutions");
        } else {
            nextButton.setText("Next Solution " + (currentSolutionIndex + 2) + "/" + solutions.size());
        }

        // Update the text on the "Previous Solution" button with solution info
        if (currentSolutionIndex == 0) {
            prevButton.setText("No Previous Solution");
        } else {
            prevButton.setText("Previous Solution " + (currentSolutionIndex) + "/" + solutions.size());
        }

        // Update the text on the "Current Solution" button with solution info
        currentButton.setText("Current Solution " + (currentSolutionIndex + 1) + "/" + solutions.size());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NQueensGUI();
            }
        });
    }
}

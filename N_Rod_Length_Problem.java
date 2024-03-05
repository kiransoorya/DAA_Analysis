import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class N_Rod_Length_Problem extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private RodPanel rodPanel; // Use custom JPanel for rod drawing
    private double[] Cost_Array;

    public N_Rod_Length_Problem() {
        setTitle("N Rod Problem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(null);

        JLabel promptLabel = new JLabel("Enter the length of the rod:");
        JTextField lengthField = new JTextField();
        JButton createTableButton = new JButton("Create Table");

        promptLabel.setBounds(10, 10, 200, 20);
        lengthField.setBounds(10, 40, 150, 20);
        createTableButton.setBounds(170, 40, 200, 20);

        add(promptLabel);
        add(lengthField);
        add(createTableButton);

        createTableButton.addActionListener(e -> {
            try {
                int length = Integer.parseInt(lengthField.getText());
                updateTable(length);
                JFrame frame = new JFrame();
                frame.setTitle("Rod Cut Visualizer");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setBounds(400, 300, 400, 300);
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());

                rodPanel = new RodPanel(); // Use custom JPanel
                mainPanel.add(rodPanel, BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                frame.add(mainPanel);
                frame.setVisible(true);
                int rodlength = length;
                if (rodlength > 0) {
                    RodCutting(Cost_Array, rodlength);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numerical value for the length.");
            }
        });
    }

    private void updateTable(int length) {
        Cost_Array = new double[length];
        if (table == null) {
            tableModel = new DefaultTableModel();
            tableModel.addColumn("Length");
            tableModel.addColumn("Cost");

            table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBounds(10, 80, 300, 20 + length * 20);
            add(scrollPane);
        }

        tableModel.setRowCount(0);

        for (int i = 0; i <= length; i++) {
            double cost = 0;
            if (i > 0) {
                String costInput = JOptionPane.showInputDialog("Enter the cost for length " + i + ":");
                if (costInput == null) {
                    cost = 0;
                } else {
                    try {
                        cost = Double.parseDouble(costInput);
                        Cost_Array[i - 1] = cost;
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Please enter a valid numerical value for the cost.");
                        return;
                    }
                }
            }

            tableModel.addRow(new Object[]{i, cost});
        }

        revalidate();
        repaint();
    }

    private void RodCutting(double[] price, int n) {
        double val[] = new double[n + 1];
        int cuts[] = new int[n + 1];
        int[] completeCut = new int[n];
        for (int i = 0; i < n; i++) {
            completeCut[i] = 0;
        }
        val[0] = 0;

        // Bottom up approach to solve the problem
        for (int i = 1; i <= n; i++) {
            double maxVal = Integer.MIN_VALUE;
            for (int j = 0; j < i; j++) {
                if (price[j] + val[i - j - 1] > maxVal) {
                    maxVal = price[j] + val[i - j - 1];
                    cuts[i] = j + 1; // Record the cut position
                }
            }
            val[i] = maxVal;
        }
        int k = 0;
        // Print the cuts
        System.out.print("Cut positions: ");
        while (n > 0) {
            System.out.print(cuts[n] + " ");
            completeCut[k] = cuts[n];
            k++;
            n = n - cuts[n];
        }
        System.out.println();
        for (int j = 0; j < k; j++) {
            System.out.print(completeCut[j] + " ");
        }
        System.out.println();
        System.out.println();
        rodPanel.drawCutPiece(completeCut, n , val[price.length]);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            N_Rod_Length_Problem calculator = new N_Rod_Length_Problem();
            calculator.setVisible(true);
        });
    }
}

class RodPanel extends JPanel {

    private int[] cutIndexes;
    private double maxCost;
    public void drawCutPiece(int[] cuts, int size , double max_cost) {
        cutIndexes = cuts;
        maxCost = max_cost;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (cutIndexes != null) {
            int startX = 50;
            int startY = 200;
            int width = 20;
            g.setColor(Color.BLACK);
            for (int cutIndex : cutIndexes) {
                g.fillRect(startX, startY - cutIndex * width, 50, cutIndex * width);
                if(cutIndex != 0 ) {
                    g.drawString(String.valueOf(cutIndex), startX + 20, startY + 20);
                }
                startX += 60; // Adjust spacing between cut pieces
            }
            g.setColor(Color.BLUE);
            g.drawString("Maximum price after sold is :"+String.valueOf(maxCost), 20, startY + 40);
            // Display rod size below the rod drawings
        }
    }
}

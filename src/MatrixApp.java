import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Власне виключення для негативних значень матриці
class NegativeElementException extends ArithmeticException {
    public NegativeElementException(String message) {
        super(message);
    }
}

public class MatrixApp extends JFrame {
    private JTextField fileField;
    private JTable table;
    private JLabel resultLabel;
    private DefaultTableModel tableModel;
    private int[][] matrix;
    private int n;

    public MatrixApp() {
        setTitle("Matrix Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Панель введення файлу
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(new JLabel("Enter file path:"));
        fileField = new JTextField(20);
        inputPanel.add(fileField);
        JButton loadButton = new JButton("Load Matrix");
        inputPanel.add(loadButton);

        add(inputPanel, BorderLayout.NORTH);

        // Таблиця для відображення матриці
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        add(tableScrollPane, BorderLayout.CENTER);

        // Кнопка для обробки матриці
        JPanel bottomPanel = new JPanel();
        JButton processButton = new JButton("Process Matrix");
        bottomPanel.add(processButton);
        resultLabel = new JLabel("");
        bottomPanel.add(resultLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        // Обробник події завантаження файлу
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMatrixFromFile(fileField.getText());
            }
        });

        // Обробник події обробки матриці
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processMatrix();
            }
        });
    }

    // Метод для читання матриці з файлу
    private void loadMatrixFromFile(String filePath) {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            n = scanner.nextInt(); // Читаємо розмір матриці
            if (n > 20) {
                throw new IllegalArgumentException("Matrix size should be <= 20");
            }

            matrix = new int[n][n];
            tableModel.setRowCount(n);
            tableModel.setColumnCount(n);

            // Читаємо матрицю з файлу
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (!scanner.hasNextInt()) {
                        throw new NumberFormatException("Invalid data format in file.");
                    }
                    matrix[i][j] = scanner.nextInt();
                    tableModel.setValueAt(matrix[i][j], i, j);

                    // Генерація власного виключення для негативних значень
                    if (matrix[i][j] < 0) {
                        throw new NegativeElementException("Matrix contains negative elements.");
                    }
                }
            }

            resultLabel.setText("Matrix loaded successfully.");
            scanner.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "File not found: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid data format: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NegativeElementException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Метод для обробки матриці - переміщення максимального елемента в лівий верхній кут
    private void processMatrix() {
        if (matrix == null || n == 0) {
            resultLabel.setText("Matrix is not loaded.");
            return;
        }

        int maxElement = matrix[0][0];
        int maxRow = 0, maxCol = 0;

        // Пошук максимального елемента
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] > maxElement) {
                    maxElement = matrix[i][j];
                    maxRow = i;
                    maxCol = j;
                }
            }
        }

        // Перестановка рядків і стовпців для переміщення максимального елемента в [0][0]
        swapRows(0, maxRow);
        swapColumns(0, maxCol);

        // Оновлення таблиці після обробки
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tableModel.setValueAt(matrix[i][j], i, j);
            }
        }

        resultLabel.setText("Matrix processed successfully.");
    }

    // Метод для перестановки рядків
    private void swapRows(int row1, int row2) {
        int[] temp = matrix[row1];
        matrix[row1] = matrix[row2];
        matrix[row2] = temp;
    }

    // Метод для перестановки стовпців
    private void swapColumns(int col1, int col2) {
        for (int i = 0; i < n; i++) {
            int temp = matrix[i][col1];
            matrix[i][col1] = matrix[i][col2];
            matrix[i][col2] = temp;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MatrixApp().setVisible(true);
            }
        });
    }
}

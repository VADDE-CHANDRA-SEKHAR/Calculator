import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Calculator extends JFrame {
    private JTabbedPane tabbedPane;
    
    public Calculator() {
        setTitle("Advanced Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Add tabs
        tabbedPane.addTab("Basic", new BasicCalculator());
        tabbedPane.addTab("Scientific", new ScientificCalculator());
        tabbedPane.addTab("Currency", new CurrencyConverter());
        
        add(tabbedPane);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calc = new Calculator();
            calc.setVisible(true);
        });
    }
}

// Basic Calculator Panel
class BasicCalculator extends JPanel implements ActionListener {
    private JTextField display;
    private JLabel expressionLabel;
    private String currentInput = "0";
    private String expression = "";
    private String lastResult = "";
    
    public BasicCalculator() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(102, 126, 234));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Display panel
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout(5, 5));
        displayPanel.setBackground(new Color(42, 42, 62));
        displayPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        expressionLabel = new JLabel(" ");
        expressionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        expressionLabel.setForeground(new Color(136, 136, 136));
        expressionLabel.setHorizontalAlignment(JLabel.RIGHT);
        
        display = new JTextField("0");
        display.setFont(new Font("Segoe UI", Font.BOLD, 32));
        display.setForeground(Color.WHITE);
        display.setBackground(new Color(42, 42, 62));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBorder(BorderFactory.createEmptyBorder());
        
        displayPanel.add(expressionLabel, BorderLayout.NORTH);
        displayPanel.add(display, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        buttonsPanel.setBackground(new Color(102, 126, 234));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Create buttons
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        buttonsPanel.add(createButton("C"), gbc);
        gbc.gridx = 1;
        buttonsPanel.add(createButton("DEL"), gbc);
        gbc.gridx = 2;
        buttonsPanel.add(createButton("%"), gbc);
        gbc.gridx = 3;
        buttonsPanel.add(createButton("÷"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        buttonsPanel.add(createButton("7"), gbc);
        gbc.gridx = 1;
        buttonsPanel.add(createButton("8"), gbc);
        gbc.gridx = 2;
        buttonsPanel.add(createButton("9"), gbc);
        gbc.gridx = 3;
        buttonsPanel.add(createButton("×"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        buttonsPanel.add(createButton("4"), gbc);
        gbc.gridx = 1;
        buttonsPanel.add(createButton("5"), gbc);
        gbc.gridx = 2;
        buttonsPanel.add(createButton("6"), gbc);
        gbc.gridx = 3;
        buttonsPanel.add(createButton("−"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        buttonsPanel.add(createButton("1"), gbc);
        gbc.gridx = 1;
        buttonsPanel.add(createButton("2"), gbc);
        gbc.gridx = 2;
        buttonsPanel.add(createButton("3"), gbc);
        gbc.gridx = 3;
        buttonsPanel.add(createButton("+"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        buttonsPanel.add(createButton("0"), gbc);
        gbc.gridx = 2; gbc.gridwidth = 1;
        buttonsPanel.add(createButton("."), gbc);
        gbc.gridx = 3;
        buttonsPanel.add(createButton("="), gbc);
        
        add(displayPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);
    }
    
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        
        if (text.equals("C")) {
            btn.setBackground(new Color(245, 101, 101));
        } else if (text.equals("DEL")) {
            btn.setBackground(new Color(237, 137, 54));
        } else if (text.equals("%")) {
            btn.setBackground(new Color(159, 122, 234));
        } else if (text.equals("=")) {
            btn.setBackground(new Color(72, 187, 120));
        } else if (text.matches("[÷×−+]")) {
            btn.setBackground(new Color(102, 126, 234));
        } else {
            btn.setBackground(new Color(58, 58, 78));
        }
        
        return btn;
    }
    
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (command.matches("[0-9]")) {
            addNumber(command);
        } else if (command.equals(".")) {
            addDecimal();
        } else if (command.matches("[÷×−+]")) {
            addOperator(command);
        } else if (command.equals("=")) {
            calculate();
        } else if (command.equals("C")) {
            clearCalc();
        } else if (command.equals("DEL")) {
            deleteLast();
        } else if (command.equals("%")) {
            addPercent();
        }
    }
    
    private void updateDisplay() {
        display.setText(currentInput);
        expressionLabel.setText(expression.isEmpty() ? " " : expression);
    }
    
    private void addNumber(String num) {
        if (currentInput.equals("0") || currentInput.equals(lastResult)) {
            currentInput = num;
            lastResult = "";
        } else {
            currentInput += num;
        }
        updateDisplay();
    }
    
    private void addDecimal() {
        if (currentInput.equals(lastResult)) {
            currentInput = "0";
            lastResult = "";
        }
        if (!currentInput.contains(".")) {
            currentInput += ".";
            updateDisplay();
        }
    }
    
    private void addOperator(String op) {
        if (!expression.isEmpty() && !currentInput.equals(lastResult)) {
            calculate();
        }
        expression = currentInput + " " + op;
        lastResult = currentInput;
        updateDisplay();
    }
    
    private void addPercent() {
        try {
            double value = Double.parseDouble(currentInput);
            currentInput = String.valueOf(value / 100);
            updateDisplay();
        } catch (NumberFormatException ex) {
            currentInput = "Error";
            updateDisplay();
        }
    }
    
    private void deleteLast() {
        if (currentInput.length() > 1 && !currentInput.equals(lastResult)) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
        } else {
            currentInput = "0";
        }
        updateDisplay();
    }
    
    private void clearCalc() {
        currentInput = "0";
        expression = "";
        lastResult = "";
        updateDisplay();
    }
    
    private void calculate() {
        if (expression.isEmpty()) return;
        
        try {
            String[] parts = expression.split(" ");
            double num1 = Double.parseDouble(parts[0]);
            String operator = parts[1];
            double num2 = Double.parseDouble(currentInput);
            double result = 0;
            
            switch(operator) {
                case "+": result = num1 + num2; break;
                case "−": result = num1 - num2; break;
                case "×": result = num1 * num2; break;
                case "÷":
                    if (num2 != 0) result = num1 / num2;
                    else { currentInput = "Error"; expression = ""; updateDisplay(); return; }
                    break;
            }
            
            result = Math.round(result * 100000000.0) / 100000000.0;
            currentInput = (result == (long) result) ? String.valueOf((long) result) : String.valueOf(result);
            lastResult = currentInput;
            expression = "";
            updateDisplay();
        } catch (Exception ex) {
            currentInput = "Error";
            expression = "";
            updateDisplay();
        }
    }
}

// Scientific Calculator Panel
class ScientificCalculator extends JPanel implements ActionListener {
    private JTextField display;
    private JLabel expressionLabel;
    private double currentValue = 0;
    private String currentInput = "0";
    private String operator = "";
    private String expression = "";
    private boolean startNewNumber = false;
    
    public ScientificCalculator() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(102, 126, 234));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Display
        JPanel displayPanel = new JPanel(new BorderLayout(5, 5));
        displayPanel.setBackground(new Color(42, 42, 62));
        displayPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        expressionLabel = new JLabel(" ");
        expressionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        expressionLabel.setForeground(new Color(136, 136, 136));
        expressionLabel.setHorizontalAlignment(JLabel.RIGHT);
        
        display = new JTextField("0");
        display.setFont(new Font("Segoe UI", Font.BOLD, 32));
        display.setForeground(Color.WHITE);
        display.setBackground(new Color(42, 42, 62));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBorder(BorderFactory.createEmptyBorder());
        
        displayPanel.add(expressionLabel, BorderLayout.NORTH);
        displayPanel.add(display, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(6, 4, 8, 8));
        buttonsPanel.setBackground(new Color(102, 126, 234));
        
        String[] buttons = {
            "C", "DEL", "π", "e",
            "sin", "cos", "tan", "√",
            "x²", "xʸ", "log", "ln",
            "7", "8", "9", "÷",
            "4", "5", "6", "×",
            "1", "2", "3", "−"
        };
        
        for (String text : buttons) {
            buttonsPanel.add(createButton(text));
        }
        
        JPanel lastRow = new JPanel(new GridLayout(1, 4, 8, 8));
        lastRow.setBackground(new Color(102, 126, 234));
        JButton zeroBtn = createButton("0");
        zeroBtn.setPreferredSize(new Dimension(100, 50));
        lastRow.add(zeroBtn);
        lastRow.add(createButton("."));
        lastRow.add(createButton("+"));
        lastRow.add(createButton("="));
        
        JPanel mainButtons = new JPanel(new BorderLayout(8, 8));
        mainButtons.setBackground(new Color(102, 126, 234));
        mainButtons.add(buttonsPanel, BorderLayout.CENTER);
        mainButtons.add(lastRow, BorderLayout.SOUTH);
        
        add(displayPanel, BorderLayout.NORTH);
        add(mainButtons, BorderLayout.CENTER);
    }
    
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setForeground(Color.WHITE);
        btn.addActionListener(this);
        
        if (text.equals("C")) {
            btn.setBackground(new Color(245, 101, 101));
        } else if (text.equals("DEL")) {
            btn.setBackground(new Color(237, 137, 54));
        } else if (text.equals("=")) {
            btn.setBackground(new Color(72, 187, 120));
        } else if (text.matches("[÷×−+]|xʸ")) {
            btn.setBackground(new Color(102, 126, 234));
        } else if (text.matches("sin|cos|tan|√|x²|log|ln|π|e")) {
            btn.setBackground(new Color(159, 122, 234));
        } else {
            btn.setBackground(new Color(58, 58, 78));
        }
        
        return btn;
    }
    
    private void updateDisplay() {
        display.setText(currentInput);
        expressionLabel.setText(expression.isEmpty() ? " " : expression);
    }
    
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        
        try {
            if (cmd.matches("[0-9]")) {
                if (currentInput.equals("0") || startNewNumber) {
                    currentInput = cmd;
                    startNewNumber = false;
                } else {
                    currentInput += cmd;
                }
                updateDisplay();
            } else if (cmd.equals(".")) {
                if (startNewNumber) {
                    currentInput = "0.";
                    startNewNumber = false;
                } else if (!currentInput.contains(".")) {
                    currentInput += ".";
                }
                updateDisplay();
            } else if (cmd.equals("C")) {
                currentInput = "0";
                currentValue = 0;
                operator = "";
                expression = "";
                startNewNumber = false;
                updateDisplay();
            } else if (cmd.equals("DEL")) {
                if (!startNewNumber && currentInput.length() > 1) {
                    currentInput = currentInput.substring(0, currentInput.length() - 1);
                } else {
                    currentInput = "0";
                }
                updateDisplay();
            } else if (cmd.equals("π")) {
                currentInput = String.valueOf(Math.PI);
                startNewNumber = true;
                updateDisplay();
            } else if (cmd.equals("e")) {
                currentInput = String.valueOf(Math.E);
                startNewNumber = true;
                updateDisplay();
            } else if (cmd.equals("sin")) {
                double val = Math.sin(Math.toRadians(Double.parseDouble(currentInput)));
                currentInput = formatResult(val);
                startNewNumber = true;
                updateDisplay();
            } else if (cmd.equals("cos")) {
                double val = Math.cos(Math.toRadians(Double.parseDouble(currentInput)));
                currentInput = formatResult(val);
                startNewNumber = true;
                updateDisplay();
            } else if (cmd.equals("tan")) {
                double val = Math.tan(Math.toRadians(Double.parseDouble(currentInput)));
                currentInput = formatResult(val);
                startNewNumber = true;
                updateDisplay();
            } else if (cmd.equals("√")) {
                double val = Math.sqrt(Double.parseDouble(currentInput));
                currentInput = formatResult(val);
                startNewNumber = true;
                updateDisplay();
            } else if (cmd.equals("x²")) {
                double val = Math.pow(Double.parseDouble(currentInput), 2);
                currentInput = formatResult(val);
                startNewNumber = true;
                updateDisplay();
            } else if (cmd.equals("log")) {
                double val = Math.log10(Double.parseDouble(currentInput));
                currentInput = formatResult(val);
                startNewNumber = true;
                updateDisplay();
            } else if (cmd.equals("ln")) {
                double val = Math.log(Double.parseDouble(currentInput));
                currentInput = formatResult(val);
                startNewNumber = true;
                updateDisplay();
            } else if (cmd.matches("[÷×−+]|xʸ")) {
                if (!operator.isEmpty()) {
                    calculateResult();
                }
                currentValue = Double.parseDouble(currentInput);
                operator = cmd;
                expression = currentInput + " " + cmd;
                startNewNumber = true;
                updateDisplay();
            } else if (cmd.equals("=")) {
                calculateResult();
            }
        } catch (Exception ex) {
            display.setText("Error");
            currentInput = "0";
            operator = "";
            expression = "";
            startNewNumber = true;
        }
    }
    
    private void calculateResult() {
        if (operator.isEmpty()) return;
        
        try {
            double num2 = Double.parseDouble(currentInput);
            double result = 0;
            
            switch(operator) {
                case "+": result = currentValue + num2; break;
                case "−": result = currentValue - num2; break;
                case "×": result = currentValue * num2; break;
                case "÷": 
                    if (num2 != 0) result = currentValue / num2;
                    else throw new ArithmeticException("Division by zero");
                    break;
                case "xʸ": result = Math.pow(currentValue, num2); break;
            }
            
            currentInput = formatResult(result);
            operator = "";
            expression = "";
            startNewNumber = true;
            updateDisplay();
        } catch (Exception ex) {
            display.setText("Error");
            currentInput = "0";
            operator = "";
            expression = "";
        }
    }
    
    private String formatResult(double val) {
        // Round to avoid floating point errors
        val = Math.round(val * 100000000.0) / 100000000.0;
        
        // Remove .0 if result is whole number
        if (val == (long) val) {
            return String.valueOf((long) val);
        } else {
            return String.valueOf(val);
        }
    }
}

// Currency Converter Panel
class CurrencyConverter extends JPanel {
    private JTextField amountField;
    private JComboBox<String> fromCurrency, toCurrency;
    private JLabel resultLabel;
    private JButton convertBtn;
    
    public CurrencyConverter() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(102, 126, 234));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(42, 42, 62));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Title
        JLabel titleLabel = new JLabel("Currency Converter");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Amount input
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setForeground(Color.WHITE);
        amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(amountLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        amountField = new JTextField("100");
        amountField.setFont(new Font("Segoe UI", Font.BOLD, 20));
        amountField.setMaximumSize(new Dimension(300, 40));
        amountField.setHorizontalAlignment(JTextField.CENTER);
        mainPanel.add(amountField);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // From currency
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setForeground(Color.WHITE);
        fromLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        fromLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(fromLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        String[] currencies = {"USD", "EUR", "GBP", "JPY", "INR", "AUD", "CAD", "CNY", "CHF", "SEK"};
        fromCurrency = new JComboBox<>(currencies);
        fromCurrency.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        fromCurrency.setMaximumSize(new Dimension(300, 35));
        mainPanel.add(fromCurrency);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // To currency
        JLabel toLabel = new JLabel("To:");
        toLabel.setForeground(Color.WHITE);
        toLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        toLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(toLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        toCurrency = new JComboBox<>(currencies);
        toCurrency.setSelectedIndex(1);
        toCurrency.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        toCurrency.setMaximumSize(new Dimension(300, 35));
        mainPanel.add(toCurrency);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Convert button
        convertBtn = new JButton("Convert");
        convertBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        convertBtn.setBackground(new Color(72, 187, 120));
        convertBtn.setForeground(Color.WHITE);
        convertBtn.setFocusPainted(false);
        convertBtn.setBorderPainted(false);
        convertBtn.setMaximumSize(new Dimension(200, 45));
        convertBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        convertBtn.addActionListener(e -> convertCurrency());
        mainPanel.add(convertBtn);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Result
        resultLabel = new JLabel(" ");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        resultLabel.setForeground(new Color(72, 187, 120));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(resultLabel);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void convertCurrency() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String from = (String) fromCurrency.getSelectedItem();
            String to = (String) toCurrency.getSelectedItem();
            
            // Simple conversion rates (you would normally get these from an API)
            double rate = getExchangeRate(from, to);
            double result = amount * rate;
            
            resultLabel.setText(String.format("%.2f %s = %.2f %s", amount, from, result, to));
        } catch (NumberFormatException ex) {
            resultLabel.setText("Invalid amount");
        }
    }
    
    private double getExchangeRate(String from, String to) {
        // Simplified exchange rates (as of example data)
        // In a real app, you'd fetch this from an API like exchangerate-api.com
        if (from.equals(to)) return 1.0;
        
        // Base rates to USD
        double[][] rates = {
            // USD, EUR, GBP, JPY, INR, AUD, CAD, CNY, CHF, SEK
            {1.0, 0.92, 0.79, 149.5, 83.2, 1.52, 1.36, 7.24, 0.88, 10.87}
        };
        
        String[] curr = {"USD", "EUR", "GBP", "JPY", "INR", "AUD", "CAD", "CNY", "CHF", "SEK"};
        int fromIdx = -1, toIdx = -1;
        
        for (int i = 0; i < curr.length; i++) {
            if (curr[i].equals(from)) fromIdx = i;
            if (curr[i].equals(to)) toIdx = i;
        }
        
        if (fromIdx == -1 || toIdx == -1) return 1.0;
        
        // Convert from -> USD -> to
        double toUSD = 1.0 / rates[0][fromIdx];
        return toUSD * rates[0][toIdx];
    }
}
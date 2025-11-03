import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;

public class SimpleMain extends JFrame {
    private static final Color DARK_BG = new Color(18, 18, 18);
    private static final Color CARD_BG = new Color(28, 28, 30);
    private static final Color ACCENT = new Color(0, 122, 255);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY = new Color(174, 174, 178);
    private static final Color FIELD_BG = new Color(44, 44, 46);
    
    private JTextField fromField, toField, subjectField;
    private JPasswordField passwordField;
    private JTextArea bodyArea;

    public SimpleMain() {
        setTitle("Mail Sender");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(DARK_BG);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel headerPanel = createHeaderPanel();
        JPanel formCard = createFormCard();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formCard, BorderLayout.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane);
        setSize(600, 750);
        setLocationRelativeTo(null);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(DARK_BG);
        panel.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        JLabel title = new JLabel("Mail Sender");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        panel.add(title);
        
        return panel;
    }
    
    private JPanel createFormCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(58, 58, 60), 1),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        fromField = createStyledTextField("your.email@gmail.com");
        passwordField = createStyledPasswordField("App Password");
        toField = createStyledTextField("recipient@example.com");
        subjectField = createStyledTextField("Enter subject");
        bodyArea = createStyledTextArea();
        JButton sendButton = createStyledButton();
        
        card.add(createFieldGroup("From", fromField));
        card.add(Box.createVerticalStrut(20));
        card.add(createFieldGroup("Password", passwordField));
        card.add(Box.createVerticalStrut(20));
        card.add(createFieldGroup("To", toField));
        card.add(Box.createVerticalStrut(20));
        card.add(createFieldGroup("Subject", subjectField));
        card.add(Box.createVerticalStrut(20));
        card.add(createFieldGroup("Message", new JScrollPane(bodyArea)));
        card.add(Box.createVerticalStrut(30));
        
        // Center the send button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(CARD_BG);
        buttonPanel.add(sendButton);
        card.add(buttonPanel);
        
        return card;
    }
    
    private JPanel createFieldGroup(String label, JComponent field) {
        JPanel group = new JPanel(new BorderLayout(0, 8));
        group.setBackground(CARD_BG);
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelComp.setForeground(TEXT_SECONDARY);
        
        group.add(labelComp, BorderLayout.NORTH);
        group.add(field, BorderLayout.CENTER);
        
        return group;
    }
    
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(0, 0, getBackground(), 0, getHeight(), getBackground().brighter());
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT);
        field.setBorder(new EmptyBorder(12, 16, 12, 16));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setOpaque(false);
        
        // Add hover effect
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                field.setBackground(FIELD_BG.brighter());
                field.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                field.setBackground(FIELD_BG);
                field.repaint();
            }
        });
        
        field.setText(placeholder);
        field.setForeground(TEXT_SECONDARY);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_PRIMARY);
                }
                // Animate focus
                animateFieldFocus(field, true);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(TEXT_SECONDARY);
                }
                animateFieldFocus(field, false);
            }
        });
        return field;
    }
    
    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT);
        field.setBorder(new EmptyBorder(12, 16, 12, 16));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        field.setText(placeholder);
        field.setForeground(TEXT_SECONDARY);
        field.setEchoChar((char) 0);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_PRIMARY);
                    field.setEchoChar('*');
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (new String(field.getPassword()).isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(TEXT_SECONDARY);
                    field.setEchoChar((char) 0);
                }
            }
        });
        return field;
    }
    
    private JTextArea createStyledTextArea() {
        JTextArea area = new JTextArea(6, 0);
        area.setBackground(FIELD_BG);
        area.setForeground(TEXT_PRIMARY);
        area.setCaretColor(ACCENT);
        area.setBorder(new EmptyBorder(12, 16, 12, 16));
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        
        area.setText("Type your message here...");
        area.setForeground(TEXT_SECONDARY);
        area.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (area.getText().equals("Type your message here...")) {
                    area.setText("");
                    area.setForeground(TEXT_PRIMARY);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (area.getText().isEmpty()) {
                    area.setText("Type your message here...");
                    area.setForeground(TEXT_SECONDARY);
                }
            }
        });
        
        return area;
    }
    
    private JButton createStyledButton() {
        JButton button = new JButton("Send Message") {
            private boolean isPressed = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor;
                if (isPressed) {
                    bgColor = ACCENT.darker().darker();
                } else if (getModel().isRollover()) {
                    bgColor = ACCENT.brighter();
                } else {
                    bgColor = ACCENT;
                }
                
                // Gradient effect
                GradientPaint gradient = new GradientPaint(0, 0, bgColor, 0, getHeight(), bgColor.darker());
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Glow effect on hover
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 10, 10);
                }
                
                // Text
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.setColor(Color.WHITE);
                g2.drawString(getText(), x, y);
                
                g2.dispose();
            }
        };
        
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add click animation
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                animateButtonPress(button, true);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                animateButtonPress(button, false);
            }
        });
        
        button.addActionListener(e -> {
            animateButtonClick(button);
            Timer timer = new Timer(200, evt -> sendMail());
            timer.setRepeats(false);
            timer.start();
        });
        
        return button;
    }
    
    private void sendMail() {
        String from = getFieldText(fromField, "your.email@gmail.com");
        String password = getPasswordText(passwordField, "App Password");
        String to = getFieldText(toField, "recipient@example.com");
        String subject = getFieldText(subjectField, "Enter subject");
        String body = getTextAreaText(bodyArea, "Type your message here...");
        
        if (from.isEmpty() || password.isEmpty() || to.isEmpty() || subject.isEmpty() || body.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Animate success message
        Timer successTimer = new Timer(100, null);
        final int[] fadeStep = {0};
        
        successTimer.addActionListener(e -> {
            fadeStep[0]++;
            if (fadeStep[0] >= 3) {
                successTimer.stop();
                JOptionPane.showMessageDialog(this, "✓ Message ready to send!\n\nFrom: " + from + "\nTo: " + to + "\nSubject: " + subject, "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        successTimer.start();
    }
    
    private String getFieldText(JTextField field, String placeholder) {
        String text = field.getText();
        return text.equals(placeholder) ? "" : text;
    }
    
    private String getTextAreaText(JTextArea area, String placeholder) {
        String text = area.getText();
        return text.equals(placeholder) ? "" : text;
    }
    
    private String getPasswordText(JPasswordField field, String placeholder) {
        String text = new String(field.getPassword());
        return text.equals(placeholder) ? "" : text;
    }
    
    private void animateFieldFocus(JTextField field, boolean focused) {
        Timer timer = new Timer(10, null);
        final int[] step = {0};
        final int maxSteps = 10;
        
        timer.addActionListener(e -> {
            step[0]++;
            float progress = (float) step[0] / maxSteps;
            
            if (focused) {
                int newWidth = (int) (field.getPreferredSize().width * (1 + 0.02f * progress));
                field.setPreferredSize(new Dimension(newWidth, field.getPreferredSize().height));
            } else {
                int newWidth = (int) (field.getPreferredSize().width * (1 - 0.02f * progress));
                field.setPreferredSize(new Dimension(newWidth, field.getPreferredSize().height));
            }
            
            field.revalidate();
            field.repaint();
            
            if (step[0] >= maxSteps) {
                timer.stop();
            }
        });
        
        timer.start();
    }
    
    private void animateButtonPress(JButton button, boolean pressed) {
        Timer timer = new Timer(5, null);
        final int[] step = {0};
        final int maxSteps = 5;
        
        timer.addActionListener(e -> {
            step[0]++;
            float progress = (float) step[0] / maxSteps;
            
            if (pressed) {
                int newSize = (int) (50 * (1 - 0.05f * progress));
                button.setPreferredSize(new Dimension(200, newSize));
            } else {
                int newSize = (int) (47 + 3 * progress);
                button.setPreferredSize(new Dimension(200, newSize));
            }
            
            button.revalidate();
            button.repaint();
            
            if (step[0] >= maxSteps) {
                timer.stop();
            }
        });
        
        timer.start();
    }
    
    private void animateButtonClick(JButton button) {
        Timer pulseTimer = new Timer(50, null);
        final int[] pulseStep = {0};
        
        pulseTimer.addActionListener(e -> {
            pulseStep[0]++;
            button.repaint();
            
            if (pulseStep[0] >= 4) {
                pulseTimer.stop();
            }
        });
        
        pulseTimer.start();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SimpleMain().setVisible(true);
        });
    }
}
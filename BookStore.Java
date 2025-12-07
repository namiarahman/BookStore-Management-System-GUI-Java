import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class BookStore extends JFrame implements MouseListener {
    private JPanel panel;
    private JLabel[] imageLabels, nameLabels, priceLabels;
    private JComboBox[] quantityBoxes;
    private JButton[] addToCartButtons;
    private JButton checkoutButton;
    private JComboBox<String> paymentMethodBox;
    private JLabel totalLabel;
    private JTextArea orderHistoryArea;
    private ImageIcon[] icons;

    private String[] bookNames = {"Java Basics", "Effective Java", "C Programming",
                                  "Python for Beginners", "Data Structures", "Algorithms"};
    private int[] prices = {570, 600, 1450, 550, 700, 900};

    private int[] cartQuantities = new int[6];

    public BookStore() {
        super("Simple Book Store");
        setBounds(50, 50, 1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(173, 216, 230));
        add(panel);

        imageLabels = new JLabel[6];
        nameLabels = new JLabel[6];
        priceLabels = new JLabel[6];
        quantityBoxes = new JComboBox[6];
        addToCartButtons = new JButton[6];
        icons = new ImageIcon[6];

        for (int i = 0; i < 6; i++) {
            int x = 65 + (190 * i);

            icons[i] = new ImageIcon("book" + (i+1) + ".jpg"); 
            JPanel imagePanel = new JPanel();
            imagePanel.setBounds(x, 50, 150, 180);
            imagePanel.setBackground(Color.WHITE);
            imageLabels[i] = new JLabel(icons[i]);
            imagePanel.add(imageLabels[i]);
            panel.add(imagePanel);


            nameLabels[i] = new JLabel(bookNames[i]);
            nameLabels[i].setBounds(x, 240, 150, 25);
            nameLabels[i].setForeground(Color.BLACK);
            nameLabels[i].setHorizontalAlignment(JLabel.CENTER);
            panel.add(nameLabels[i]);


            priceLabels[i] = new JLabel("Price: " + prices[i]);
            priceLabels[i].setBounds(x, 270, 150, 25);
            priceLabels[i].setForeground(Color.RED);
            priceLabels[i].setHorizontalAlignment(JLabel.CENTER);
            panel.add(priceLabels[i]);

            quantityBoxes[i] = new JComboBox();
            for (int j = 0; j <= 7; j++) {
                quantityBoxes[i].addItem(j);
            }
            quantityBoxes[i].setBounds(x, 300, 150, 25);
            panel.add(quantityBoxes[i]);

            addToCartButtons[i] = new JButton("Add to Cart");
            addToCartButtons[i].setBounds(x, 330, 150, 25);
            addToCartButtons[i].addMouseListener(this);
            panel.add(addToCartButtons[i]);
        }

        totalLabel = new JLabel("Total Price : 0");
        totalLabel.setBounds(65, 380, 200, 30);
        totalLabel.setOpaque(true);
        totalLabel.setBackground(Color.WHITE);
        panel.add(totalLabel);

        paymentMethodBox = new JComboBox<>(new String[]{"Bkash", "Nagad", "Visa Card", "MasterCard", "Amex Card", "Rocket"});
        paymentMethodBox.setBounds(300, 380, 150, 30);
        panel.add(paymentMethodBox);

        checkoutButton = new JButton("Checkout");
        checkoutButton.setBounds(470, 380, 120, 30);
        checkoutButton.addMouseListener(this);
        panel.add(checkoutButton);

        orderHistoryArea = new JTextArea();
        orderHistoryArea.setEditable(false);
        orderHistoryArea.setBounds(65, 430, 1000, 200);
        panel.add(orderHistoryArea);
    }

    public void mouseClicked(MouseEvent me) {
        for (int i = 0; i < 6; i++) {
            if (me.getSource() == addToCartButtons[i]) {
                int quantity = (int) quantityBoxes[i].getSelectedItem();
                if (quantity > 0) {
                    cartQuantities[i] += quantity; 
                    JOptionPane.showMessageDialog(this, bookNames[i] + " added to cart!");
                    updateTotalPrice(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Select at least 1 quantity!");
                }
            }
        }

        if (me.getSource() == checkoutButton) {
            try {
                File file = new File("orders.txt");
                if (!file.exists()) {
                    file.createNewFile();
                }

                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);

                int totalPrice = 0;
                String paymentMethod = (String) paymentMethodBox.getSelectedItem();

                orderHistoryArea.setText("");
                orderHistoryArea.append("===== Checkout Summary =====\n");
                pw.println("===== Checkout Summary =====");

                for (int i = 0; i < 6; i++) {
                    if (cartQuantities[i] > 0) {
                        int subtotal = cartQuantities[i] * prices[i];
                        totalPrice += subtotal;

                        String line = bookNames[i] + " | Price: " + prices[i] + " | Qty: " + cartQuantities[i] + " | Subtotal: " + subtotal;
                        orderHistoryArea.append(line + "\n");
                        pw.println(line);
                    }
                }

                orderHistoryArea.append("Payment Method: " + paymentMethod + "\n");
                orderHistoryArea.append("Net Total: " + totalPrice + "\n");
                orderHistoryArea.append("--------------------------------\n\n");

                pw.println("Payment Method: " + paymentMethod);
                pw.println("Net Total: " + totalPrice);
                pw.println("-------------------------------------");
                pw.close();

                totalLabel.setText("Total Price : " + totalPrice);
                cartQuantities = new int[6]; // reset cart
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateTotalPrice() {
        int totalPrice = 0;
        for (int i = 0; i < 6; i++) {
            totalPrice += cartQuantities[i] * prices[i];
        }
        totalLabel.setText("Total Price : " + totalPrice);
    }

    public void mousePressed(MouseEvent me) {}
    public void mouseReleased(MouseEvent me) {}
    public void mouseEntered(MouseEvent me) {}
    public void mouseExited(MouseEvent me) {}

    public static void main(String[] args) {
        new BookStore().setVisible(true);
    }
}


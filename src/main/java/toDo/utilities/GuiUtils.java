package toDo.utilities;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GuiUtils {

    public static JPanel createBorderedPanel(int width, int height) {

        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.BLACK));
        panel.setPreferredSize(new Dimension(width, height));
        return panel;
    }

    public static JPanel createRedBorderedPanel(int width, int height) {

        JPanel panel = createBorderedPanel(width, height);
        panel.setBorder(new LineBorder(Color.RED));
        return panel;
    }

    public static JButton createButtonWithListener(int width, int height, String text, ActionListener listener) {

        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.addActionListener(listener);
        return button;
    }

    public static JPanel createLabelPanel(int width, int height, int fontSize, String text) {

        JPanel panel = createRedBorderedPanel(width, height);
        JLabel label = new JLabel(text);
        panel.setLayout(new GridBagLayout());
        label.setFont(new Font("Comic Sans MS", Font.PLAIN, fontSize));
        panel.add(label);

        return panel;
    }

    public static JComboBox createComboBox(int width, int height, String... options) {

        JComboBox comboBox = new JComboBox(options);
        comboBox.setPreferredSize(new Dimension(width,height));
        return comboBox;
    }

    public static JTextArea createJTextArea(int width, int height) {
        JTextArea ta = new JTextArea();
        ta.setPreferredSize(new Dimension(width,height));
        ta.setBorder(new LineBorder(Color.BLACK));
        return ta;
    }

    public static JTextField createJTextField(int width, int height) {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(width,height));
        return tf;
    }

    public static JTextField createJTextFieldWithMaxChars(int width, int height, int maxChars) {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(width,height));
        tf.addKeyListener(new MaxCharKeyListener(tf, maxChars));
        return tf;
    }

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    static class MaxCharKeyListener implements KeyListener
    {

        JTextField tf;
        int maxChars;

        MaxCharKeyListener(JTextField newTF, int maxChars)
        {
            tf = newTF;
            this.maxChars = maxChars;
        }

        public void keyPressed(KeyEvent arg0) {}

        public void keyReleased(KeyEvent arg0) {}

        public void keyTyped(KeyEvent arg0)
        {
            if (tf.getText().length() >= maxChars)
            {
                tf.setText(tf.getText().substring(0,maxChars));
            }
        }

    }
}

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Calculatrice extends JFrame {

    public static final String TITLE = "Calculatrice";

    public static final String ERROR_OCCUR = "Erreur ! Vérifiez le calcul.";


    private final JLabel screenResult = new JLabel("0");

    private boolean update = false;

    private boolean lockPoint = false;

    private boolean lockOperator = false;

    public Calculatrice() {
        this.setTitle(TITLE);
        this.setSize(375, 375);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setContentPane(constructInterface());
        this.setVisible(true);
    }

    public Container constructInterface() {
        JPanel screen = new JPanel();

        // Screen Result
        screenResult.setHorizontalAlignment(SwingConstants.RIGHT);
        screenResult.setPreferredSize(new Dimension(310, 50));
        screenResult.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        screen.add(screenResult);

        // Button number
        for (int i = 1; i < 11; i++) {
            JButton numberButton = new JButton(String.valueOf(i));
            numberButton.setPreferredSize(new Dimension(100, 50));
            numberButton.addActionListener(new NumberListener());
            if (i == 10) {
                numberButton.setText("0");
            }
            screen.add(numberButton);
        }

        // Spécial Button
        List<String> special = new ArrayList<>();
        special.add(".");
        special.add("=");

        for (String value : special) {
            JButton customButton = new JButton(value);
            customButton.setPreferredSize(new Dimension(100, 50));
            if (Objects.equals(value, ".")) {
                customButton.addActionListener(new PointListener());
            } else {
                customButton.addActionListener(new EqualListener());
            }
            screen.add(customButton);
        }

        // Operator Button
        List<String> operator = new ArrayList<>();
        operator.add("+");
        operator.add("-");
        operator.add("*");
        operator.add("/");

        for (String value : operator) {
            JButton operatorButton = new JButton(value);
            operatorButton.setPreferredSize(new Dimension(55, 50));
            operatorButton.addActionListener(new OperatorListener(value));
            screen.add(operatorButton);
        }

        //Clear Button
        JButton clearButton = new JButton("C");
        clearButton.setBackground(Color.RED);
        clearButton.setPreferredSize(new Dimension(55, 50));
        clearButton.addActionListener(new ClearListener());
        screen.add(clearButton);

        return screen;
    }

    public static void main(String[] args) {
        new Calculatrice();
    }

    public class NumberListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String number  = ((JButton) e.getSource()).getText();

            // Lors du premier appel, on écrase la valeur 0 pour la remplacer
            if (!update) {
                screenResult.setText(number);
                update = true;
                return;
            }
            screenResult.setText(screenResult.getText() + number);
        }
    }

    private class OperatorListener implements ActionListener {
        private final String action;

        OperatorListener(String action) {
            this.action = action;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if (!lockOperator) {
                screenResult.setText(screenResult.getText() + " " + action + " ");
            }
            update = true;
            lockOperator = true;
            lockPoint = false;
        }
    }

    private class ClearListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // On repart avec les valeurs initiales
            screenResult.setText("0");
            update = false;
            lockPoint = false;
            lockOperator = false;
        }
    }

    private class PointListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String number  = ((JButton) e.getSource()).getText();
            if (!lockPoint) {
                screenResult.setText(screenResult.getText() + number);
            }

            // Dans le cas où l'on veut rentrer une valeur 0.X
            // On indique que maintenant, on met à jour la valeur sur le screenResult
            update = true;

            // On empêche les doubles virgules pour éviter des erreurs lors du calcul
            lockPoint = true;
        }
    }

    private class EqualListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                float result = calculResult(screenResult.getText());
                screenResult.setText(String.valueOf(result));
                update = false;
                lockOperator = false;
                lockPoint = false;

            } catch (Exception exception) {
                update = false;
                lockPoint = false;
                screenResult.setText(ERROR_OCCUR);
            }
        }

        private float calculResult(String calcul) {
            List<String> values = new ArrayList<>(List.of(calcul.split(" ")));

            if (values.size() != 3) {
                throw new RuntimeException("Format wrong, the calcul has something weird !");
            }

            float firstValue = Float.parseFloat(String.valueOf(values.get(0)));

            String operatorValue =values.get(1);

            float secondValue = Float.parseFloat(String.valueOf(values.get(2)));

            if (Objects.equals(operatorValue, "+")) {
                return firstValue + secondValue;
            }
            if (Objects.equals(operatorValue, "-")) {
                return firstValue - secondValue;
            }
            if (Objects.equals(operatorValue, "*")) {
                return firstValue * secondValue;
            }
            return firstValue / secondValue;
        }
    }
}
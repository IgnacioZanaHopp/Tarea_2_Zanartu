package frontend;

import backend.Item;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;

public class TestPanel extends JPanel {
    private JLabel lblHeader      = new JLabel();
    private JTextArea txtQuestion = new JTextArea();
    private JPanel optionsPanel   = new JPanel();
    private JButton btnPrev       = new JButton("Volver atrás");
    private JButton btnNext       = new JButton("Siguiente");
    private Runnable onPrev;
    private Consumer<String> onNext;

    public TestPanel(Runnable back, Consumer<String> next) {
        this.onPrev = back;
        this.onNext = next;

        setLayout(new BorderLayout(10,10));

        // Encabezado con número y nivel
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblHeader, BorderLayout.NORTH);

        //  Panel central con pregunta y opciones
        JPanel center = new JPanel(new BorderLayout(5,5));

        txtQuestion.setLineWrap(true);
        txtQuestion.setWrapStyleWord(true);
        txtQuestion.setEditable(false);
        center.add(new JScrollPane(txtQuestion), BorderLayout.CENTER);

        optionsPanel.setLayout(new GridLayout(0, 1, 5, 5));
        center.add(optionsPanel, BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);

        // Panel de navegacion con botones
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        nav.add(btnPrev);
        nav.add(btnNext);
        add(nav, BorderLayout.SOUTH);

        btnPrev.addActionListener(e -> onPrev.run());
        btnNext.addActionListener(e -> {
            String resp = getSelected();
            if (resp != null) {
                onNext.accept(resp);
            }
        });
    }

    /**
     * Muestra el ítem actual:
     *  - Pregunta (txtQuestion)
     *  - Opciones (JRadioButton)
     *  - Desactiva "Volver atrás" si es la 1ª pregunta
     *  - Cambia texto de btnNext a "Enviar respuestas" en la última
     */
    public void showItem(Item item, int idx, int total) {
        lblHeader.setText(String.format("Pregunta %d de %d [%s]", idx, total, item.getBloomLevel()));
        txtQuestion.setText(item.getQuestion());

        optionsPanel.removeAll();
        ButtonGroup bg = new ButtonGroup();
        List<String> opts = item.getOptions();
        for (String opt : opts) {
            JRadioButton rb = new JRadioButton(opt);
            bg.add(rb);
            optionsPanel.add(rb);
            if (opt.equals(item.getResponse())) {
                rb.setSelected(true);
            }
        }

        btnPrev.setEnabled(idx > 1);
        btnNext.setText(idx == total ? "Enviar respuestas" : "Siguiente");

        revalidate();
        repaint();
    }

    /** Devuelve la opción seleccionada (texto) o null si no hay ninguna */
    private String getSelected() {
        for (Component c : optionsPanel.getComponents()) {
            if (c instanceof JRadioButton && ((JRadioButton)c).isSelected()) {
                return ((JRadioButton)c).getText();
            }
        }
        return null;
    }
}

package frontend;

import backend.Item;
import backend.TestManager;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReviewPanel extends JPanel {
    private TestManager manager;
    private JLabel lblHeader = new JLabel();
    private JTextArea txtQuestion = new JTextArea();
    private JLabel lblResult = new JLabel();
    private JPanel optionsPanel = new JPanel();
    private JButton btnPrev = new JButton("Volver atrás");
    private JButton btnNext = new JButton("Siguiente");
    private JButton btnSummary = new JButton("Volver a Resumen");

    private int idx;

    public ReviewPanel(TestManager m) {
        this.manager = m;
        setLayout(new BorderLayout(5,5));
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblHeader, BorderLayout.NORTH);
        txtQuestion.setLineWrap(true);
        txtQuestion.setWrapStyleWord(true);
        txtQuestion.setEditable(false);
        add(new JScrollPane(txtQuestion), BorderLayout.CENTER);
        add(lblResult, BorderLayout.EAST);
        add(optionsPanel, BorderLayout.SOUTH);
        JPanel nav = new JPanel();
        nav.add(btnPrev);
        nav.add(btnNext);
        nav.add(btnSummary);
        add(nav, BorderLayout.SOUTH);
        btnPrev.addActionListener(e -> show(idx-1));
        btnNext.addActionListener(e -> show(idx+1));
        btnSummary.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
        });
    }

    public void startReview() {
        show(0);
    }

    private void show(int i) {
        int total = manager.getItemCount();
        idx = Math.max(0, Math.min(i, total-1));
        Item it = manager.getItem(idx);
        lblHeader.setText(String.format("Pregunta %d de %d [%s]", idx+1, total, it.getBloomLevel()));
        txtQuestion.setText(it.getQuestion());
        optionsPanel.removeAll();
        optionsPanel.setLayout(new GridLayout(it.getOptions().size(),1));
        for (String opt : it.getOptions()) {
            JLabel lbl = new JLabel(opt + (opt.equals(it.getCorrectAnswer()) ? " (Correcta)" : ""));
            if (opt.equals(it.getResponse())) lbl.setText(lbl.getText() + " <- Tu respuesta");
            optionsPanel.add(lbl);
        }
        lblResult.setText(it.isCorrect() ? "Correcto" : "Incorrecto");
        btnPrev.setEnabled(idx > 0);
        btnNext.setEnabled(idx < total-1);
        btnSummary.setVisible(idx == total-1);
        revalidate();
        repaint();
    }
}

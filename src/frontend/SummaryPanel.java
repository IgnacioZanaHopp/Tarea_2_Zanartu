package frontend;

import java.awt.*;
import java.util.Map;
import javax.swing.*;

public class SummaryPanel extends JPanel {
    private JTextArea txtSummary = new JTextArea();
    private JButton btnReview = new JButton("Revisar Respuestas");

    public SummaryPanel(Runnable onReview) {
        setLayout(new BorderLayout(5,5));
        txtSummary.setEditable(false);
        add(new JScrollPane(txtSummary), BorderLayout.CENTER);
        JPanel south = new JPanel();
        south.add(btnReview);
        add(south, BorderLayout.SOUTH);
        btnReview.addActionListener(e -> onReview.run());
    }

    public void updateSummary(Map<String, Double> byBloom, Map<String, Double> byType) {
        StringBuilder sb = new StringBuilder();
        sb.append("% Correctas por nivel Bloom:\n");
        byBloom.forEach((k,v) -> sb.append(String.format("%s: %.1f%%\n", k, v)));
        sb.append("\n% Correctas por tipo de ítem:\n");
        byType.forEach((k,v) -> sb.append(String.format("%s: %.1f%%\n", k, v)));
        txtSummary.setText(sb.toString());
    }
}

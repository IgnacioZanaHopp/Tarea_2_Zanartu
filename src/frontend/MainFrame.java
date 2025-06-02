package frontend;

import backend.TestManager;
import backend.Item;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Map;

public class MainFrame extends JFrame implements TestManager.TestListener {
    private TestManager manager = new TestManager();
    private CardLayout cards = new CardLayout();
    private JPanel mainPanel = new JPanel(cards);
    private LoadPanel loadPanel;
    private TestPanel testPanel;
    private SummaryPanel summaryPanel;
    private ReviewPanel reviewPanel;

    public MainFrame() {
        super("Tarea 2 - Pruebas Bloom");
        manager.addListener(this);

        // Inicializa paneles con callbacks:
        loadPanel    = new LoadPanel(this::onFileChosen, manager::start);
        testPanel    = new TestPanel(manager::previous, manager::next);
        summaryPanel = new SummaryPanel(this::startReview);
        reviewPanel  = new ReviewPanel(manager);

        mainPanel.add(loadPanel,    "LOAD");
        mainPanel.add(testPanel,    "TEST");
        mainPanel.add(summaryPanel, "SUMMARY");
        mainPanel.add(reviewPanel,  "REVIEW");
        add(mainPanel);

        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void onFileChosen(File f) {
        try {
            manager.loadFromFile(f);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando archivo: " + e.getMessage());
        }
    }

    private void startReview() {
        reviewPanel.startReview();
        cards.show(mainPanel, "REVIEW");
    }

    @Override
    public void onLoaded(int itemCount, int totalTime) {
        loadPanel.updateInfo(itemCount, totalTime);
        cards.show(mainPanel, "LOAD");
    }

    @Override
    public void onItemChanged(Item item, int index, int total) {
        testPanel.showItem(item, index, total);
        cards.show(mainPanel, "TEST");
    }

    @Override
    public void onTestFinished() {
        // No se utiliza en este fluj o
    }

    @Override
    public void onSummaryReady(Map<String, Double> byBloom, Map<String, Double> byType) {
        summaryPanel.updateSummary(byBloom, byType);
        cards.show(mainPanel, "SUMMARY");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
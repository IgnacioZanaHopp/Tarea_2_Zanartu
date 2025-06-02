package frontend;

import java.awt.*;
import java.io.File;
import java.util.function.Consumer;
import javax.swing.*;

public class LoadPanel extends JPanel {
    private JLabel infoLabel  = new JLabel("Cargar ítems para la prueba");
    private JButton btnLoad   = new JButton("Cargar Archivo");
    private JButton btnStart  = new JButton("Iniciar Prueba");
    private File selectedFile;
    private Consumer<File> onFileChosen;
    private Runnable onStart;

    /**
     * @param onFileChosen callback para cuando se elige un archivo
     *                     (se habilita el botón "Iniciar Prueba")
     * @param onStart    callback para cuando se inicia la prueba   
     */
    public LoadPanel(Consumer<File> onFileChosen, Runnable onStart) {
        this.onFileChosen = onFileChosen;
        this.onStart      = onStart;

        setLayout(new BorderLayout(10,10));
        add(infoLabel, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.add(btnLoad);
        south.add(btnStart);
        add(south, BorderLayout.SOUTH);

        btnStart.setEnabled(false);

        btnLoad.addActionListener(e -> chooseFile());
        btnStart.addActionListener(e -> {
            if (selectedFile != null) {
                onStart.run();   // Arranca la prueba, no vuelve a cargar
            }
        });
    } // fin constructor

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            onFileChosen.accept(selectedFile);
        }
    }

    /**
     * Actualiza la etiqueta con la cantidad de ítems y habilita "Iniciar Prueba"
     */
    public void updateInfo(int count, int time) {
        infoLabel.setText(String.format("Ítems: %d   Tiempo total: %d seg", count, time));
        btnStart.setEnabled(true);
    }
}

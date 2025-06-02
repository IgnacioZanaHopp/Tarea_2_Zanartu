package backend;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TestManager {
    private List<Item> items = new ArrayList<>();
    private int currentIndex = 0;
    private List<TestListener> listeners = new ArrayList<>();

    public interface TestListener {
        void onLoaded(int itemCount, int totalTime);
        void onItemChanged(Item item, int index, int total);
        void onTestFinished();
        void onSummaryReady(Map<String, Double> byBloom, Map<String, Double> byType);
    }

    public void addListener(TestListener l) {
        listeners.add(l);
    }
    public void loadFromFile(File file) throws IOException {
        items.clear();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)))
        {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }
                if (line.trim().isEmpty()) continue;

                // Divide en 6 partes como: pregunta, tipo, nivel, tiempo, opciones, correcta
                String[] parts = line.split(",", 6);
                if (parts.length < 6) continue;

                String q = parts[0].trim();
                Item.Type t;
                try {
                    t = Item.Type.valueOf(parts[1].trim());
                } catch (IllegalArgumentException ex) {
                    //  Tipo no válido, omite esta línea
                    continue;
                }
                String level = parts[2].trim();
                int tm = Integer.parseInt(parts[3].trim());
                String optsRaw = parts[4].trim();
                List<String> opts = Arrays.asList(optsRaw.split("\\|"));
                String corr = parts[5].trim();

                items.add(new Item(q, t, level, tm, opts, corr));
            }
        }

        // Calcula tiempo total
        int totalTime = items.stream().mapToInt(Item::getTime).sum();
        // Notifica a listeners
        for (TestListener l : listeners) {
            l.onLoaded(items.size(), totalTime);
        }
    }

    public void start() {
        if (items.isEmpty()) return;
        currentIndex = 0;
        notifyItemChanged();
    }

    public void next(String response) {
        items.get(currentIndex).setResponse(response);
        if (currentIndex == items.size() - 1) {
            for (TestListener l : listeners) {
                l.onTestFinished();
            }
            computeSummary();
        } else {
            currentIndex++;
            notifyItemChanged();
        }
    }

    public void previous() {
        if (currentIndex > 0) {
            currentIndex--;
            notifyItemChanged();
        }
    }

    private void notifyItemChanged() {
        Item current = items.get(currentIndex);
        for (TestListener l : listeners) {
            l.onItemChanged(current, currentIndex + 1, items.size());
        }
    }

    private void computeSummary() {
        Map<String, List<Item>> byBloom = new HashMap<>();
        Map<String, List<Item>> byType = new HashMap<>();

        for (Item it : items) {
            byBloom.computeIfAbsent(it.getBloomLevel(), k -> new ArrayList<>()).add(it);
            byType.computeIfAbsent(it.getType().name(), k -> new ArrayList<>()).add(it);
        }

        Map<String, Double> pctBloom = new HashMap<>();
        Map<String, Double> pctType = new HashMap<>();

        for (String lvl : byBloom.keySet()) {
            List<Item> list = byBloom.get(lvl);
            long correct = list.stream().filter(Item::isCorrect).count();
            pctBloom.put(lvl, correct * 100.0 / list.size());
        }
        for (String ty : byType.keySet()) {
            List<Item> list = byType.get(ty);
            long correct = list.stream().filter(Item::isCorrect).count();
            pctType.put(ty, correct * 100.0 / list.size());
        }

        for (TestListener l : listeners) {
            l.onSummaryReady(pctBloom, pctType);
        }
    }

    /** Devuelve el item en la posicion dada (0-based) */
    public Item getItem(int index) {
        return items.get(index);
    }

    /** Devulve cuantos ítems se cargaron */
    public int getItemCount() {
        return items.size();
    }
}

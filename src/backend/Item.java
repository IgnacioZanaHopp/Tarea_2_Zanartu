package backend;

import java.util.List;

public class Item {
    public enum Type { MULTIPLE_CHOICE, TRUE_FALSE }

    private String question;
    private Type type;
    private String bloomLevel; // Recordar, Entender, Aplicar, Analizar, Evaluar, Crear
    private int time; // tiempo en segundos para responder
    private List<String> options;
    private String correctAnswer;
    private String response; // respuesta del usuario

    public Item(String question, Type type, String bloomLevel, int time,
                List<String> options, String correctAnswer) {
        this.question = question;
        this.type = type;
        this.bloomLevel = bloomLevel;
        this.time = time;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    // Getters and setters
    public String getQuestion() { return question; }
    public Type getType() { return type; }
    public String getBloomLevel() { return bloomLevel; }
    public int getTime() { return time; }
    public List<String> getOptions() { return options; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
    public boolean isCorrect() {
        return response != null && response.equals(correctAnswer);
    }
}
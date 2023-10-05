import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class Preprocessing {
    public static void main(String[] args) {
        String cremaDirectory = "AudioWAV";
        File[] cremaFiles = new File(cremaDirectory).listFiles();
        List<String> fileEmotion = new ArrayList<>();
        List<String> filePath = new ArrayList<>();
        for (File file : cremaFiles) {
            filePath.add(cremaDirectory + file.getName());
            String[] parts = file.getName().split("_");
            String emotion = "Unknown"; // Default emotion if not recognized
            if (parts.length >= 3) {
                String emotionCode = parts[2];
                switch (emotionCode) {
                    case "SAD":
                        emotion = "sad";
                        break;
                    case "ANG":
                        emotion = "angry";
                        break;
                    case "DIS":
                        emotion = "disgust";
                        break;
                    case "FEA":
                        emotion = "fear";
                        break;
                    case "HAP":
                        emotion = "happy";
                        break;
                    case "NEU":
                        emotion = "neutral";
                        break;
                }
            }
            fileEmotion.add(emotion);
        }
        // Create a DataFrame for emotions
        StringBuilder emotionCsv = new StringBuilder();
        for (String emotion : fileEmotion) {
            emotionCsv.append(emotion).append("\n");
        }
        writeToFile("emotion.csv", emotionCsv.toString());
        StringBuilder pathCsv = new StringBuilder();
        for (String path : filePath) {
            pathCsv.append(path).append("\n");
        }
        writeToFile("path.csv", pathCsv.toString());
        StringBuilder combinedCsv = new StringBuilder();
        for (int i = 0; i < fileEmotion.size(); i++) {
            combinedCsv.append(fileEmotion.get(i)).append(",").append(filePath.get(i)).append("\n");
        }
        writeToFile("combined.csv", combinedCsv.toString());
        System.out.println("Preprocessing completed.");
    }
    private static void writeToFile(String fileName, String content) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

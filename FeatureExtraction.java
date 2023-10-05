import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.MathArrays;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class FeatureExtraction {
    public static void main(String[] args) {
        String inputDirectory = "AudioFilesDirectory";
        String outputFilePath = "features.csv";
        File inputDir = new File(inputDirectory);
        File[] audioFiles = inputDir.listFiles((dir, name) -> name.endsWith(".wav"));
        if (audioFiles != null) {
            List<double[]> featuresList = new ArrayList<>();
            List<String> labelsList = new ArrayList<>();
            for (File audioFile : audioFiles) {
                String filePath = audioFile.getAbsolutePath();
                String emotion = extractEmotionFromPath(filePath);
                double[] features = extractFeatures(filePath);
                featuresList.add(features);
                labelsList.add(emotion);
                double[] augmentedFeatures = applyDataAugmentation(features, sampleRate);
                featuresList.add(augmentedFeatures);
                labelsList.add(emotion);
                double[] noiseData = applyNoise(features);
                double[] noiseFeatures = extractFeaturesFromData(noiseData);
                featuresList.add(noiseFeatures);
                labelsList.add(emotion);
                double[] stretchedData = applyStretch(features, stretchRate);
                double[] stretchedFeatures = extractFeaturesFromData(stretchedData);
                featuresList.add(stretchedFeatures);
                labelsList.add(emotion);
                double[] pitchedData = applyPitch(features, pitchFactor);
                double[] pitchedFeatures = extractFeaturesFromData(pitchedData);
                featuresList.add(pitchedFeatures);
                labelsList.add(emotion);
            }
            double[][] featuresArray = featuresList.toArray(new double[0][]);
            String[] labelsArray = labelsList.toArray(new String[0]);
            saveFeaturesToCsv(featuresArray, labelsArray, outputFilePath);
        }
    }
    private static double[] extractFeatures(String filePath) {
        double[] features = null;
        features = performFeatureExtraction(filePath);
        return features;
    }
    private static double[] applyDataAugmentation(double[] features, double sampleRate) {
        double[] augmentedFeatures = null;
        augmentedFeatures = applyNoise(features);
        return augmentedFeatures;
    }
    private static double[] applyNoise(double[] features) {
        double[] noisyFeatures = null;
        noisyFeatures = addNoise(features);
        return noisyFeatures;
    }
    private static double[] applyStretch(double[] features, double stretchRate) {
        double[] stretchedFeatures = null;
        stretchedFeatures = stretch(features, stretchRate);
        return stretchedFeatures;
    }
    private static double[] applyPitch(double[] features, double pitchFactor) {
        double[] pitchedFeatures = null;
        pitchedFeatures = pitchShift(features, pitchFactor);
        return pitchedFeatures;
    }
    private static void saveFeaturesToCsv(double[][] features, String[] labels, String filePath) {
       saveToCsv(features, labels, filePath);
    }
    private static String extractEmotionFromPath(String path) {
        String emotion = null;
        emotion = extractEmotion(path);
        return emotion;
    }
}

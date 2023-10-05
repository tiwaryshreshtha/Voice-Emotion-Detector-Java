import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFloatConverter;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;
import be.tarsos.dsp.io.TarsosDSPAudioInputStreamWrapper;
import be.tarsos.dsp.mfcc.MFCC;
import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.analysis.function.Exp;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.MathArrays;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;
import be.tarsos.dsp.util.fft.FFT;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
public class FeatureNormalization {
    public static void main(String[] args) {
        String filePath = "audio.wav";
        double[] features = extractFeatures(filePath);
        }
    private static double[] extractFeatures(String filePath) {
        List<Double> featureList = new ArrayList<>();
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            TarsosDSPAudioFormat audioFormat = new TarsosDSPAudioFormat(audioInputStream.getFormat());
            TarsosDSPAudioInputStream audioStream = new TarsosDSPAudioInputStreamWrapper(audioInputStream, audioFormat);
            int bufferSize = 1024; 
            int overlap = 512;     
            MFCC mfcc = new MFCC(bufferSize, audioFormat.getSampleRate(), 13, 40, 300, 8000);
            audioStream.setOverlap(overlap);
            audioStream.skip(1024); // Skip initial samples if needed
            TarsosDSPAudioFloatConverter converter = TarsosDSPAudioFloatConverter.getConverter(audioFormat);
            byte[] buffer = new byte[bufferSize];
            float[] audioBuffer = new float[bufferSize / 2];
            while (audioStream.read(buffer) != -1) {
                converter.toFloatArray(buffer, audioBuffer);
                mfcc.process(audioBuffer);
            }
            double[] mfccValues = mfcc.getMFCC();
            for (double mfccValue : mfccValues) {
                featureList.add(mfccValue);
            }
            audioStream.close();
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        double[] features = new double[featureList.size()];
        for (int i = 0; i < featureList.size(); i++) {
            features[i] = featureList.get(i);
        }
        // Normalization using StandardScaler
        features = normalizeFeatures(features);
        return features;
    }
    private static double[] normalizeFeatures(double[] features) {
        double mean = calculateMean(features);
        double stdDev = calculateStandardDeviation(features, mean);
        for (int i = 0; i < features.length; i++) {
            features[i] = (features[i] - mean) / stdDev;
        }
        return features;
    }
    private static double calculateMean(double[] values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }
    private static double calculateStandardDeviation(double[] values, double mean) {
        double sum = 0;
        for (double value : values) {
            sum += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sum / values.length);
    }
}

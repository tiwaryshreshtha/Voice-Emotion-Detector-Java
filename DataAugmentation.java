import org.apache.commons.math3.analysis.function.Add;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.FastMath;
import org.jtransforms.fft.DoubleFFT_1D;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
public class DataAugmentation {
    public static void main(String[] args) {
        String inputFilePath = "InputAudio.wav";
        String outputFilePath = "OutputAudio_augmented.wav";
        try {
            // Load the audio file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(inputFilePath));
            AudioFormat format = audioInputStream.getFormat();
            int frameSize = format.getFrameSize();
            byte[] audioData = new byte[(int) (audioInputStream.getFrameLength() * frameSize)];
            audioInputStream.read(audioData);
            // Apply data augmentation techniques
            byte[] augmentedData = applyDataAugmentation(audioData, format.getSampleRate());
            // Save the augmented audio to a new file
            AudioInputStream augmentedAudioInputStream = new AudioInputStream(
                    new ByteArrayInputStream(augmentedData),
                    format,
                    audioData.length / frameSize
            );
            AudioSystem.write(augmentedAudioInputStream, AudioFileFormat.Type.WAVE, new File(outputFilePath));
            System.out.println("Data augmentation completed.");
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }
    private static byte[] applyDataAugmentation(byte[] audioData, float sampleRate) {
        // Convert byte array to float array
        float[] audioFloatData = new float[audioData.length / 2];
        ByteBuffer.wrap(audioData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(audioFloatData);
        // Apply noise
        float noiseAmplitude = 0.035f * (new Random().nextFloat()) * max(audioFloatData);
        for (int i = 0; i < audioFloatData.length; i++) {
            audioFloatData[i] += noiseAmplitude * new Random().nextGaussian();
        }
        // Apply stretching (change speed)
        float stretchRate = 0.8f;
        int newLength = (int) (audioFloatData.length * stretchRate);
        float[] stretchedData = new float[newLength];
        for (int i = 0; i < newLength; i++) {
            float index = i * audioFloatData.length / (float) newLength;
            int lowIndex = (int) index;
            int highIndex = lowIndex + 1;
            float weight = index - lowIndex;
            if (highIndex < audioFloatData.length) {
                stretchedData[i] = (1 - weight) * audioFloatData[lowIndex] + weight * audioFloatData[highIndex];
            }
        }
        // Apply pitch shift
        float pitchFactor = 0.7f;
        float[] pitchedData = new float[audioFloatData.length];
        float step = 1 / pitchFactor;
        for (int i = 0; i < audioFloatData.length; i++) {
            float newIndex = i / pitchFactor;
            int lowIndex = (int) newIndex;
            int highIndex = lowIndex + 1;
            float weight = newIndex - lowIndex;
            if (highIndex < audioFloatData.length) {
                pitchedData[i] = (1 - weight) * stretchedData[lowIndex] + weight * stretchedData[highIndex];
            }
        }
        // Convert float array back to byte array
        byte[] augmentedData = new byte[pitchedData.length * 2];
        ByteBuffer.wrap(augmentedData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(pitchedData);
        return augmentedData;
    }
    private static float max(float[] array) {
        float max = array[0];
        for (float value : array) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}

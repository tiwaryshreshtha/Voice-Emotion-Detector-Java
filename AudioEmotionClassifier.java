import org.deeplearning4j.nn.conf.*;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.schedule.ScheduleType;
import org.nd4j.linalg.schedule.StepSchedule;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.conf.*;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.schedule.ScheduleType;
import org.nd4j.linalg.schedule.StepSchedule;
import org.nd4j.linalg.schedule.MapSchedule;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerType;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.schedule.ScheduleType;
import org.nd4j.linalg.schedule.StepSchedule;
public class AudioEmotionClassifier {
    public static void main(String[] args) {
        int numClasses = 8; 
        int numFeatures = X[0].length; 
        int numHiddenNodes = 32;
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .seed(123)
            .weightInit(WeightInit.XAVIER)
            .updater(new Adam(0.001))
            .list()
            .layer(new Convolution1DLayer.Builder()
                .nIn(1)
                .nOut(256)
                .kernelSize(5)
                .stride(1)
                .padding(0)
                .activation(Activation.RELU)
                .build())
            .layer(new Subsampling1DLayer.Builder()
                .poolingType(SubsamplingLayer.PoolingType.MAX)
                .kernelSize(5)
                .stride(2)
                .padding(0)
                .build())
            .layer(new Convolution1DLayer.Builder()
                .nOut(256)
                .kernelSize(5)
                .stride(1)
                .padding(0)
                .activation(Activation.RELU)
                .build())
            .layer(new Subsampling1DLayer.Builder()
                .poolingType(SubsamplingLayer.PoolingType.MAX)
                .kernelSize(5)
                .stride(2)
                .padding(0)
                .build())
            .layer(new Convolution1DLayer.Builder()
                .nOut(128)
                .kernelSize(5)
                .stride(1)
                .padding(0)
                .activation(Activation.RELU)
                .build())
            .layer(new Subsampling1DLayer.Builder()
                .poolingType(SubsamplingLayer.PoolingType.MAX)
                .kernelSize(5)
                .stride(2)
                .padding(0)
                .build())
            .layer(new DropoutLayer(0.2))
            .layer(new Convolution1DLayer.Builder()
                .nOut(64)
                .kernelSize(5)
                .stride(1)
                .padding(0)
                .activation(Activation.RELU)
                .build())
            .layer(new Subsampling1DLayer.Builder()
                .poolingType(SubsamplingLayer.PoolingType.MAX)
                .kernelSize(5)
                .stride(2)
                .padding(0)
                .build())
            .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .nOut(numClasses)
                .activation(Activation.SOFTMAX)
                .build())
            .setInputType(InputType.convolutional1D(numFeatures, 1, 1))
            .build();
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        System.out.println(model.summary());
        DataNormalization normalizer = new NormalizerStandardize();
        normalizer.fit(x_train);
        normalizer.transform(x_train);
        normalizer.transform(x_test);
        model.fit(x_train, y_train);
        Evaluation eval = model.evaluate(x_test, y_test);
        System.out.println(eval.stats());
    }
}

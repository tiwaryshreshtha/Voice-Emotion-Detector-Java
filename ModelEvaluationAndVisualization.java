import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
// Print accuracy on test data
Evaluation evaluation = model.evaluate(testDataIterator);
double accuracy = evaluation.accuracy() * 100;
System.out.println("Accuracy of our model on test data: " + accuracy + "%");
// Plot training and testing loss/accuracy
int[] epochs = new int[50]; // Create an array with epoch numbers
for (int i = 0; i < epochs.length; i++) {
    epochs[i] = i;
}
double[] trainLoss = history.getDouble("loss");
double[] testLoss = history.getDouble("val_loss");
double[] trainAccuracy = history.getDouble("accuracy");
double[] testAccuracy = history.getDouble("val_accuracy");
// Create and display the plots
PlotUtil.plot2D(
    "Training & Testing Loss",
    "Epochs",
    "Loss",
    epochs,
    new double[][]{trainLoss, testLoss},
    new String[]{"Training Loss", "Testing Loss"}
);
PlotUtil.plot2D(
    "Training & Testing Accuracy",
    "Epochs",
    "Accuracy",
    epochs,
    new double[][]{trainAccuracy, testAccuracy},
    new String[]{"Training Accuracy", "Testing Accuracy"}
);
// Predict on test data
INDArray predictions = model.output(testDataIterator);
int[] predictedLabels = predictions.argMax(1).toIntVector();
// Convert predicted and actual labels back to original classes
String[] predictedLabelsStr = encoder.inverseTransform(predictedLabels);
String[] actualLabelsStr = encoder.inverseTransform(testLabels);
// Create a DataFrame to display predicted and actual labels
String[] columnNames = {"Predicted Labels", "Actual Labels"};
String[][] data = new String[predictedLabelsStr.length][2];
for (int i = 0; i < predictedLabelsStr.length; i++) {
    data[i][0] = predictedLabelsStr[i];
    data[i][1] = actualLabelsStr[i];
}
DataFrame df = new DefaultDataFrame(columnNames, data);
System.out.println(df.head(10));
// Create and display the confusion matrix
int[][] confusionMatrix = EvaluationUtil.calculateConfusionMatrix(testLabels, predictedLabels, numClasses);
DataFrame cmDataFrame = EvaluationUtil.createConfusionMatrixDataFrame(confusionMatrix, encoder.getLabels());
System.out.println(cmDataFrame.toString());
// Display the classification report
String classificationReport = EvaluationUtil.calculateClassificationReport(testLabels, predictedLabels, encoder.getLabels());
System.out.println(classificationReport);

package com.komarzz.KNN;

import com.komarzz.KNN.data.DataProcessor;
import com.komarzz.KNN.data.SplitData;
import tech.tablesaw.api.Table;

import java.lang.annotation.Target;

public class Main {

    public static void main(String[] args) {

        DataProcessor data = new DataProcessor("src/main/java/com/komarzz/KNN/data/dataStorage/diabetes.csv");

        Table df  = data.getNormalizedData();



        SplitData splitData = DataProcessor.splitData(df, 0.6);


        Table trainData = splitData.trainingData();
        Table testData = splitData.testData();

        KNN knn = new KNN(9);

        knn.fit(trainData);

        Table predictedOutcome = knn.predict(testData);
        Table trueOutcome = Table.create("Outcome", testData.column("Outcome"));


        printConfusionMatrix(trueOutcome, predictedOutcome);


//        VisualizationUtility.showHistograms(df);

    }

    public static void printConfusionMatrix(Table trueLabels, Table predictedLabels) {
        // Предполагаем, что у нас есть два класса: 0 и 1
        int[][] confusionMatrix = new int[2][2]; // Матрица размером 2x2 для бинарной классификации

        for (int i = 0; i < trueLabels.rowCount(); i++) {
            int actual = (int)trueLabels.doubleColumn("Outcome").get(i).doubleValue(); // Замените "actual" на имя вашего столбца с фактическими метками
            int predicted = (int)predictedLabels.doubleColumn("Outcome").get(i).doubleValue(); // Замените "predicted" на имя вашего столбца с предсказанными метками
            confusionMatrix[actual][predicted]++;
        }

        System.out.println("Confusion Matrix:");
        System.out.println("                 0    1");
        System.out.print("Predicted: 0    ");
        System.out.println(confusionMatrix[0][0] + "    " + confusionMatrix[0][1]);
        System.out.print("Actual:    1    ");
        System.out.println(confusionMatrix[1][0] + "    " + confusionMatrix[1][1]);
    }

}

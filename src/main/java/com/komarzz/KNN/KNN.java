package com.komarzz.KNN;

import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class KNN {

    private Table trainingData;
    private int k;

    public KNN(int k) {
        this.k = k;
    }

    public void fit(Table data) {
        // В k-NN, метод fit обычно просто запоминает данные
        this.trainingData = data;
    }

    public Table predict(Table testData) {
        Table predictions = Table.create("Predictions");
        Column<?> targetColumn = testData.column(testData.columnCount() - 1);
        predictions.addColumns(targetColumn.emptyCopy());

        for (int rowIndex = 0; rowIndex < testData.rowCount(); rowIndex++) {
            double[] queryInstance = new double[testData.columnCount() - 1];
            for (int i = 0; i < testData.columnCount() - 1; i++) {
                queryInstance[i] = testData.doubleColumn(i).get(rowIndex);
            }

            // Вычисляем расстояния от текущего примера до всех примеров в тренировочном наборе
            List<DistanceInstance> distanceInstances = new ArrayList<>();
            for (int i = 0; i < trainingData.rowCount(); i++) {
                double[] trainInstance = new double[trainingData.columnCount() - 1];
                for (int j = 0; j < trainingData.columnCount() - 1; j++) {
                    trainInstance[j] = trainingData.doubleColumn(j).get(i);
                }
                double distance = calculateDistance(queryInstance, trainInstance);
                distanceInstances.add(new DistanceInstance(distance, i));
            }

            // Сортируем и выбираем k ближайших соседей
            Collections.sort(distanceInstances);
            double prediction = makePrediction(distanceInstances.subList(0, k), trainingData);

            // Добавляем предсказание в таблицу
            predictions.doubleColumn(0).append(prediction);
        }

        return predictions;
    }


    private double calculateDistance(double[] queryInstance, double[] trainInstance) {
        double sum = 0;
        for (int i = 0; i < queryInstance.length; i++) {
            sum += Math.pow(queryInstance[i] - trainInstance[i], 2);
        }
        return Math.sqrt(sum);
    }

    private double makePrediction(List<DistanceInstance> nearestNeighbors, Table data) {
        int countOnes = 0;
        int countZeros = 0;



        for (DistanceInstance neighbor : nearestNeighbors) {
            Double outcome = data.doubleColumn("Outcome").get(neighbor.index);
            if (outcome == 1.0) {
                countOnes++;
            } else {
                countZeros++;
            }
        }

        // Если количество единиц больше, возвращаем 1, иначе возвращаем 0
        return countOnes > countZeros ? 1.0 : 0.0;
    }





    private static class DistanceInstance implements Comparable<DistanceInstance> {
        double distance;
        int index;

        public DistanceInstance(double distance, int index) {
            this.distance = distance;
            this.index = index;
        }

        @Override
        public int compareTo(DistanceInstance o) {
            return Double.compare(this.distance, o.distance);
        }
    }
}

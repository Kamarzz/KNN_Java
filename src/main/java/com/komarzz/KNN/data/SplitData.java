package com.komarzz.KNN.data;

import tech.tablesaw.api.Table;

public record SplitData(Table trainingData, Table testData) {}

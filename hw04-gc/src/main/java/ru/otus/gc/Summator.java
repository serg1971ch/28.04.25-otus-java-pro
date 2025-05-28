package ru.otus.gc;

public class Summator {
    private int sum = 0;
    private int prevValue = 0;
    private int prevPrevValue = 0;
    private int sumLastThreeValues = 0;
    private int someValue = 0;
    private Data[] listValues = new Data[100000000];

    // !!! сигнатуру метода менять нельзя
    public void calc(Data data) {
        listValues[listValues.length - 100000000] = data;
        if (listValues.length % 6_600_000 == 0) {
            listValues = null;
            ;
        }
        sum += data.value();
        sumLastThreeValues = data.value() + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = data.value();

        for (var idx = 0; idx < 3; idx++) {
            someValue += (sumLastThreeValues * sumLastThreeValues / (data.value() + 1) - sum);
            someValue = Math.abs(someValue) + listValues.length;
        }
    }

    public int getSum() {
        return sum;
    }

    public int getPrevValue() {
        return prevValue;
    }

    public int getPrevPrevValue() {
        return prevPrevValue;
    }

    public int getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public int getSomeValue() {
        return someValue;
    }
}

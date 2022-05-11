import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class MonthReport {
    private String[] contentSeparatedByEOL;
    private String[][] stringCellAt; //хранится содержимое файла, представленное в виде ячеек
    private int numberOfLines; //число строк в файле отчета
    private HashMap<String, Double> earnings = new HashMap<>(); //хранится вся выручка в виде <Наименование, Цена * Количество>
    private HashMap<String, Double> losses = new HashMap<>();
    public double maxLoss = 0.0, maxEarning = 0.0; //самый большой доход(расход) за месяц
    public String nameOfMaxLoss, nameOfMaxEarning; //и его наименование


    public MonthReport(String path) throws IOException {
        contentSeparatedByEOL = Files.readString(Path.of(path)).split("\n");
        fileContentToCells();
        earningsToMap();
        lossesToMap();
    }

    private void fileContentToCells() { //инициализация двумерного массива stringCellAt содержимым файла
        stringCellAt = new String[contentSeparatedByEOL.length][];

        for(int i = 0; i < contentSeparatedByEOL.length; i++) {
            stringCellAt[i] = contentSeparatedByEOL[i].split(",");
        }

        numberOfLines = contentSeparatedByEOL.length;
    }

    private void earningsToMap() { //инициализация HashMap<String, Double> earnings;
        double product;
        boolean is_expense;

        for(int line = 1; line < numberOfLines; line++) { //собрали мапу в виде (название товара, сумма)
            is_expense = Boolean.parseBoolean(stringCellAt[line][1]);
            if(!is_expense) {
                product = Double.parseDouble(stringCellAt[line][2]) *
                        Double.parseDouble(stringCellAt[line][3]);
                earnings.put(stringCellAt[line][0], product);
            }
        }
    }

    private void lossesToMap() { //инициализация HashMap<String, Double> losses;
        double product;
        boolean is_expense;

        for(int line = 1; line < numberOfLines; line++) { //собрали мапу в виде (название товара, сумма)
            is_expense = Boolean.parseBoolean(stringCellAt[line][1]);
            if(is_expense) {
                product = Double.parseDouble(stringCellAt[line][2]) *
                        Double.parseDouble(stringCellAt[line][3]);
                losses.put(stringCellAt[line][0], product);
            }
        }
    }

    public void determineMaxEarning() { //определяет наименование и стоимость самой большой выручки
        for(String name : earnings.keySet()) {
            if(earnings.get(name) > maxEarning) {
                maxEarning = earnings.get(name);
                nameOfMaxEarning = name;
            }
        }
    }

    public void determineMaxLoss() { //определяет наименование и стоимость самого большого расхода
        for(String name : losses.keySet()) {
            if(losses.get(name) > maxLoss) {
                maxLoss = losses.get(name);
                nameOfMaxLoss = name;
            }
        }
    }

    public double calculateEarnings() { //считает всю выручку за месяц
        double earningsTotal = 0.0;

        for(double earning : earnings.values()) {
            earningsTotal += earning;
        }
        return earningsTotal;
    }

    public double calculateLosses() { //считает все расходы за месяц
        double lossesTotal = 0.0;

        for(double loss : losses.values()) {
            lossesTotal += loss;
        }

        return lossesTotal;
    }

}

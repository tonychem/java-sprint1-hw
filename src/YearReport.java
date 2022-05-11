import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;


public class YearReport {
    private String[] contentSeparatedByEOL;
    private String[][] cellRepresentation; //хранится содержимое файла, представленное в виде ячеек
    public HashMap<Integer, Double> earningsInMonth = new HashMap<>(); //хранится вся выручка в виде <Номер месяца, размер выручки>
    public HashMap<Integer, Double> lossesInMonth = new HashMap<>();
    public int numberOfLines; //число строк в файле отчета

    public YearReport(String path) throws IOException {
        contentSeparatedByEOL = Files.readString(Path.of(path)).split("\n");
        fileContentToCells();
        calculateReport();
    }

    private void fileContentToCells() { //инициализация двумерного массива stringCellAt содержимым файла
        cellRepresentation = new String[contentSeparatedByEOL.length][];

        for(int i = 0; i < contentSeparatedByEOL.length; i++) {
            cellRepresentation[i] = contentSeparatedByEOL[i].split(",");
        }

        numberOfLines = contentSeparatedByEOL.length;
    }

    private void calculateReport() { //инициализация HashMap<Integer, Double> earningsInMonth и lossesInMonth;
        int currentLineInFile = 1;

        for(int month = 0; month < 3; month++) {
            String monthNotation = "0" + (month + 1);
            double sumOfEarningsInMonth = 0.0;
            double sumOfLossesInMonth = 0.0;
            boolean isExpense;

            while (currentLineInFile != numberOfLines && monthNotation.equals(cellRepresentation[currentLineInFile][0])) {
                isExpense = Boolean.parseBoolean(cellRepresentation[currentLineInFile][2]);
                if (isExpense) {
                    sumOfLossesInMonth += Integer.parseInt(cellRepresentation[currentLineInFile][1]);
                } else {
                    sumOfEarningsInMonth += Integer.parseInt(cellRepresentation[currentLineInFile][1]);
                }
                currentLineInFile++;
            }

            lossesInMonth.put(month, sumOfLossesInMonth);
            earningsInMonth.put(month, sumOfEarningsInMonth);
        }
    }

    public double getIncome(int month) { //расчитывает прибыль по номеру месяца
        return earningsInMonth.get(month) - lossesInMonth.get(month);
    }

    public double getAverageEarnings() { //расчет средней выручки за год
        double sum = 0.0;

        for(double earning : earningsInMonth.values()) {
            sum += earning;
        }

        return sum / earningsInMonth.size();
    }

    public double getAverageLosses() { //расчет средних расходов за год
        double sum = 0.0;

        for(double loss : lossesInMonth.values()) {
            sum += loss;
        }

        return sum / lossesInMonth.size();
    }
}

import java.io.IOException;
import java.util.Scanner;


public class Main {

    static Scanner scan = new Scanner(System.in);
    static MonthReport[] monthReports = new MonthReport[12];
    static YearReport year2021;
    static boolean monthReportsInitialized = false;
    static boolean yearReportInitialized = false;

    public static void main(String[] args) {
        int userChoice;
        boolean inLoop = true;

        while(inLoop) {
            printMenu();
            userChoice = scan.nextInt();
            inLoop = choiceHandler(userChoice);
        }
    }

    static void printMenu() {
        System.out.println("Что вы хотите сделать?\n" +
                "1 -> Загрузить все месячные отчёты\n" +
                "2 -> Загрузить годовой отчёт\n" +
                "3 -> Сверить отчёты\n" +
                "4 -> Вывести информацию о всех месячных отчётах\n" +
                "5 -> Вывести информацию о годовом отчёте\n" +
                "0 -> Выход\n");
    }

    static void printMont(int month) {
        switch(month) {
            case 0:
                System.out.println("Январь");
                break;
            case 1:
                System.out.println("Февраль");
                break;
            case 2:
                System.out.println("Март");
        }
    }

    static boolean choiceHandler(int userChoice) {
        if(userChoice == 1) {
            loadMonthReports();
        } else if (userChoice == 2) {
            loadYearReports();
        } else if (userChoice == 3) {
            compareReports();
        } else if (userChoice == 4) {
            printMonthReport();
        } else if (userChoice == 5) {
            printYearReport();
        } else if (userChoice == 0) {
            return false;
        } else
            System.out.println("Такая команда недоступна\n");
        return true;
    }

    static void loadMonthReports() {
        String root = "resources/m.20210", extension = ".csv";
        String pathToMonthReport;
        boolean noError = true;
        monthReports = new MonthReport[12];

        for(int month = 0; month < 3 ; month++) {
            pathToMonthReport = root + (month + 1) + extension;
            try {
                monthReports[month] = new MonthReport(pathToMonthReport);
            } catch (IOException exc) {
                System.out.println("Файл не доступен по пути /" + pathToMonthReport + "\n");
                noError = false;
            }
        }
        monthReportsInitialized = true;
        if(noError) {
            System.out.println("Все файлы месячных отчетов загружены успешно.\n");
        }
    }

    static void loadYearReports() {
        String pathToYearReport = "resources/y.2021.csv";
        boolean noError = true;

        try {
            year2021 = new YearReport(pathToYearReport);
        } catch(IOException exc) {
            System.out.println("Файл не доступен по пути /" + pathToYearReport + "\n");
            noError = false;
        }

        yearReportInitialized = true;
        if(noError) {
            System.out.println("Все файлы годовых отчетов загружены успешно.\n");
        }
    }

    static void compareReports() {
        boolean noError = true;

        if(!monthReportsInitialized || !yearReportInitialized) {
            System.out.println("Сначала инициализируйте отчеты\n");
            return;
        }

        for(int month = 0; month < 3; month++) {
            if(monthReports[month].calculateEarnings() != year2021.earningsInMonth.get(month) ||
                    monthReports[month].calculateLosses() != year2021.lossesInMonth.get(month)) {
                System.out.print("Несоответствие в месяце: ");
                printMont(month);
                noError = false;
            }
        }

        if(noError) {
            System.out.println("Информация в месячных отчетах соответствует годовому отчету.\n");
        }
    }

    static void printMonthReport() {
        if(!monthReportsInitialized) {
            System.out.println("Сначала загрузите месячные отчеты");
            return;
        }

        for(int month = 0; month < 3; month++) {
            printMont(month);
            monthReports[month].determineMaxLoss();
            monthReports[month].determineMaxEarning();
            System.out.println("Самый прибыльный товар: " + monthReports[month].nameOfMaxEarning + ". Выручка составила: " + monthReports[month].maxEarning);
            System.out.println("Самый большой расход : "  + monthReports[month].nameOfMaxLoss + ". Убыток составил: " + monthReports[month].maxLoss + "\n");
        }
    }

    static void printYearReport() {
        if(!yearReportInitialized) {
            System.out.println("Сначала загрузите годовые отчеты");
            return;
        }

        System.out.println("2021\n");

        for(int month = 0; month < 3; month++) {
            printMont(month);
            System.out.println("Прибыль в этом месяце составила " + year2021.getIncome(month) + "\n");
        }

        System.out.println("Средняя выручка за год составила: " + year2021.getAverageEarnings());
        System.out.println("Средний расход за год составил: " + year2021.getAverageLosses() + "\n");
    }
}


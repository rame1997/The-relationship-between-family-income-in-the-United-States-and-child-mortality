/*
         * To change this license header, choose License Headers in Project Properties.
         * To change this template file, choose Tools | Templates
         * and open the template in the editor.
 */
package dataset;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 *
 * @author zero1
 */
public class DataSet extends Application {

    static Map<String, Double> groupedMap = null;
    static HashMap<String, Double> group = null;
    static TreeMap<String, Double> sorted = null;
    static TreeMap<String, Double> sorted1 = null;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Line Chart ");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("years");
        yAxis.setLabel("Values*100");
        final LineChart<String, Number> lineChart
                = new LineChart<String, Number>(xAxis, yAxis);

        lineChart.setTitle("Relationship of family income to the mortality rate of persons aged 1 to 19 years from 1953 to 2015");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("income");

        for (Map.Entry<String, Double> entry : sorted.entrySet()) {
            String key = entry.getKey();
            double sum = entry.getValue();
            series1.getData().add(new XYChart.Data(key, sum));

        }

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Death rate");

        for (Map.Entry<String, Double> entry : sorted1.entrySet()) {
            String key = entry.getKey();
            double sum = entry.getValue();
            series1.getData().add(new XYChart.Data(key, sum / 100));

        }
        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().addAll(series1, series2);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws FileNotFoundException {
        read1();
        read2();
        launch(args);

    }

    public static void read1() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("C:\\Users\\zero1\\Documents\\Digital Preservation\\dataset\\NCHS_-_Childhood_Mortality_Rates.csv"));
        scanner.useDelimiter("\n");

        Map<String, ArrayList<String>> linesMap = new HashMap<>();
        ArrayList<String> tempValue = null;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("Year")) {
                continue;
            }
            if (linesMap.containsKey(line.split(",")[0])) {
                linesMap.get(line.split(",")[0]).add(line);
            } else {
                tempValue = new ArrayList<>();
                tempValue.add(line);
                linesMap.put(line.split(",")[0], tempValue);
            }
        }

        groupedMap = new HashMap<String, Double>();

        ArrayList<Double> tempDeathRateList = null;
        linesMap.entrySet().forEach((e) -> {
            double summedDeathRate = 0;
            for (String str : e.getValue()) {
                summedDeathRate += Double.parseDouble(str.split(",")[2]);
            }
            double key = Double.parseDouble(e.getKey());
            if (key >= 1953) {
                groupedMap.put(e.getKey(), summedDeathRate);

            }

        });
        System.out.println("HashMap for Death of children");

        sortbykey();

        scanner.close();
    }

    public static void sortbykey() {
        sorted = new TreeMap<>();
        sorted.putAll(groupedMap);
        for (Map.Entry<String, Double> entry : sorted.entrySet()) {
            System.out.println(entry.getKey() + "  :  " + entry.getValue());
        }
    }

    public static void sortbykey2() {
        sorted1 = new TreeMap<>();
        sorted1.putAll(group);
        for (Map.Entry<String, Double> entry : sorted1.entrySet()) {
            System.out.println(entry.getKey() + "  :  " + entry.getValue());
        }
    }

    public static void read2() throws FileNotFoundException {
        ArrayList<String> arrOfStr = new ArrayList<String>();
        group = new HashMap<String, Double>();

        String[] array = null;
        String[] arrayyear = null;
        Scanner scanner = new Scanner(new File("C:\\Users\\zero1\\Documents\\Digital Preservation\\dataset\\الدخل\\real-mean-family-and-personal-income-in-the-us\\real-mean-family-income-in-the-united-states.csv"));
        scanner.useDelimiter(",");
        String line = "";
        while (scanner.hasNext()) {
            line = scanner.nextLine() + " ";
            arrOfStr.add(line);
        }
        for (int i = 1; i < arrOfStr.size(); i++) {
            array = arrOfStr.get(i).split(",");
            arrayyear = array[3].split("-");
            double key2 = Double.parseDouble(arrayyear[0]);
            if (key2 <= 2015) {
                group.put(arrayyear[0], Double.parseDouble(array[2]));
            }

        }
        System.out.println("HashMap for income");
        sortbykey2();
        scanner.close();

    }

}

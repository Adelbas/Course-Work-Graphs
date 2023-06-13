package ru.adel;

import org.jfree.chart.*;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        GraphGenerator generator = new GraphGenerator();
        Random random = new Random();
        XYSeries dijkstraDatasetDense = new XYSeries("Алгоритм Дейкстры на плотном графе");
        XYSeries dijkstraDatasetDenseAll = new XYSeries("Алгоритм Дейкстры для всех вершин на плотном графе");
        XYSeries dijkstraDatasetSparse = new XYSeries("Алгоритм Дейкстры на разряженном графе");
        XYSeries dijkstraDatasetSparseAll = new XYSeries("Алгоритм Дейкстры для всех вершин на разряженном графе");
        XYSeries bfDatasetAdjDense = new XYSeries("Алгоритм Беллмана-Форд (список смежности) на плотном графе");
        XYSeries bfDatasetAdjSparse = new XYSeries("Алгоритм Беллмана-Форд (список смежности) на разряженном графе");
        XYSeries bfDatasetEdgesDense = new XYSeries("Алгоритм Беллмана-Форд (список рёбер) на плотном графе");
        XYSeries bfDatasetEdgesDenseNegative = new XYSeries("Алгоритм Беллмана-Форд (список рёбер) на плотном графе c отрицательными рёбрами");
        XYSeries bfDatasetEdgesSparse = new XYSeries("Алгоритм Беллмана-Форд (список рёбер) на разряженном графе");
        XYSeries bfDatasetEdgesSparseNegative = new XYSeries("Алгоритм Беллмана-Форд (список рёбер) на разряженном графе с отрицательными рёбрами");
        XYSeries aStarDatasetDense = new XYSeries("Алгоритм А* (Евклидово расстояние) на плотном графе");
        XYSeries aStarDatasetSparse = new XYSeries("Алгоритм А* (Евклидово расстояние) на разряженном графе");
        XYSeries aStarDatasetConstant = new XYSeries("Алгоритм А* (Константа)");
        long start, end, time;
        //Поиск кратчайшего пути в плотных и разряженных графах с положительными весами рёбер
        for (int vertexCount = 5; vertexCount <= 1000; vertexCount +=10) {
            System.out.println(vertexCount);
            //Ассоциативный массив для хранения координат вершин
            HashMap<Integer, Point> vertexCoordsDense = new HashMap<>();
            HashMap<Integer, Point> vertexCoordsSparse = new HashMap<>();
            ArrayList<ArrayList<Node>> denseGraph = generator.generatePlaneGraph(vertexCount, 90, vertexCoordsDense);
            ArrayList<ArrayList<Node>> sparseGraph = generator.generatePlaneGraph(vertexCount, 30, vertexCoordsSparse);
            ArrayList<ArrayList<Node>> denseNegativeGraph = generator.generateGraph(vertexCount,90,1000,-100,10);
            ArrayList<ArrayList<Node>> sparseNegativeGraph = generator.generateGraph(vertexCount,30,1000,-100,10);
            int source = random.nextInt(vertexCount); // выбираем случайную начальную вершину
            int target = random.nextInt(vertexCount); // выбираем случайную конечную вершину
            while (source == target) //Начальная вершина не должна совпадать с конечной
                target = random.nextInt(vertexCount);

            //Алгоритм Дейкстры для плотного графа
            start = System.nanoTime();
            double pathLengthDijkstraDense = Dijkstra.getShortestPathWeight(denseGraph, source, target);
            end = System.nanoTime();
            time = end - start;
            dijkstraDatasetDense.add(vertexCount, time);
//
            //Алгоритм Дейкстры для разряженного графа
            start = System.nanoTime();
            double pathLengthDijkstraSparse = Dijkstra.getShortestPathWeight(sparseGraph, source, target);
            end = System.nanoTime();
            time = end - start;
            dijkstraDatasetSparse.add(vertexCount, time);

//            Алгоритм Декйстры для плотного графа для поиска путей ко всем вершинам
            start = System.nanoTime();
            double[] allPathsDijkstraDense = Dijkstra.getShortestPathWeightToAllVertexes(denseGraph, source);
            end = System.nanoTime();
            time = end - start;
            dijkstraDatasetDenseAll.add(vertexCount, time);

//            Алгоритм Декйстры для разряженного графа для поиска путей ко всем вершинам
            start = System.nanoTime();
            double[] allPathsDijkstraSparse = Dijkstra.getShortestPathWeightToAllVertexes(sparseGraph, source);
            end = System.nanoTime();
            time = end - start;
            dijkstraDatasetSparseAll.add(vertexCount, time);

//            Алгоритм Беллмана-Форда со списком смежности
//            Для плотного графа
            start = System.nanoTime();
            double pathLengthBf1 = BellmanFord.getShortestPathWeightAdj(denseNegativeGraph,source,target);
            end = System.nanoTime();
            time = end-start;
            bfDatasetAdjDense.add(vertexCount, time);
//            Для разряженного графа
            start = System.nanoTime();
            double pathLengthBf2 = BellmanFord.getShortestPathWeightAdj(sparseNegativeGraph,source,target);
            end = System.nanoTime();
            time = end-start;
            bfDatasetAdjSparse.add(vertexCount, time);

//            Алгоритм Беллмана-Форда со списком рёбер
//            Создание списка рёбер для плотных графов
            ArrayList<Edge> edgesDense = edgesFromAdj(denseGraph);
            ArrayList<Edge> edgesDenseNegative = edgesFromAdj(denseNegativeGraph);
            //Создание списка рёбер для разряженных графов
            ArrayList<Edge> edgesSparse = edgesFromAdj(sparseGraph);
            ArrayList<Edge> edgesSparseNegative = edgesFromAdj(sparseNegativeGraph);
            //Поиск пути для плотного графа с отрицательными рёбрами
            start = System.nanoTime();
            double bellmanNegativeDense= BellmanFord.getShortestPathWeightEdges(edgesDenseNegative,vertexCount, source,target);
            end = System.nanoTime();
            time = end-start;
            bfDatasetEdgesDenseNegative.add(vertexCount, time);
            //Поиск пути для разряженного графа с отрицательными рёбрами
            start = System.nanoTime();
            double bellmanNegativeSparse= BellmanFord.getShortestPathWeightEdges(edgesSparseNegative,vertexCount, source,target);
            end = System.nanoTime();
            time = end-start;
            bfDatasetEdgesSparseNegative.add(vertexCount, time);

            //Поиск пути до всех вершин для плотного графа для сравнения с Дейкстрой
            start = System.nanoTime();
            double[] toAllBellmanDense = BellmanFord.getShortestPathWeightEdgesToAll(edgesDense,vertexCount, source);
            end = System.nanoTime();
            time = end-start;
            bfDatasetEdgesDense.add(vertexCount, time);
            //Поиск пути до всех вершин для разряженного графа с Дейкстрой
            start = System.nanoTime();
            double[] toAllBellmanSparse = BellmanFord.getShortestPathWeightEdgesToAll(edgesSparse,vertexCount, source);
            end = System.nanoTime();
            time = end-start;
            bfDatasetEdgesSparse.add(vertexCount, time);


            //Алгоритм А* эвристика - евклидово расстояние
            //Плотные графы
            start = System.nanoTime();
            double pathLengthAStarDense = AStar.getShortestPathWeight(denseGraph,source,target, vertexCoordsDense, false);
            end = System.nanoTime();
            time = end-start;
            aStarDatasetDense.add(vertexCount, time);
//            Разряженные графы
            start = System.nanoTime();
            double pathLengthAStarSparse = AStar.getShortestPathWeight(sparseGraph,source,target, vertexCoordsSparse, false);
            end = System.nanoTime();
            time = end-start;
            aStarDatasetSparse.add(vertexCount, time);

//            //Алгоритм А* эвристика - константа
            start = System.nanoTime();
            double pathLengthAStarConst = AStar.getShortestPathWeight(denseGraph,source,target, vertexCoordsDense, true);
            end = System.nanoTime();
            time = end-start;
            aStarDatasetConstant.add(vertexCount, time);
        }

//      Создаем коллекции серий данных и добавляем в них списки данных
        XYSeriesCollection dijkstraDataset = new XYSeriesCollection();
        dijkstraDataset.addSeries(dijkstraDatasetDense);
        dijkstraDataset.addSeries(dijkstraDatasetSparse);

        XYSeriesCollection bellmanFordDataset = new XYSeriesCollection();
        bellmanFordDataset.addSeries(bfDatasetAdjDense);
        bellmanFordDataset.addSeries(bfDatasetAdjSparse);
        bellmanFordDataset.addSeries(bfDatasetEdgesDense);
        bellmanFordDataset.addSeries(bfDatasetEdgesSparse);

        XYSeriesCollection aStarDataset = new XYSeriesCollection();
        aStarDataset.addSeries(aStarDatasetDense);
        aStarDataset.addSeries(aStarDatasetSparse);

        XYSeriesCollection aStarConstantAndDijkstra = new XYSeriesCollection();
        aStarConstantAndDijkstra.addSeries(aStarDatasetConstant);
        aStarConstantAndDijkstra.addSeries(dijkstraDatasetDense);

        XYSeriesCollection allAlgorithmsDense = new XYSeriesCollection();
        allAlgorithmsDense.addSeries(dijkstraDatasetDense);
        allAlgorithmsDense.addSeries(bfDatasetEdgesDense);
        allAlgorithmsDense.addSeries(aStarDatasetDense);

        XYSeriesCollection dijkstraAndBellFordDense = new XYSeriesCollection();
        dijkstraAndBellFordDense.addSeries(dijkstraDatasetDenseAll);
        dijkstraAndBellFordDense.addSeries(bfDatasetEdgesDense);

        XYSeriesCollection dijkstraAndBellFordSparse = new XYSeriesCollection();
        dijkstraAndBellFordSparse.addSeries(dijkstraDatasetSparseAll);
        dijkstraAndBellFordSparse.addSeries(bfDatasetEdgesSparse);

        XYSeriesCollection dijkstraAndAStarDense = new XYSeriesCollection();
        dijkstraAndAStarDense.addSeries(dijkstraDatasetDense);
        dijkstraAndAStarDense.addSeries(aStarDatasetDense);

        XYSeriesCollection dijkstraAndAStarSparse = new XYSeriesCollection();
        dijkstraAndAStarSparse.addSeries(dijkstraDatasetSparse);
        dijkstraAndAStarSparse.addSeries(aStarDatasetSparse);

        //Создаем график для алгоритма Дейкстры
        JFreeChart chartDijkstra = createChart(
                "График зависимости времени работы алгоритма Дейкстры от количества вершин",
                dijkstraDataset);
        //Создаём график для алгоритма Беллмана-Форда
        JFreeChart chartBellmanFord = createChart(
                "График зависимости времени работы алгоритма Беллмана-Форда от количества вершин",
                bellmanFordDataset);
        //Создаём график для алгоритма A*
        JFreeChart chartAStar = createChart(
                "График зависимости времени работы алгоритма A* от количества вершин",
                aStarDataset);
        //Создаём график для сравнения алгоритма А* (при эвристике - константе) и алгоритма Дейкстры
        JFreeChart chartAStarConstant = createChart(
                "График зависимости времени работы алгоритма A* и алгоритма Дейкстры от количества вершин",
                aStarConstantAndDijkstra);
        //Создаём график для всех алгоритмов
        JFreeChart chartAllDense = createChart(
                "График зависимости времени работы алгоритмов от количества вершин на плотных графах",
                allAlgorithmsDense);
        //Создаём график для сравнения алгоритма Беллмана-Форда и Дейкстры на плотных графах
        JFreeChart chartDiBfDense = createChart(
                "График зависимости времени работы алгоритма Беллмана-Форда с графиком алгоритма Дейкстры от количества вершин на плотных графах",
                dijkstraAndBellFordDense);
        //Создаём график для сравнения алгоритма Беллмана-Форда и Дейкстры на разряженных графах
        JFreeChart chartDiBfSparse = createChart(
                "График зависимости времени работы алгоритма Беллмана-Форда с графиком алгоритма Дейкстры от количества вершин на разряженных графах",
                dijkstraAndBellFordSparse);
        //Создаём график для сравнения алгоритма Дейкстры и А* на плотных графах
        JFreeChart chartDiAStarDense = createChart(
                "График зависимости времени работы алгоритма Дейкстры с графиком алгоритма А* от количества вершин на плотных графах",
                dijkstraAndAStarDense);
        //Создаём график для сравнения алгоритма Дейкстры и А* на разряженных графах
        JFreeChart chartDiAStarSparse = createChart(
                "График зависимости времени работы алгоритма Дейкстры с графиком алгоритма А* от количества вершин на разряженных графах",
                dijkstraAndAStarSparse);


        //Отображаем графики в окне
        JFrame jFrame1 = setJFrame("Алгоритм Дейкстры", chartDijkstra);
        JFrame jFrame2 = setJFrame("Алгоритм Беллмана-Форда", chartBellmanFord);
        JFrame jFrame3 = setJFrame("Алгоритм A*", chartAStar);
        JFrame jFrame4 = setJFrame("Алгоритм A* (константа)", chartAStarConstant);
        JFrame jFrame5 = setJFrame("Все Алгоритмы", chartAllDense);
        JFrame jFrame6 = setJFrame("Дейкстра и Беллман-Форд на плотных графах", chartDiBfDense);
        JFrame jFrame7 = setJFrame("Дейкстра и Беллман-Форд на разряженных графах", chartDiBfSparse);
        JFrame jFrame8 = setJFrame("Дейкстра и А* на плотных графах", chartDiAStarDense);
        JFrame jFrame9 = setJFrame("Дейкстра и А* на разряженных графах", chartDiAStarSparse);
        //Сохраняем графики
        saveLineChartAsPNG("Dijkstra.png", chartDijkstra);
        saveLineChartAsPNG("BellmanFord.png", chartBellmanFord);
        saveLineChartAsPNG("AStar.png", chartAStar);
        saveLineChartAsPNG("AStar(const).png", chartAStarConstant);
        saveLineChartAsPNG("AllAlgorithms.png", chartAllDense);
        saveLineChartAsPNG("DijBfDense.png", chartDiBfDense);
        saveLineChartAsPNG("DijBfSparse.png", chartDiBfSparse);
        saveLineChartAsPNG("DijAStarDense.png", chartDiAStarDense);
        saveLineChartAsPNG("DijAStarSparse.png", chartDiAStarSparse);
    }


    //Преобразование списка смежности в список рёбер
    private static ArrayList<Edge> edgesFromAdj(ArrayList<ArrayList<Node>> adjList) {
        ArrayList<Edge> edges = new ArrayList<>();
        for(int i =0; i<adjList.size(); i++){
            for(int j=0; j<adjList.get(i).size(); j++){
                edges.add(new Edge(i,adjList.get(i).get(j).getVertex(),adjList.get(i).get(j).getWeight()));
            }
        }
        return edges;
    }

    //Создание графика
    private static JFreeChart createChart(String title, XYSeriesCollection dijkstraDatasetDense) {
        return ChartFactory.createXYLineChart(
                title,
                "Количество Вершин",
                "Время, нс.",
                dijkstraDatasetDense,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    //Задание параметров для окна
    private static JFrame setJFrame(String title, JFreeChart chart) {
        chart.getRenderingHints().put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        chart.setPadding(new RectangleInsets(10, 10, 10, 10));
        XYItemRenderer renderer = chart.getXYPlot().getRenderer();
        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesPaint(1, Color.RED);
        renderer.setSeriesPaint(2, Color.BLUE);
        renderer.setSeriesPaint(3, Color.ORANGE);
        renderer.setSeriesPaint(4, Color.GREEN);
        renderer.setBaseStroke(new BasicStroke(2.0f));
        ((AbstractRenderer) renderer).setAutoPopulateSeriesStroke(false);
        ChartPanel chartPanel = new ChartPanel(chart, false);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPreferredSize(new Dimension(980, 605));
        chartPanel.setDoubleBuffered(false);
        JFrame jFrame = new JFrame(title);
        jFrame.add(chartPanel);
        jFrame.pack();
        RefineryUtilities.centerFrameOnScreen(jFrame);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        return jFrame;
    }

    //Функция для сохранения графика
    private static void saveLineChartAsPNG(String fileName, JFreeChart chart) {
        File lineChart = new File("images/"+fileName);
        try {
            ChartUtilities.saveChartAsPNG(lineChart, chart, 980, 605);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Функция для вывода всех вершин графа с координатами и рёбрами
    public static void showGraph(ArrayList<ArrayList<Node>> graph, HashMap<Integer, Point> vertexCoords) {
        for (int v = 0; v < graph.size(); v++) {
            System.out.println("vertex = " + v + " coords: " + vertexCoords.get(v));
            System.out.println("outgoing edges:");
            for (int e = 0; e < graph.get(v).size(); e++) {
                System.out.println("( " + v + " " + graph.get(v).get(e).getVertex() + " ) weight = " + graph.get(v).get(e).getWeight());
            }
            System.out.println();
        }
    }
}
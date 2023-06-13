package ru.adel;

import org.jgrapht.Graph;
import org.jgrapht.GraphTests;
import org.jgrapht.generate.GnpRandomGraphGenerator;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.util.SupplierUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GraphGenerator {
    //Генерация связного ориентированного взвешенного графа в виде списка смежности
    private ArrayList<ArrayList<Node>> generateConnectedDirectedWeightedGraph(int vertexCount, double densityPercent){
        GnpRandomGraphGenerator<Integer, DefaultWeightedEdge> generator = new GnpRandomGraphGenerator<>(vertexCount, densityPercent/100);
        Graph<Integer, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(SupplierUtil.createIntegerSupplier(), SupplierUtil.createDefaultWeightedEdgeSupplier());
        generator.generateGraph(graph,null);
        //Проверка связности графа
        while(!GraphTests.isConnected(graph)){
            graph = new SimpleDirectedWeightedGraph<>(SupplierUtil.createIntegerSupplier(), SupplierUtil.createDefaultWeightedEdgeSupplier());
            generator.generateGraph(graph, null);
        }
        ArrayList<ArrayList<Node>> adjList = new ArrayList<>(vertexCount);
        for (int j = 0; j < vertexCount; j++) {
            adjList.add(new ArrayList<>(vertexCount));
        }
        for (DefaultWeightedEdge e : graph.edgeSet()){
            adjList.get(graph.getEdgeSource(e)).add(new Node(graph.getEdgeTarget(e),0));
        }
        return adjList;
    }

    //Генерация плотного/разряженного/ графа с положительными/отрицательными весами рёбер
    public ArrayList<ArrayList<Node>> generateGraph(int vertexCount, double densityPercent, int edgeMax,  int edgeMin, int negativeFreq) {
        ArrayList<ArrayList<Node>> adjList = generateConnectedDirectedWeightedGraph(vertexCount,densityPercent);
        //Присвоение рёбрам случайных весов
         Random random = new Random();
        for(int i = 0; i<adjList.size(); i++){
            for(int j = 0; j<adjList.get(i).size(); j++){
                if(edgeMin!=0 && random.nextInt(100)<negativeFreq){
                    adjList.get(i).get(j).setWeight(-1*random.nextInt(1-edgeMin));
                }
                else adjList.get(i).get(j).setWeight(random.nextInt(edgeMax+1)+1);
            }
        }
        return adjList;
    }
    //Перегрузка метода для генерации графов только с положительными весами рёбер
    public ArrayList<ArrayList<Node>> generateGraph(int vertexCount, double densityPercent, int edgeMax){
        return generateGraph(vertexCount, densityPercent, edgeMax,0,0);
    }
    //Генерация плотного/разряженного графа лежащего на координатной плоскости
    public ArrayList<ArrayList<Node>> generatePlaneGraph(int vertexCount, double densityPercent, HashMap<Integer,Point> vertexCoords){
        ArrayList<ArrayList<Node>> adjList = generateConnectedDirectedWeightedGraph(vertexCount,densityPercent);

        Random random = new Random();
        for(int i = 0; i<vertexCount; i++){
            //Присвоение случайной уникальной точки для каждой вершины
            Point p = new Point(random.nextInt(3*vertexCount), random.nextInt(3*vertexCount));
            while(vertexCoords.containsValue(p))
                p = new Point(random.nextInt(3*vertexCount), random.nextInt(3*vertexCount));
            vertexCoords.put(i,p);
        }
        //Присвоение весам рёбер значение высчитанных при помощи теоремы пифагора
        for(int i = 0; i<adjList.size(); i++) {
            for (int j = 0; j < adjList.get(i).size(); j++) {
                adjList.get(i).get(j).setWeight(Point.calculateDistance(vertexCoords.get(i),vertexCoords.get(adjList.get(i).get(j).getVertex())));
            }
        }
        return adjList;
    }
}
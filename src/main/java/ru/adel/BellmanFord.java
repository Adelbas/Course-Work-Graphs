package ru.adel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BellmanFord {
    private static int[] parent;
    //Вычисление кратчайшего пути при входных данных в виде списка рёбер
    public static double[] getShortestPathWeightEdgesToAll(ArrayList<Edge> edges, int V, int source) {

        //Создадим массив расстояний, для начальной вершины зададим равным 0
        double[] distance = new double[V];
        Arrays.fill(distance, Double.MAX_VALUE);
        distance[source] = 0;
        //Массив для хранения предков вершин в кратчайшем пути
        parent = new int[V];

        //Проходим по всем рёбрам v-1 раз
        for (int i = 0; i < V - 1; i++) {
            for (Edge e : edges) {
                int u = e.getSource();
                int v = e.getTarget();
                double weight = e.getWeight();
                //Если существует путь до вершины u и путь до вершины v можно укоротить за счёт ребра (u, v)
                if (distance[u] != Double.MAX_VALUE && distance[u] + weight < distance[v]) {
                    // обновить расстояние до вершины v
                    distance[v] = distance[u] + weight;
                    // установить родителя v как `u`
                    parent[v] = u;
                }
            }
        }
        //Проверка наличия отрицательного цикла
        for (Edge e : edges) {
            int u = e.getSource();
            int v = e.getTarget();
            double weight = e.getWeight();
            //Если после v-1 итераций можно уменьшить расстояние до какой-либо вершины, то существует отрицательный цикл
            if (distance[u] != Double.MAX_VALUE && distance[u] + weight < distance[v]) {
                System.out.println("Negative cycle was found!");
                Arrays.fill(distance,Double.MAX_VALUE);
                return distance;
            }
        }
        return distance;
    }

    //Перегрузка метода для возвращения длины пути до конкретной вершины
    public static double getShortestPathWeightEdges(ArrayList<Edge> edges, int V, int source, int target){
        double[]distances = getShortestPathWeightEdgesToAll(edges, V, source);
        return distances[target];
    }

    //Вычисление кратчайшего пути при входных данных в виде списка смежности
    public static double getShortestPathWeightAdj(ArrayList<ArrayList<Node>> adjList, int source, int target) {

        int V = adjList.size();

        //Создадим массив расстояний, для начальной вершины зададим равным 0
        double[] distance = new double[V];
        Arrays.fill(distance, Double.MAX_VALUE);
        distance[source] = 0;
        //Массив для хранения предков вершин в кратчайшем пути
        parent = new int[V];
        //Проходим по всем рёбрам v-1 раз
        for (int i = 0; i < V - 1; i++) {
            for (int u = 0; u < adjList.size(); u++) {
                for (int j = 0; j < adjList.get(u).size(); j++) {
                    int v = adjList.get(u).get(j).getVertex();
                    double weight = adjList.get(u).get(j).getWeight();
                    //Если существует путь до вершины u и путь до вершины v можно укоротить за счёт ребра (u, v)
                    if (distance[u] != Double.MAX_VALUE && distance[u] + weight < distance[v]) {
                        // обновить расстояние до вершины v
                        distance[v] = distance[u] + weight;
                        // установить родителя v как `u`
                        parent[v] = u;
                    }
                }
            }
        }
        //Проверка наличия отрицательного цикла
        for (int u = 0; u < adjList.size(); u++) {
            for (int j = 0; j < adjList.get(u).size(); j++) {
                int v = adjList.get(u).get(j).getVertex();
                double weight = adjList.get(u).get(j).getWeight();
                //Если после v-1 итераций можно уменьшить расстояние до какой-либо вершины, то существует отрицательный цикл
                if (distance[u] != Double.MAX_VALUE && distance[u] + weight < distance[v]) {
                    System.out.println("Negative cycle was found!");
                    return Double.MAX_VALUE;
                }
            }
        }
        return distance[target];
    }

    //Функция для возвращения самого пути в виде списка вершин
    public static List<Integer> getPath(ArrayList<Edge> edges, int V, int source, int target) {
        if (parent == null)
            getShortestPathWeightEdges(edges, V, source, target);
        List<Integer> path = new ArrayList<>();
        path.add(target);
        while (target != source) {
            target = parent[target];
            path.add(target);
        }
        Collections.reverse(path);
        return path;
    }
}

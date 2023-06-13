package ru.adel;

import java.util.*;
import java.util.function.Function;

public class AStar {
    private static int[]parent;
    public static double getShortestPathWeight(ArrayList<ArrayList<Node>> adjList, int source, int target,
                                               HashMap<Integer,Point>vertexCoords, boolean isConstantHeuristic){
        int V = adjList.size();
        //Создадим массив расстояний, для начальной вершины зададим равным 0
        double[] distance = new double[V];
        Arrays.fill(distance, Double.MAX_VALUE);
        distance[source] = 0;
        //Массив для хранения предков вершин в кратчайшем пути
        parent = new int[V];
        Arrays.fill(parent, -1);
        //Создадим приоритетную очередь, в которой приоритет определяется по формуле f(x) = g(x) + h(x)
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingDouble(v->v.getWeight()
                +heuristic(vertexCoords.get(v.getVertex()),vertexCoords.get(target), isConstantHeuristic)));
        //Добавим начальную вершину
        pq.add(new Node(source, 0));

        while (!pq.isEmpty()) {
            //извлекаем узел с минимальным приоритетом
            Node current = pq.poll();
            //если извлеченный узел является вершиной назначения алгоритм заканчивает работу
            if(current.getVertex()==target)
                break;
            //проверка фиктивности узла
            if(current.getWeight()>distance[current.getVertex()])
                continue;
            //Проходим по всем смежным вершинам
            for (Node n : adjList.get(current.getVertex())) {
                if (distance[current.getVertex()] + n.getWeight() < distance[n.getVertex()]) {
                    distance[n.getVertex()] = n.getWeight() + distance[current.getVertex()];
                    //обновляем значение предыдущей вершины
                    parent[n.getVertex()]=current.getVertex();
                    pq.add(new Node(n.getVertex(), distance[n.getVertex()]));
                }
            }
        }

        return distance[target];
    }

    //Вычисление эвристики
    private static double heuristic(Point vertex, Point target, boolean isConstantHeuristic) {
        //Эвристика - константа
        if(isConstantHeuristic)
            return 1;
        //Эвристика - евклидово расстояние до конечной вершины
        else return Point.calculateDistance(vertex,target);
    }
    //Функция для возвращения самого пути в виде списка вершин
    public static List<Integer> getPath(ArrayList<ArrayList<Node>> adjList, int source,int target,
                                        HashMap<Integer,Point> vertexes, boolean isConstantHeuristic){
        if(parent==null)
            getShortestPathWeight(adjList,source,target,vertexes,isConstantHeuristic);
        List<Integer> path = new ArrayList<>();
        path.add(target);
        while(target!=source){
            target=parent[target];
            path.add(target);
        }
        Collections.reverse(path);
        return path;
    }
}


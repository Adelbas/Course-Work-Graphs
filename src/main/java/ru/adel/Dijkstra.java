package ru.adel;

import java.util.*;

public class Dijkstra {
    private static int[]parent;
    public static double getShortestPathWeight(ArrayList<ArrayList<Node>> adjList, int source, int target){
        int V = adjList.size();
        //Создадим массив расстояний, для начальной вершины зададим равным 0
        double[] distance = new double[V];
        Arrays.fill(distance, Double.MAX_VALUE);
        distance[source] = 0;
        //Массив для хранения предков вершин в кратчайшем пути
        parent = new int[V];
        Arrays.fill(parent, -1);
        //Создадим приоритетную очередь и добавь в неё узел с начальной вершиной
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingDouble(Node::getWeight));
        pq.add(new Node(source, 0));

        while (!pq.isEmpty()) {
            //извлекаем узел с минимальным расстоянием до начальной вершины
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

    public static double[] getShortestPathWeightToAllVertexes(ArrayList<ArrayList<Node>> adjList, int source){
        int V = adjList.size();
        //Создадим массив расстояний, для начальной вершины зададим равным 0
        double[] distance = new double[V];
        Arrays.fill(distance, Double.MAX_VALUE);
        distance[source] = 0;
        //Массив для хранения предков вершин в кратчайшем пути
        parent = new int[V];
        Arrays.fill(parent, -1);
        //Создадим приоритетную очередь и добавь в неё узел с начальной вершиной
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingDouble(Node::getWeight));
        pq.add(new Node(source, 0));

        while (!pq.isEmpty()) {
            //извлекаем узел с минимальным расстоянием до начальной вершины
            Node current = pq.poll();
            //если извлеченный узел является вершиной назначения алгоритм заканчивает работу
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

        return distance;
    }

    //Функция для возвращения самого пути в виде списка вершин
    public static List<Integer> getPath(ArrayList<ArrayList<Node>> adjList, int source,int target){
        if(parent==null)
            getShortestPathWeight(adjList,source,target);
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

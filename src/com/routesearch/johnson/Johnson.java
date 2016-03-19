package com.routesearch.johnson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.print.attribute.standard.RequestingUserName;

import com.routesearch.graph.Graph;
import com.routesearch.graph.GraphEdge;
import com.routesearch.graph.GraphNode;
import com.routesearch.heap.Heap;

/**
 * 
 * @author moqiguzhu
 * @date 2015-08-18
 * @version 1.0
 */

public class Johnson {
  /* 图结构 */
  private Graph graph;

  /* 到源节点的初始距离 */
  private double MAX;

  /* 所有节点对之间的最短距离 */
  private Map<GraphNode, Map<GraphNode, Double>> node_node_dist;

  /* 所有节点对之间的最短路径 */
  private Map<GraphNode, Map<GraphNode, List<GraphNode>>> node_node_path;

  /* 所有节点对最短路径中的最短路径的距离 */
  private double minDist;

  private int addedNodeLabel = -1;
  
  private Map<GraphEdge, GraphEdge> t_edge_edge;

  public Johnson(Graph graph) {
    // direct use graph passed in
    this.graph = graph;

    this.MAX = Double.MAX_VALUE;
    this.node_node_dist = new HashMap<>();
    this.node_node_path = new HashMap<>();
    this.minDist = MAX;
    this.t_edge_edge = new HashMap<>();
  }

  /**
   * Johnson算法
   * 
   * @return
   */
  public boolean solveAPSP() {
    // 目前阶段是不存在negative cycle 但是在日后不保证不会出现
    Map<GraphNode, Double> shortestDists = new HashMap<>();
    if (!solveBellmanFord(shortestDists)) {
      return false;
    }
//    System.out.println(shortestDists);
    for (GraphEdge edge : graph.getEdge_edge().keySet()) {
      GraphNode left = edge.getLeftNode();
      GraphNode right = edge.getRightNode();

      double weight1 = shortestDists.get(left) - shortestDists.get(right) + edge.getWeight();
      GraphEdge t_edge = new GraphEdge(left, right, weight1);

      t_edge_edge.put(t_edge, t_edge);
    }

    Set<GraphNode> nodes = graph.getNodes();

    List<Double> keys = new ArrayList<Double>();
    for (int i = 0; i < nodes.size(); i++) {
      keys.add(MAX);
    }

    // !!!
    for (GraphNode node : nodes) {
      if (node.isCritical()) { 
        Heap<GraphNode, Double> t_heap =
            new Heap<GraphNode, Double>(new ArrayList<GraphNode>(nodes), keys);
        t_heap.setKey(node, .0);

        Map<GraphNode, Double> dists = new HashMap<>();
        for (GraphNode tnode : nodes) {
          if (tnode.equals(node)) {
            dists.put(tnode, .0);
          } else {
            dists.put(tnode, MAX);
          }
        }

        Map<GraphNode, List<GraphNode>> path = new HashMap<>();

        solveDijkstra(node, t_heap, graph.getNode_neighborNodes(), t_edge_edge, dists, path);
        node_node_dist.put(node, dists);
        node_node_path.put(node, path);
      }
    }

    for (GraphNode node : node_node_dist.keySet()) {
      for (GraphNode tnode : node_node_dist.get(node).keySet()) {
        double weight = node_node_dist.get(node).get(tnode) - shortestDists.get(node)
            + shortestDists.get(tnode);
        node_node_dist.get(node).put(tnode, weight);
        minDist = Math.min(weight, minDist);
      }
    }

    return true;
  }

  /**
   * Dijkstra算法
   * 
   * @param heap
   * @param node_neighborNodes
   * @param edge_edge
   * @param shortestDists
   * @return
   */
  // 是不是知道关键节点之间的最短路径之后就可以及时退出
  // source到source的最短路径只有一个节点 就是source本身
  // source到某个节点之间要是不可达，路径为空
  public Map<GraphNode, Double> solveDijkstra(GraphNode source, Heap<GraphNode, Double> heap,
      Map<GraphNode, List<GraphNode>> node_neighborNodes, Map<GraphEdge, GraphEdge> edge_edge,
      Map<GraphNode, Double> dists, Map<GraphNode, List<GraphNode>> path) {

    if(node_neighborNodes.get(source) == null) {
      return dists;
    }
    Map<GraphNode, GraphNode> node_parent = new HashMap<>();

    while (heap.size() > 0) {
      List<Object> element_key = heap.poll();
      GraphNode node = (GraphNode) element_key.get(0);
      Double dist = (Double) element_key.get(1);

      List<GraphNode> t_list = new ArrayList<>();
      if (node_parent.get(node) != null) {
        t_list.addAll(path.get(node_parent.get(node)));
      } else {
        // do nothing
      }
      
      // 不可达
      if(dist != MAX) {
        t_list.add(node);
      }
      path.put(node, t_list);
      
      // 只有入度的节点作为source节点
      if(node_neighborNodes.get(node) == null) {
        continue;
      }
      for (GraphNode tnode : node_neighborNodes.get(node)) {
        GraphEdge tedge = new GraphEdge(node, tnode, 0);
        double oldDist = dists.get(tnode);
        double newDist = dists.get(node) + edge_edge.get(tedge).getWeight();
        
        if (newDist < oldDist) {
          heap.setKey(tnode, newDist);
          dists.put(tnode, newDist);
          node_parent.put(tnode, node);
        }
      }
    }

    return dists;
  }

  /**
   * Bellman-Ford算法
   * 
   * @param shortestDists
   * @return
   */
  public boolean solveBellmanFord(Map<GraphNode, Double> shortestDists) {
    // System.out.println(graph.getNodes());
    
    // including added node
    int numNodes = graph.getNodes().size() + 1;
    ArrayList<Map<Integer, Double>> numNodes_node = new ArrayList<>();

    // 初始化
    numNodes_node.add(new HashMap<>());
    numNodes_node.add(new HashMap<>());
    numNodes_node.get(0 % 2).put(addedNodeLabel, .0);
    numNodes_node.get(1 % 2).put(addedNodeLabel, .0);
    for (GraphNode node : graph.getNodes()) {
      numNodes_node.get(0 % 2).put(node.getLabel(), MAX);
    }

    Set<GraphNode> t_nodes = new HashSet<>(graph.getNodes());
    t_nodes.add(new GraphNode(addedNodeLabel));

    // Bellman-Ford算法
    for (int i = 1; i < numNodes + 1; i++) {
      for (GraphNode node : t_nodes) {
        double temp = numNodes_node.get((i - 1) % 2).get(node.getLabel());

        if (graph.getNeighborNodes_node().get(node) != null) {
          for (GraphNode nd : graph.getNeighborNodes_node().get(node)) {
            GraphEdge edge = new GraphEdge(nd, node, 0);
            double ttemp = numNodes_node.get((i - 1) % 2).get(nd.getLabel())
                + graph.getEdge_edge().get(edge).getWeight();
            temp = Math.min(ttemp, temp);
          }
          
        }
        // 任何节点到新增加的节点的距离是0
        temp = Math.min(temp, 0 + 0);
        numNodes_node.get(i % 2).put(node.getLabel(), temp);
      }
    }

    // 判断是不是有negative cycle
    for (GraphNode node : t_nodes) {
      double x1 = numNodes_node.get((numNodes - 1) % 2).get(node.getLabel());
      double x2 = numNodes_node.get((numNodes) % 2).get(node.getLabel()); 
      
      // ??? 把x1 x2 用上面的赋值表达式替代出错
      if (x1 != x2) {
        System.out.println("图中存在negative cycle!!!");
        return false;
      }
    }

    // 所有节点到源节点的最短路径
    for (GraphNode node : t_nodes) {
      shortestDists.put(node, numNodes_node.get((numNodes) % 2).get(node.getLabel()));
    }

    return true;
  }

  /**
   * 
   * @return 所有节点对之间的最短距离
   */
  public double getMinDist() {
    return minDist;
  }

  public Map<GraphNode, Map<GraphNode, List<GraphNode>>> getNode_node_path() {
    return this.node_node_path;
  };
  
  public Map<GraphNode, Map<GraphNode, Double>> getNode_node_dist() {
    return this.node_node_dist;
  }

  // test this class
  public static void main(String[] args) {

  }
}

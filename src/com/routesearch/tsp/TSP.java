package com.routesearch.tsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.routesearch.graph.Graph;
import com.routesearch.graph.GraphEdge;
import com.routesearch.graph.GraphNode;


/**
 * 使用了Nearest Neighbor启发式策略，2-opt-move优化策略，3-opt-move优化策略。
 * 注意这个算法并不能保证最后得到的结果是最优结果，特别是在节点变多的时候，算法的精度也随之下降。
 * 
 * @author moqiguzhu
 * @version 1.0
 * @date 2015-04-25
 */
// 这个方法好鸡肋  应该没什么用
public class TSP {
  private Graph graph;
  private Map<GraphNode, Map<GraphNode, Double>> node_node_dist;
  private Map<GraphNode, Map<GraphNode, List<GraphNode>>> node_node_path;
  private GraphNode source, dest;
  
  public TSP(Graph graph,GraphNode source, GraphNode dest) {
    this.graph = graph;
    this.node_node_dist = graph.getNode_node_dist();
    this.node_node_path = graph.getNode_node_path();
    this.source = source;
    this.dest = dest;
  }

  /**
   * 
   * @param rand 从哪个节点开始使用最近邻策略，图有多少个节点，就可以有多少个不同rand
   * @return
   */
  public OpenTour nearestNeighbor(GraphNode node) {
    
    List<GraphEdge> tourEdges = new ArrayList<>();
    double cost = 0;
    
    // 所有已经访问过的节点
    Set<GraphNode> visited = new HashSet<GraphNode>();
    
    // 所有还没有被访问过的节点
    Set<GraphNode> criticalUnvisited = new HashSet<>(node_node_dist.keySet());
    
    // source到第一个关键节点
    visited.add(source);
    criticalUnvisited.remove(source);
    tourEdges.add(new GraphEdge(source, node, node_node_dist.get(source).get(node)));
    cost += node_node_dist.get(source).get(node);
    for(GraphNode tnode : node_node_path.get(source).get(node)) {
      visited.add(tnode);
      if(criticalUnvisited.contains(tnode)) {
        criticalUnvisited.remove(tnode);
      }
    }
  
    // 使用策略的地方 先按最短路径来   
    GraphNode curNode = node, nextNode = null;
    while (!criticalUnvisited.isEmpty()) {
      Double curMin = Double.MAX_VALUE;

      for (GraphNode tnode : criticalUnvisited) {
        if (node_node_dist.get(curNode).get(tnode) < curMin) {
          boolean flag = true;
          List<GraphNode> tlist = node_node_path.get(curNode).get(tnode);
          for(int i = 1; i < tlist.size(); i++) {
            if (visited.contains(tlist.get(i))) {
              flag = false;
              break;
            }
          }
          // 不会形成环
          if (flag) {
            curMin = node_node_dist.get(curNode).get(tnode);
            nextNode = tnode;
          }
        }
      }
      // 此路不通
      if (curMin == Double.MAX_VALUE) {
        return null;
      } else {        
        //这个里面加入的节点也有可能是critical node
        for(GraphNode tnode : node_node_path.get(curNode).get(nextNode)) {
          visited.add(tnode);
          if(criticalUnvisited.contains(tnode)) {
            criticalUnvisited.remove(tnode);
          }
        }
        GraphEdge edge = new GraphEdge(curNode, nextNode, curMin);
        tourEdges.add(edge);
        cost += curMin;

        curNode = nextNode;
      }
    }
    
    // 到dest 也是最后一条边
    // 不一定要走最短路径
    List<GraphNode> tlist = node_node_path.get(nextNode).get(dest);
    for(int i = 1; i < tlist.size(); i++) {
      if (visited.contains(tlist.get(i))) {
        return null; // 此路不通
      }
    }
    
    GraphEdge edge = new GraphEdge(nextNode, dest, node_node_dist.get(nextNode).get(dest));
    tourEdges.add(edge);
    cost += edge.getWeight();

    OpenTour tour = new OpenTour(tourEdges, cost, source, dest, visited);

    return tour;
  }
  
  public Map<String, Double> getValidPath() {
    Map<String, Double> path_dist = new HashMap<>();
    for(GraphNode node : node_node_dist.keySet()) {
      if(!node.equals(source)) {
        OpenTour te = nearestNeighbor(node);
        StringBuilder sb = new StringBuilder();
        if(te == null) {
          continue;
        }
        GraphNode[] orders = te.getOrder();
         
        sb.append(source.getLabel() + ",");
        for(int i = 0; i < orders.length - 1; i++) {
          List<GraphNode> tlist = node_node_path.get(orders[i]).get(orders[i+1]);
          for(int j = 1; j < tlist.size(); j++) {
            sb.append(tlist.get(j) + ",");
          }
        }
        sb.delete(sb.length()-1, sb.length());
        path_dist.put(sb.toString(), te.getCost());
      }
    }
    
    return path_dist;
  }

  /**
   * 此算法的时间复杂度是O(n^3)
   * 
   * @param t Hanmilton圈
   * @return 经过两步最优策略之后，Hanmilton圈的边权值之和
   * 
   */
  public double two_opt_move(OpenTour t) {
    GraphNode source = t.getSource(), dest = t.getDest();
    int numNodes = t.getOrder().length;
    // 存储城市的访问顺序
    GraphNode[] order = t.getOrder();
    Set<GraphNode> visited = t.getVisited();
    
    double finalCost = t.getCost();
    double minchange;
    do {
      minchange = 0;
      int mini = -1, minj = -1;
      int i, j;
      for (i = 0; i < numNodes - 3; i++) {
        for (j = i + 2; j < numNodes - 1; j++) {
          if( (source.equals(order[i]) && dest.equals(order[i+1])) || 
              (source.equals(order[j]) && dest.equals(order[j+1])) ) {
            continue;
          }
          double change = node_node_dist.get(order[i]).get(order[j])
              + node_node_dist.get(order[i + 1]).get(order[j + 1])
              - node_node_dist.get(order[i]).get(order[i + 1]) - 
              node_node_dist.get(order[j]).get(order[j + 1]);
          if (minchange > change) {
            // 检查合法性
            // 没法检查 试想一下如果两个关键节点之间的最短路径需要通过另外一个关键节点
            minchange = change;
            mini = i;
            minj = j;
          }
        }
      }

      // apply mini/minj move
      GraphNode[] new_order = new GraphNode[numNodes];
      int index, new_index = 0;
      for (int p = 0; p <= mini; p++) {
        index = p;
        new_order[new_index + p] = order[index];
      }
      new_index = new_index + mini;
      for (int p = 1; p <= minj - mini; p++) {
        index = minj - p + 1;
        new_order[new_index + p] = order[index];
      }
      new_index = new_index + minj - mini;
      for (int p = 1; p < numNodes - minj; p++) {
        index = minj + p;
        new_order[new_index + p] = order[index];
      }
      order = new_order;

      // System.out.println(Arrays.toString(order));

      finalCost += minchange;
      t.setOrder(order);
      t.setCost(finalCost);
    } while (minchange < 0);

    return finalCost;
  }
  
  // 暂时没有考虑3-pot move

  // test this class
  public static void main(String[] args) {
    
  }
}

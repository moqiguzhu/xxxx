package com.routesearch.tsp;

import java.util.List;
import java.util.Set;

import com.routesearch.graph.GraphEdge;
import com.routesearch.graph.GraphNode;

/**
 * @author moqiguzhu
 * @version 1.0
 * @date 2015-04-25
 * 
 *       description: Hanmilton圈类，在解决TSP问题时定义的类。TSP问题的实质就是要找到最小的Hanmilton圈
 */
public class OpenTour {
  /* Hanmilton圈的所有边权值之和 */
  private double cost;

  /* 存储结点的访问顺序 */
  private GraphNode[] order;

  private GraphNode source;
  private GraphNode dest;
  private Set<GraphNode> visited;

  public OpenTour(List<GraphEdge> edges, double cost, GraphNode source, GraphNode dest,
      Set<GraphNode> visited) {
    this.cost = cost;
    this.source = source;
    this.dest = dest;
    this.visited = visited;

    // 初始化order
    order = new GraphNode[edges.size() + 1];
    order[0] = source;
    for (int i = 0; i < edges.size(); i++) {
      order[i+1] = edges.get(i).getRightNode();
    }
    assert(order[edges.size()] == dest);
  }

  public double getCost() {
    return cost;
  }

  public void setCost(double cost) {
    this.cost = cost;
  }

  public GraphNode[] getOrder() {
    return order;
  }

  public void setOrder(GraphNode[] order) {
    this.order = order;
  }

  public GraphNode getSource() {
    return source;
  }

  public GraphNode getDest() {
    return dest;
  }

  public Set<GraphNode> getVisited() {
    return visited;
  }

  
}


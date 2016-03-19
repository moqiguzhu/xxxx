package com.routesearch.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.PRIVATE_MEMBER;

/**
 * 
 * @author Andy
 * @date 2016-03-06
 * @version 1.0
 */

// 记录图中所有的信息
public class Graph {
  /* 存储结点及其相邻的节点信息(出度信息) */
  private Map<GraphNode, List<GraphNode>> node_neighborNodes = new HashMap<>();

  /* 存储节点及其相邻的节点信息(入度信息) */
  private Map<GraphNode, List<GraphNode>> neighborNodes_node = new HashMap<>();

  /* 存储图的边权值信息 */
  private Map<GraphEdge, GraphEdge> edge_edge = new HashMap<>();

  /* 存储所有的节点 */
  private Set<GraphNode> nodes = new HashSet<>();

  /* 节点的数目 */
  private int numNodes;
  
  private Map<GraphNode, Map<GraphNode, Double>> node_node_dist;
  private Map<GraphNode, Map<GraphNode, List<GraphNode>>> node_node_path;

  public Graph(Map<GraphNode, List<GraphNode>> node_neighborNodes, 
      Map<GraphNode, List<GraphNode>> neighborNodes_node,
      Map<GraphEdge, GraphEdge> edge_edge, Set<GraphNode> nodes) {
    this.node_neighborNodes = node_neighborNodes;
    this.neighborNodes_node = neighborNodes_node;
    this.edge_edge = edge_edge;
    this.nodes = nodes;
    this.numNodes = nodes.size();
  }

  public Map<GraphNode, List<GraphNode>> getNode_neighborNodes() {
    return node_neighborNodes;
  }

  public void setNode_neighborNodes(Map<GraphNode, List<GraphNode>> node_neighborNodes) {
    this.node_neighborNodes = node_neighborNodes;
  }

  public Map<GraphNode, List<GraphNode>> getNeighborNodes_node() {
    return neighborNodes_node;
  }

  public void setNeighborNodes_node(Map<GraphNode, List<GraphNode>> neighborNodes_node) {
    this.neighborNodes_node = neighborNodes_node;
  }

  public Map<GraphEdge, GraphEdge> getEdge_edge() {
    return edge_edge;
  }

  public void setEdge_edge(Map<GraphEdge, GraphEdge> edge_edge) {
    this.edge_edge = edge_edge;
  }

  public Set<GraphNode> getNodes() {
    return nodes;
  }

  public void setNodes(Set<GraphNode> nodes) {
    this.nodes = nodes;
  }

  public int getNumNodes() {
    return numNodes;
  }

  public void setNumNodes(int numNodes) {
    this.numNodes = numNodes;
  }
  
  public Map<GraphNode, Map<GraphNode, Double>> getNode_node_dist() {
    return node_node_dist;
  }

  public void setNode_node_dist(Map<GraphNode, Map<GraphNode, Double>> node_node_dist) {
    this.node_node_dist = node_node_dist;
  }

  public Map<GraphNode, Map<GraphNode, List<GraphNode>>> getNode_node_path() {
    return node_node_path;
  }

  public void setNode_node_path(Map<GraphNode, Map<GraphNode, List<GraphNode>>> node_node_path) {
    this.node_node_path = node_node_path;
  }

  @Override
  public String toString() {
	  StringBuilder sb = new StringBuilder();
	  sb.append("图信息\n");
	  sb.append("出度信息： " + this.node_neighborNodes.toString() + "\n");
	  sb.append("入度信息： " + this.neighborNodes_node.toString() + "\n");
	  sb.append("边信息： " + this.edge_edge.keySet() + "\n");
	  
	  return sb.toString();
  }
}

package com.routesearch.graph;

public class GraphEdge {
  /* 边标号 注意边不是按照label来确定唯一性的*/
  private int label;
  /* 边左边的节点 */
  private GraphNode leftNode;
  /* 边右边的节点 */
  private GraphNode rightNode;
  /* 边权值 */
  private double weight;
  /* 边可以被经过的次数*/
  private int canBeUsed;

  public GraphEdge(GraphNode leftNode, GraphNode rightNode, double weight) {
    this.leftNode = leftNode;
    this.rightNode = rightNode;
    this.weight = weight;
  }
  
  public GraphEdge(int label, GraphNode leftNode, GraphNode rightNode, double weight) {
    this.label = label;
    this.leftNode = leftNode;
    this.rightNode = rightNode;
    this.weight = weight;
  }
  
  public GraphEdge edgeCopy(GraphEdge edge) {
    // 目前GraphNode还是一个值类型 可以这样写
    return new GraphEdge(edge.label, edge.leftNode, edge.rightNode, edge.weight);
  }

  public GraphNode getLeftNode() {
    return leftNode;
  }

  public GraphNode getRightNode() {
    return rightNode;
  }

  public double getWeight() {
    return weight;
  }
  
  

  @Override
  public boolean equals(Object o) {
    GraphEdge edge = (GraphEdge) o;
    return (edge.getLeftNode().equals(this.leftNode) && edge.getRightNode().equals(this.rightNode));
  }

  @Override
  public int hashCode() {
    return this.leftNode.hashCode() * this.rightNode.hashCode() % Integer.MAX_VALUE;
  }

  @Override
  public String toString() {
    return "left:" + leftNode.getLabel() + " " + "right:" + rightNode.getLabel() + " " + "weight:"
        + weight;
  }
}

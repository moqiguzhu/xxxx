package com.routesearch.graph;

/**
 * @author moqiguzhu
 * @date 2015-07-23
 * @version 1.0
 */

/*
 * 节点类，节点标识符一旦确定，不允许改变 如果后续给节点加入更多的属性，则添加对应的方法
 */
public class GraphNode implements Comparable<GraphNode> {
  /* 节点标识符 */
  private int label;
//  /* 节点的颜色，在计算图的强连通分量的时候用到 */
//  private char color;
//  /* 节点第一次被访问到的时间，在计算图的强连通分量的时候用到 */
//  private long start;
//  /* 此节点的所有相邻节点都被访问完成的时间，在计算图的强连通分量时用到 */
//  private long end;
  /* 节点是否属于关键节点 */
  private boolean isCritical = false;
  
  /* 节点可以被使用的次数*/
  private int canBeUsed;

  public GraphNode(int label) {
    this.label = label;
  }

  public GraphNode(int label, boolean isCritical) {
    this.label = label;
    this.isCritical = isCritical;
  }

  public GraphNode(int label, char color, long start, long end) {
    this.label = label;
//    this.color = color;
//    this.start = start;
//    this.end = end;
  }

  public int getLabel() {
    return this.label;
  }

//  public char getColor() {
//    return color;
//  }
//
//  public void setColor(char color) {
//    this.color = color;
//  }
//
//  public long getStart() {
//    return start;
//  }
//
//  public void setStart(long start) {
//    this.start = start;
//  }
//
//  public long getEnd() {
//    return end;
//  }
//
//  public void setEnd(long end) {
//    this.end = end;
//  }

  public String print() {
//    return "" + label + " " + color + " " + start + " " + end;
    return "" + label;
  }

  public boolean isCritical() {
    return isCritical;
  }

  public void setCritical(boolean isCritical) {
    this.isCritical = isCritical;
  }

  @Override
  public String toString() {
    return "" + label;
  }

  @Override
  public boolean equals(Object o) {
    GraphNode node = (GraphNode) o;
    return node.getLabel() == label;
  }

  @Override
  public int hashCode() {
    return label;
  }

  @Override
  public int compareTo(GraphNode o) {
    // 跟DFS的写法有关
    return this.isCritical ? -1 : 1;
  }
}

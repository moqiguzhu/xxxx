package com.filetool.util;

import java.util.List;
import java.util.Map;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import com.routesearch.graph.GraphNode;

public class PrintHelper {
  private static final String delimiter = "**************************************\n";
  private static final double MAX = Double.MAX_VALUE;

  public static void print_node_node_dist(Map<GraphNode, Map<GraphNode, Double>> node_node_dist) {
    int count = 0;
    StringBuilder sb = new StringBuilder();
    sb.append(delimiter);
    sb.append("关键节点到其他节点之间的最短距离：\n");
    for (GraphNode node1 : node_node_dist.keySet()) {
      for (GraphNode node2 : node_node_dist.get(node1).keySet()) {
        String t;
        if (node_node_dist.get(node1).get(node2) == MAX) {
          t = "MAX";
        } else {
          t = "" + node_node_dist.get(node1).get(node2);
        }
        sb.append(node1.getLabel() + " --> " + node2.getLabel() + ": " + t);
        sb.append("\t");
        if (++count % 5 == 0) {
          sb.append("\n");
        }
      }
    }
    sb.append("\n");
    sb.append(delimiter);
    System.out.println(sb.toString());
  }

  public static void print_node_node_path(
      Map<GraphNode, Map<GraphNode, List<GraphNode>>> node_node_path) {
    StringBuilder sb = new StringBuilder();
    sb.append(delimiter);
    sb.append("关键节点到其他节点之间的最短路径：\n");
    for (GraphNode node1 : node_node_path.keySet()) {
      for (GraphNode node2 : node_node_path.get(node1).keySet()) {
        sb.append(node1.getLabel() + " --> " + node2.getLabel() + ": ");
        for (GraphNode node : node_node_path.get(node1).get(node2)) {
          sb.append(node.getLabel() + " ");
        }
        sb.append("\n");
      }
    }
    sb.append(delimiter);
    System.out.println(sb.toString());
  }
}

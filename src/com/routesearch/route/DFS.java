package com.routesearch.route;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.routesearch.graph.Graph;
import com.routesearch.graph.GraphNode;

public class DFS {
  private Graph graph;
  private GraphNode source;
  private GraphNode dest;
  private List<GraphNode> mustPass;
  private Set<GraphNode> mustPassSet;
  private boolean foundPathFlag;
  private List<GraphNode> oneValidPath;

  public DFS(Graph graph, GraphNode source, GraphNode dest, List<GraphNode> mustPass) {
    this.graph = graph;
    this.source = source;
    this.dest = dest;
    this.mustPass = mustPass;
    this.mustPassSet = new HashSet<>(mustPass);
    this.foundPathFlag = false;
  }

  public List<GraphNode> findOneValidPath() {
    List<GraphNode> path = new ArrayList<>();

    Set<GraphNode> visited = new HashSet<>();
    visited.add(source);

    Set<GraphNode> alreadyPassSet = new HashSet<>();

    DFSHelp(path, source, visited, alreadyPassSet);

    return oneValidPath;
  }

  public void DFSHelp(List<GraphNode> path, GraphNode cur, Set<GraphNode> visited,
      Set<GraphNode> alreadyPassSet) {
    if(foundPathFlag) return;
    if(graph.getNode_neighborNodes().get(cur) != null) {
      for (GraphNode node : graph.getNode_neighborNodes().get(cur)) {
        if (!visited.contains(node)) {
          if(mustPassSet.size() == alreadyPassSet.size() && node.equals(dest)) {
            foundPathFlag = true;
            oneValidPath = new ArrayList<>(path);
            oneValidPath.add(cur);
            oneValidPath.add(dest);
          }
          path.add(cur);
          visited.add(node);
          if(mustPassSet.contains(node)) {
            alreadyPassSet.add(node);
          }

          DFSHelp(path, node, visited, alreadyPassSet);

          path.remove(path.size() - 1);
          visited.remove(node);
          if(mustPassSet.contains(node)) {
            alreadyPassSet.remove(node);
          }
        }
      }
    }

  }
}

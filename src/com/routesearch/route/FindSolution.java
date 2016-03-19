package com.routesearch.route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.Source;

import com.filetool.util.PrintHelper;
import com.routesearch.graph.Graph;
import com.routesearch.graph.GraphEdge;
import com.routesearch.graph.GraphNode;
import com.routesearch.johnson.Johnson;
import com.routesearch.tsp.TSP;

public class FindSolution {
	public Graph createGraph(String graphContent) {
	    Map<GraphNode, List<GraphNode>> node_neighborNodes = new HashMap<>();
	    Map<GraphNode, List<GraphNode>> neighborNodes_node = new HashMap<>();
	    Map<GraphEdge, GraphEdge> edge_edge = new HashMap<>();
	    Set<GraphNode> nodes = new HashSet<>();
	    
	    String regex1 = "\n";
	    String regex2 = "\\,";
	    String[] lines = graphContent.split(regex1);
	    
	    String[] elements;
	    for(String line : lines) {
	      elements = line.split(regex2);
	      
	      int edgeLabel = Integer.valueOf(elements[0]);
	      GraphNode left = new GraphNode(Integer.valueOf(elements[1]));
	      GraphNode right = new GraphNode(Integer.valueOf(elements[2]));
	      double weight = Double.valueOf(elements[3]);
	      
	      GraphEdge edge = new GraphEdge(edgeLabel, left, right, weight);
	      
	      // 两个顶点之间可能存在多条边，只保留权重最小的那条
	      if(edge_edge.containsKey(edge)) {
	        if(edge_edge.get(edge).getWeight() > edge.getWeight()) {
	          edge_edge.put(edge, edge);
	        }
	        continue;
	      }
	      edge_edge.put(edge, edge);
	      
	      // !!!
	      if (!node_neighborNodes.containsKey(left)) {
	        node_neighborNodes.put(left, new ArrayList<GraphNode>());
	      }
	      if (!neighborNodes_node.containsKey(right)) {
	        neighborNodes_node.put(right, new ArrayList<GraphNode>());
	      }
	      node_neighborNodes.get(left).add(right);
	      neighborNodes_node.get(right).add(left);

	      nodes.add(left);
	      nodes.add(right);
	    }
	    
	    Graph gra = new Graph(node_neighborNodes, neighborNodes_node, edge_edge, nodes);
	    
	    return gra;
	  }
	  
	  public List<GraphNode> parseCondition(String condition) {
	    List<GraphNode> res = new ArrayList<>();
	    
	    String regex = "\\,|\\|";
	    condition = condition.trim();
	    condition = condition.replaceAll("\\s+", "");
	  	    
	    String[] elements = condition.split(regex);
//	    System.out.println(elements[0]);
	    
	    // 到处都在新定义节点
	    GraphNode source = new GraphNode(Integer.parseInt(elements[0]));
	    GraphNode dest = new GraphNode(Integer.parseInt(elements[1]));
	    res.add(source);
	    res.add(dest);
	    
	    for(int i = 2; i < elements.length; i++) {
	      res.add(new GraphNode(Integer.parseInt(elements[i])));
	    }
	    
	    return res;
	  }
	  
	  public String solve(String graphContent, String condition) {
		// 解析graphContent 生成图
		Graph graph = createGraph(graphContent);
//		System.out.println(graph.toString());
		
		// 解析condition
		List<GraphNode> tmp = parseCondition(condition);
		GraphNode source = tmp.get(0);
		GraphNode dest = tmp.get(1);
		List<GraphNode> mustPass = tmp.subList(2, tmp.size());
		System.out.println("source: " + source.toString());
		System.out.println("dest: " + dest.toString());
		System.out.println("mustPass: " + mustPass.toString());
		
		// set critical node
		for(GraphNode node : mustPass) {
		  node.setCritical(true);
		}
		// source节点也看成是关键节点
		source.setCritical(true);
		
		// 机智还是我机智啊
		graph.getNodes().removeAll(mustPass);
		graph.getNodes().addAll(mustPass);
		graph.getNodes().remove(source);
		graph.getNodes().add(source);
		    
		// 判断NA DFS
		DFS dfs = new DFS(graph, source, dest, mustPass);
		List<GraphNode> oneValidPath = dfs.findOneValidPath();
		if(oneValidPath == null) {
			System.out.println("NA");
			return "NA";
		} else {
			System.out.println("one valid path: " + oneValidPath);
		}
		    
		// Johnson算法
//		Johnson js = new Johnson(graph); 
//		js.solveAPSP();
//		graph.setNode_node_path(js.getNode_node_path());
//		graph.setNode_node_dist(js.getNode_node_dist());
//	    PrintHelper.print_node_node_dist(js.getNode_node_dist());
//	    PrintHelper.print_node_node_path(js.getNode_node_path());
//		
//		// TSP算法
//		TSP tsp = new TSP(graph, source, dest);
//		System.out.println(tsp.getValidPath());

		return "";
	  }
	  
	  // test this class
	  // !!! add more tests
	  public static void main(String[] args) {
		FindSolution fs = new FindSolution();
		String graphContent = "0,0,1,1\n1,0,2,2\n2,0,3,1\n3,2,1,3\n4,3,1,1\n5,2,3,1\n6,3,2,1\n";
		String condition = "0,1,2|3";
		fs.solve(graphContent, condition);
	  }
}

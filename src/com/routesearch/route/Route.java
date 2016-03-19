/**
 * 实现代码文件
 * 
 * @author XXX
 * @since 2016-3-4
 * @version V1.0
 */
package com.routesearch.route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.routesearch.graph.Graph;
import com.routesearch.graph.GraphEdge;
import com.routesearch.graph.GraphNode;

public final class Route {
  /**
   * 你需要完成功能的入口
   * 
   * @author XXX
   * @since 2016-3-4
   * @version V1
   */
  // 静态方法？？？
  public static String searchRoute(String graphContent, String condition) {
    FindSolution fs = new FindSolution();
    
    return fs.solve(graphContent, condition);
  }
  
}

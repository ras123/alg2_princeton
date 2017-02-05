import java.util.*;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class SAP {

   private Digraph G;

   // constructor takes a digraph (not necessarily a DAG)
   public SAP(Digraph G) {
      this.G = G;
   }

   private Map<Integer, Integer> bfs(int s) {
      Map<Integer, Integer> distancesFromS = new HashMap<>();
      distancesFromS.put(s, 0);
      Deque<Integer> queue = new LinkedList<>();
      queue.add(s);

      while (!queue.isEmpty()) {
         int v = queue.remove();
         int distanceToV = distancesFromS.get(v);
         for (int adjacentToV : G.adj(v)) {
            if (!distancesFromS.containsKey(adjacentToV)) {  // TODO: We can optimize by reducing autoboxing
               queue.add(adjacentToV);
               distancesFromS.put(adjacentToV, distanceToV + 1);
            }
         }
      }

      return distancesFromS;
   }

   private static class NearestAncestor {
      int ancestor;
      int distance;

      public NearestAncestor(int ancestor, int distance) {
         this.ancestor = ancestor;
         this.distance = distance;
      }
   }

   private Optional<NearestAncestor> getNearestAncestor(int v, int w) {
      Optional<NearestAncestor> nearestAncestor = Optional.empty();
      Map<Integer, Integer> distancesFromV = bfs(v);
      Map<Integer, Integer> distancesFromW = bfs(w);
      int ancestor = -1;
      int minDistance = Integer.MAX_VALUE;

      for (Map.Entry<Integer, Integer> distanceFromV : distancesFromV.entrySet()) {
         Integer x = distanceFromV.getKey();
         if (distancesFromW.containsKey(x)) {
            if (minDistance > distancesFromV.get(x) + distancesFromW.get(x)) {
               ancestor = x;
               minDistance = distancesFromV.get(x) + distancesFromW.get(x);
            }
         }
      }

      if (ancestor != -1) {
         nearestAncestor = Optional.of(new NearestAncestor(ancestor, minDistance));
      }

      return nearestAncestor;
   }

   // length of shortest ancestral path between v and w; -1 if no such path
   public int length(int v, int w) {
      int distance = -1;
      Optional<NearestAncestor> nearestAncestor = getNearestAncestor(v, w);

      if (nearestAncestor.isPresent()) {
         distance = nearestAncestor.get().distance;
      }

      return distance;
   }

   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
   public int ancestor(int v, int w) {
      int ancestor = -1;
      Optional<NearestAncestor> nearestAncestor = getNearestAncestor(v, w);

      if (nearestAncestor.isPresent()) {
         ancestor = nearestAncestor.get().ancestor;
      }

      return ancestor;
   }

   // length of shortest ancestral path between any v in v and any v in w; -1 if no such path
   public int length(Iterable<Integer> vs, Iterable<Integer> ws) {
      int minDistance = Integer.MAX_VALUE;

      for (Integer v : vs) {
         for (Integer w : ws) {
            int distance = length(v, w);
            if (distance != -1 && distance < minDistance) {
               minDistance = distance;
            }
         }
      }

      if (minDistance == Integer.MAX_VALUE) {
         minDistance = -1;
      }

      return minDistance;
   }

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
   public int ancestor(Iterable<Integer> vs, Iterable<Integer> ws) {
      int minAncestor = -1;
      int minDistance = Integer.MAX_VALUE;

      for (Integer v : vs) {
         for (Integer w : ws) {
            Optional<NearestAncestor> nearestAncestor = getNearestAncestor(v, w);
            if (nearestAncestor.isPresent() && nearestAncestor.get().distance < minDistance) {
               minAncestor = nearestAncestor.get().ancestor;
               minDistance = nearestAncestor.get().distance;
            }
         }
      }

      return minAncestor;
   }

   // do unit testing of this class
   public static void main(String[] args) {
      Digraph G = new Digraph(new In(args[0]));
      SAP sap = new SAP(G);
      System.out.println("length = " + sap.length(3, 11) + ", ancestor = " + sap.ancestor(3, 11));
      System.out.println("length = " + sap.length(9, 12) + ", ancestor = " + sap.ancestor(9, 12));
      System.out.println("length = " + sap.length(7, 2) + ", ancestor = " + sap.ancestor(7, 2));
      System.out.println("length = " + sap.length(1, 6) + ", ancestor = " + sap.ancestor(1, 6));
   }
}

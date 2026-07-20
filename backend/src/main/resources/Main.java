import java.util.Scanner;
public class Main
{
	public static void main(String[] args) {
	    Scanner sc = new Scanner(System.in);
	    int V = sc.nextInt();
	    int[][] graph = new int[V][V];
	    int E = sc.nextInt();
	    for(int i = 0;i<E;i++){
	        int u = sc.nextInt();
	        int v = sc.nextInt();
	        graph[u][v] = 1;
	        graph[v][u] = 1;
	    }
	    for(int i = 0;i < u;i++){
	        for(int j = 0;j <v;j++){
	            System.out.println(graph[i][j] + " ");
	        }
	    }
	    System.out.println();
	}
}
//
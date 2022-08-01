package tutorial;

import java.util.Arrays;
import java.util.Random;

import easyopt.common.EasyArray;

public class TSPmethods {
  Routes rt=new Routes();
  
  /**定义一个局部对象，可以存储两个数组，用于存储迭代贪婪算法破坏过程获得的两个子集*/
  public class Routes{
    int[] RT1;
    int[] RT2;
  }
  /**Based on the input parameter n and S-one dimension array, 
   * to create a two dimension array which is the result of inserting n into each position of S 
   * */
  public int[][] insertArray(int n, int[] S){
    int q=S.length+1;
    int[][] NS=new int[q][q];
    for(int i=0;i<q;i++){
      for(int j=0;j<i;j++){
        NS[i][j]=S[j];
      }
      NS[i][i]=n;
      for(int j=i+1;j<q;j++){
        NS[i][j]=S[j-1];
      }
    }
    
    return NS;
    
  }
  
  /**根据输入的数值d和数组route，从route中随机提取d个元素存入rt.RT1中，route中剩下的元素存入rt.RT2中*/
  public void destruction(int d,int[] route){
    rt.RT1=new int[d];
    int[] midRoute=Arrays.copyOf(route, route.length);//将route中的值付给对象tsp中的rt中的RT2数组；
    int qty=route.length;
    for(int i=0;i<d;i++){
      Random r=new Random();
      int idx=r.nextInt(qty);
      rt.RT1[i]=midRoute[idx];
      for(int j=idx;j<qty-1;j++){
        midRoute[j]=midRoute[j+1];
      }
      qty--;
    }
    //RT1已经获得了具体的内容，midRoute需要将其后面的d个元素舍去,将其保留的内容赋值给RT2；将midRoute中前qty-d个元素赋值给RT2
    qty=route.length;
    rt.RT2=new int[qty-d];
    for(int i=0;i<qty-d;i++){
      rt.RT2[i]=midRoute[i];
    }
      
    
  }

  public int[] nChoosek(int n,int k){

    int[] route=new int[n];
    for(int i=0;i<n;i++){
      route[i]=i;
    }
    this.destruction(k, route);  
    return rt.RT1;
  }
  
  // 根据输入的城市之间的距离矩阵和旅行商的行走路线，获得路线总长度
  public static double getDist(double[][] distances, int[] route){
      double sumDist = 0;
      for (int i = 0; i < route.length - 1; i++) {
          sumDist += distances[route[i]][route[i+1]];
      }
      sumDist += distances[route[route.length-1]][route[0]];

      return sumDist;
  }  
  
  // calculate the sum completion time of jobs based on the parameters: route and ptimes
  public static double getSumTime(double[] ptimes, int[] route){
    //first , calculate the completion times of all jobs
    int jobQty=route.length;
    double[] ct=new double[jobQty];
    double canStartTime=0;
    for(int i=0;i<jobQty;i++){
      int nowJobId=route[i];
      ct[i]=canStartTime+ptimes[nowJobId];
      canStartTime=ct[i];
    }
    //calculate the sum completion time
      double sumDist = 0;
      for (int i = 0; i < jobQty; i++) {
          sumDist += ct[i];
      }

      return sumDist;
  }  
  
  public static int[] getRoute(int cityQty) {
    int runTime=0;
    // b、动态初始化一个数组
    int[] S0 = new int[cityQty];//5
    // c、遍历数组，为每个位置生成对应的号码。
    Random r = new Random();
    for (int i = 0; i < cityQty - 1; i++) {
        // 为当前位置找出一个之间不重复的数字
        while (true){
            int citysquense = r.nextInt(cityQty);
            // d、注意：必须判断当前随机的这个号码之前是否出现过，出现过要重新随机一个，直到不重复为止，才可以存入数组中去。
            // 定义一个flag变量，默认认为data是没有重复的

            boolean flag = true;
            for (int j = 0; j <i; j++) {
                if (S0[j] == citysquense){
                    // 当前这个数据之前出现过，不能用
                    flag = false;
                    break;
                }
                runTime++;
            }
            if (flag){
                // 这个数据之前没有出现过，可以使用了
                S0[i] = citysquense;
                break;
            }
        }
    }
    System.out.println("runtime1: "+runTime);
    return S0;
}  

  /**根据输入的城市数量，生成[0,cityQty-1]这cityQty个整数值 的随机乱序数组*/
  public static int[] getRoute2(int cityQty) {
    int runTime=0;
    // b、动态初始化一个数组
    int[] S = new int[cityQty];//初始化顺序数组
    int[] RS=new int[cityQty];//最后的随机数组
    for(int i=0;i<cityQty;i++){
      S[i]=i;
      runTime++;
    }
    // c、遍历数组，为每个位置生成对应的号码。
    Random r = new Random();
    for (int i = 0; i < cityQty - 1; i++) {
        // 为当前位置找出一个之间不重复的数字
      int nowQty=S.length;
      int idx = r.nextInt(nowQty);
      RS[i]=S[idx];
      int[] S2=new int[nowQty-1];
      for(int j=0;j<idx;j++){
        S2[j]=S[j];
        runTime++;
      }
      for(int j=idx;j<nowQty-1;j++){
        S2[j]=S[j+1];
        runTime++;
      }
      S=S2;

    }
    RS[cityQty-1]=S[0];
    //System.out.println("runTime2: "+runTime);
    return RS;
  }  
  

}

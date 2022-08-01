package tutorial;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Tabu4TSP {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    //step1:数据初始化，包括点间距离数组、算法参数：禁忌代数、每代评价邻居数量、最大迭代代数
//    double[][] distances = {{0, 15, 19, 30, 6},{15, 0, 34, 44, 14},{19, 34, 0, 16, 22},
//    {30, 44, 16, 0, 35},{6, 14, 22, 35, 0}};
    double[][] distances = {{0,15,19,30,6,11,18,14,24},{15,0,34,44,14,23,29,4,38},{19,34,0,16,22,15,19,32,9},{30,44,16,0,35,30,18,44,7},{6,14,22,35,0,9,24,11,28},{11,23,15,30,9,0,24,20,23},
        {18,29,19,18,24,24,0,30,15},{14,4,32,44,11,20,30,0,37},{24,38,9,7,28,23,15,37,0}}; 
    
    int tabuLength=3,evalNeighborQty=6,maxGen=50;
    //根据初始化数据获得一些中间变量
    int cityQty=distances.length;//获得城市数量6+14+34+16+30

    int[][] twoPointSet=getCombination(cityQty);//*****根据cityQty获得禁忌表-肯定需要使用特定的方法实现
    //System.out.println(Arrays.deepToString(twoPointSet));
    int allCombQty=twoPointSet.length;
    int[][] tabuTable=new int[cityQty][cityQty];//定义禁忌表
    //step2:初始化问题解
    int[] nowRoute = initRoute(cityQty);//******
    System.out.println(Arrays.toString(nowRoute));
    //step3：自定义一些变量或变量数组：比如最优解【各个路径城市的先后关系】、最优解对应目标函数、当前迭代代数
    int[] optRoute=Arrays.copyOf(nowRoute, cityQty);
    double nowFit,optFit;//根据路径矩阵distances获得nowRoute=optRoute的总路长作为目标函数值、也为迄今为止最优解
    nowFit=getDistance(nowRoute, distances);//********
    optFit=nowFit;
    System.out.println(" nowRoute: "+ nowFit);
    int nowGen=0;
    //step4：执行禁忌搜索迭代过程
    while(nowGen<maxGen){
      //随机生成evalNeighborQty个整数【0-(allCombQty-1)】之间的值并存入数组changePos
      int[] changePos=getNeighborPos(allCombQty,evalNeighborQty);
      //根据获取的随机changePos和twoPointSet获得两点互换的集合changePairs
      int[][] changePairs=getNeighborPair(twoPointSet, changePos);
      //System.out.println("pairs: "+ Arrays.deepToString(changePairs));
      //根据changePairs和nowRoute生成evalNeighborQty个邻域解routes
      int[][] neighborRoutes=getNewRoutes(changePairs, nowRoute);
     // System.out.println("newRoutes: "+ Arrays.deepToString(neighborRoutes));      
      //计算routes中各条路径的距离，得对应的目标函数fset，并判断禁忌与否状态，存入一个数组，以便后续获取最好的解替代当前解
      double[][] fitArray=new double[evalNeighborQty][3];
      for(int i=0;i<evalNeighborQty;i++){
        fitArray[i][0]=i;
        fitArray[i][1]=getDistance(neighborRoutes[i], distances);
        //根据当前邻居所互换的两个点和禁忌表，判断当前邻居是否处于禁忌状态
        fitArray[i][2]=isTabu(changePairs[i],tabuTable);
      }
     // System.out.println("fitArrays: "+ Arrays.deepToString(fitArray)); 
      //排序fset，将fset对应的第一个解更新nowRoute
      fitArray=sortArray(fitArray,new int[]{2,1});
     // System.out.println("sorted fitArrays: "+ Arrays.deepToString(fitArray));       
      int[] firstRoute=neighborRoutes[(int) fitArray[0][0]];
      double nowOptFit=fitArray[0][1];
      nowRoute=Arrays.copyOf(firstRoute, cityQty);
      //更新禁忌表
      tabuTable=updateTabuTable(changePairs[(int)fitArray[0][0]], tabuTable,tabuLength);
      
      //判断是否更新optRoute和optFit
      if(nowOptFit<optFit){
        optFit=nowOptFit;
        optRoute=Arrays.copyOf(firstRoute, cityQty);
      }     
      nowGen++;  
      System.out.println("nowGen: "+ nowGen+"   nowOpt: "+ nowOptFit+"  optFit: "+ optFit);
    }
    //step5:输出最优结果
    System.out.println(" optimal route: "+ optRoute.toString());
    System.out.println(" optimal distance: "+ optFit);
    
  }
  
//  public static void tabu4TSP(double[][] distance, int tabuLength, int evalNeighborQty, int maxGen){
//    
//  }
  
  //根据当前互换的点对和禁忌表，对禁忌表进行更新并返回
  public static int[][] updateTabuTable(int[] pairs, int[][] tabuTable,int tabuLength){
    for(int i=0;i<tabuTable.length;i++){
      for(int j=0;j<tabuTable[0].length;j++){
        if(tabuTable[i][j]>0){
          tabuTable[i][j]--;
        }
      }
    }
    tabuTable[pairs[0]][pairs[1]]=tabuLength;
    
    
    return tabuTable;
    
  }      
  
  //sort array: sort the array by multiple cols at ascend or descend
  public static double[][] sortArray(double[][] ob, int[] order){
    int[] orderSign=new int[order.length];
    for(int i=0;i<order.length;i++){
      if(order[i]<0){
        orderSign[i]=-1;
      }else{
        orderSign[i]=1;
      }
      order[i]=Math.abs(order[i]);
    }
    Arrays.sort(ob, new Comparator<Object>() {    
        public int compare(Object o1, Object o2) {   
            double[] one = (double[]) o1;    
            double[] two = (double[]) o2;             
            for (int i = 0; i < order.length; i++) {    
                int k = order[i];    
                if (one[k] > two[k]) {    
                    return 1*orderSign[i];    
                } else if (one[k] < two[k]) {    
                    return -1*orderSign[i];    
                } else {    
                    continue;  //如果按一条件比较结果相等，就使用第二个条件进行比较。  
                }    
            }    
            return 0;    
        }    
    }); 
    return ob;
  }    
  

  //判断互换的两个点是否处于禁忌状态，若是，返回1，否则返回0
  public static int isTabu(int[] points, int[][] tabuTable){
    int a=0;
    if(tabuTable[points[0]][points[1]]>0){
      a=1;
    }
    return a;
    
  }    
  
  //根据当前路径和两点互换的点集合生成当前解的邻域集合
  public static int[][] getNewRoutes(int[][] swapPairs, int[] nowRoute){
    int row=swapPairs.length;
    int col=nowRoute.length;
    int[][] newRoutes=new int[row][col];
    for(int i=0;i<row;i++){
      int[] nowPair=swapPairs[i];
      int[] midNewRoute=new int[col];
      for(int j=0;j<col;j++){
        if(nowRoute[j]!=nowPair[0]&&nowRoute[j]!=nowPair[1]){
          midNewRoute[j]=nowRoute[j];
        }else{
          if(nowRoute[j]!=nowPair[0]){
            midNewRoute[j]=nowPair[0];
          }else{
            midNewRoute[j]=nowPair[1];
          } 
        }
      }
      newRoutes[i]=midNewRoute;
      
    }
    return newRoutes;
    
  }   
    
  
  //将combSet中对应与choicePos中的数对提取出来，形成互换的数组
  public static int[][] getNeighborPair(int[][] combSet, int[] choicePos){
    int row=choicePos.length;
    int[][] pairs=new int[row][2];
    for(int i=0;i<row;i++){
      pairs[i]=combSet[choicePos[i]];
    }
    
    return pairs;
    
  }   
  
  //从【0到n-1】这n个数值中提取q个不同的数值构成数组
  public static int[] getNeighborPos(int n, int q){
    int[] randArray=initRoute(n);
//    System.out.println("all points: "+Arrays.toString(randArray));
    int[] neighborIds=Arrays.copyOf(randArray, q);
//    System.out.println("selected points: "+Arrays.toString(neighborIds));
    
    return neighborIds;
    
  } 
  
  
  //根据输入的路径和城市节点间的距离矩阵，获得该路径的总里程
  public static double getDistance(int[] route,double[][] distances){
    double sumDist = 0;
    for (int i = 0; i < route.length - 1; i++) {
        sumDist += distances[route[i]][route[i+1]];
    }
    sumDist += distances[route[route.length-1]][route[0]];

    return sumDist;
    
  } 

  /**根据输入的城市数量，生成[0,cityQty-1]这cityQty个整数值 的随机乱序数组*/
  public static int[] initRoute(int cityQty) {
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
  
  //根据输入的参数n生成从n中去2的全部组合，形成一个数组
  public static int[][] getCombination(int n){
    int rows=n*(n-1)/2;
    int[][] comArray=new int[rows][2];
    int idx=0;
    for(int i=0;i<n-1;i++){
      for(int j=i+1;j<n;j++){
        comArray[idx][0]=i;
        comArray[idx][1]=j;
        idx++;
      }
    }
    return comArray;
    
  }
  
  

}

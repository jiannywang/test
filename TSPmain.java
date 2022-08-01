package tutorial;

import java.util.Arrays;

import easyopt.common.EasyArray;

public class TSPmain {

  public static void main(String[] args) {
    //singleMachSumCT();
    oldIterated();  


      }

  public static void singleMachSumCT(){
    //1、初始化数据
//  double[][] distances = {{0, 15, 19, 30, 6},{15, 0, 34, 44, 14},{19, 34, 0, 16, 22},
//          {30, 44, 16, 0, 35},{6, 14, 22, 35, 0}};
  double[] pt = {5,3,9,2,7,10,20,9,7};      
  int d=2;
  //创建解决方案并记录最佳解决方案及其目标
  int cityQty=pt.length;
  int[] nowRoute = TSPmethods.getRoute2(cityQty);
  System.out.println(Arrays.toString(nowRoute));
  int maxNum=0;
  boolean improved=true;
  while(improved&&maxNum<20){//主体循环step3
      improved=false;
      maxNum++;
      double fitS0=TSPmethods.getSumTime(pt,nowRoute);//需要一个小函数，能够根据传入的路径S0和距离矩阵distances，获得该路径的路程；
      TSPmethods tspm=new TSPmethods();
      tspm.destruction(d, nowRoute);
//      System.out.println(" uuuS1----: "+ Arrays.toString(tspm.rt.RT1));
//      System.out.println(" uuuS2----: "+ Arrays.toString(tspm.rt.RT2));            
      int[] S1=Arrays.copyOf(tspm.rt.RT1,d);
      int[] S2=Arrays.copyOf(tspm.rt.RT2,cityQty-d);
//      System.out.println(nowRoute+" S1address: "+S1+" S2address: "+S2);
////      System.out.println(" nowRoute: "+ Arrays.toString(nowRoute));
//      System.out.println(" S1: "+ Arrays.toString(S1));
//      System.out.println(" S2: "+ Arrays.toString(S2)); 
      //--get the optimal route by inserting each element into S2;
      for(int i=0;i<S1.length;i++){
        int nowNum=S1[i];
        int[][] newS2=tspm.insertArray(nowNum,S2);//
        double minDist=TSPmethods.getSumTime(pt,newS2[0]);
        int minIdx=0;
        for(int j=1;j<newS2.length;j++){
           double nowDist= TSPmethods.getSumTime(pt,newS2[j]);
           if(nowDist<minDist){
             minDist=nowDist;
             minIdx=j;
           }
        }
        S2=newS2[minIdx];

     }

      double nowOptDist=TSPmethods.getSumTime(pt,S2);
      System.out.println("fitS0: "+fitS0+"    nowOptDist: "+nowOptDist);
      System.out.println(Arrays.toString(S2));
      if(nowOptDist<=fitS0){
        fitS0=nowOptDist;
        nowRoute=Arrays.copyOf(S2, cityQty);
        improved=true;
      }


      
      
  }    
}
  
  
  public static void oldIterated(){
        //1、初始化数据
    //  double[][] distances = {{0, 15, 19, 30, 6},{15, 0, 34, 44, 14},{19, 34, 0, 16, 22},
    //          {30, 44, 16, 0, 35},{6, 14, 22, 35, 0}};
      double[][] distances = {{0,15,19,30,6,11,18,14,24},{15,0,34,44,14,23,29,4,38},{19,34,0,16,22,15,19,32,9},{30,44,16,0,35,30,18,44,7},{6,14,22,35,0,9,24,11,28},{11,23,15,30,9,0,24,20,23},
          {18,29,19,18,24,24,0,30,15},{14,4,32,44,11,20,30,0,37},{24,38,9,7,28,23,15,37,0}};      
      int d=4;
      //创建解决方案并记录最佳解决方案及其目标
      int cityQty=distances.length;
      int[] nowRoute = TSPmethods.getRoute2(cityQty);
//      System.out.println(Arrays.toString(nowRoute));
      int maxNum=0;
      boolean improved=true;
      while(improved&&maxNum<20){//主体循环step3
          improved=false;
          maxNum++;
          double fitS0=TSPmethods.getDist(distances,nowRoute);//需要一个小函数，能够根据传入的路径S0和距离矩阵distances，获得该路径的路程；
          TSPmethods tspm=new TSPmethods();
          tspm.destruction(d, nowRoute);
    //      System.out.println(" uuuS1----: "+ Arrays.toString(tspm.rt.RT1));
    //      System.out.println(" uuuS2----: "+ Arrays.toString(tspm.rt.RT2));            
          int[] S1=Arrays.copyOf(tspm.rt.RT1,d);
          int[] S2=Arrays.copyOf(tspm.rt.RT2,cityQty-d);
//          System.out.println(nowRoute+" S1address: "+S1+" S2address: "+S2);
//    //      System.out.println(" nowRoute: "+ Arrays.toString(nowRoute));
//          System.out.println(" S1: "+ Arrays.toString(S1));
//          System.out.println(" S2: "+ Arrays.toString(S2)); 
          //--get the optimal route by inserting each element into S2;
          for(int i=0;i<S1.length;i++){
            int nowNum=S1[i];
            int[][] newS2=tspm.insertArray(nowNum,S2);//
            double minDist=TSPmethods.getDist(distances,newS2[0]);
            int minIdx=0;
            for(int j=1;j<newS2.length;j++){
               double nowDist= TSPmethods.getDist(distances,newS2[j]);
               if(nowDist<minDist){
                 minDist=nowDist;
                 minIdx=j;
               }
            }
            S2=newS2[minIdx];
    
         }

          double nowOptDist=TSPmethods.getDist(distances,S2);
          System.out.println("fitS0: "+fitS0+"    nowOptDist: "+nowOptDist);
          System.out.println(Arrays.toString(S2));
          if(nowOptDist<=fitS0){
            fitS0=nowOptDist;
            nowRoute=Arrays.copyOf(S2, cityQty);
            improved=true;
          }
    
    
          
          
      }    
  }

  
  public static void newIterated(){
        //1、初始化数据
//      double[][] distances = {{0, 15, 19, 30, 6},{15, 0, 34, 44, 14},{19, 34, 0, 16, 22},
//              {30, 44, 16, 0, 35},{6, 14, 22, 35, 0}};
      double[][] distances = {{0,15,19,30,6,11,18,14,24},{15,0,34,44,14,23,29,4,38},{19,34,0,16,22,15,19,32,9},{30,44,16,0,35,30,18,44,7},{6,14,22,35,0,9,24,11,28},{11,23,15,30,9,0,24,20,23},
          {18,29,19,18,24,24,0,30,15},{14,4,32,44,11,20,30,0,37},{24,38,9,7,28,23,15,37,0}};      
      int d=3;
      //创建解决方案并记录最佳解决方案及其目标
      int cityQty=distances.length;
      int[] nowRoute = TSPmethods.getRoute2(cityQty);
      System.out.println(Arrays.toString(nowRoute));
      boolean improved=true;
      while(improved){//主体循环step3
          improved=false;
          double fitS0=TSPmethods.getDist(distances,nowRoute);//需要一个小函数，能够根据传入的路径S0和距离矩阵distances，获得该路径的路程；
          //System.out.println(" nowRoute: "+ Arrays.toString(nowRoute));
          TSPmethods tspm=new TSPmethods();
          int[] pos=tspm.nChoosek(cityQty, d);
          //System.out.println(" pos: "+ Arrays.toString(pos));
          int[] S1=new int[d];
          int[] S2=new int[cityQty-d];
          for(int i=0;i<d;i++){
            S1[i]=nowRoute[pos[i]];
          }        
          int idx=0;
          for(int i=0;i<cityQty;i++){
            boolean notFind=true;
            for(int j=0;j<d;j++){
              if(i==pos[j]){
                notFind=false;
                break;
              }
            }
            if(notFind){
              S2[idx]=nowRoute[i];
              idx++;
            }
          }
          

          //--get the optimal route by inserting each element into S2;
          for(int i=0;i<S1.length;i++){
            int nowNum=S1[i];
            int[][] newS2=tspm.insertArray(nowNum,S2);//
            double minDist=TSPmethods.getDist(distances,newS2[0]);
            int minIdx=0;
            for(int j=1;j<newS2.length;j++){
              if(i==S1.length-1&&j==pos[i]){
              
              }else{
                double nowDist= TSPmethods.getDist(distances,newS2[j]);
                if(nowDist<minDist){
                  minDist=nowDist;
                  minIdx=j;
                }
              }
            }
            S2=newS2[minIdx];
    
         }
          double nowOptDist=TSPmethods.getDist(distances,S2);
          System.out.println("fitS0: "+fitS0+"    nowOptDist: "+nowOptDist);
          System.out.println(Arrays.toString(S2));
          if(nowOptDist<=fitS0){
            fitS0=nowOptDist;
            nowRoute=Arrays.copyOf(S2, cityQty);
            improved=true;
          }
    
    
          
          
      }    
    
  }

}

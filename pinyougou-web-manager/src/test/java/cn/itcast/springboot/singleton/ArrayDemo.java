package cn.itcast.springboot.singleton;

/**
 * @ClassName ArrayDemo
 * @Description 获取数组中重复的元素
 * @Author 传智播客
 * @Date 8:44 2019/5/22
 * @Version 2.1
 **/
public class ArrayDemo {

    public static void main(String[] args) {
        int[] arr = {1,4,1,4,2,5,4,5,8,7,8,77,88,5,4,9,6,2,4,1,5};
        int[] array = new int[100];
        for(int i=0; i<arr.length; i++){
            array[arr[i]]++;
        }
        for(int j=0; j<array.length; j++){
            if(array[j] != 0){
                System.out.println(j+"出现了："+array[j]+"次数");
            }

        }
    }
}

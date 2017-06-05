import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by Roman on 28.05.2017.
 */

import java.io.BufferedReader;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Generator {
    // Импульсное заполенение ЛРС
    int L1State = 11;
    int L2State = 11;
    int L3State = 1;
    // Важные значения
    // N = 265
    // C = 50

    // Получить определённый бит из числа
    public int getBitValue(int number,int position){
        return (number>>position)&1;
    }
    // Получить количество единичек в битовом представлении
    public static int NumberOfBits(int i) {
        i = i - ((i >>> 1) & 0x55555555);
        i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
        return (((i + (i >>> 4)) & 0x0F0F0F0F) * 0x01010101) >>> 24;
    }

    public int L1Iteration(){
        int xNew = getBitValue(L1State,0)^getBitValue(L1State,1)^
                getBitValue(L1State,4)^getBitValue(L1State,6);
        // Узнаём бит,который мы выталкиваем
        int pushedBit = L1State&1;
        L1State = L1State>>1;
        L1State = L1State + (xNew<<29);
        return  pushedBit;
    }
    public int[] L1FillingIteration(int[] filling){
        int pushedBit = L1Iteration();
        for(int i=1;i<filling.length;i++){

            int newPushedBit = filling[i] & 1;
            filling[i] = filling[i]>>1;
            filling[i] += pushedBit<<31;
            pushedBit = newPushedBit;
        }
        filling[0] = L1State;
        return filling;
    }


    public int L2Iteration(){
        int xNew = getBitValue(L2State,0)^getBitValue(L2State,3);
        int pushedBit = L1State&1;
        L2State = L2State>>1;
        L2State = L2State + (xNew<<30);
        return  pushedBit;
    }
    public int[] L2FillingIteration(int[] filling){
        int pushedBit = L2Iteration();
        for(int i=1;i<filling.length;i++){

            int newPushedBit = filling[i] & 1;
            filling[i] = filling[i]>>1;
            filling[i] += pushedBit<<31;
            pushedBit = newPushedBit;
        }
        filling[0] = L2State;
        return filling;
    }

    public int L3Iteration(){
        int xNew = getBitValue(L3State,0)^getBitValue(L3State,1)^
                getBitValue(L3State,2)^getBitValue(L3State,3)^
                getBitValue(L3State,5)^getBitValue(L3State,7);
        int pushedBit = L1State&1;
        L3State = L3State>>1;
        L3State = L3State + (xNew<<31);
        return  pushedBit;
    }
    public int[] L3FillingIteration(int[] filling){
        int pushedBit = L3Iteration();
        for(int i=1;i<filling.length;i++){

            int newPushedBit = filling[i] & 1;
            filling[i] = filling[i]>>1;
            filling[i] += pushedBit<<31;
            pushedBit = newPushedBit;
        }
        filling[0] = L3State;
        return filling;
    }



    // Наш генератор (будет генерить и L1, и L2, и L3
    public List<int[]> L1Generator(String bitsFromFile){
        /* Вот все эти N=265 знаков
           будут храниться в массиве filling
           8*32 + 30 бит моего ЛРС
        */

        List<int[]> acceptedFillings = new ArrayList<int[]>();
        int[] filling = new int[9];
        // Слева стоит генератор
        filling[0] = L1State;
        // Нужно сгенерировать начальное заполнение
        // То есть сгенерить ещё 286-30 = 256 знаков


        // Считали 286 бит из текстового файла
        int[] textFillings = textFillings(bitsFromFile,286);
        for(int i = 0; i < 256; i++){
            filling  = L1FillingIteration(filling);
            filling[0] = L1State;
        }
        // Тупой дебаг
        int minR=100000000;

        for(int i=0;i<1073741567;i++){
            filling  = L1FillingIteration(filling);
            filling[0] = L1State;
//                MyRunnable Part1 = new MyRunnable(filling,textFillingsPart1);
//                MyRunnable Part2 = new MyRunnable(filling,textFillingPart2);
//                Thread Thread1 = new Thread(Part1);
//                Thread Thread2 = new Thread(Part2);
//                Thread1.run();
//                Thread2.run();
            int C = 97;
            int R = comparsion(filling,textFillings);
            if(R<minR) minR = R;
            if(R<C){
                int[] acceptedFilling = new int[9];
                System.arraycopy(filling,0,acceptedFilling,0,9);
                acceptedFillings.add(acceptedFilling);
            }

        }
        System.out.print("Минимальное значение:");
        System.out.println(minR);
        return acceptedFillings;
    }
    public List<int[]> L2Generator(String bitsFromFile){
        List<int[]> acceptedFillings = new ArrayList<int[]>();
        int[] filling = new int[9];
        // Слева стоит генератор
        // Он теперь 31 бита, а не 30 как в L1
        filling[0] = L2State;
        int[] textFillings = textFillings(bitsFromFile,287);
        for(int i = 0; i < 256; i++){
            filling  = L2FillingIteration(filling);
            filling[0] = L2State;
        }
        // Тупой дебаг
        int minR=100000000;
        // 2**31 - 1
        for(int i=0;i<2147483647;i++){
            filling  = L2FillingIteration(filling);
            filling[0] = L2State;
//                MyRunnable Part1 = new MyRunnable(filling,textFillingsPart1);
//                MyRunnable Part2 = new MyRunnable(filling,textFillingPart2);
//                Thread Thread1 = new Thread(Part1);
//                Thread Thread2 = new Thread(Part2);
//                Thread1.run();
//                Thread2.run();
            int C = 105;
            int R = comparsion(filling,textFillings);
            if(R<minR) minR = R;
            if(R<C){
                int[] acceptedFilling = new int[9];
                System.arraycopy(filling,0,acceptedFilling,0,9);
                acceptedFillings.add(acceptedFilling);
            }
        }
        System.out.print("Минимальное значение:");
        System.out.println(minR);
        return acceptedFillings;
    }

    public static int comparsion(int[] L1values,int[] textValues ){
        int R = 0;
        for (int i = 0; i < L1values.length;i++){
            R += NumberOfBits(L1values[i]^textValues[i]);
        }
        return R;
    }

    // +
    // Получить amount последних бит из файлика
    public int[] textFillings(String string,int amount){
//        int[][] fillings = new int[1763][9];
        int[] fillings = new int[9];
        // Массив будет типа arr[k][s]
        String textFilling = string.substring(2048-amount,2048);
        // Нужно парсить НАОБОРОТ!!!!!
        String first = textFilling.substring(0,amount - 32*8);
        fillings[0] = Integer.parseInt(first,2);
        for(int s = 0;s<8;s++){
                // Кусок в 32 бита
                String newPart = textFilling.substring(s*32+(amount - 32*8),(s+1)*32 + (amount - 32*8));
                long newPartNumber = Long.parseLong(newPart,2);
                fillings[s+1] = (int) newPartNumber;
        }
        return fillings;
    }

    // Перевести наш регистр в битовое представление
    public String fillingToString(int[] filling){
        String resultString = "";
        for(int i=0;i<filling.length;i++){
            resultString.concat(Integer.toBinaryString(filling[i]));
        }
        return resultString;
    }

    public static void main(String[] args){
        long startTime = System.nanoTime();
        try{
            String fileName = "D:\\CryptLabs\\lab3\\our_variant_reversed.txt";
            Scanner sc = new Scanner(new File(fileName));
            String bitsFromFile = sc.nextLine();
            Generator L1 = new Generator();
//            for(int i=0;i<1073741823;i++) {
//                L1.L1Iteration();
//            }
//            System.out.println(L1.L1State);
            List result = L1.L2Generator(bitsFromFile);

            System.out.println(result.size());

            long endTime = System.nanoTime();

            long duration = (endTime - startTime);

            System.out.println("Spent time:"+duration);
        }catch (IOException e){
            System.out.print("What a pitty!");
        }

    }
}






//class MyRunnable implements Runnable {
//    public int[] L1values;
//    public int[][] text;
//
//    public MyRunnable(int[] L1values, int[][] text) {
//        this.L1values = L1values;
//        this.text = text;
//    }
//    public void run(){
//        Generator.totalComparsion(this.L1values,this.text);
//    }
//}
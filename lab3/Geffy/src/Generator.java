import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Roman on 28.05.2017.
 */
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// Не забудь,что твоя последовательность,тип как перевёрнутая
    // Так что переверни последовательность в файле

import java.io.BufferedReader;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Generator {
    // Импульсное заполенение ЛРС
    int L1State = 1;
    int L2State = 1;
    int L3State = 1;
    // Важные значения
    // N = 265
    // C = 50

    // Not tested
    // Получить определённый бит из числа
    public int getBitValue(int number,int position){
        return (number>>position)&1;
    }
    public static int NumberOfBits(int i) {
        i = i - ((i >>> 1) & 0x55555555);
        i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
        return (((i + (i >>> 4)) & 0x0F0F0F0F) * 0x01010101) >>> 24;
    }
    // Number will represent our register like that:
    // x29,...,x0

    // Not tested
    // 29 бит в регистре
    // x(i+30) = x(i) + x(i+1) + x(i+4) + x(i+6)
    public int L1Iteration(){
        int xNew = getBitValue(L1State,0)^getBitValue(L1State,1)^
                getBitValue(L1State,4)^getBitValue(L1State,6);
        // Узнаём бит,который мы выталкиваем
        int pushedBit = L1State&1;
        L1State = L1State>>1;
        L1State = L1State + (xNew<<29);
        return  pushedBit;
    }
    // Это 1 такт генератора L1 внутри filling
    public int[] L1FillingIteration(int[] filling){
        int pushedBit = L1Iteration();
        for(int i=1;i<filling.length;i++){

            int newPushedBit = filling[i] & 1;
            filling[i] = filling[i]>>1;
            filling[i] += pushedBit<<31;
            pushedBit = newPushedBit;
        }
        return filling;
    }
    // Все такты L1FillingIteration
    public int[] L1Generator(){
        /* Вот все эти N=265 знаков
           будут храниться в массиве filling
           8*32 + 30 бит моего ЛРС
        */
        int[] filling = new int[9];
        // Слева стоит генератор
        filling[0] = L1State;
        // Нужно сгенерировать начальное заполнение
        // То есть сгенерить ещё 286-30 = 256 знаков

        try {
            String fileName = "D:\\CryptLabs\\lab3\\our_variant_reversed.txt";
            Scanner sc = new Scanner(new File(fileName));
            String bits = sc.nextLine();
            int[][] textFillings = textFillings(bits);
            int[][] textFillingsPart1 = Arrays.copyOfRange(textFillings,0,880);
            int[][] textFillingPart2 = Arrays.copyOfRange(textFillings,880,1763);
            for(int i = 0; i < 256; i++){
                filling  = L1FillingIteration(filling);
                filling[0] = L1State;
            }

            for(int i=0;i<1073741567;i++){
                if(i==10000000) System.out.print("pizdez");
                filling  = L1FillingIteration(filling);
                filling[0] = L1State;
                MyRunnable Part1 = new MyRunnable(filling,textFillingsPart1);
                MyRunnable Part2 = new MyRunnable(filling,textFillingPart2);
                Thread Thread1 = new Thread(Part1);
                Thread Thread2 = new Thread(Part2);
                Thread1.run();
                Thread2.run();
//                totalComparsion(filling,textFillings);
            }
        }catch (IOException e){
            System.out.print("Pizdos");
        }

        return filling;
    }

    public static int totalComparsion(int[] L1values, int[][] text){

        for(int i=0; i< text.length; i++){
            comparsion(L1values, text[i]);
        }
        //test return
        return 0;
    }

    public static int comparsion(int[] L1values,int[] textValues ){
        int R = 0;
        for (int i = 0; i < L1values.length;i++){
            R += NumberOfBits(L1values[i]^textValues[i]);
        }
        return R;
    }

    public int[][] textFillings(String string){
        int[][] fillings = new int[1763][9];

        // Массив будет типа arr[k][s]
        int i = 2048;
        while(i != 285){
            int k = i - 286;
            // Одно заполнение на 286 бит
            String textFilling = string.substring(i-286,i);
            // Первый int особенный - в нём будет 30 бит, а не 32
            String first = textFilling.substring(0,30);

            fillings[k][0] = Integer.parseInt(first,2);
            for(int s = 0;s<8;s++){
                // Кусок в 32 бита
                String newPart = textFilling.substring(s*32+30,(s+1)*32 + 30);
                long newPartNumber = Long.parseLong(newPart,2);
                fillings[k][s+1] = (int) newPartNumber;
            }
            i -= 1;
        }
        return fillings;
    }

    public static void main(String[] args){
        long startTime = System.nanoTime();

        Generator L1 = new Generator();
//        // Импульсное заполнение
//        L1.L1State = 1;
//        System.out.println(L1.L1Generator()[0]);

//        FileReader fr = new FileReader("D:\\CryptLabs\\lab3\\our_variant_reversed.txt");
//        BufferedReader  br = new BufferedReader(fr);
        L1.L1Generator();

        long endTime = System.nanoTime();

        long duration = (endTime - startTime);

        System.out.println(duration);
    }
}

class MyRunnable implements Runnable {
    public int[] L1values;
    public int[][] text;

    public MyRunnable(int[] L1values, int[][] text) {
        this.L1values = L1values;
        this.text = text;
    }
    public void run(){
        Generator.totalComparsion(this.L1values,this.text);
    }
}
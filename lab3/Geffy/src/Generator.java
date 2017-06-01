/**
 * Created by Roman on 28.05.2017.
 */
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// Не забудь,что твоя последовательность,тип как перевёрнутая
    // Так что переверни последовательность в файле



public class Generator {
    // Начальное заполенение ЛРС
    int L1State;
    int L2State;
    int L3State;
    // Важные значения
    // N = 265
    // C = 50

    // Not tested
    // Get exactly bit of number
    public int getBitValue(int number,int position){
        return (number>>position)&1;
    }

    // Number will represent our register like that:
    // x29,...,x0

    // Not tested
    // 29 бит в регистре
    // x(i+30) = x(i) + x(i+1) + x(i+4) + x(i+6)
    public int L1Iteration(){
        int[] result = new int[3];
        int xNew = getBitValue(L1State,0)^getBitValue(L1State,1)^
                getBitValue(L1State,4)^getBitValue(L1State,6);
        // Узнаём бит,который мы выталкиваем
        int pushedBit = L1State&1;
        L1State = L1State>>1;
        L1State = L1State + (xNew<<29);
//        result[0] = xNew;
//        result[1] = L1State;
//        result[2] = pushedBit;
        System.out.println(Integer.toBinaryString(L1State));
        return  pushedBit;
    }
    public int[] L1Generation(int[] filling){
        int pushedBit = L1Iteration();
        for(int i=1;i<filling.length;i++){

            int newPushedBit = filling[i] & 1;
            filling[i] = filling[i]>>1;
            filling[i] += pushedBit<<31;
            pushedBit = newPushedBit;
        }

        return filling;
    }
    public int[] L1Generator(){
        /* Вот все эти N=265 знаков
           256 бит
        */
        int[] filling = new int[9];
        // Слева стоит генератор
        filling[0] = L1State;
        // Нужно сгенерировать начальное заполнение
        // То есть сгенерить ещё 265-30 = 235 знаков
        for(int i=0;i<265;i++){
            filling  = L1Generation(filling);
        }
        // DEBUG
        for (int i=1;i<filling.length;i++){
            System.out.println(Integer.toBinaryString(filling[i]));
        }
        return filling;
    }










    // ---------------------------------------------
    // Not tested
    // 30 бит в регистре
    // y(i+31) = y(i) + y(i+3)
//    public int L2Iteration(){
//        int xNew = getBitValue(L2State,0)^getBitValue(L2State,3);
//        L2State = L2State>>1;
//        L2State = L2State + xNew<<30;
//        return  xNew;
//    }
//    // Not tested
//    // s(i+32) = s(i) + s(i+1) + s(i+2) + s(i+3) +s (i+5) + s(i+7)
//    public int L3Iteration(){
//        // Нашли новое значени
//        int xNew = getBitValue(L3State,0)^getBitValue(L3State,1)^
//                getBitValue(L3State,3)^getBitValue(L3State,5)^
//                getBitValue(L3State,7);
//        // А вот тут пасандобль: нужно сдвинуть не только основное число,но ещё и дибильный 33-й бит4
//        L3State = L3State>>1;
//        // Сдвигаем 33-й бит в основное число
//        L3State += L3StateTail<<31;
//        // А вот и новый бит втулился в хвост
//        L3StateTail = xNew;
//        return  xNew;
//    }
//    // F(x,y,s) = sx + (1+s)y
//    public boolean F(boolean x,boolean y,boolean s){
//        return s&&x ^ (true^s) && y;
//    }

    public static void main(String[] args){
        Generator L1 = new Generator();
        // Импульсное заполнение
        L1.L1State = 1;
        L1.L1Generator();
    }
}


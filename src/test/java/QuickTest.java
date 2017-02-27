/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 socraticphoenix@gmail.com
 * Copyright (c) 2016 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import com.gmail.socraticphoenix.forge.randore.RandoresProbability;

import java.util.Random;

public class QuickTest {

    public static void main(String[] args) {
        Random random = new Random();
        int[] res = new int[50];
        for (int i = 0; i < 10000000; i++) {
            int n = (int) RandoresProbability.oneSidedInflectedNormalRand(0, 50, 20, random);
            res[n] = res[n] + 1;
        }
        for (int i = 0; i < res.length; i++) {
            System.out.print(i + ":");
            for (int j = 0; j < 5 - String.valueOf(i).length(); j++) {
                System.out.print(" ");
            }
            for (int j = 0; j < res[i] / 10000; j++) {
                System.out.print("-");
            }
            System.out.println();
        }
    }


}

/*
 * Copyright 2020 denjossal. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package mutant.services;

import javax.inject.Singleton;
import mutant.utils.MutantFinderUtil;

@Singleton
public class MutantFinderService {

  private static final int MAX_LENGTH_DNA = 4;
  private static final int MAX_MUTANT_SERIES = 2;


  public MutantFinderService() {
    //You can use this constructor if you need load more information to calculate values
  }

  public boolean isMutant(String[] dna) {
    boolean isDnaMutant = false;
    long startTime = System.currentTimeMillis();
    long endTime = System.currentTimeMillis();
    int c = 0;
    int matrixSize = dna.length;
    //Using Memory vs Time
    char[][] matrix = new char[matrixSize][matrixSize];
    int[][] dleft = new int[matrixSize][matrixSize];
    int[][] down = new int[matrixSize][matrixSize];
    int[][] drigth = new int[matrixSize][matrixSize];
    int[][] rigth = new int[matrixSize][matrixSize];
    char[] subArr;
    for (int i = matrixSize - 1; i >= 0; i--) {
      subArr = dna[i].toCharArray();
      for (int j = matrixSize - 1; j >= 0; j--) {
        if (c >= MAX_MUTANT_SERIES) {
          isDnaMutant = true;
          break;
        } else {
          if (MutantFinderUtil.isValidChipDna(subArr[j])) {
            matrix[i][j] = subArr[j];
            c = counterSeriesMutant(matrix, matrixSize, dleft, down, drigth, rigth, i, j, c);
          }
        }
      }
    }
    System.out.println("Total execution time: " + (endTime - startTime) + "ms");
    return isDnaMutant;
  }

  private int counterSeriesMutant(char[][] matrix, int matrixSize, int[][] dleft, int[][] down,
      int[][] drigth, int[][] rigth, int i, int j, int c) {
    MutantFinderUtil.fillByPositionAllMatrix(dleft, down, drigth, rigth, i, j);
    if (((i + 1) < matrixSize && (j - 1) < matrixSize - 1 && (j - 1) >= 0)
        && matrix[i + 1][j - 1] == matrix[i][j]) {
      dleft[i][j] = dleft[i + 1][j - 1] + 1;
      if (dleft[i][j] == MAX_LENGTH_DNA) {
        c++;
      }
    }
    if (((i + 1) < matrixSize && matrix[i + 1][j] == matrix[i][j])) {
      down[i][j] = down[i + 1][j] + 1;
      if (down[i][j] == MAX_LENGTH_DNA) {
        c++;
      }
    }
    if (((i + 1) < matrixSize && (j + 1) < matrixSize) && matrix[i + 1][j + 1] == matrix[i][j]) {
      drigth[i][j] = drigth[i + 1][j + 1] + 1;
      if (drigth[i][j] == MAX_LENGTH_DNA) {
        c++;
      }
    }
    if ((j + 1) < matrixSize && matrix[i][j + 1] == matrix[i][j]) {
      rigth[i][j] = rigth[i][j + 1] + 1;
      if (rigth[i][j] == MAX_LENGTH_DNA) {
        c++;
      }
    }
    return c;
  }
}

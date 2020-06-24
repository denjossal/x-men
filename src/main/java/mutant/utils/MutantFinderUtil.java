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
package mutant.utils;

public class MutantFinderUtil {

  private MutantFinderUtil() {
  }

  public static boolean isValidChipDna(char c) {
    return c == 'A' || c == 'C' || c == 'G' || c == 'T';
  }

  public static void fillByPositionAllMatrix(int[][] dleft, int[][] down, int[][] drigth,
      int[][] rigth, int i, int j) {
    if (dleft[i][j] == 0) {
      dleft[i][j] = 1;
    }
    if (down[i][j] == 0) {
      down[i][j] = 1;
    }
    if (drigth[i][j] == 0) {
      drigth[i][j] = 1;
    }
    if (rigth[i][j] == 0) {
      rigth[i][j] = 1;
    }
  }
}
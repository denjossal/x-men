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
package mutant.models.response;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class MutantFinderStatsResponse {

  public static final int PRECISION = 4;
  public static final int EXP = 10;
  private int count_human_dna;
  private int count_mutant_dna;
  private double ratio;

  public MutantFinderStatsResponse(int count_human_dna, int count_mutant_dna) {
    this.count_human_dna = count_human_dna;
    this.count_mutant_dna = count_mutant_dna;
    this.ratio = (count_human_dna == 0 ||
        count_mutant_dna == 0) ?
        count_mutant_dna : round(((double) count_mutant_dna / (double) count_human_dna), PRECISION);
  }

  public int getCount_mutant_dna() {
    return count_mutant_dna;
  }

  public int getCount_human_dna() {
    return count_human_dna;
  }

  public double getRatio() {
    return ratio;
  }

  public void setCount_mutant_dna(int count_mutant_dna) {
    this.count_mutant_dna = count_mutant_dna;
  }

  public void setCount_human_dna(int count_human_dna) {
    this.count_human_dna = count_human_dna;
  }

  private static double round(double value, int precision) {
    int scale = (int) Math.pow(EXP, precision);
    return (double) Math.round(value * scale) / scale;
  }
}

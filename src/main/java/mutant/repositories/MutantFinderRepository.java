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
package mutant.repositories;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import mutant.models.response.MutantFinderStatsResponse;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Singleton
public class MutantFinderRepository {

  private static final int COUNT_GENERAL_DNA = 0;
  private static final String TABLE_NAME = "stats";
  private static final String ID_COLUMN = "id";
  private static final String HUMAN_COLUMN = "humanCount";
  private static final String MUTANT_COLUMN = "mutantCount";
  private static final String MUTANT_STATS = "MUTANT_STATS";
  private static final int INCREMENT = 1;

  private final AmazonDynamoDBAsync client;

  public MutantFinderRepository(AmazonDynamoDBAsync client) {
    this.client = client;
  }

  public Maybe<MutantFinderStatsResponse> getStats() {
    Map<String, AttributeValue> searchCriteria = new HashMap<>();
    searchCriteria.put(ID_COLUMN, new AttributeValue().withS(MUTANT_STATS));

    GetItemRequest request = new GetItemRequest()
        .withTableName(TABLE_NAME)
        .withKey(searchCriteria)
        .withAttributesToGet(HUMAN_COLUMN)
        .withAttributesToGet(MUTANT_COLUMN);
    return Maybe.fromFuture(client.getItemAsync(request))
        .subscribeOn(Schedulers.io())
        .filter(result -> result.getItem() != null)
        .map(result -> new MutantFinderStatsResponse(
            Integer.parseInt(result.getItem().get(HUMAN_COLUMN).getN()),
            Integer.parseInt(result.getItem().get(MUTANT_COLUMN).getN())));
  }

  public void saveStats(boolean mutant) {
    MutantFinderStatsResponse mutantFinderStatsResponse = getMutantFinderStatsResponse(mutant);
    Map<String, AttributeValue> item = new HashMap<>();
    item.put(ID_COLUMN, new AttributeValue().withS(MUTANT_STATS));
    item.put(HUMAN_COLUMN,
        new AttributeValue().withN(String.valueOf(mutantFinderStatsResponse.getCount_human_dna())));
    item.put(MUTANT_COLUMN, new AttributeValue()
        .withN(String.valueOf(mutantFinderStatsResponse.getCount_mutant_dna())));

    PutItemRequest putRequest = new PutItemRequest()
        .withTableName(TABLE_NAME)
        .withItem(item);

    Single.fromFuture(client.putItemAsync(putRequest))
        .subscribeOn(Schedulers.io())
        .map(result -> MUTANT_STATS);
  }

  private MutantFinderStatsResponse getMutantFinderStatsResponse(boolean mutant) {
    Maybe<MutantFinderStatsResponse> mutantFinderStatsResponseMaybe = getStats();
    MutantFinderStatsResponse mutantFinderStatsResponse = mutantFinderStatsResponseMaybe
        .blockingGet();
    if (Objects.isNull(mutantFinderStatsResponse)) {
      mutantFinderStatsResponse = new MutantFinderStatsResponse(COUNT_GENERAL_DNA, COUNT_GENERAL_DNA);
    }
    if (mutant) {
      mutantFinderStatsResponse
          .setCount_mutant_dna(mutantFinderStatsResponse.getCount_mutant_dna() + INCREMENT);
    } else {
      mutantFinderStatsResponse
          .setCount_human_dna(mutantFinderStatsResponse.getCount_human_dna() + INCREMENT);
    }
    return mutantFinderStatsResponse;
  }
}
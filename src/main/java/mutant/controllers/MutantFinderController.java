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
package mutant.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.reactivex.Maybe;
import mutant.models.request.MutantFinderRequest;
import mutant.models.response.MutantFinderStatsResponse;
import mutant.repositories.MutantFinderRepository;
import mutant.services.MutantFinderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller()
public class MutantFinderController {

  private static final Logger logger = LoggerFactory.getLogger(MutantFinderController.class);

  private final MutantFinderService mutantFinderService;
  private final MutantFinderRepository mutantFinderRepository;

  public MutantFinderController(MutantFinderService mutantFinderService,
      MutantFinderRepository mutantFinderRepository) {
    this.mutantFinderService = mutantFinderService;
    this.mutantFinderRepository = mutantFinderRepository;
  }

  @Post(value = "/mutant", consumes = MediaType.APPLICATION_JSON)
  public HttpResponse mutant(@Body MutantFinderRequest dna) {
    logger.info("Processing a new DNA");
    boolean resp = mutantFinderService.isMutant(dna.getDna());
    mutantFinderRepository.saveStats(resp);
    return mutantFinderService.isMutant(dna.getDna()) ? HttpResponse.ok()
        : HttpResponse.badRequest();
  }

  @Get(value = "/stats", produces = MediaType.APPLICATION_JSON)
  public Maybe<MutantFinderStatsResponse> stats() {
    return mutantFinderRepository.getStats();
  }

}

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
package mutant;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import javax.inject.Inject;
import mutant.models.request.MutantFinderRequest;
import mutant.repositories.MutantFinderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
public class MutantFinderControllerTest {
    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    MutantFinderRepository mutantFinderRepository;

    @Test
    void testIsMutant() {
        String[]mutantDna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        MutantFinderRequest mutantFinderRequest = new MutantFinderRequest(mutantDna);
        HttpRequest<MutantFinderRequest> request = HttpRequest.POST("/mutant",mutantFinderRequest);
        HttpResponse response = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    void testIsNotMutant() {
        String[]humanDna = {"ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"};;
        MutantFinderRequest mutantFinderRequest = new MutantFinderRequest(humanDna);
        HttpRequest<MutantFinderRequest> request = HttpRequest.POST("/mutant",mutantFinderRequest);
        HttpClientResponseException e = Assertions.assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().exchange(request));
        HttpResponse response = e.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    }
}

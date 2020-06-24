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

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.*;
import com.amazonaws.services.lambda.runtime.*;
import io.micronaut.function.aws.proxy.MicronautLambdaContainerHandler;
import java.io.*;

public class StreamLambdaHandler implements RequestStreamHandler {
    private static MicronautLambdaContainerHandler handler; // <1>
    static {
        try {
            handler = MicronautLambdaContainerHandler.getAwsProxyHandler();
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Micronaut", e);
        }
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
        handler.proxyStream(inputStream, outputStream, context); // <2>
    }
}
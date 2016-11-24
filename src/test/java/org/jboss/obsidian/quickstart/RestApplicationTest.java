/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.obsidian.quickstart;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.Json;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.jboss.obsidian.quickstart.service.Greeting;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class RestApplicationTest {

    Vertx vertx;

    @Before
    public void before() {
        vertx = Vertx.vertx();
        vertx.deployVerticle(new RestApplication());
    }

    @After
    public void after(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void callGreetingTest(TestContext context) {
        // Send a request and get a response
        HttpClient client = vertx.createHttpClient();
        Async async = context.async();
        HttpClientRequest req =  client.get(8080, "localhost", "/greeting", resp -> {
            context.assertEquals(200, resp.statusCode());
            resp.bodyHandler(body -> {
                final Greeting greeting = Json.decodeValue(body.toString(), Greeting.class);
                context.assertEquals("Hello, World!",greeting.getContent());
            });
            async.complete();
        });
        req.exceptionHandler(context::fail);
        req.end();
    }

    @Test
    public void callGreetingWithParamTest(TestContext context) {
        // Send a request and get a response
        HttpClient client = vertx.createHttpClient();
        Async async = context.async();
        HttpClientRequest req =  client.get(8080, "localhost", "/greeting?name=Charles", resp -> {
            context.assertEquals(200, resp.statusCode());
            resp.bodyHandler(body -> {
                final Greeting greeting = Json.decodeValue(body.toString(), Greeting.class);
                context.assertEquals("Hello, Charles!",greeting.getContent());
            });
            async.complete();
        });
        req.exceptionHandler(context::fail);
        req.end();
    }

}

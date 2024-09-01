package org.vaaron.vertx.web.handler;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpResponseExpectation;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ChainAuthHandler;
import io.vertx.ext.web.handler.SimpleAuthenticationHandler;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Test class for ChainAuthHandler implementation.
 *
 * @author Andrei Tulba
 */
@ExtendWith(VertxExtension.class)
@Timeout(10_000)
class ChainAuthHandlerImplTest {

  /**
   * Tests the nested chain authentication handler.
   *
   * @param vertx the Vert.x instance
   * @param testContext the test context
   */
  @Test
  void testNestedChainHandler(Vertx vertx, VertxTestContext testContext) {
    Router router = Router.router(vertx);
    router
        .get("/test")
        .handler(BodyHandler.create())
        .handler(securityHandler())
        .handler(
            routingContext -> {
              String body = "HELLO WORLD";
              routingContext
                  .response()
                  .putHeader("Content-Length", "" + body.length())
                  .write(body)
                  .onFailure(routingContext::fail);
            });
    Future<WebClient> webClientFuture = startHttpServer(vertx, router);
    webClientFuture
        .compose(
            webClient ->
                webClient
                    .get("/test")
                    .timeout(2000)
                    .send()
                    .expecting(HttpResponseExpectation.SC_OK))
        .onComplete(testContext.succeedingThenComplete());
  }

  /** Creates a security handler using a chain of authentication handlers. */
  private AuthenticationHandler securityHandler() {
    SimpleAuthenticationHandler doNothingHandler =
        SimpleAuthenticationHandler.create()
            .authenticate(context -> Future.succeededFuture(User.fromName("test")));
    SimpleAuthenticationHandler authFailureHandler =
        SimpleAuthenticationHandler.create()
            .authenticate(context -> Future.failedFuture("Authentication failed"));
    ChainAuthHandler doNothingChainHandler =
        ChainAuthHandler.any().add(doNothingHandler).add(doNothingHandler);
    return ChainAuthHandler.any()
        .add(authFailureHandler)
        .add(authFailureHandler)
        .add(doNothingChainHandler);
  }

  /** Starts an HTTP server with the given router. */
  private static Future<WebClient> startHttpServer(Vertx vertx, Router router) {
    return vertx
        .createHttpServer()
        .requestHandler(router)
        .listen(0)
        .map(
            server ->
                WebClient.create(
                    vertx,
                    new WebClientOptions()
                        .setDefaultPort(server.actualPort())
                        .setConnectTimeout(1000)));
  }
}

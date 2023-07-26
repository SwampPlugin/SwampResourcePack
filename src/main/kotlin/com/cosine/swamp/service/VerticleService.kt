package com.cosine.swamp.service

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.RoutingContext
import java.nio.file.Files
import java.nio.file.Paths
import java.util.logging.Logger

class VerticleService(
    private val logger: Logger
) : AbstractVerticle() {

    override fun start(startPromise: Promise<Void>) {
        vertx.createHttpServer()
            .requestHandler { request ->
                request.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x!")
            }
            .listen(25564) { http ->
                if (http.succeeded()) {
                    startPromise.complete()
                    logger.info("HTTP 서버가 성공적으로 켜졌습니다. [포트: 25564]")
                } else {
                    startPromise.fail(http.cause())
                }
            }
    }
}

package auth.handler;

import io.jsonwebtoken.Jwts;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

import static java.time.temporal.ChronoUnit.MINUTES;

public class JwtHandler implements HttpHandler {

    private final Key key;

    public JwtHandler(Key key) {
        this.key = key;
    }

    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) {
        httpServerExchange.getResponseHeaders().put(new HttpString("jwt"),  createJwt());
        httpServerExchange.endExchange();
    }

    private String createJwt() {
        return Jwts.builder()
            .setSubject("admin")
            .signWith(key)
            .setExpiration(Date.from(Instant.now().plus(10, MINUTES)))
            .compact();
    }
}

package auth.handler;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;

import java.security.Key;

public class SecurityHandler implements HttpHandler {

    private final Key key;
    private final HttpHandler next;

    public SecurityHandler(Key key, HttpHandler next) {
        this.key = key;
        this.next = next;
    }

    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) {
        HeaderValues jwt = httpServerExchange.getRequestHeaders().get("jwt");
        try {
            if (jwt != null) {
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt.getFirst());
                next.handleRequest(httpServerExchange);
                return;
            }
            endRequest(httpServerExchange);
        } catch (Exception e) {
            System.out.println("JWT NOT TRUSTED");
            endRequest(httpServerExchange);
        }
    }

    private void endRequest(HttpServerExchange httpServerExchange) {
        httpServerExchange.setStatusCode(401).endExchange();
    }
}

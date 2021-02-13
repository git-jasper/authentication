package auth.handler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import static io.undertow.util.Methods.OPTIONS;

public class CorsHandler implements HttpHandler {

    private final HttpHandler next;

    public CorsHandler(HttpHandler next) {
        this.next = next;
    }

    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
        httpServerExchange.getResponseHeaders().put(new HttpString("Access-Control-Allow-Origin"), "http://localhost:8080");
        httpServerExchange.getResponseHeaders().put(new HttpString("Access-Control-Allow-Methods"), "POST, GET, OPTIONS");
        httpServerExchange.getResponseHeaders().put(new HttpString("Access-Control-Allow-Headers"), "*");
        httpServerExchange.getResponseHeaders().put(new HttpString("Access-Control-Expose-Headers"), "jwt");
        if (preFlightRequest(httpServerExchange)) {
            httpServerExchange.endExchange();
            return;
        }
        next.handleRequest(httpServerExchange);
    }

    private boolean preFlightRequest(HttpServerExchange httpServerExchange) {
        return OPTIONS.equals(httpServerExchange.getRequestMethod());
    }
}

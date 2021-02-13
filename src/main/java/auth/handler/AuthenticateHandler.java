package auth.handler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.security.Key;

public class AuthenticateHandler implements HttpHandler {

    private final JwtHandler jwtHandler;

    public AuthenticateHandler(Key key) {
        this.jwtHandler = new JwtHandler(key);
    }

    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) {
        StringBuilder requestBody = new StringBuilder((int) httpServerExchange.getResponseBytesSent());
        httpServerExchange.getRequestReceiver().receiveFullString((exchange, message) -> requestBody.append(message));
        if ("admin".equals(requestBody.toString())) {
            jwtHandler.handleRequest(httpServerExchange);
            return;
        }
        httpServerExchange.setStatusCode(401).endExchange();
    }
}

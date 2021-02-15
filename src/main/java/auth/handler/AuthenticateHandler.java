package auth.handler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
        JSONObject json = parseToJson(requestBody);
        if ("user".equals(json.get("username")) && "pass".equals(json.get("password"))) {
            jwtHandler.handleRequest(httpServerExchange);
            return;
        }
        httpServerExchange.setStatusCode(401).endExchange();
    }

    private JSONObject parseToJson(StringBuilder requestBody) {
        try {
            return (JSONObject) new JSONParser().parse(requestBody.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }
}

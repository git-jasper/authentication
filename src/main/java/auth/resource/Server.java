package auth.resource;

import auth.handler.AuthenticateHandler;
import auth.handler.CorsHandler;
import auth.handler.HelloHandler;
import auth.handler.SecurityHandler;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.undertow.Handlers;
import io.undertow.Undertow;

import java.security.Key;

public class Server {

    public static void main(final String[] args) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        Undertow server = Undertow.builder()
            .addHttpListener(8081, "localhost")
            .setHandler(Handlers.path()
                .addExactPath("/secure", new CorsHandler(new SecurityHandler(key, new HelloHandler())))
                .addExactPath("/authenticate", new CorsHandler(new AuthenticateHandler(key))))
            .build();
        server.start();
    }
}

package com.example.colabplatform.security;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.List;

public class JwtValidator {
    private static final List<String> allowedIssuers = Collections.singletonList("http://127.0.0.1:8080/realms/ColabPlatformRealm");

    private String getKeycloakCertificateUrl(DecodedJWT token) {
        return token.getIssuer() + "/protocol/openid-connect/certs";
    }

    private RSAPublicKey loadPublicKey(DecodedJWT token) throws JwkException, MalformedURLException {

        final String url = getKeycloakCertificateUrl(token);
        JwkProvider provider = new UrlJwkProvider(new URL(url));

        return (RSAPublicKey) provider.get(token.getKeyId()).getPublicKey();
    }

    public void validate(String token) throws JWTDecodeException, JwkException, MalformedURLException, InvalidParameterException {
        final DecodedJWT jwt = JWT.decode(token);
        if (!allowedIssuers.contains(jwt.getIssuer())) {
            throw new InvalidParameterException(String.format("Unknown Issuer %s", jwt.getIssuer()));
        }
        RSAPublicKey publicKey = loadPublicKey(jwt);
        Algorithm algorithm = Algorithm.RSA256(publicKey, null);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(jwt.getIssuer())
                .build();
        verifier.verify(token);
    }
}

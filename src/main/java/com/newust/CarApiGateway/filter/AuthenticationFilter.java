package com.newust.CarApiGateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>
{

    public static class Config{}

    public AuthenticationFilter(){
        super(Config.class);
    }
    @Autowired
    private RouteValidator validator;

    @Override
    public GatewayFilter apply(Config config){
        return((exchange, chain) ->{
            if(validator.isSecured.test(exchange.getRequest())){
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new RuntimeException("missing authorization header");
                }
                String authHeaderToken = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeaderToken != null && authHeaderToken.startsWith("Bearer")) {
                    // remove Bearer from front
                    authHeaderToken = authHeaderToken.substring(7);
                }
                try {
                    // now consume /api/auth/validate/token of authentication-service using
                    // RestClient
                    // can keep this call in a seperate JwtUtil class and call
                    RestClient restClient = RestClient.create();
                    restClient
                            .get()
                            .uri("http://localhost:8011/api/auth/validate/token?token=" + authHeaderToken)
                            .retrieve()
                            .body(Boolean.class);
                    // also instead of making a RestClient call for every request, we can validate
                    // the token here in api-gateway itself
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    throw new RuntimeException("Inavlid Access!! : " + e.getMessage());
                } }

            return chain.filter(exchange);


        });


    }
}



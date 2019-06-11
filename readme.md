# Spring & Grafana

Modules:

- **spring-zuul**: zull reverse proxy that stands in front of grafana instance. It uses spring oauth2 security to authorize grafana requests.
- **spring-authorization**: the authorization server that creates oauth2 jwt tokens
- **grafana-json-datasource**: backend implementation for [simple-json-datasource](https://grafana.com/plugins/grafana-simple-json-datasource)

## What is Grafana?

The leading open source software for time series analytics

https://grafana.com/


### Installation

Grafana is very easy to install and run using the official [docker container](https://grafana.com/docs/installation/docker/).

docker-compose.yml config example:

      grafana:
        image: grafana/grafana:master
        ports:
          - "3030:3000"
        environment:
          - GF_SERVER_DOMAIN=gateway:8080
          - GF_SERVER_ROOT_URL=%(protocol)s://%(domain)s/grafana
          - GF_AUTH_PROXY_ENABLED=true
          - GF_AUTH_PROXY_HEADER_NAME=X-WEBAUTH-USER
          - GF_AUTH_PROXY_HEADER_PROPERTY=username
          - GF_AUTH_PROXY_AUTO_SIGN_UP=true
          - GF_AUTH_PROXY_WHITELIST=
          - GF_DATABASE_HOST=postgres:5432
          - GF_DATABASE_TYPE=postgres
          - GF_DATABASE_USER=postgres
          - GF_DATABASE_PASSWORD=postgres
          - GF_INSTALL_PLUGINS=grafana-clock-panel,grafana-simple-json-datasource,grafana-piechart-panel,grafana-worldmap-panel,natel-plotly-panel
          
Grafana instance will run behind a reverse proxy and will store it's data in postgres.
      
###  Authentication

Grafana Auth proxy config with docker:

```$xslt       
- GF_SERVER_DOMAIN=gateway:8080
- GF_SERVER_ROOT_URL=%(protocol)s://%(domain)s/grafana
- GF_AUTH_PROXY_ENABLED=true
- GF_AUTH_PROXY_HEADER_NAME=X-WEBAUTH-USER
- GF_AUTH_PROXY_HEADER_PROPERTY=username
- GF_AUTH_PROXY_AUTO_SIGN_UP=true
- GF_AUTH_PROXY_WHITELIST=
```

## spring-zuul: reverse proxy & authentication

The zuul gateway will redirect all requests that match /grafana/** to the grafana instance:
 

```$xslt
zuul:
  routes:
    grafana:
      sensitiveHeaders: Authorization
      path: /grafana/**
      url: http://${GRAFANA_HOST:localhost}:3000
```

The gateway uses @EnableResourceServer and will redirect the incoming requests to grafana if a valid OAuth2 access token is present.


```$xslt

@Configuration
@EnableResourceServer
public class SecurityConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(final ResourceServerSecurityConfigurer resources) {
        resources.tokenExtractor(new CookieTokenExtractor());
    }


    @Override
    public void configure(final HttpSecurity http) throws Exception {

        http.authorizeRequests().
                antMatchers("/authorization/oauth/**").
                permitAll().
                antMatchers("/authorization/status").
                authenticated().
                antMatchers("/grafana/public/**").permitAll().
                antMatchers("/grafana/**").authenticated();
    }

}
```

By default @EnableResourceServer uses an BearerTokenExtractor that will search for an OAuth2 access token in the Authorization Bearer or in the request parameter.
The grafana requests will send an "access_token" cookie, so an CookieTokenExtractor that extends the default BearerTokenExtractor is needed.

```$xslt

@Component
public class CookieTokenExtractor extends BearerTokenExtractor {

    static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";

    @Override
    public Authentication extract(HttpServletRequest request) {
        Authentication authentication = super.extract(request);
        if (authentication != null) {
            return authentication;
        }


        if (request.getHeader("Authorization") == null) {
            Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]);
            return Arrays.stream(cookies)
                    .filter(cookie -> ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .map(token -> new PreAuthenticatedAuthenticationToken(token, ""))
                    .orElse(null);
        }

        return null;
    }
}

```

The GrafanaPreFilter will run after spring authorization and will set the grafana **X-WEBAUTH-USER** header 
with the grafana username(admin, viewer, etc) according to users's ROLE.

Additionally, it will set the access_token cookie inside the iframe.


```
@Component
public class GrafanaPreFilter extends ZuulFilter {

    @Value("${zuul.routes.grafana.url}")
    String grafanaHost;

    @Override
    public Object run() {
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean hasAdminRole = authorities.stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        final RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader("X-WEBAUTH-USER", hasAdminRole ? "admin" : "viewer");

        Optional.ofNullable(ctx.get("ACCESS_TOKEN")).map(Object::toString).ifPresent(token -> {
            ctx.getResponse().addCookie(new Cookie(GrafanaCookieTokenExtractor.ACCESS_TOKEN_COOKIE_NAME, token));
        });

        return null;
    }

    @Override
    public boolean shouldFilter() {
        return grafanaHost.equals(RequestContext.getCurrentContext().getRouteHost().toString());
    }

    @Override
    public int filterOrder() {
//        after OAuth2TokenRelayFilter
        return 11;
    }

    @Override
    public String filterType() {
        return "pre";
    }

}

```

### Json datasource backend implementation

#### Auth

In order to use spring authorization the access_token cookie should be whitelisted in the simple-json-datasource config.

The spring application will use a cookie token enhancer and will validate the provided token.

#### Implementation

https://github.com/grafana/simple-json-datasource

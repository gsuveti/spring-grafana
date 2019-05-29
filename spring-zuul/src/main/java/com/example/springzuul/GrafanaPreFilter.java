package com.example.springzuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.util.Optional;

@Component
public class GrafanaPreFilter extends ZuulFilter {

    @Value("${zuul.routes.grafana.url}")
    String grafanaHost;

    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader("X-WEBAUTH-USER", "admin");

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

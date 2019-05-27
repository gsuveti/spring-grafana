package com.example.springzuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GrafanaPostFilter extends ZuulFilter {

    @Value("${zuul.routes.grafana.url}")
    String grafanaHost;

    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return grafanaHost.equals(RequestContext.getCurrentContext().getRouteHost().toString());
    }

    @Override
    public int filterOrder() {
        return 1120;
    }

    @Override
    public String filterType() {
        return "post";
    }

}

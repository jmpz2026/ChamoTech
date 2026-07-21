package com.chamo.chamotech.security;

import java.util.List;

public class PublicRoutes {

    public static final List<String> PUBLIC_ROUTES = List.of(
            "/auth",
            "/swagger-ui",
            "/v3/api-docs",
            "/h2-console"
    );

    public static final List<String> PUBLIC_GET_ROUTES = List.of(
            "/customers",
            "/categories",
            "/tags",
            "/products"
    );

    private PublicRoutes() {
    }
}

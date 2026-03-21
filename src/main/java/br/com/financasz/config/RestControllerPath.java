package br.com.financasz.config;

public final class RestControllerPath {

    private RestControllerPath() {}

    public static final String API_ROOT = "/api/v1";

    public static final String USERS = API_ROOT + "/users";
    
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String ID = "/{id}";
    
}

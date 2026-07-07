package core;

import challenge.login.LoginHandler;
import challenge.login.LoginService;
import config.Configuration;
import config.ConfigurationLoader;
import database.DatabaseManager;
import database.SchemaInitializer;
import exceptions.LoginServiceException;
import exceptions.SchemaInitializerException;
import http.IndexHandler;
import http.Route;
import http.Router;
import http.VulnaHttpServer;


public class Main {
    public static void main(String[] args) throws Exception, SchemaInitializerException, LoginServiceException {
        // configs
        Configuration appConfig   = ConfigurationLoader.load("application.properties");
        Configuration loginConfig = ConfigurationLoader.load("challenges/login/challenge.properties");

        // database
        DatabaseManager dbManager = new DatabaseManager(appConfig);

        // schema
        new SchemaInitializer(dbManager, loginConfig).initialize("login");

        // challenges
        LoginService loginService = new LoginService(dbManager);
        LoginHandler loginHandler = new LoginHandler(loginService, loginConfig);

        // routing
        Router router = new Router();
//        router.register(new Route("/",      new IndexHandler()));
        router.register(new Route("/login", loginHandler));

        // server
        VulnaHttpServer server = new VulnaHttpServer(appConfig);

        // application
        new Application(server, router).start();
    }
}

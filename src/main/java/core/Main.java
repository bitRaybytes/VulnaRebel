package core;

import challenge.login.LoginSqliHandler;
import challenge.login.LoginService;
import challenge.reflectedxss.ReflectedXssHandler;
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
        LoginSqliHandler loginHandler = new LoginSqliHandler(loginService, loginConfig);

        // TODO: Optional - for later:
        // Idea for a challenge abstraction.
        // But overloading a constructor would be difficult
        // to maintain and also there are challenges that do
        // not rely on a database at all like XSS routing
        Router router = new Router();
        router.register(new Route("/",      new IndexHandler()));
        router.register(new Route("/login", loginHandler));
        router.register(new Route("/reflectedxss", new ReflectedXssHandler()));

        // server
        VulnaHttpServer server = new VulnaHttpServer(appConfig);

        // application
        new Application(server, router).start();
    }
}

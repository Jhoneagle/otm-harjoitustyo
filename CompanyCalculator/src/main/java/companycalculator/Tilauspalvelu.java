package companycalculator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import companycalculator.database.DbLauncher;
import companycalculator.ui.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Tilauspalvelu extends Application {

    private DbLauncher db;
    private Scene mainScene;
    private Scene newTuoteScene;
    private Scene newAsiakasScene;
    private Scene newTilausScene;
    private Scene listTuoteScene;
    private Scene listAsiakasScene;
    private Scene listTilausScene;
    private Scene tuoteLoopScene;
    private Stage stage;

    @Override
    public void init() {
        Properties properties = new Properties();
        String databaseAddress;

        try {
            properties.load(new FileInputStream("config.properties"));
            databaseAddress = "jdbc:sqlite:" + properties.getProperty("mainDatabase");
        } catch(Exception e) {
            databaseAddress = "jdbc:sqlite:main.db";
        }

        this.db = new DbLauncher(databaseAddress);
        this.db.initializeSqlTables();
        this.db.intializeAllDao();

        // Main Scene initialize
        FXMLLoader mainSceneLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent mainPane = null;
        try {
            mainPane = mainSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(DbLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }
        MainController mainController = mainSceneLoader.getController();
        mainController.setDbLauncher(this.db);
        mainController.setApplication(this);
        this.mainScene = new Scene(mainPane);

        // New tuote Scene initialize
        FXMLLoader newTuoteSceneLoader = new FXMLLoader(getClass().getResource("/fxml/newtuote.fxml"));
        Parent newTuotePane = null;
        try {
            newTuotePane = newTuoteSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(DbLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }
        NewTuoteController newTuoteController = newTuoteSceneLoader.getController();
        newTuoteController.setDbLauncher(this.db);
        newTuoteController.setApplication(this);
        this.newTuoteScene = new Scene(newTuotePane);

        // New asiakas Scene initialize
        FXMLLoader newAsiakasSceneLoader = new FXMLLoader(getClass().getResource("/fxml/newasiakas.fxml"));
        Parent newAsiakasPane = null;
        try {
            newAsiakasPane = newAsiakasSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(DbLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }
        NewAsiakasController newAsiakasController = newAsiakasSceneLoader.getController();
        newAsiakasController.setDbLauncher(this.db);
        newAsiakasController.setApplication(this);
        this.newAsiakasScene = new Scene(newAsiakasPane);

        // New tilaus Scene initialize
        FXMLLoader newTilausSceneLoader = new FXMLLoader(getClass().getResource("/fxml/newtilaus.fxml"));
        Parent newTilausPane = null;
        try {
            newTilausPane = newTilausSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(DbLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }
        NewTilausController newTilausController = newTilausSceneLoader.getController();
        newTilausController.setDbLauncher(this.db);
        newTilausController.setApplication(this);
        this.newTilausScene = new Scene(newTilausPane);

        // List tuote Scene initialize
        FXMLLoader listTuoteSceneLoader = new FXMLLoader(getClass().getResource("/fxml/listtuote.fxml"));
        Parent listTuotePane = null;
        try {
            listTuotePane = listTuoteSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(DbLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }
        ListTuoteController listTuoteController = listTuoteSceneLoader.getController();
        listTuoteController.setDbLauncher(this.db);
        listTuoteController.setApplication(this);
        this.listTuoteScene = new Scene(listTuotePane);

        // List asiakas Scene initialize
        FXMLLoader listAsiakasSceneLoader = new FXMLLoader(getClass().getResource("/fxml/listasiakas.fxml"));
        Parent listAsiakasPane = null;
        try {
            listAsiakasPane = listAsiakasSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(DbLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }
        ListAsiakasController listAsiakasController = listAsiakasSceneLoader.getController();
        listAsiakasController.setDbLauncher(this.db);
        listAsiakasController.setApplication(this);
        this.listAsiakasScene = new Scene(listAsiakasPane);

        // List tilaus Scene initialize
        FXMLLoader listTilausSceneLoader = new FXMLLoader(getClass().getResource("/fxml/listtilaus.fxml"));
        Parent listTilausPane = null;
        try {
            listTilausPane = listTilausSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(DbLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }
        ListTilausController listTilausController = listTilausSceneLoader.getController();
        listTilausController.setDbLauncher(this.db);
        listTilausController.setApplication(this);
        this.listTilausScene = new Scene(listTilausPane);

        // Tuote loop Scene initialize
        FXMLLoader tuoteLoopSceneLoader = new FXMLLoader(getClass().getResource("/fxml/tuoteloop.fxml"));
        Parent tuoteLoopPane = null;
        try {
            tuoteLoopPane = tuoteLoopSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(DbLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }
        TuoteLoopController tuoteLoopController = tuoteLoopSceneLoader.getController();
        tuoteLoopController.setDbLauncher(this.db);
        tuoteLoopController.setApplication(this);
        this.tuoteLoopScene = new Scene(tuoteLoopPane);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.stage = primaryStage;

        this.stage.setTitle("Tilausten käsittely palvelu");
        setMainScene();
        this.stage.show();
    }

    public void setMainScene() {
        this.stage.setScene(this.mainScene);
    }

    public void setNewTuoteScene() {
        this.stage.setScene(this.newTuoteScene);
    }

    public void setNewAsiakasScene() {
        this.stage.setScene(this.newAsiakasScene);
    }

    public void setNewTilausScene() {
        this.stage.setScene(this.newTilausScene);
    }

    public void setListTuoteScene() {
        this.stage.setScene(this.listTuoteScene);
    }

    public void setListAsiakasScene() {
        this.stage.setScene(this.listAsiakasScene);
    }

    public void setListTilausScene() {
        this.stage.setScene(this.listTilausScene);
    }

    public void setTuoteLoopScene() {
        this.stage.setScene(this.tuoteLoopScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package companycalculator;

import companycalculator.ui.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tilauspalvelu extends Application {

    private Scene mainScene;
    private Scene newTuoteScene;
    private Scene newAsiakasScene;
    private Scene newTilausScene;
    private Scene listTuoteScene;
    private Scene listAsiakasScene;
    private Scene listTilausScene;
    private Scene tuoteLoopScene;
    private Stage stage;
    private Scene editTuoteScene;
    private Scene editTilausScene;

    @Override
    public void init() {
        // Main Scene initialize
        FXMLLoader mainSceneLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent mainPane = null;
        try {
            mainPane = mainSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(Tilauspalvelu.class.getName()).log(Level.SEVERE, null, ex);
        }
        MainController mainController = mainSceneLoader.getController();
        mainController.setApplication(this);
        this.mainScene = new Scene(mainPane);

        // New tuote Scene initialize
        FXMLLoader newTuoteSceneLoader = new FXMLLoader(getClass().getResource("/fxml/newtuote.fxml"));
        Parent newTuotePane = null;
        try {
            newTuotePane = newTuoteSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(Tilauspalvelu.class.getName()).log(Level.SEVERE, null, ex);
        }
        NewTuoteController newTuoteController = newTuoteSceneLoader.getController();
        newTuoteController.setApplication(this);
        this.newTuoteScene = new Scene(newTuotePane);

        // New asiakas Scene initialize
        FXMLLoader newAsiakasSceneLoader = new FXMLLoader(getClass().getResource("/fxml/newasiakas.fxml"));
        Parent newAsiakasPane = null;
        try {
            newAsiakasPane = newAsiakasSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(Tilauspalvelu.class.getName()).log(Level.SEVERE, null, ex);
        }
        NewAsiakasController newAsiakasController = newAsiakasSceneLoader.getController();
        newAsiakasController.setApplication(this);
        this.newAsiakasScene = new Scene(newAsiakasPane);

        // New tilaus Scene initialize
        FXMLLoader newTilausSceneLoader = new FXMLLoader(getClass().getResource("/fxml/newtilaus.fxml"));
        Parent newTilausPane = null;
        try {
            newTilausPane = newTilausSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(Tilauspalvelu.class.getName()).log(Level.SEVERE, null, ex);
        }
        NewTilausController newTilausController = newTilausSceneLoader.getController();
        newTilausController.setApplication(this);
        this.newTilausScene = new Scene(newTilausPane);

        // List tuote Scene initialize
        FXMLLoader listTuoteSceneLoader = new FXMLLoader(getClass().getResource("/fxml/listtuote.fxml"));
        Parent listTuotePane = null;
        try {
            listTuotePane = listTuoteSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(Tilauspalvelu.class.getName()).log(Level.SEVERE, null, ex);
        }
        ListTuoteController listTuoteController = listTuoteSceneLoader.getController();
        listTuoteController.setApplication(this);
        this.listTuoteScene = new Scene(listTuotePane);

        // List asiakas Scene initialize
        FXMLLoader listAsiakasSceneLoader = new FXMLLoader(getClass().getResource("/fxml/listasiakas.fxml"));
        Parent listAsiakasPane = null;
        try {
            listAsiakasPane = listAsiakasSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(Tilauspalvelu.class.getName()).log(Level.SEVERE, null, ex);
        }
        ListAsiakasController listAsiakasController = listAsiakasSceneLoader.getController();
        listAsiakasController.setApplication(this);
        this.listAsiakasScene = new Scene(listAsiakasPane);

        // List tilaus Scene initialize
        FXMLLoader listTilausSceneLoader = new FXMLLoader(getClass().getResource("/fxml/listtilaus.fxml"));
        Parent listTilausPane = null;
        try {
            listTilausPane = listTilausSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(Tilauspalvelu.class.getName()).log(Level.SEVERE, null, ex);
        }
        ListTilausController listTilausController = listTilausSceneLoader.getController();
        listTilausController.setApplication(this);
        this.listTilausScene = new Scene(listTilausPane);

        // Tuote loop Scene initialize
        FXMLLoader tuoteLoopSceneLoader = new FXMLLoader(getClass().getResource("/fxml/tuoteloop.fxml"));
        Parent tuoteLoopPane = null;
        try {
            tuoteLoopPane = tuoteLoopSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(Tilauspalvelu.class.getName()).log(Level.SEVERE, null, ex);
        }
        TuoteLoopController tuoteLoopController = tuoteLoopSceneLoader.getController();
        tuoteLoopController.setApplication(this);
        this.tuoteLoopScene = new Scene(tuoteLoopPane);
        
        // Edit tuote Scene initialize
        FXMLLoader editTuoteSceneLoader = new FXMLLoader(getClass().getResource("/fxml/edittuote.fxml"));
        Parent editTuotePane = null;
        try {
            editTuotePane = editTuoteSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(Tilauspalvelu.class.getName()).log(Level.SEVERE, null, ex);
        }
        EditTuoteController editTuoteController = editTuoteSceneLoader.getController();
        editTuoteController.setApplication(this);
        this.editTuoteScene = new Scene(editTuotePane);
        
        // Edit tilaus Scene initialize
        FXMLLoader editTilausSceneLoader = new FXMLLoader(getClass().getResource("/fxml/edittilaus.fxml"));
        Parent editTilausPane = null;
        try {
            editTilausPane = editTilausSceneLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(Tilauspalvelu.class.getName()).log(Level.SEVERE, null, ex);
        }
        EditTilausController editTilausController = editTilausSceneLoader.getController();
        editTilausController.setApplication(this);
        this.editTilausScene = new Scene(editTilausPane);
    }

    @Override
    public void start(Stage primaryStage) {
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
    
    public void seteditTuoteScene() {
        this.stage.setScene(this.editTuoteScene);
    }

    public void seteditTilausScene() {
        this.stage.setScene(this.editTilausScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package companycalculator.ui;

import companycalculator.Tilauspalvelu;
import companycalculator.database.DbLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;

public class TuoteLoopController implements Initializable {
    private DbLauncher dbLauncher;
    private Tilauspalvelu application;

    public void setDbLauncher(DbLauncher todoService) {
        this.dbLauncher = todoService;
    }

    public void setApplication(Tilauspalvelu application) {
        this.application = application;
    }

    @FXML
    private void handleSubmit(ActionEvent event) {


        this.application.setMainScene();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

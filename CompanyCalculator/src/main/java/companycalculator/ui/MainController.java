package companycalculator.ui;

import companycalculator.Tilauspalvelu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private Tilauspalvelu application;

    public void setApplication(Tilauspalvelu application) {
        this.application = application;
    }

    @FXML
    private void handleNavigation(ActionEvent event) {
        Node node = (Node) event.getSource();
        String data = (String) node.getUserData();
        int value = Integer.parseInt(data);

        switch (value) {
            case 0:
                this.application.setMainScene();
                break;
            case 1:
                this.application.setNewTuoteScene();
                break;
            case 2:
                this.application.setListTuoteScene();
                break;
            case 3:
                this.application.setNewAsiakasScene();
                break;
            case 4:
                this.application.setListAsiakasScene();
                break;
            case 5:
                this.application.setNewTilausScene();
                break;
            default:
                this.application.setListTilausScene();
                break;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

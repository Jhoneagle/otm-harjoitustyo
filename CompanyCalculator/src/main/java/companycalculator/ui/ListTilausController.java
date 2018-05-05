package companycalculator.ui;

import companycalculator.Tilauspalvelu;
import companycalculator.database.DbLauncher;
import companycalculator.domain.Tilaus;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class ListTilausController implements Initializable {
    private DbLauncher dbLauncher;
    private Tilauspalvelu application;

    @FXML private ListView<String> listView;
    
    @FXML
    private Button removeButton;

    public void setDbLauncher(DbLauncher todoService) {
        this.dbLauncher = todoService;
    }

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

    @FXML
    void remove(ActionEvent event) {
        String teksti = this.listView.getSelectionModel().getSelectedItem();
        if (teksti != null) {
            String[] split = teksti.split(", ");
            String yTunnus = split[1].substring(20);
            String status = split[3].substring(8);
            
            Tilaus tilaus = this.dbLauncher.getTilauspalvelu().findTilaus(yTunnus, status);
            this.dbLauncher.getTilauspalvelu().removeTilaus(tilaus.getId());
        }
    }
    
    @FXML
    void edit(ActionEvent event) {
        
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.listView.getItems().addAll(this.dbLauncher.getTilauspalvelu().listTilaukset());
    }
}

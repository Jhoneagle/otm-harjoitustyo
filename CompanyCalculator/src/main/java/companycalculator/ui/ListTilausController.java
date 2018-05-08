package companycalculator.ui;

import companycalculator.Tilauspalvelu;
import companycalculator.advancelogic.Tilaustoiminnallisuus;
import companycalculator.dao.TilausDao;
import companycalculator.database.JavafxConnectDB;
import companycalculator.domain.Tilaus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ListTilausController implements Initializable {
    private Tilaustoiminnallisuus tilausToiminnallisuus;
    private Tilauspalvelu application;
    private TilausDao tilausdao;

    @FXML private ListView<String> listView;
    
    @FXML
    private Button removeButton;

    public void refresh() {
        this.listView.getItems().setAll(this.tilausToiminnallisuus.listTilaukset());
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
    public void remove(ActionEvent event) {
        String teksti = this.listView.getSelectionModel().getSelectedItem();
        if (teksti != null) {
            String[] split = teksti.split(". ");
            int id = Integer.parseInt(split[0]);
            
            this.tilausToiminnallisuus.removeTilaus(id);
            refresh();
        }
    }
    
    @FXML
    public void edit(ActionEvent event) {
        String teksti = this.listView.getSelectionModel().getSelectedItem();
        if (teksti != null) {
            String[] split = teksti.split(". ");
            int id = Integer.parseInt(split[0]);
            
            Tilaus tilaus = this.tilausdao.findOne(id);
            EditTilausController.setEdit(tilaus);
            
            this.application.seteditTilausScene();
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tilausToiminnallisuus = JavafxConnectDB.getTT();
        this.tilausdao = JavafxConnectDB.getTD();
        this.listView.getItems().setAll(this.tilausToiminnallisuus.listTilaukset());
    }
}

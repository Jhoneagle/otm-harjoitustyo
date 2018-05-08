package companycalculator.ui;

import companycalculator.Tilauspalvelu;
import companycalculator.dao.TuoteDao;
import companycalculator.database.Database;
import companycalculator.database.JavafxConnectDB;
import companycalculator.domain.Tuote;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ListTuoteController implements Initializable {
    private TuoteDao tuotedao;
    private Tilauspalvelu application;

    @FXML private ListView<Tuote> listView;
    
    public void setApplication(Tilauspalvelu application) {
        this.application = application;
    }

    private void refresh() {
        this.listView.getItems().setAll(this.tuotedao.findAll());
    }
    
    @FXML
    private void relist(ActionEvent event) {
        refresh();
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
        Tuote tuote = this.listView.getSelectionModel().getSelectedItem();
        if (tuote != null) {
            this.tuotedao.delete(tuote.getId());
            refresh();
        }
    }
    
    @FXML
    public void edit(ActionEvent event) {
        Tuote tuote = this.listView.getSelectionModel().getSelectedItem();
        if (tuote != null) {
            EditTuoteController.setEdit(tuote);
            
            this.application.seteditTuoteScene();
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Database db = JavafxConnectDB.getDB();
        this.tuotedao = new TuoteDao(db);
        this.listView.getItems().setAll(this.tuotedao.findAll());
    }
}

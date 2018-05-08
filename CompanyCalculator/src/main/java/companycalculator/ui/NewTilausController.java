package companycalculator.ui;

import companycalculator.Tilauspalvelu;
import companycalculator.advancelogic.Tilaustoiminnallisuus;
import companycalculator.database.JavafxConnectDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NewTilausController implements Initializable {
    private Tilaustoiminnallisuus tilausToiminnallisuus;
    private Tilauspalvelu application;

    @FXML
    private TextField ytunnus;
    
    @FXML
    private TextField status;
    
    @FXML
    private TextField paiva;
    
    @FXML 
    private Label errorTunnus;
    
    @FXML 
    private Label errorStatus;
    
    @FXML 
    private Label errorPaiva;
    
    public void setApplication(Tilauspalvelu application) {
        this.application = application;
    }

    @FXML
    private void submit(ActionEvent event) {
        String tunnus = this.ytunnus.getText();
        String date = this.paiva.getText();
        String stats = this.status.getText();
        
        boolean a = tunnus.isEmpty();
        boolean b = stats.isEmpty();
        boolean c = date.isEmpty();
        
        if (a) {
            this.errorTunnus.setText("lis‰‰ uniikki y-tunnus.");
        } else {
            this.errorTunnus.setText("");
        }
        if (b) {
            this.errorStatus.setText("lis‰‰  status (tarjous vai tilaus).");
        } else {
            this.errorStatus.setText("");
        }
        if (c) {
            this.errorPaiva.setText("lis‰‰ p‰iv‰maar‰.");
        } else {
            this.errorPaiva.setText("");
        }
        
        if (!a && !b && !c) {
            this.ytunnus.setText("");
            this.paiva.setText("");
            this.status.setText("");
            
            Tilaustoiminnallisuus.prepareAdd(tunnus, stats, date);
            this.application.setTuoteLoopScene();
        }
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
        this.tilausToiminnallisuus = JavafxConnectDB.getTT();
    }
}



package companycalculator.ui;

import companycalculator.Tilauspalvelu;
import companycalculator.advancelogic.TilausToiminnallisuus;
import companycalculator.dao.PaivaDao;
import companycalculator.database.JavafxConnectDB;
import companycalculator.domain.Paiva;
import companycalculator.domain.Tilaus;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditTilausController implements Initializable {
    private TilausToiminnallisuus tilausToiminnallisuus;
    private PaivaDao paivadao;
    private Tilauspalvelu application;
    
    @FXML
    private TextField status;
    
    @FXML
    private TextField paiva;
    
    @FXML
    private ListView listOfTuote; 
    
    private static Tilaus editTilaus;
    
    public static void setEdit(Tilaus edit) {
        editTilaus = edit;
    }
    
    public void setApplication(Tilauspalvelu application) {
        this.application = application;
    }
    
    @FXML
    private void submit() {
        String date = this.paiva.getText();
        String stats = this.status.getText();
        
        boolean a = stats.isEmpty();
        boolean b = date.isEmpty();
        
        if (a) {
            stats = editTilaus.getStatus();
            a = false;
        }
        if (b) {
            Paiva paiva = this.paivadao.findOne(editTilaus.getPaivaId());
            date = paiva.getPaiva();
            b = false;
        }
        
        if (!a && !b) {
            this.paiva.setText("");
            this.status.setText("");
            
            this.tilausToiminnallisuus.editTilausta(editTilaus.getId(), stats, date);
            this.application.setMainScene();
        }
    }
    
    @FXML
    private void backHandle() {
        this.application.setListTilausScene();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.tilausToiminnallisuus = JavafxConnectDB.getTT();
        this.paivadao = new PaivaDao(JavafxConnectDB.getDB());
    }    
    
}

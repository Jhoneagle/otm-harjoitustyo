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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NewTuoteController implements Initializable {
    private TuoteDao tuoteDao;
    private Tilauspalvelu application;

    @FXML
    private Label errorNimi;
    
    @FXML
    private TextField nimi;
    
    @FXML
    private Label errorKoodi;
    
    @FXML
    private TextField koodi;
    
    @FXML
    private Label errorHinta;
    
    @FXML
    private TextField hinta;
    
    @FXML
    private Label errorAlv;
    
    @FXML
    private TextField alv;
    
    public void setApplication(Tilauspalvelu application) {
        this.application = application;
    }

    @FXML
    private void submit(ActionEvent event) {
        String tuoteenNimi = this.nimi.getText();
        String tuoteenKoodi = this.koodi.getText();
        String tuoteenHinta = this.hinta.getText();
        String tuoteenAlv = this.alv.getText();
        
        boolean a = tuoteenNimi.isEmpty();
        boolean b = tuoteenKoodi.isEmpty();
        
        double maksaa = -1;
        double alvOn = -1;
        
        try {
            maksaa = Double.parseDouble(tuoteenHinta);
            alvOn = Double.parseDouble(tuoteenAlv);
        } catch(Exception e) {
            int g;
        }
        
        boolean c = maksaa < 0;
        boolean d = alvOn < 0 || alvOn > maksaa;
        
        if (a) {
            this.errorNimi.setText("Kirjoita tuotteelle nimi.");
        } else {
            this.errorNimi.setText("");
        }
        if (b) {
            this.errorKoodi.setText("Anna tuotteelle uniikki tuotekoodi.");
        } else {
            this.errorKoodi.setText("");
        }
        if (c) {
            this.errorHinta.setText("Aseta positiivinen kokonaisluku tai nolla.");
        } else {
            this.errorHinta.setText("");
        }
        if (d) {
            this.errorAlv.setText("Annettu alv ei ole mahdollinen.");
        } else {
            this.errorAlv.setText("");
        }
        
        if (!a && !b && !c && !d) {
            this.nimi.setText("");
            this.koodi.setText("");
            this.hinta.setText("");
            this.alv.setText("");
            
            Tuote uusi = new Tuote(0, tuoteenNimi, tuoteenKoodi, maksaa, alvOn);
            this.tuoteDao.save(uusi);
            this.application.setMainScene();
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
        Database db = JavafxConnectDB.getDB();
        this.tuoteDao = new TuoteDao(db);
    }
}

package companycalculator.ui;

import companycalculator.Tilauspalvelu;
import companycalculator.dao.AsiakasDao;
import companycalculator.database.Database;
import companycalculator.database.JavafxConnectDB;
import companycalculator.domain.Asiakas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NewAsiakasController implements Initializable {
    private AsiakasDao asiakasdao;
    private Tilauspalvelu application;

    @FXML
    private Label errorMessage;
    
    @FXML
    private TextField nimi;
    
    @FXML
    private TextField yrityksennimi;
    
    @FXML
    private TextField ytunnus;
    
    @FXML
    private TextField osoite;
    
    @FXML
    private TextField sahkoposti;
    
    @FXML
    private TextField puhelin;
    
    @FXML
    private TextField toimipaikka;
    
    @FXML
    private TextField postinumero;
    
    public void setApplication(Tilauspalvelu application) {
        this.application = application;
    }

    @FXML
    private void submit(ActionEvent event) {
        String name = this.nimi.getText();
        String email = this.sahkoposti.getText();
        String puh = this.puhelin.getText();
        String yritysnimi = this.yrityksennimi.getText();
        String yTunnus = this.ytunnus.getText();
        String address = this.osoite.getText();
        String postiNumero = this.postinumero.getText();
        String postitoimipaikka = this.toimipaikka.getText();
        
        boolean a = name.isEmpty();
        boolean b = email.isEmpty();
        boolean c = puh.isEmpty();
        boolean d = yritysnimi.isEmpty();
        boolean e = yTunnus.isEmpty();
        boolean f = address.isEmpty();
        boolean g = postiNumero.isEmpty();
        boolean h = postitoimipaikka.isEmpty();
        
        if (a || b || c || d || e || f || g || h) {
            this.errorMessage.setText("Täydennä tyhjät kentät.");
        } else if (this.asiakasdao.findByYtunnus(yTunnus) != null) {
            this.errorMessage.setText("y-tunnus on jo varattu.");
        }else {
            this.errorMessage.setText("");
            this.nimi.setText("");
            this.osoite.setText("");
            this.postinumero.setText("");
            this.puhelin.setText("");
            this.sahkoposti.setText("");
            this.toimipaikka.setText("");
            this.yrityksennimi.setText("");
            this.ytunnus.setText("");
            
            Asiakas uusi = new Asiakas(0, yritysnimi, yTunnus, name, email, puh, address, postiNumero, postitoimipaikka);
            this.asiakasdao.save(uusi);
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
        this.asiakasdao = new AsiakasDao(db);
    }
}
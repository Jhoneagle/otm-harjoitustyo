package companycalculator.ui;

import companycalculator.Tilauspalvelu;
import companycalculator.dao.TuoteDao;
import companycalculator.database.Database;
import companycalculator.database.JavafxConnectDB;
import companycalculator.domain.Tuote;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditTuoteController implements Initializable {
    private TuoteDao tuotedao;
    private Tilauspalvelu application;
    
    @FXML
    private TextField nimi;
    
    @FXML
    private TextField koodi;
    
    @FXML
    private TextField hinta;
    
    @FXML
    private TextField alv;
    
    private static Tuote editTuote;
    
    public static void setEdit(Tuote edit) {
        editTuote = edit;
    }
    
    public void setApplication(Tilauspalvelu application) {
        this.application = application;
    }
    
    @FXML
    private void submit() {
        String tuoteenNimi = this.nimi.getText();
        String tuoteenKoodi = this.koodi.getText();
        String tuoteenHinta = this.hinta.getText();
        String tuoteenAlv = this.alv.getText();
        
        boolean a = tuoteenNimi.isEmpty();
        boolean b = tuoteenKoodi.isEmpty();
        
        double maksaa = -1;
        double alvOn = -1;
        
        if (a) {
            tuoteenNimi = editTuote.getNimi();
            a = false;
        }
        if (b || (this.tuotedao.findByTuotekoodi(tuoteenKoodi) != null)) {
            tuoteenKoodi = editTuote.getTuotekoodi();
            b = false;
        }
        if (tuoteenHinta.isEmpty()) {
            tuoteenHinta = ""+editTuote.getHinta();
        }
        if (tuoteenAlv.isEmpty()) {
            tuoteenAlv = ""+editTuote.getAlv();
        }
        
        try {
            maksaa = Double.parseDouble(tuoteenHinta);
            alvOn = Double.parseDouble(tuoteenAlv);
        } catch(Exception e) {
            int g;
        }
        
        boolean c = maksaa < 0;
        boolean d = alvOn < 0 || alvOn > maksaa;
        
        if (!a && !b && !c && !d) {
            this.nimi.setText("");
            this.koodi.setText("");
            this.hinta.setText("");
            this.alv.setText("");
            
            Tuote muokattu = new Tuote(editTuote.getId(), tuoteenNimi, tuoteenKoodi, maksaa, alvOn);
            this.tuotedao.update(muokattu);
            this.application.setMainScene();
        }
    }
    
    @FXML
    private void backHandle() {
        this.application.setListTuoteScene();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Database db = JavafxConnectDB.getDB();
        this.tuotedao = new TuoteDao(db);
    }    
    
}

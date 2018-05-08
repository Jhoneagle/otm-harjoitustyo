package companycalculator.ui;

import companycalculator.Tilauspalvelu;
import companycalculator.advancelogic.TilausToiminnallisuus;
import companycalculator.dao.TuoteDao;
import companycalculator.database.JavafxConnectDB;
import companycalculator.domain.Tuote;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TuoteLoopController implements Initializable {
    private TilausToiminnallisuus tilausToiminnallisuus;
    private Tilauspalvelu application;
    private TuoteDao tuotedao;

    @FXML
    private ListView<String> lisattavat;
    
    @FXML
    private ComboBox<Tuote> lista;
    
    @FXML
    private TextField maara;
    
    @FXML
    private TextArea tieto;
    
    public void setApplication(Tilauspalvelu application) {
        this.application = application;
    }

    private void refresh() {
         this.lisattavat.refresh();
    }
    
    @FXML
    private void selected(ActionEvent event) {
        Tuote tuote = this.lista.getSelectionModel().getSelectedItem();
        String tiedot = "tuotekoodi: "+tuote.getTuotekoodi()+"\n"+"tuotteen nimi: "+tuote.getNimi()+"\n"
                +"hinta: "+tuote.getHinta()+"\n"+"Alv: "+tuote.getAlv();
        this.tieto.setText(tiedot);
    }
    
    @FXML
    private void add(ActionEvent event) {
        int amount = 0;
        
        try {
            amount = Integer.parseInt(this.maara.getText());
        } catch (Exception e) {
            return;
        }
        
        Tuote tuote = this.lista.getSelectionModel().getSelectedItem();
        String add = tuote.getTuotekoodi()+": "+amount;
        
        this.lisattavat.getItems().add(add);
        refresh();
    }
    
    @FXML
    private void remove(ActionEvent event) {
        String away = this.lisattavat.getSelectionModel().getSelectedItem();
        this.lisattavat.getItems().remove(away);
        refresh();
    }
    
    @FXML
    private void handleSubmit(ActionEvent event) {
        ObservableList<String> items = this.lisattavat.getItems();

        List<String> tuotekoodit = new ArrayList<>();
        List<Integer> lukumaarat = new ArrayList<>();
        
        for (int i = 0; i < items.size(); i++) {
            String temp = items.get(i);
            String[] piece = temp.split(": ");
            
            tuotekoodit.add(piece[0]);
            lukumaarat.add(Integer.parseInt(piece[1]));
        }
        
        this.tilausToiminnallisuus.executeAdd(tuotekoodit, lukumaarat);
        this.application.setMainScene();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tilausToiminnallisuus = JavafxConnectDB.getTT();
        this.tuotedao = new TuoteDao(JavafxConnectDB.getDB());
        
        this.lista.getItems().setAll(this.tuotedao.findAll());
    }
}

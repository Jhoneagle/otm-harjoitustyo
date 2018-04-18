package Johneagle.companyCalculator.advancelogic;

import Johneagle.companyCalculator.Dao.AsiakasDao;
import Johneagle.companyCalculator.Dao.PaivaDao;
import Johneagle.companyCalculator.Dao.TilausDao;
import Johneagle.companyCalculator.Dao.TuoteDao;
import Johneagle.companyCalculator.database.Database;
import Johneagle.companyCalculator.domain.Asiakas;
import Johneagle.companyCalculator.domain.Paiva;
import Johneagle.companyCalculator.domain.Tilaus;
import Johneagle.companyCalculator.domain.Tuote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TilausToiminnallisuus {
    private Database database;
    private AsiakasDao asiakasDao;
    private TuoteDao tuoteDao;
    private PaivaDao paivaDao;
    private TilausDao tilausDao;

    public TilausToiminnallisuus(Database database, AsiakasDao asiakasDao, TuoteDao tuoteDao, PaivaDao paivaDao, TilausDao tilausDao) {
        this.database = database;
        this.asiakasDao = asiakasDao;
        this.tuoteDao = tuoteDao;
        this.paivaDao = paivaDao;
        this.tilausDao = tilausDao;
    }

    public void lisaaTilaus(Tilaus uusi, List<String> tuotekoodit, String ytunnus, String paiva, List<Integer> tuotemaara) throws SQLException {
        Asiakas asiakas = asiakasDao.findByYtunnus(ytunnus);
        Paiva paivamaara = new Paiva(0, paiva, asiakas.getId());

        paivamaara = paivaDao.save(paivamaara);
        uusi.setPaiva_id(paivamaara.getId());

        int tilausId = tilausDao.save(uusi).getId();

        for (int i = 0; i < tuotekoodit.size(); i++) {
            Tuote temp = tuoteDao.findByTuotekoodi(tuotekoodit.get(i));

            try (Connection conn = database.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO tilaustuote(tuotemaara, tilaus_id, tuote_id) VALUES(?, ?, ?)");
                stmt.setInt(1, tuotemaara.get(i));
                stmt.setInt(2, tilausId);
                stmt.setInt(3, temp.getId());

                stmt.executeUpdate();
            }
        }
    }

    public List<String> listaaTilaukset() throws SQLException {
        List<Tilaus> tilaukset = tilausDao.findAll();
        List<String> merkkijonot = new ArrayList<>();

        for (int i = 0; i < tilaukset.size(); i++) {
            Tilaus temp = tilaukset.get(i);
            Paiva paivamaara = this.paivaDao.findOne(temp.getPaiva_id());

            String lisattava = "yritys: "+temp.getAsiakas().getYritysNimi()+", paiva: "+paivamaara.getPaiva()+", status: "+temp.getStatus();
            merkkijonot.add(lisattava);
        }

        return merkkijonot;
    }
}

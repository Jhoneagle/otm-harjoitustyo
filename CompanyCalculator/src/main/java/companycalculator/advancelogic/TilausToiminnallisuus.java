package companycalculator.advancelogic;

import companycalculator.dao.AsiakasDao;
import companycalculator.dao.PaivaDao;
import companycalculator.dao.TilausDao;
import companycalculator.dao.TuoteDao;
import companycalculator.database.Database;
import companycalculator.domain.Asiakas;
import companycalculator.domain.Paiva;
import companycalculator.domain.Tilaus;
import companycalculator.domain.Tuote;

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
        uusi.setPaivaId(paivamaara.getId());

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
            Paiva paivamaara = this.paivaDao.findOne(temp.getPaivaId());

            String lisattava = new StringBuilder().append("yritys: ").append(temp.getAsiakas().getYritysNimi()).append(", paiva: ")
                    .append(paivamaara.getPaiva()).append(", status: ").append(temp.getStatus()).toString();
            merkkijonot.add(lisattava);
        }

        return merkkijonot;
    }
}
package companycalculator.dao;

import companycalculator.database.Database;
import companycalculator.domain.Asiakas;
import companycalculator.domain.Paiva;
import companycalculator.domain.Tilaus;
import companycalculator.domain.Tuote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TilausDao implements Dao<Tilaus, Integer> {
    private Database database;
    private AsiakasDao asiakasDao;
    private TuoteDao tuoteDao;
    private PaivaDao paivaDao;

    public TilausDao(Database database, AsiakasDao asiakasDao, TuoteDao tuoteDao, PaivaDao paivaDao) {
        this.database = database;
        this.asiakasDao = asiakasDao;
        this.tuoteDao = tuoteDao;
        this.paivaDao = paivaDao;
    }

    @Override
    public Tilaus findOne(Integer key) throws SQLException {
        List<Tilaus> kaikki = findAll();

        for (int i = 0; i < kaikki.size(); i++) {
            Tilaus temp = kaikki.get(i);

            if (temp.getId() == key) {
                return temp;
            }
        }

        return null;
    }

    @Override
    public List<Tilaus> findAll() throws SQLException {
        List<Tilaus> tilaukset = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            ResultSet result = conn.prepareStatement("SELECT * FROM tilaus").executeQuery();

            while (result.next()) {
                List<Tuote> tuotteet = new ArrayList<>();
                Paiva paiva = this.paivaDao.findOne(result.getInt("paiva_id"));
                Asiakas asiakas = asiakasDao.findOne(paiva.getAsiakasId());

                try (Connection conn2 = database.getConnection()) {
                    PreparedStatement stmt = conn2.prepareStatement("SELECT * FROM tilaustuote WHERE tilaus_id = ?");
                    stmt.setInt(1, result.getInt("id"));
                    ResultSet result2 = stmt.executeQuery();

                    while (result2.next()) {
                        tuotteet.add(tuoteDao.findOne(result2.getInt("tuote_id")));
                    }
                }

                tilaukset.add(new Tilaus(result.getInt("id"), result.getString("status"), tuotteet, result.getInt("paiva_id"), asiakas));
            }
        }

        return tilaukset;
    }

    @Override
    public Tilaus save(Tilaus uusi) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO tilaus(status, paiva_id) VALUES(?, ?)");
            stmt.setString(1, uusi.getStatus());
            stmt.setInt(2, uusi.getPaivaId());
            stmt.executeUpdate();
        }

        List<Tilaus> tilaukset = findAll();
        return tilaukset.get(tilaukset.size() - 1);
    }

    @Override
    public Tilaus update(Tilaus object) throws SQLException {
        return null;
    }

    @Override
    public void delete(Integer key) throws SQLException {

    }
}

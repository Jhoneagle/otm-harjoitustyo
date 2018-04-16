package dao;

import database.Database;
import domain.Asiakas;
import domain.Tilaus;
import domain.Tuote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TilausDao implements Dao<Tilaus, Integer> {
    private Database database;

    public TilausDao(Database database) {
        this.database = database;
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
        return null;
    }

    public List<Tilaus> findAllTilaukset(AsiakasDao asiakasDao, TuoteDao tuoteDao) throws SQLException {
        List<Tilaus> tilaukset = new ArrayList<>();

        try ( Connection conn = database.getConnection() ) {
            ResultSet result = conn.prepareStatement("SELECT * FROM paiva").executeQuery();

            while (result.next()) {
                List<Tuote> tuotteet = new ArrayList<>();
                Asiakas asiakas = asiakasDao.findOne(result.getInt("asiakas_id"));

                try ( Connection conn2 = database.getConnection() ) {
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
    public Tilaus save(Tilaus object) throws SQLException {
        return null;
    }

    @Override
    public Tilaus update(Tilaus object) throws SQLException {
        return null;
    }

    @Override
    public void delete(Integer key) throws SQLException {

    }
}

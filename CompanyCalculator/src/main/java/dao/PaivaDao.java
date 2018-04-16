package dao;

import database.Database;
import domain.Paiva;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaivaDao implements Dao<Paiva, Integer> {
    private Database database;

    public PaivaDao(Database database) {
        this.database = database;
    }

    @Override
    public Paiva findOne(Integer key) throws SQLException {
        List<Paiva> kaikki = findAll();

        for (int i = 0; i < kaikki.size(); i++) {
            Paiva temp = kaikki.get(i);

            if (temp.getId() == key) {
                return temp;
            }
        }

        return null;
    }

    @Override
    public List<Paiva> findAll() throws SQLException {
        List<Paiva> paivat = new ArrayList<>();

        try ( Connection conn = database.getConnection() ) {
            ResultSet result = conn.prepareStatement("SELECT * FROM paiva").executeQuery();

            while (result.next()) {
                paivat.add(new Paiva(result.getInt("id"), result.getDate("paiva"), result.getInt("asiakas_id")));
            }
        }

        return paivat;
    }

    @Override
    public Paiva save(Paiva uusi) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO paiva(paiva, asiakas_id) VALUES(?, ?)");
            stmt.setDate(1, (Date) uusi.getPaiva());
            stmt.setInt(2, uusi.getAsiakas_id());
            stmt.executeUpdate();
        }

        List<Paiva> paivat = findAll();
        return paivat.get(paivat.size() - 1);
    }

    @Override
    public Paiva update(Paiva object) throws SQLException {
        return null;
    }

    @Override
    public void delete(Integer key) throws SQLException {

    }
}
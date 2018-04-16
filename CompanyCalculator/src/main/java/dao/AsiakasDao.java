package dao;

import database.Database;
import domain.Asiakas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AsiakasDao implements Dao<Asiakas, Integer> {

    private Database database;

    public AsiakasDao(Database database) {
        this.database = database;
    }

    @Override
    public Asiakas findOne(Integer key) throws SQLException {
        List<Asiakas> kaikki = findAll();

        for (int i = 0; i < kaikki.size(); i++) {
            Asiakas temp = kaikki.get(i);

            if (temp.getId() == key) {
                return temp;
            }
        }

        return null;
    }

    @Override
    public List<Asiakas> findAll() throws SQLException {
        List<Asiakas> asiakaat = new ArrayList<>();

        try ( Connection conn = database.getConnection() ) {
            ResultSet result = conn.prepareStatement("SELECT * FROM asiakas").executeQuery();

            while (result.next()) {
                asiakaat.add(new Asiakas(result.getInt("id"), result.getString("yritys_nimi"), result.getString("y-tunnus"),
                        result.getString("nimi"), result.getString("sahkoposti"), result.getString("puhelinnumero"),
                        result.getString("osoite"), result.getString("postinumero"), result.getString("postitoimipaikka")));
            }
        }

        return asiakaat;
    }

    @Override
    public Asiakas save(Asiakas object) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO asiakas(yritys_nimi, y-tunnus, nimi, sahkoposti, puhelinnumero, osoite, " +
                    "postinumero, postitoimipaikka) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, object.getYritysNimi());
            stmt.setString(2, object.getyTunnus());
            stmt.setString(3, object.getNimi());
            stmt.setString(4, object.getSahkoposti());
            stmt.setString(5, object.getPuhelinnumero());
            stmt.setString(6, object.getOsoite());
            stmt.setString(7, object.getPostinumero());
            stmt.setString(8, object.getPostitoimipaikka());

            stmt.executeUpdate();
        }

        return findByYtunnus(object.getyTunnus());
    }

    @Override
    public Asiakas update(Asiakas object) throws SQLException {
        return null;
    }

    @Override
    public void delete(Integer key) throws SQLException {

    }

    public Asiakas findByYtunnus(String yTunnus) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM asiakas WHERE y-tunnus = ?");
            stmt.setString(1, yTunnus);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Asiakas(result.getInt("id"), result.getString("yritys_nimi"), result.getString("y-tunnus"),
                    result.getString("nimi"), result.getString("sahkoposti"), result.getString("puhelinnumero"),
                    result.getString("osoite"), result.getString("postinumero"), result.getString("postitoimipaikka"));
        }
    }
}

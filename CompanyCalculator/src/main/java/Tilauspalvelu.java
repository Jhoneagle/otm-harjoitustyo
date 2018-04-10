import dao.TuoteDao;
import database.Database;
import domain.Tuote;

import java.util.List;
import java.util.Scanner;

public class Tilauspalvelu {
    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:data.db");
        TuoteDao tuotedao = new TuoteDao(database);
        Scanner lukija = new Scanner(System.in);

        database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tuote(id INTEGER PRIMARY KEY, tuotekoodi varchar(255), nimi varchar(255), " +
                "hinta REAL, alv REAL)").execute();

        System.out.println("Tuotteiden listaus palvelu: ");
        System.out.println("komento 'lisaa' uuden tuotteen lisaamiseksi");
        System.out.println("'listaa' kaikkien tuotteiden listaamiseksi");
        System.out.println("'poista' poistaaksesi tuotteen");
        System.out.println("'paivita' muokataksesi tuotetta");
        System.out.println("'valmis' sulkeaksesi ohjelman");
        System.out.println();

        while (true) {
            System.out.print("> ");
            String komento = lukija.nextLine();
            if (komento.contains("valmis")) {
                break;
            } else if (komento.contains("lisaa")) {
                System.out.print("tuotekoodi (täytyy olla uniikki): ");
                String tuotekoodi = lukija.nextLine();
                System.out.print("nimi: ");
                String nimi = lukija.nextLine();
                System.out.print("hinta: ");
                double hinta = Double.parseDouble(lukija.nextLine());
                System.out.print("alv: ");
                double alv = Double.parseDouble(lukija.nextLine());

                Tuote tulos = tuotedao.saveOrUpdate(new Tuote(0, tuotekoodi, nimi, hinta, alv));
                if (tulos != null) {
                    System.out.println("Lisays onnistui.");
                } else {
                    System.out.println("Tuotekoodi on varattu. Yritä uudelleen");
                }
            } else if (komento.contains("listaa")) {
                List<Tuote> tuotteet = tuotedao.findAll();
                for (int i = 0; i < tuotteet.size(); i++) {
                    Tuote temp = tuotteet.get(i);
                    System.out.println("id: "+temp.getId()+", tuotekoodi: "+temp.getTuotekoodi()+", nimi: "+temp.getNimi()+", hinta: "+temp.getHinta()+", alv: "+temp.getAlv());
                }
            } else if (komento.contains("paivita")) {
                System.out.print("paivitettavan id: ");
                int id = Integer.parseInt(lukija.nextLine());
                System.out.print("uusi tuotekoodi: ");
                String uusiTuotekoodi = lukija.nextLine();
                System.out.print("uusi nimi: ");
                String uusiNimi = lukija.nextLine();
                System.out.print("uusi hinta: ");
                double uusiHinta = Double.parseDouble(lukija.nextLine());
                System.out.print("uusi alv: ");
                double uusiAlv = Double.parseDouble(lukija.nextLine());

                Tuote tulos = tuotedao.update(new Tuote(id, uusiTuotekoodi, uusiNimi, uusiHinta, uusiAlv));
                if (tulos != null) {
                    System.out.println("paivitys onnistui.");
                } else {
                    System.out.println("Tuotekoodi on varattu. Yritä uudelleen");
                }
            } else if (komento.contains("poista")) {
                System.out.print("poistettavan tuotteen id: ");
                int id = Integer.parseInt(lukija.nextLine());
                tuotedao.delete(id);
            } else {
                System.out.println("tuntematon tai virheellinen komento!");
            }
        }
    }
}

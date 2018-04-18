package Johneagle.companyCalculator.kayttoliittyma;

import Johneagle.companyCalculator.advancelogic.TilausToiminnallisuus;
import Johneagle.companyCalculator.Dao.AsiakasDao;
import Johneagle.companyCalculator.Dao.TuoteDao;
import Johneagle.companyCalculator.domain.Asiakas;
import Johneagle.companyCalculator.domain.Tilaus;
import Johneagle.companyCalculator.domain.Tuote;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tekstikayttoliittyma {

    private Scanner lukija;
    private TuoteDao tuotedao;
    private AsiakasDao asiakasdao;
    private TilausToiminnallisuus tilauspalvelu;

    public Tekstikayttoliittyma(Scanner lukija, TuoteDao tuotedao, AsiakasDao asiakasdao, TilausToiminnallisuus tilauspalvelu) {
        this.lukija = lukija;
        this.tuotedao = tuotedao;
        this.asiakasdao = asiakasdao;
        this.tilauspalvelu = tilauspalvelu;
    }

    public void start() throws Exception {
        System.out.println("Tuotteiden listaus palvelu: ");
        System.out.println("komento 'lisaa tuote' uuden tuotteen lisaamiseksi");
        System.out.println("'listaa tuotteet' kaikkien tuotteiden listaamiseksi");
        System.out.println("'poista tuote' poistaaksesi tuotteen");
        System.out.println("'paivita tuote' muokataksesi tuotetta");
        System.out.println("'lisaa asiakas' lisätäkseksi asiakaan");
        System.out.println("'lisaa tilaus' lisätäkseksi uuden tilauksen");
        System.out.println("'listaa tilaukset' listatakseksi tilaukset");
        System.out.println("'valmis' sulkeaksesi ohjelman");
        System.out.println();
        System.out.println();

        while (true) {
            System.out.print("> ");
            String komento = lukija.nextLine();
            if (komento.contains("valmis")) {
                break;
            } else if (komento.contains("lisaa tuote")) {
                lisaaTuote();
            } else if (komento.contains("listaa tuotteet")) {
                listaaTuoteet();
            } else if (komento.contains("paivita tuote")) {
                paivitaTuote();
            } else if (komento.contains("poista tuote")) {
                poistaTuote();
            } else if (komento.contains("lisaa asiakas")) {
                lisaaAsiakas();
            } else if (komento.contains("lisaa tilaus")) {
                lisaaTilaus();
            } else if (komento.contains("listaa tilaukset")) {
                ListaaTilaukset();
            } else {
                System.out.println("tuntematon tai virheellinen komento!");
            }
        }
    }

    private void lisaaTuote() throws Exception {
        System.out.print("tuotekoodi (täytyy olla uniikki): ");
        String tuotekoodi = lukija.nextLine();
        System.out.print("nimi: ");
        String nimi = lukija.nextLine();
        System.out.print("hinta: ");
        double hinta = Double.parseDouble(lukija.nextLine());
        System.out.print("alv: ");
        double alv = Double.parseDouble(lukija.nextLine());

        Tuote tulos = tuotedao.save(new Tuote(0, tuotekoodi, nimi, hinta, alv));
        if (tulos != null) {
            System.out.println("Lisays onnistui.");
        } else {
            System.out.println("Tuotekoodi on varattu. Yritä uudelleen");
        }
    }

    private void paivitaTuote() throws Exception {
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
    }

    private void listaaTuoteet() throws Exception {
        List<Tuote> tuotteet = tuotedao.findAll();
        for (int i = 0; i < tuotteet.size(); i++) {
            Tuote temp = tuotteet.get(i);
            System.out.println("id: " + temp.getId() + ", tuotekoodi: " + temp.getTuotekoodi() + ", nimi: " + temp.getNimi() + ", hinta: " + temp.getHinta() + ", alv: " + temp.getAlv());
        }
    }

    private void poistaTuote() throws Exception {
        System.out.print("poistettavan tuotteen id: ");
        int id = Integer.parseInt(lukija.nextLine());
        tuotedao.delete(id);
    }

    private void lisaaAsiakas() {
        System.out.print("yrityksen nimi: ");
        String yritysNimi = lukija.nextLine();
        System.out.print("yrityksen y-tunnus: ");
        String yTunnus = lukija.nextLine();
        System.out.print("nimi: ");
        String nimi = lukija.nextLine();
        System.out.print("sahkoposti: ");
        String sahkoposti = lukija.nextLine();
        System.out.print("puhelinnumero: ");
        String puhelinNumero = lukija.nextLine();
        System.out.print("osoite: ");
        String osoite = lukija.nextLine();
        System.out.print("postinumero: ");
        String postinumero = lukija.nextLine();
        System.out.print("postitoimipaikka: ");
        String postitoimipaikka = lukija.nextLine();

        Asiakas lisattava = new Asiakas(0, yritysNimi, yTunnus, nimi, sahkoposti, puhelinNumero, osoite, postinumero, postitoimipaikka);

        try {
            asiakasdao.save(lisattava);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ListaaTilaukset() {
        List<String> tilaukset = null;
        try {
            tilaukset = tilauspalvelu.listaaTilaukset();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < tilaukset.size(); i++) {
            System.out.println(tilaukset.get(i));
        }
    }

    private void lisaaTilaus() {
        System.out.print("asiakaan y-tunnus: ");
        String yTunnus = lukija.nextLine();
        System.out.print("tilauksen status (tarjous vai tilaus): ");
        String status = lukija.nextLine();
        System.out.print("paivamaara: ");
        String paiva = lukija.nextLine();
        System.out.print("tuotemaara: ");
        int tuotemaara = Integer.parseInt(lukija.nextLine());

        List<String> tuotekoodit = new ArrayList<>();
        System.out.print("tuotekoodit (lopettaaksesi paina enter): ");

        while (true) {
            String tuotekoodi = lukija.nextLine();

            if (tuotekoodi.isEmpty()) {
                break;
            } else {
                tuotekoodit.add(tuotekoodi);
            }
        }

        Tilaus tilaus = new Tilaus(0, status, new ArrayList<>(), 0, new Asiakas());
        try {
            tilauspalvelu.lisaaTilaus(tilaus, tuotekoodit, yTunnus, paiva, tuotemaara);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

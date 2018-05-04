package companycalculator.ui;

import companycalculator.dao.AsiakasDao;
import companycalculator.dao.TuoteDao;
import companycalculator.advancelogic.TilausToiminnallisuus;
import companycalculator.domain.Asiakas;
import companycalculator.domain.Tilaus;
import companycalculator.domain.Tuote;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Textual user interface.
 * not used if javafx interface works.
 */
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

    private void ohjeet() {
        System.out.println("komento 'lisaa tuote' uuden tuotteen lisaamiseksi");
        System.out.println("'listaa tuotteet' kaikkien tuotteiden listaamiseksi");
        System.out.println("'poista tuote' poistaaksesi tuotteen");
        System.out.println("'paivita tuote' muokataksesi tuotetta");
        System.out.println("'lisaa asiakas' lisätäkseksi asiakaan");
        System.out.println("'listaa asiakaat' listataksesi kaikki asiakaat");
        System.out.println("'lisaa tilaus' lisätäkseksi uuden tilauksen");
        System.out.println("'listaa tilaukset' listatakseksi tilaukset");
        System.out.println("'muokkaa tilausta' muokataksesi olemassa olevaa tilausta");
        System.out.println("'poista tilaus' poistaaksesi tilauksen");
        System.out.println("'komennot' saadaksesi kaikki toiminnot nkyviin");
        System.out.println("'valmis' sulkeaksesi ohjelman");
        System.out.println();
        System.out.println();
    }

    public void start() throws Exception {
        System.out.println("Tilausten käsittely palvelu: ");
        System.out.println("ohjeet: ");
        System.out.println();
        ohjeet();

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
            } else if (komento.contains("listaa asiakaat")) {
                listaaAsiakaat();
            } else if (komento.contains("lisaa tilaus")) {
                lisaaTilaus();
            } else if (komento.contains("listaa tilaukset")) {
                ListaaTilaukset();
            } else if (komento.contains("muokkaa tilausta")) {
                muokkaaTilausta();
            } else if (komento.contains("poista tilaus")) {
                poistaTilaus();
            } else if (komento.contains("komennot")) {
                ohjeet();
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

    private void listaaAsiakaat() throws SQLException {
        List<Asiakas> asiakaat = asiakasdao.findAll();

        for (int i = 0; i < asiakaat.size(); i++) {
            Asiakas temp = asiakaat.get(i);
            System.out.println(new StringBuilder().append("Nimi: ").append(temp.getNimi()).append("yrityksen nimi: ").append(temp.getYritysNimi())
                    .append(", yrityksen y-tunnus: ").append(temp.getyTunnus()).append(", osoite: ").append(temp.getOsoite())
                    .append(", postinumero: ").append(temp.getPostinumero()).append(", postitoimipaikka: ").append(temp.getPostitoimipaikka())
                    .append(", sähköposti: ").append(temp.getSahkoposti()).append(" ja puhelinnumero: ").append(temp.getPuhelinnumero()).toString());
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
        
        List<Integer> tuotemaarat = new ArrayList<>();
        List<String> tuotekoodit = new ArrayList<>();
        System.out.print("tuotekoodit (lopettaaksesi paina enter): ");

        while (true) {
            System.out.print("tuotekoodi: ");
            String tuotekoodi = lukija.nextLine();
            
            if (tuotekoodi.isEmpty()) {
                break;
            } else {
                tuotekoodit.add(tuotekoodi);
            }
            
            System.out.print("tuoteen lukumaara: ");
            int tuotemaara = Integer.parseInt(lukija.nextLine());
            tuotemaarat.add(tuotemaara);
        }

        Tilaus tilaus = new Tilaus(0, status, new ArrayList<>(), 0, new Asiakas());
        try {
            tilauspalvelu.lisaaTilaus(tilaus, tuotekoodit, yTunnus, paiva, tuotemaarat);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void muokkaaTilausta() throws SQLException {
        System.out.print("tilauksen id: ");
        int id = Integer.parseInt(lukija.nextLine());

        System.out.print("tilauksen uusi status (tilaus vai toimitettu): ");
        String status = lukija.nextLine();
        System.out.print("uusi paivamaara tai tyhjä, jos ei muutosta: ");
        String paiva = lukija.nextLine();

        this.tilauspalvelu.muokkaaTilausta(id, status, paiva);
    }

    private void poistaTilaus() throws SQLException {
        System.out.print("poistettavan tilauksen id: ");

        int id = Integer.parseInt(lukija.nextLine());
        this.tilauspalvelu.poistaTilaus(id);
    }
}

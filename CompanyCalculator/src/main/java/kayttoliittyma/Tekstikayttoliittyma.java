package kayttoliittyma;

import dao.TuoteDao;
import domain.Tuote;

import java.util.List;
import java.util.Scanner;

public class Tekstikayttoliittyma {

    private Scanner lukija;
    private TuoteDao tuotedao;

    public Tekstikayttoliittyma(Scanner lukija, TuoteDao tuotedao) {
        this.lukija = lukija;
        this.tuotedao = tuotedao;
    }

    public void start() throws Exception {
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
                lisaaTuote();
            } else if (komento.contains("listaa")) {
                listaaTuoteet();
            } else if (komento.contains("paivita")) {
                paivitaTuote();
            } else if (komento.contains("poista")) {
                poistaTuote();
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
}

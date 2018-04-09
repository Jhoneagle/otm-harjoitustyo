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

            } else if (komento.contains("listaa")) {
                List<Tuote> tuotteet = tuotedao.findAll();
                for (int i = 0; i < tuotteet.size(); i++) {
                    Tuote temp = tuotteet.get(i);
                    System.out.println("tuotekoodi: "+temp.getTuotekoodi()+", nimi: "+temp.getNimi()+"hinta: "+temp.getHinta()+", alv: "+temp.getAlv());
                }
            } else if (komento.contains("paivita")) {

            } else if (komento.contains("poista")) {

            } else {
                System.out.println("tuntematon tai virheellinen komento!");
            }
        }
    }
}

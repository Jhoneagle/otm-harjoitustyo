package kayttoliittyma;

import dao.TuoteDao;
import database.Database;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.Properties;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class TekstikayttoliittymaTest {

    private TuoteDao tuotedao;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private File tempFile;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();

        properties.load(new FileInputStream("config.properties"));
        tempFile = tempFolder.newFile(properties.getProperty("testDatabaseFile"));
        String databaseAddress = "jdbc:sqlite:"+tempFile.getAbsolutePath();

        Database database = new Database(databaseAddress);
        this.tuotedao = new TuoteDao(database);

        database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tuote(id INTEGER PRIMARY KEY, tuotekoodi varchar(255), nimi varchar(255), " +
                "hinta REAL, alv REAL)").execute();

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void tuotteenLisays() throws Exception {
        Scanner lukija = new Scanner ("lisaa \n"+"testikamo \n"+"armo \n"+"1 \n"+"1.53 \n"+"valmis");
        Tekstikayttoliittyma liittyma = new Tekstikayttoliittyma(lukija, this.tuotedao);
        liittyma.start();

        String tulostus= "Tuotteiden listaus palvelu: \r\n" +
                "komento 'lisaa' uuden tuotteen lisaamiseksi\r\n" +
                "'listaa' kaikkien tuotteiden listaamiseksi\r\n" +
                "'poista' poistaaksesi tuotteen\r\n" +
                "'paivita' muokataksesi tuotetta\r\n" +
                "'valmis' sulkeaksesi ohjelman\r\n" +
                "\r\n" +
                "> tuotekoodi (täytyy olla uniikki): nimi: hinta: alv: Lisays onnistui.\r\n" +
                "> ";

        assertEquals(tulostus, outContent.toString());
    }

    @After
    public void restore() {
        System.setOut(System.out);
        System.setErr(System.err);
        tempFile.delete();
    }
}
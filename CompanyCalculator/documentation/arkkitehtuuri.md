# Arkkitehtuurikuvaus

## Rakenne

Sovelluksen rakenne on kaksi tasoinen, jos mukaan ei oteta varsinaisen ohjelman takana py�riv�� tietojenhallintaj�rjestelm�n eli Sqliten toteutusta, miss� tieto varsinaisesti tallennetaan tiedostoon, joka sille on alussa m��ritelty.

Sovelluksen kaksi tasoa ovat k�ytt�liittym�n toteuttavat FXML-tiedostot ja niiden controller-luokat ja Sovelluslogiikassa dao-luokat, jotka vastaavat yksitt�isten tietokantatauluun kohdistuvien toimintojen toteuttamisesta. Sek� yksi dao-luokien antamien toimintojen yhdistelmien toteuttamiseen.

## K�ytt�liittym�

Sovelluksen k�ytt�liittym� koostuu 10 n�kym�st�, joista ensimm�inen sovelluksen k�ynnistyess� on p��n�kym�.

muita n�kymi� ovat

* tuotteiden listaus
* tuotteiden lis�ys
* tuotteiden muokkaus
* asiakaiden listaus
* asiakaiden lis�ys
* tilausten listaus
* tilausten lis�ys (kaksi n�kym��)
* tilausten muokkaus

N�ist� jokainen on toteutettu FXML-tiedoston ja javafx controller-luokan yhdistelm�n avulla. Joista jokainen on sovelluksen k�ynnistyess� tallennettuna erilliseen Scene-olioon ja yksi kerrallaan asetettu n�kym��n varsinaiseen Stage-olioon.

Sovelluslogiikka on pyritty irroittamaan mahdollisimman suurelta osin k�ytt�liittym�st� luomatta ylim��r�isi� luokkia, jotka vain ohjaisivat suoraan eteenp�in tekem�tt� itse mit��n. 

Kun sovelluksen lis�ys tai muokkaus toiminnallisuuksia on k�ytetty, niin k�ytt�j�n t�ytyy painaa listan uudelleen lataus nappia, jotta muutokset tulee n�kyviin. 

## Sovelluslogiikka

Sovelluksen looginen datamalli

![sovelluksen datamalli](kuvat/datamalli.jpg)

Koko luokkakaavio

![sovelluslogiikka](kuvat/luokkakaavio.jpg)

Molemmat generoitu k�ytt�en Intellj IDEA Ultimatea

## Tietojen pysyv�istallennus

Tiedot tallennetaan paikalliseen tietokantaan k�ytt�en sqlite-tiedonhallinta j�rjestelm��. T�t� yhteytt� varten on luotu Dao-luokat kaikille varsinaisille tietokanta tauluille, jotka toteuttavat kaikki niiden osalta t�rkeimm�t toiminnot. Joita voidaan sittemmin k�ytt��, joko monimutkaisemmassa sovellus logiikassa tai suoraan k�ytt�liittym�st� k�sin. 

## Tiedostot

Sovelluksen luodessa yhteytt� tietokantaan. Se m��rittelee tietokannalle fyysist� tallennusta varten tiedoston nimen.

Sovelluksen juureen sijoitettu [konfiguraatiotiedosto](https://github.com/Jhoneagle/otm-harjoitustyo/blob/master/CompanyCalculator/documentation/kayttoohje.md) [config.properties](https://github.com/Jhoneagle/otm-harjoitustyo/blob/master/CompanyCalculator/config.properties) m��rittelee sqlite tietokannan k�ytt�mien tiedostojen nimet.

## P��toiminnallisuudet

### tuoteen lis�ys

Kun k�ytt�j� on valinnut tuoteen lis�ys n�kym�n ja sy�tt�nyt tuoteelle nimen, tuotekoodin, hinnan ja alvin. Mitk� t�ytt�v�t niukan validoinnin ja painaa "lis��" buttonia. Controlleri suorittaa seuraavan sarjan: 

![tuoteen lis�ys](kuvat/sekvenssiTuoteAdd.jpg)

Eli aluksi luodaan saaduista tiedostoista uusi Tuote-olio. T�m�n j�lkeen se l�hetet��n TuoteDaon metodille save, jossa sen avulla tehd��n lis�ys kysely sql tietokantaan eli sqlitelle t�ss� tapauksessa. Mink� j�lkeen metodi palauttaa Tuote olion paluu arvona, kun lis�ys on tehty. Lopuksi k�ytt�liittym� siirt�� k�ytt�j�n n�kym�n takaisin aloitus n�kym��n. 

### tuoteen muokkaus

K�ytt�j�n valittua tuote listasta ja painettua "muokkaa". Tulee n�kymiin kysely johon voi m��ritell� uusia arvoja tai j�tt�� tyhj�ksi, jos ei halua p�ivitt��. Lopuksi painaa "p�ivit�" nappia, kunhan validointi on ok, kontrolleri toteutaa seuraavan prosessin: 

![tuoteen p�ivitys](kuvat/sekvenssiTuoteUpdate.jpg)

Ensiksi siis luodaan Tuote olio tiedoista. Joko uusista tai vanhoista. T�m�n j�lkeen kutsutaan TuoteDaon udate metodia, jolle parametrina annetaan Tuote olio. metodi tekee p�ivitys kyselyn tietokantaan ja lopuksi palauttaa Tuote olion, joka on sen j�lkeen tietokannassa. Lopuksi k�ytt�liittym� siirt�� n�kym�n aloitus n�kym��n. 

### tilauksen lis�ys

K�ytt�j�n avattua tilauksen lis�ys n�kym�n ja annettua asiakaan y-tunnuksen, tilauksen statuksen ja p�iv�m��r�n. validointi tarkastuksen menness� l�pi kontrolleri toteuttaa seuraavan prosessin: 

![tilauksen lis�ys](kuvat/sekvenssiTilausAdd.jpg)

Aluksi saadut kolme arvo laitetaan talteen lis�yst� varten, jonka j�lkeen siirryt��n seuraavaan n�kym��n (tuoteloopScene), jossa k�ytt�j� antaa kaikki tilaukseen liittyv�t tuottee ja m��r�t. T�m�n valmistuttua ja "lis��" nappia painettua ohjelma jatkaa ty�t�. Ensiksi kaikki tiedot kasataan, nill� kutsutaan varsinaista lis�ys metodia. 

Ensiksi addTilaus metodi etsii AsiakasDaon avulla oikean asiakaan, joka vastaa y-tunnusta. T�m�n j�lkeen luodaan PaivaDaon avulla uusi paiva tietue ja ne ydistet��n Tilaus-oliohon. Mik� t�m�n j�lkeen lis�t��n TilausDaon avulla tietokantaan.

Lopuksi metodi suorittaa X kertaa loopin, jossa se hakee tuotekoodin perusteella Tuote-olioita TuoteDaon kautta. Mink� j�lkeen sen ja tilausDaon save-metodilta saadun Tilaus-oliolla luodaan tarvittava tietue TilausTuote-liitostauluun. Kun se on ohi k�ytt�j�lle annetaan aloitusn�kym�. 

### Muut toiminnallisuudet

Asiakaan lis�ys toimii vastaavan laisesti, kuin tuoteen lis�ys, mutta eri n�kym�n ja eri Daon avulla. Taas tilauksen muokkaus vastaa tuoteeen muokkausta, mutta TuoteDaon sijasta on TIlaustoiminnallisuus luokkan metodi. 

Listaukset ja poistot taas tapahtuvat saman kontrolerin kautta ja k�ytt�v�t niille varattuja findAll ja delete metodeja. 

## Ohjelman rakenteeseen j��neet heikkoudet

### K�ytt�liittym�

Graaffinen k�ytt�liittym� n�kymien vaihto on j�ykk�, sill se ei tue kunnolla sovelluksen lis�ys ja muokkaus toiminnallisuuksien k�ytt��. T�m� johtuu siit�, ett� sovellus on joko k�ynnistett�v� uudelleen tai k�ytt�j�n on listaus n�kym�ss� painettava listauksen p�ivitt�misnappia, jotta lis�yksen tai muokkauksen vaikutuksen pystyy n�kem��n.

### Logiikka

Sovelluslogiikka voisi olla erotettu enemm�n k�ytt�liittym�st� luomalla erillinen luokka, joka hallinnoi kaiken k�ytt�liittym�n ja logiikan v�lill� olevan liikenteen. 

My�s logiikan refactorointi helpottuisi, jos testit kattaisivat paremmin luokkien yhteisk�yt��n liittyvi� tilanteita. 


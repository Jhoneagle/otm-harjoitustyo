# Arkkitehtuurikuvaus

## Rakenne



## K‰yttˆliittym‰



## Sovellus logiikka

sovelluksen looginen datamalli

![sovelluksen datamalli](kuvat/datamalli.jpg)

koko luokkakaavio

![sovelluslogiikka](kuvat/luokkakaavio.jpg)

## Tietojen pysyv‰istallennus

tiedot tallennetaan paikalliseen tietokantaan k‰ytt‰en sqlite-tiedonhallinta j‰rjestelm‰‰. T‰t‰ yhteytt‰ varten on luotu Dao-luokat kaikille varsinaisille tietokanta tauluille, jotka toteuttavat kaikki niiden osalta t‰rkeimm‰t toiminnot. Joita voidaan sittemmin k‰ytt‰‰, joko monimutkaisemmassa sovellus logiikassa tai suoraan k‰yttˆliittym‰st‰ k‰sin. 

## Tiedostot

Sovelluksen luodessa yteytt‰ tietokantaan. Se m‰‰rittelee tietokannalle fyysist‰ tallennusta varten tiedoston nimen.

Sovelluksen juureen sijoitettu [konfiguraatiotiedosto](https://github.com/Jhoneagle/otm-harjoitustyo/blob/master/CompanyCalculator/documentation/kayttoohje.md) [config.properties](https://github.com/Jhoneagle/otm-harjoitustyo/blob/master/CompanyCalculator/config.properties) m‰‰rittelee sqlite tietokannan k‰ytt‰mien tiedostojen nimet.

## P‰‰toiminnallisuudet



### muut toiminnallisuudet



## Ohjelman rakenteeseen j‰‰neet heikkoudet




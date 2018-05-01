# Käyttöohje

Lataa tiedosto [calculator.jar](https://github.com/Jhoneagle/otm-harjoitustyo/releases/tag/v1.0)

## Konfigurointi

Ohjelma olettaa, että sen käynnistyshakemistossa on konfiguraatiotiedosto _config.properties_, joka määrittelee sqlitelle sen tarvitsemat tietokantojen fyysiseen tallennukseen tarvitsemien tiedostojen nimet. Tiedoston muoto on seuraava

```
mainDatabase=data.db
testDatabaseFile=test.db
```

## Ohjelman käynnistäminen

Ohjelma käynnistetään komennolla 

```
java -jar calculator.jar
```

# OTM-harjoitustyö

## Dokumentaatio

[Arkkitehtuurikuvaus](https://github.com/Jhoneagle/otm-harjoitustyo/blob/master/CompanyCalculator/documentation/arkkitehtuuri.md) 

[Työaikakirjanpito](https://github.com/Jhoneagle/otm-harjoitustyo/blob/master/CompanyCalculator/documentation/tuntikirjanpito.md) 

[Käyttöohje](https://github.com/Jhoneagle/otm-harjoitustyo/blob/master/CompanyCalculator/documentation/kayttoohje.md)

[Testausdokumentti](https://github.com/Jhoneagle/otm-harjoitustyo/blob/master/CompanyCalculator/documentation/testausdokumentti.md)

[Vaatimusmäärittely](https://github.com/Jhoneagle/otm-harjoitustyo/blob/master/CompanyCalculator/documentation/vaatimusmaarittely.md) 

## Releaset

[Viikko 5](https://github.com/Jhoneagle/otm-harjoitustyo/releases/tag/v1.0)

[Viikko 7](https://github.com/Jhoneagle/otm-harjoitustyo/releases/tag/v1.1)

[Loppupalautus](https://github.com/Jhoneagle/otm-harjoitustyo/releases/tag/v1.11)

## Komentorivitoiminnot

### Testaus

Testit suoritetaan komennolla

```
mvn test
```

Testikattavuusraportti luodaan komennolla

```
mvn jacoco:report
```

Kattavuusraporttia voi tarkastella avaamalla selaimella tiedosto _target/site/jacoco/index.html_

### Suoritettavan jarin generointi

Komento

```
mvn package
```

Generoi hakemistoon _target_ suoritettavan jar-tiedoston _Company-calculator-1.0-SNAPSHOT.jar_

### JavaDoc

JavaDoc generoidaan komennolla

```
mvn javadoc:javadoc
```

JavaDocia voi tarkastella avaamalla selaimella tiedosto _target/site/apidocs/index.html_

### Checkstyle

Tiedoston [checkstyle.xml](https://github.com/Jhoneagle/otm-harjoitustyo/blob/master/CompanyCalculator/checkstyle.xml) määrittelemät tarkistukset suoritetaan komennolla

```
 mvn jxr:jxr checkstyle:checkstyle
```

Mahdolliset virheilmoitukset selviävät avaamalla selaimella tiedosto _target/site/checkstyle.html_

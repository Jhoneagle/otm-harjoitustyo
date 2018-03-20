package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }

    @Test
    public void saldoOikein() {
        assertTrue(this.kortti.saldo()==10);
    }

    @Test
    public void kasvatusOikein() {
        this.kortti.lataaRahaa(10);
        int saldo = this.kortti.saldo();
        assertTrue(saldo==20);
    }

    @Test
    public void ottoOikein() {
        //jos rahaa tarpeeksi
        boolean onnistuiko = this.kortti.otaRahaa(10);
        int saldo = this.kortti.saldo();
        assertEquals(saldo, 0);
    }

    @Test
    public void otto2Oikein() {
        //jos rahaa tarpeeksi
        assertTrue(this.kortti.otaRahaa(5)==true);
    }

    @Test
    public void otto3Oikein() {
        //jos rahaa ei tarpeeksi
        boolean onnistuiko = this.kortti.otaRahaa(50);
        int saldo = this.kortti.saldo();
        assertEquals(saldo, 10);
    }

    @Test
    public void otto4Oikein() {
        //jos rahaa ei tarpeeksi
        assertTrue(this.kortti.otaRahaa(50)==false);
    }

}

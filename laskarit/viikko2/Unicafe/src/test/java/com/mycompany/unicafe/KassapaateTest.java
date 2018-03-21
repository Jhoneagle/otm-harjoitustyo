package com.mycompany.unicafe;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class KassapaateTest {

    Kassapaate kassa;
    Maksukortti kortti;

    @Before
    public void setUp() {
        kassa = new Kassapaate();
        kortti = new Maksukortti(10);
    }

    @Test
    public void alustusoikein() {
        assertEquals(100000, this.kassa.kassassaRahaa());
        assertEquals(0, this.kassa.edullisiaLounaitaMyyty());
        assertEquals(0, this.kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void syoMaukkaasti() {
        assertEquals(1, this.kassa.syoMaukkaasti(1));
        assertEquals(100, this.kassa.syoMaukkaasti(500));
        assertEquals(1, this.kassa.maukkaitaLounaitaMyyty());
        assertEquals(100000-400, this.kassa.kassassaRahaa());
    }

    @Test
    public void syoEdullisesti() {
        assertEquals(1, this.kassa.syoEdullisesti(1));
        assertEquals(60, this.kassa.syoEdullisesti(300));
        assertEquals(1, this.kassa.edullisiaLounaitaMyyty());
        assertEquals(100000-240, this.kassa.kassassaRahaa());
    }

    @Test
    public void syoMaukkaastiKortti() {
        assertTrue(!this.kassa.syoMaukkaasti(kortti));
        assertEquals(10, this.kortti.saldo());
        assertEquals(0, this.kassa.maukkaitaLounaitaMyyty());

        this.kortti.lataaRahaa(400);

        assertTrue(this.kassa.syoMaukkaasti(kortti));
        assertEquals(10, this.kortti.saldo());
        assertEquals(1, this.kassa.maukkaitaLounaitaMyyty());
        assertEquals(100000, this.kassa.kassassaRahaa());
    }

    @Test
    public void syEdullisestiKortti() {
        assertTrue(!this.kassa.syoEdullisesti(kortti));
        assertEquals(10, this.kortti.saldo());
        assertEquals(0, this.kassa.edullisiaLounaitaMyyty());

        this.kortti.lataaRahaa(240);

        assertTrue(this.kassa.syoEdullisesti(kortti));
        assertEquals(10, this.kortti.saldo());
        assertEquals(1, this.kassa.edullisiaLounaitaMyyty());
        assertEquals(100000, this.kassa.kassassaRahaa());
    }

    @Test
    public void lataus() {
        this.kassa.lataaRahaaKortille(kortti, 100);
        assertEquals(110, this.kortti.saldo());
        assertEquals(100100, this.kassa.kassassaRahaa());

        this.kassa.lataaRahaaKortille(kortti, -100);
        assertEquals(110, this.kortti.saldo());
        assertEquals(100100, this.kassa.kassassaRahaa());

    }

}
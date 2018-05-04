package com.company;

import org.junit.Test;

import static org.junit.Assert.*;

public class WeatherTest {

    @Test
    public void parseDoc() {
        Weather w =new Weather();
        assertNotNull(w.parseDoc("https://yandex.ru/pogoda/moscow"));
        assertNull(w.parseDoc("https://yandex.ru/pogoda/faedf"));
    }

}
package com.company;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;


public class Weather {
    private int temperature;
    private float windSpeed;
    private int pressure;
    private int humidity;
    private Document doc;
    private String url;
    private String city;


    public Weather(String city) {
        this.url = "https://yandex.ru/pogoda/" + city.toLowerCase();
        this.doc = parseDoc(url);
        if (doc != null) {
            this.city = city;
            this.temperature = findTemperature();
            this.windSpeed = findWindSpeed();
            this.pressure = findPressure();
            this.humidity = findHumidity();
        }
    }

    public Weather(Weather obj) {
        this.url = obj.url;
        this.doc = obj.doc;
        this.humidity = obj.humidity;
        this.pressure = obj.pressure;
        this.temperature = obj.temperature;
        this.windSpeed = obj.windSpeed;
        this.city = obj.city;
    }

    public Weather() {
    }

    public Document parseDoc(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException IOException) {
            System.out.println("City not found");
        }
        return doc;
    }

    private int findTemperature() {
        String s = "0";
        Elements content = doc.getElementsByClass("temp__value");
        for (Element i : content) {
            s = i.text();
            break;
        }
        if (s.contains("−")) {
            s = s.substring(1, s.length());
            return Integer.parseInt(s) * -1;
        } else
            return Integer.parseInt(s);
    }

    private float findWindSpeed() {
        String s = "0";
        Elements content = doc.getElementsByClass("wind-speed");
        for (Element i : content) {
            s = i.text();
        }
        s = s.replaceAll(",", ".");
        return Float.parseFloat(s);
    }

    private int findPressure() {
        int k = 0;
        String s = "000";
        Elements content = doc.getElementsByClass("term__value");
        for (Element i : content) {
            k++;
            if (k == 4) {
                s = i.text();
                break;
            }
        }
        s = s.substring(0, 3);
        return Integer.parseInt(s);
    }

    private int findHumidity() {
        int k = 0;
        String s = "00";
        Elements content = doc.getElementsByClass("term__value");
        for (Element i : content) {
            k++;
            if (k == 5) {
                s = i.text();
                break;
            }
        }
        s = s.substring(0, 2);
        return Integer.parseInt(s);
    }


    public int getTemperature() {
        return temperature;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public String getCity() {
        return city;
    }

    public Document getDoc() {
        return doc;
    }


    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

}

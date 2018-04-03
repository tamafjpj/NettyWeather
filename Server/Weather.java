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


public Weather(String url){
    this.url=url;
    parseDoc();
    this.temperature=setTemperature();
    this.windSpeed=setWindSpeed();
    this.pressure=setPressure();
    this.humidity=setHumidity();
}
private int parseDoc(){
    try{this.doc=Jsoup.connect(url).get();return 1;}
    catch (IOException IOException){return 0;}
}

private int setTemperature() {
    String s="0";
    Elements content = doc.getElementsByClass("temp__value");
    for (Element i : content) {
        s = i.text();
        break;
    }
    if(s.contains("−")){s=s.substring(1,s.length());return Integer.parseInt(s)*-1;}
    else
    return Integer.parseInt(s);
    }

private float setWindSpeed() {
    String s="0";
    Elements content = doc.getElementsByClass("wind-speed");
    for (Element i : content) {
        s = i.text();
    }
    s = s.replaceAll(",",".");
    return Float.parseFloat(s);
    }

private int setPressure() {
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
private int setHumidity() {
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
}

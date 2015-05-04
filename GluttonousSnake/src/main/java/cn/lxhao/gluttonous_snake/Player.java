package cn.lxhao.gluttonous_snake;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


public class Player implements Runnable {
    URL url;
    AudioClip clip;
    File file;

    public Player(String file_name) {
        file = new File("audio/" + file_name + ".wav");
        new Thread(this).start();
    }

    public void run() {
        try {
            url = file.toURL();
            clip = Applet.newAudioClip(url);
            clip.play();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}

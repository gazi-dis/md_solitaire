package com.example.md_solitaire;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class Deste {
    public ArrayList<Kart> kartlar;

    public Deste() {
        kartlariDagit();
        Collections.shuffle(kartlar);
        Log.e("toplam kart:",String.valueOf(kartlar.size()));
    }

    private void kartlariDagit() {
        String tip;
        Kart kart;
        String renk;
        kartlar = new ArrayList<Kart>();
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    tip = "karo";
                    renk = "kirmizi";
                    break;
                case 1:
                    tip = "kupa";
                    renk = "kirmizi";
                    break;
                case 2:
                    tip = "sinek";
                    renk = "siyah";
                    break;
                case 3:
                    tip = "maca";
                    renk = "siyah";
                    break;
                default:
                    tip = "";
                    renk = "";
                    break;
            }
            for (int j = 1; j <= 13; j++) {
                kart = new Kart(tip, j,renk);
                kartlar.add(kart);
            }
        }
    }

    public Kart randomKartGetir(){
        Kart kart = kartlar.get(kartlar.size()-1);
        kartlar.remove(kartlar.size() - 1);
        return kart;
    }

    public int getDesteKartSayi(){
        return kartlar.size();
    }


}

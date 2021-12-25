package com.example.md_solitaire;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class Orta extends AppCompatActivity {
    private ArrayList<Kart> ortaKartBilgileri;
    private ArrayList<Kart> ortaAcikKartBilgileri;

    public Orta(Deste deste) {
        ortaKartBilgileri = new ArrayList<Kart>();
        //24 adet karti ortaya getir
        for (int i=0;i<24;i++){
            ortaKartBilgileri.add(deste.randomKartGetir());
        }
        ortaAcikKartBilgileri = new ArrayList<Kart>();
    }

    public Kart ustKartCek() {
        if (ortaKartBilgileri.isEmpty()) {
            Log.e("orta_durum","kart bitti");
            Collections.reverse(ortaAcikKartBilgileri);
            for (Kart kart:ortaAcikKartBilgileri){
                ortaKartBilgileri.add(kart);
            }
            ortaAcikKartBilgileri.clear();
            return null;
        }
        Kart cekilen_kart = ortaKartBilgileri.get(ortaKartBilgileri.size() - 1);
        ortaKartBilgileri.remove(ortaKartBilgileri.size() - 1);
        ortaAcikKartBilgileri.add(cekilen_kart);
        return cekilen_kart;
    }

    public Kart getOrtadakiKart(){
        if(ortaAcikKartBilgileri.size()>0){
            return ortaAcikKartBilgileri.get(ortaAcikKartBilgileri.size()-1);
        }else{
            return null;
        }
    }
     public void ortadakiKartiSil(){
        ortaAcikKartBilgileri.remove(ortaAcikKartBilgileri.size()-1);
     }

     public int getOrtaKartSayi(){
        return ortaKartBilgileri.size();
     }


}

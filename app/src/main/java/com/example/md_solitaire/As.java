package com.example.md_solitaire;

import java.util.ArrayList;

public class As {
    private ArrayList<Kart> as1KartBilgileri;
    private ArrayList<Kart> as2KartBilgileri;
    private ArrayList<Kart> as3KartBilgileri;
    private ArrayList<Kart> as4KartBilgileri;

    public As(){
        as1KartBilgileri = new ArrayList<Kart>();
        as2KartBilgileri = new ArrayList<Kart>();
        as3KartBilgileri = new ArrayList<Kart>();
        as4KartBilgileri = new ArrayList<Kart>();
    }

    public boolean kartVarmi(int slot){
        switch (slot){
            case 1:
                if (as1KartBilgileri.size()>0)return true;
                break;
            case 2:
                if (as2KartBilgileri.size()>0)return true;
                break;
            case 3:
                if (as3KartBilgileri.size()>0)return true;
                break;
            case 4:
                if (as4KartBilgileri.size()>0)return true;
                break;
            default:
                return true;
        }
        return false;
    }

    public void kartEkle(Kart kart,int slot){
        switch (slot){
            case 1:
                as1KartBilgileri.add(kart);
                break;
            case 2:
                as2KartBilgileri.add(kart);
                break;
            case 3:
                as3KartBilgileri.add(kart);
                break;
            case 4:
                as4KartBilgileri.add(kart);
                break;
        }
    }

    public Kart ustKartGetir(int slot){
        switch (slot){
            case 1:
                return as1KartBilgileri.get(as1KartBilgileri.size()-1);
            case 2:
                return as2KartBilgileri.get(as2KartBilgileri.size()-1);
            case 3:
                return as3KartBilgileri.get(as3KartBilgileri.size()-1);
            case 4:
                return as4KartBilgileri.get(as4KartBilgileri.size()-1);
        }
        return null;
    }

    public int asKartSayisi(int slot){
        switch (slot){
            case 1:
                return as1KartBilgileri.size();
            case 2:
                return as2KartBilgileri.size();
            case 3:
                return as3KartBilgileri.size();
            case 4:
                return as4KartBilgileri.size();
        }
        return 0;
    }




}

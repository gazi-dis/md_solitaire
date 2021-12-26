package com.example.md_solitaire;
import android.content.ClipData;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;

import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView[] askart = new ImageView[4];
    private ImageView[][] kapaliKartlar = new ImageView[7][19];
    private Kart[][] kapaliKartlarBilgi = new Kart[7][19];
    private ImageView ortakart;
    private ImageView ortaacikkart;
    private As as;
    private Orta orta;
    private Deste deste = new Deste();
    private static boolean ortaSuruklendimi = false;
    private static int kolon_bittimi = 0;


    private void kartGoster(Kart kart, ImageView kartResim) {
        AssetManager assets = getAssets();
        try {
            InputStream resim = assets.open(kart.toString() + ".png");
            Drawable flag = Drawable.createFromStream(resim, kart.toString());
            kartResim.setImageDrawable(flag);
        } catch (IOException e) {
        }
        kartResim.setVisibility(View.VISIBLE);
    }

    private void turaBasla() {
        for (int i=0;i<7;i++){
                Kart kart = deste.randomKartGetir();
                kartGoster(kart,kapaliKartlar[i][i]);
                kapaliKartlarBilgi[i][i] = kart;
                kapaliKartlar[i][i].setOnTouchListener(new MyTouchListener());
                kapaliKartlar[i][i].setOnDragListener(new MyDragListener());
                for (int j=i;j<i+12;j++){
                    kapaliKartlar[i][j+1].setVisibility(View.INVISIBLE);
                }
        }
    }

    private void oyunaBasla() {
        deste = new Deste();
        as = new As();
        orta = new Orta(deste);
        turaBasla();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askart[0] = (ImageView) findViewById(R.id.askart1);
        askart[1] = (ImageView) findViewById(R.id.askart2);
        askart[2] = (ImageView) findViewById(R.id.askart3);
        askart[3] = (ImageView) findViewById(R.id.askart4);
        askart[0].setOnDragListener(new AsDragListener());
        askart[1].setOnDragListener(new AsDragListener());
        askart[2].setOnDragListener(new AsDragListener());
        askart[3].setOnDragListener(new AsDragListener());

        for (int i=0;i<7;i++) {
            for (int j=0;j<13+i;j++) {
                String hedef = "kapalikart";
                hedef = hedef+String.valueOf(i)+"_"+String.valueOf(j+1);
                int resID = getResources().getIdentifier(hedef, "id", getPackageName());
                kapaliKartlar[i][j] = ((ImageView) findViewById(resID));
            }
        }

        ortakart = (ImageView) findViewById(R.id.ortakart);
        ortakart.setOnClickListener(ortaKartTikla);
        ortaacikkart = (ImageView) findViewById(R.id.ortaacikkart);

        oyunaBasla();
    }

    public OnClickListener ortaKartTikla = new OnClickListener() {
        public void onClick(View v) {
            Kart cekilenKart = orta.ustKartCek();
            if (cekilenKart!=null){
                ortaacikkart.setVisibility(View.VISIBLE);
                kartGoster(cekilenKart,ortaacikkart);
                ortaacikkart.setOnTouchListener(new ortaAcikTouchListener());
            }else {
                ortaacikkart.setVisibility(View.INVISIBLE);
            }
        }
    };

    private final class ortaAcikTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                ortaSuruklendimi=true;
                return true;
            } else {
                return false;
            }
        }
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                //view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    Log.e("ortada kalan kart:",String.valueOf(orta.getOrtaKartSayi()));
                    Log.e("destede kalan kart:",String.valueOf(deste.getDesteKartSayi()));
                    Log.e("orta suruklendi mi:",String.valueOf(ortaSuruklendimi));
                    if (ortaSuruklendimi){
                        // suruklenen kart view
                        View view = (View) event.getLocalState();
                        int[] birakilan = new int[2];
                        //birakilan kart yeri indisini buluyoruz

                        for (int i=0;i<7;i++) {
                            for (int j = 0; j < 13 + i; j++) {
                                if (kapaliKartlar[i][j].getId()==v.getId()){
                                    birakilan[0]=i;
                                    birakilan[1]=j;
                                    break;
                                }
                            }
                        }
                        int eski_deger=orta.getOrtadakiKart().getDeger();
                        Log.e("k deger:",String.valueOf(eski_deger));
                        String eski_tip=orta.getOrtadakiKart().getTip();
                        String eski_renk=orta.getOrtadakiKart().getRenk();
                        int yeni_deger=kapaliKartlarBilgi[birakilan[0]][birakilan[1]].getDeger();
                        String yeni_tip=kapaliKartlarBilgi[birakilan[0]][birakilan[1]].getTip();
                        String yeni_renk=kapaliKartlarBilgi[birakilan[0]][birakilan[1]].getRenk();
                        //karti koyma sartlari
                        if (eski_deger==yeni_deger-1 && eski_tip!=yeni_tip && eski_renk!=yeni_renk ){
                            Log.e("dinlemedi","hataaaaaa");
                            //+1 dememizin sebebi bir sonrakine eklemekrtir
                            kartGoster(orta.getOrtadakiKart(),kapaliKartlar[birakilan[0]][birakilan[1]+1]);
                            kapaliKartlar[birakilan[0]][birakilan[1]+1].setOnTouchListener(new MyTouchListener());
                            //birakilan eski yere artik kart suruklenemez
                            kapaliKartlar[birakilan[0]][birakilan[1]].setOnDragListener(null);
                            //bir sonrakine suruklenebilir
                            kapaliKartlar[birakilan[0]][birakilan[1]+1].setOnDragListener(new MyDragListener());
                            //yeni kart bilgisini guncelliyoruz
                            kapaliKartlarBilgi[birakilan[0]][birakilan[1] + 1] = orta.getOrtadakiKart();
                            orta.ortadakiKartiSil();
                            if(orta.getOrtadakiKart()!=null){
                                kartGoster(orta.getOrtadakiKart(),ortaacikkart);
                            }else{
                                ortaacikkart.setImageResource(0);
                            }
                        }else if(kolon_bittimi>0 && eski_deger==13){
                            // K yi ortadan alip basa ekliyoruz
                            Log.e("k eklenmeye calisiliyor","ortadan");
                            kartGoster(orta.getOrtadakiKart(), kapaliKartlar[birakilan[0]][birakilan[1]]);
                            kapaliKartlarBilgi[birakilan[0]][birakilan[1]] = orta.getOrtadakiKart();
                            kapaliKartlar[birakilan[0]][birakilan[1]].setOnTouchListener(new MyTouchListener());
                            kapaliKartlar[birakilan[0]][birakilan[1]].setOnDragListener(new MyDragListener());
                            orta.ortadakiKartiSil();
                            if(orta.getOrtadakiKart()!=null){
                                kartGoster(orta.getOrtadakiKart(),ortaacikkart);
                            }else{
                                ortaacikkart.setImageResource(0);
                            }
                            kolon_bittimi--;
                        }
                        ortaSuruklendimi=false;
                    }else {
                        // suruklenen kart view
                        View view = (View) event.getLocalState();
                        int[] suruklenen = new int[2];
                        int[] birakilan = new int[2];
                        //kart indislerini buluyoruz
                        for (int i = 0; i < 7; i++) {
                            for (int j = 0; j < 13 + i; j++) {
                                if (kapaliKartlar[i][j].getId() == view.getId()) {
                                    suruklenen[0] = i;
                                    suruklenen[1] = j;
                                    Log.e("deger i:", String.valueOf(i));
                                    Log.e("deger j:", String.valueOf(j));
                                    break;
                                }
                            }
                        }
                        for (int i = 0; i < 7; i++) {
                            for (int j = 0; j < 13 + i; j++) {
                                if (kapaliKartlar[i][j].getId() == v.getId()) {
                                    birakilan[0] = i;
                                    birakilan[1] = j;
                                    break;
                                }
                            }
                        }
                        int eski_deger = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]].getDeger();
                        String eski_tip = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]].getTip();
                        String eski_renk = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]].getRenk();
                        int yeni_deger = kapaliKartlarBilgi[birakilan[0]][birakilan[1]].getDeger();
                        String yeni_tip = kapaliKartlarBilgi[birakilan[0]][birakilan[1]].getTip();
                        String yeni_renk = kapaliKartlarBilgi[birakilan[0]][birakilan[1]].getRenk();
                        //karti koyma sartlari
                        if (eski_deger == yeni_deger - 1 && eski_tip != yeni_tip && eski_renk != yeni_renk) {
                            //eger suruklenen kartin pesinde kartlar varsa
                            Log.e("visible deger:",String.valueOf(kapaliKartlar[suruklenen[0]][suruklenen[1]+1].getVisibility()));
                            if(kapaliKartlar[suruklenen[0]][suruklenen[1]+1].getVisibility() != View.INVISIBLE ){
                                Log.e("aradan cekildi","aradannnnnn");
                                int sayac=0;
                                while(kapaliKartlar[suruklenen[0]][suruklenen[1]+sayac].getVisibility() != View.INVISIBLE){
                                    //+1 dememizin sebebi bir sonrakine eklemekrtir
                                    kartGoster(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]+sayac], kapaliKartlar[birakilan[0]][birakilan[1] + sayac+1]);
                                    kapaliKartlar[birakilan[0]][birakilan[1] + sayac+1].setOnTouchListener(new MyTouchListener());
                                    //birakilan yere artik kart suruklenemez
                                    kapaliKartlar[birakilan[0]][birakilan[1]+sayac+1].setOnDragListener(null);
                                    //bir sonrakine suruklenebilir
                                    kapaliKartlar[birakilan[0]][birakilan[1] + sayac+1].setOnDragListener(new MyDragListener());
                                    //yeni kart bilgisini guncelliyoruz
                                    kapaliKartlarBilgi[birakilan[0]][birakilan[1] + sayac+1] = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]+sayac];
                                    kapaliKartlar[suruklenen[0]][suruklenen[1]+sayac].setVisibility(View.INVISIBLE);
                                    sayac++;
                                }


                            }else{
                                //+1 dememizin sebebi bir sonrakine eklemekrtir
                                kartGoster(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]], kapaliKartlar[birakilan[0]][birakilan[1] + 1]);
                                kapaliKartlar[birakilan[0]][birakilan[1] + 1].setOnTouchListener(new MyTouchListener());
                                //birakilan yere artik kart suruklenemez
                                kapaliKartlar[birakilan[0]][birakilan[1]].setOnDragListener(null);
                                //bir sonrakine suruklenebilir
                                kapaliKartlar[birakilan[0]][birakilan[1] + 1].setOnDragListener(new MyDragListener());
                                //yeni kart bilgisini guncelliyoruz
                                kapaliKartlarBilgi[birakilan[0]][birakilan[1] + 1] = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]];
                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setVisibility(View.INVISIBLE);
                            }
                            //alinan kartin yerine yenisini getiriyoruz
                            //eger kolonun en basinda kart yoksa bos goster
                            if(suruklenen[1]>0){
                                if (kapaliKartlar[suruklenen[0]][suruklenen[1]-1].getDrawable().getConstantState() == getResources().getDrawable( R.drawable.bos).getConstantState()){
                                    Kart kart = deste.randomKartGetir();
                                    kartGoster(kart, kapaliKartlar[suruklenen[0]][suruklenen[1] - 1]);
                                    kapaliKartlarBilgi[suruklenen[0]][suruklenen[1] - 1] = kart;
                                    Log.e("yeni kart bilgi i:",String.valueOf(suruklenen[0]));
                                    Log.e("yeni kart bilgi j:",String.valueOf(suruklenen[1]-1));
                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnTouchListener(new MyTouchListener());
                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnDragListener(new MyDragListener());
                                }else{
                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnTouchListener(new MyTouchListener());
                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnDragListener(new MyDragListener());
                                }
                            }else if (suruklenen[1]==0){
                                Log.e("kucuk","o");
                                kolon_bittimi++;
                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setImageResource(R.drawable.bos_kart);
                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setVisibility(View.VISIBLE);
                                //kapaliKartlar[suruklenen[0]][suruklenen[1]].setImageResource(0);
                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setOnDragListener(new MyDragListener());
                            }
                        }else if(kolon_bittimi>0 && eski_deger==13){
                            // K yi basa ekliyoruz
                            Log.e("k eklenmeye calisiliyor","alttan");

                            //eger k nin ardindan kartlar var ise
                            if(kapaliKartlar[suruklenen[0]][suruklenen[1]+1].getVisibility() != View.INVISIBLE ){
                                Log.e("aradan cekildi","aradannnnnn");
                                int sayac=0;
                                while(kapaliKartlar[suruklenen[0]][suruklenen[1]+sayac].getVisibility() != View.INVISIBLE){
                                    //+1 dememizin sebebi bir sonrakine eklemekrtir
                                    kartGoster(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]+sayac], kapaliKartlar[birakilan[0]][birakilan[1] + sayac]);
                                    kapaliKartlar[birakilan[0]][birakilan[1] + sayac].setOnTouchListener(new MyTouchListener());
                                    //birakilan yere artik kart suruklenemez
                                    kapaliKartlar[birakilan[0]][birakilan[1]+sayac].setOnDragListener(null);
                                    //bir sonrakine suruklenebilir
                                    kapaliKartlar[birakilan[0]][birakilan[1] + sayac].setOnDragListener(new MyDragListener());
                                    //yeni kart bilgisini guncelliyoruz
                                    kapaliKartlarBilgi[birakilan[0]][birakilan[1] + sayac] = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]+sayac];
                                    kapaliKartlar[suruklenen[0]][suruklenen[1]+sayac].setVisibility(View.INVISIBLE);
                                    sayac++;
                                }
                            }else{
                                //Sadece K basa ekleniyor
                                kartGoster(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]], kapaliKartlar[birakilan[0]][birakilan[1]]);
                                kapaliKartlarBilgi[birakilan[0]][birakilan[1]] = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]];
                                kapaliKartlar[birakilan[0]][birakilan[1]].setOnTouchListener(new MyTouchListener());
                                kapaliKartlar[birakilan[0]][birakilan[1]].setOnDragListener(new MyDragListener());
                                //suruklenen yeri Invisible yapiyoruz
                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setVisibility(View.INVISIBLE);
                            }
                            if(suruklenen[1]>0){
                                if (kapaliKartlar[suruklenen[0]][suruklenen[1]-1].getDrawable().getConstantState() == getResources().getDrawable( R.drawable.bos).getConstantState()){
                                    Kart kart = deste.randomKartGetir();
                                    kartGoster(kart, kapaliKartlar[suruklenen[0]][suruklenen[1] - 1]);
                                    kapaliKartlarBilgi[suruklenen[0]][suruklenen[1] - 1] = kart;
                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnTouchListener(new MyTouchListener());
                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnDragListener(new MyDragListener());
                                }else{
                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnTouchListener(new MyTouchListener());
                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnDragListener(new MyDragListener());
                                }
                            }else if (suruklenen[1]==0){
                                Log.e("kucuk","o");
                                kolon_bittimi++;
                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setImageResource(R.drawable.bos_kart);
                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setVisibility(View.VISIBLE);
                                //kapaliKartlar[suruklenen[0]][suruklenen[1]].setImageResource(0);
                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setOnDragListener(new MyDragListener());
                            }
                            kolon_bittimi--;
                        }
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        }

    }

    class AsDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    String tag = (String) v.getTag();
                    int asSlot = Integer.parseInt(tag);
                    if (ortaSuruklendimi){
                        Log.e("as slot:",tag);
                        //as slotunda daha oncesinde herhangi kart var mi - yok ise ortadan surukle
                        if (!as.kartVarmi(asSlot) && orta.getOrtadakiKart().getDeger()==1){
                            kartGoster(orta.getOrtadakiKart(),askart[asSlot-1]);
                            //yeni kart bilgisini as slotuna ekliyoruz
                            as.kartEkle(orta.getOrtadakiKart(),asSlot);
                            orta.ortadakiKartiSil();
                            if(orta.getOrtadakiKart()!=null){
                                kartGoster(orta.getOrtadakiKart(),ortaacikkart);
                            }else{
                                ortaacikkart.setImageResource(0);
                            }
                        }else if(as.kartVarmi(asSlot)){
                            //as slotunda daha oncesinde kart var ise - var ise ortadan surukle
                            // suruklenen kart view
                            View view = (View) event.getLocalState();
                            Kart son_kart = as.ustKartGetir(asSlot);
                            int eski_deger=orta.getOrtadakiKart().getDeger();
                            String eski_tip=orta.getOrtadakiKart().getTip();
                            String eski_renk=orta.getOrtadakiKart().getRenk();
                            int yeni_deger=son_kart.getDeger();
                            String yeni_tip=son_kart.getTip();
                            String yeni_renk=son_kart.getRenk();
                            //karti koyma sartlari
                            if (eski_deger==yeni_deger+1 && eski_deger==yeni_deger+1 && eski_tip==yeni_tip && eski_renk==yeni_renk ){
                                kartGoster(orta.getOrtadakiKart(),askart[asSlot-1]);
                                //yeni kart bilgisini as slotuna ekliyoruz
                                as.kartEkle(orta.getOrtadakiKart(),asSlot);
                                orta.ortadakiKartiSil();
                                if(orta.getOrtadakiKart()!=null){
                                    kartGoster(orta.getOrtadakiKart(),ortaacikkart);
                                }else{
                                    ortaacikkart.setImageResource(0);
                                }
                            }
                            ortaSuruklendimi=false;
                        }
                    }else {
                        //asagidan asa gelen kartlar
                        View view = (View) event.getLocalState();
                        int[] suruklenen = new int[2];
                        //kart indislerini buluyoruz
                        for (int i = 0; i < 7; i++) {
                            for (int j = 0; j < 13 + i; j++) {
                                if (kapaliKartlar[i][j].getId() == view.getId()) {
                                    suruklenen[0] = i;
                                    suruklenen[1] = j;
                                    break;
                                }
                            }
                        }
                        int eski_deger = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]].getDeger();
                        String eski_tip = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]].getTip();
                        String eski_renk = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]].getRenk();
                        //as slotunda daha oncesinde herhangi kart var mi - yok ise asagidan surukle
                        if (!as.kartVarmi(asSlot) && eski_deger==1){
                            kartGoster(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]],askart[asSlot-1]);
                            //yeni kart bilgisini as slotuna ekliyoruz
                            as.kartEkle(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]],asSlot);
                            kapaliKartlar[suruklenen[0]][suruklenen[1]].setVisibility(View.INVISIBLE);

                        }else if(as.kartVarmi(asSlot)){
                            Kart son_kart = as.ustKartGetir(asSlot);
                            int yeni_deger=son_kart.getDeger();
                            String yeni_tip=son_kart.getTip();
                            String yeni_renk=son_kart.getRenk();
                            if (eski_deger==yeni_deger+1 && eski_deger==yeni_deger+1 && eski_tip==yeni_tip && eski_renk==yeni_renk ){
                                kartGoster(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]],askart[asSlot-1]);
                                //yeni kart bilgisini as slotuna ekliyoruz
                                as.kartEkle(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]],asSlot);
                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setVisibility(View.INVISIBLE);

                            }
                        }
                        if(suruklenen[1]>0){
                            //eger suruklenen kartin oncesinde bos bir kart varsa
                            if(kapaliKartlar[suruklenen[0]][suruklenen[1]-1].getDrawable().getConstantState() == getResources().getDrawable( R.drawable.bos).getConstantState()){
                                if(suruklenen[1]>0){
                                    Kart kart = deste.randomKartGetir();
                                    kartGoster(kart, kapaliKartlar[suruklenen[0]][suruklenen[1] - 1]);
                                    kapaliKartlarBilgi[suruklenen[0]][suruklenen[1] - 1] = kart;
                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnTouchListener(new MyTouchListener());
                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnDragListener(new MyDragListener());
                                }
                            }
                        }else if (suruklenen[1]==0){
                            Log.e("kucuk","o");
                            kolon_bittimi++;
                            kapaliKartlar[suruklenen[0]][suruklenen[1]].setImageResource(R.drawable.bos_kart);
                            kapaliKartlar[suruklenen[0]][suruklenen[1]].setVisibility(View.VISIBLE);
                            //kapaliKartlar[suruklenen[0]][suruklenen[1]].setImageResource(0);
                            kapaliKartlar[suruklenen[0]][suruklenen[1]].setOnDragListener(new MyDragListener());
                        }
                    }
                    //Oyunun bitme durumu kontrolu
                    int kontrol = 0;
                    for(int i=0;i<4;i++){
                        if (as.asKartSayisi(i)==13){
                            kontrol++;
                        }else{
                            break;
                        }
                    }
                    if (kontrol==4){
                        Toast.makeText(getApplicationContext(), "Oyun Bitti Tebrikler", Toast.LENGTH_LONG).show();
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        }

    }

    


}

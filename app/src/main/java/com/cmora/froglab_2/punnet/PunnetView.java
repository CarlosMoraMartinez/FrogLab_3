package com.cmora.froglab_2.punnet;

import android.app.usage.UsageEvents;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmora.froglab_2.R;
import com.cmora.froglab_2.genetics.Individual;

import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PunnetView extends View {
        private static final int SPERM_VELOCITY = 20;
        private static final int EGG_VELOCITY = 5;
        private static final int SPERM_SIZE = 120;
        private static final int EGG_SIZE = 200;

        private Drawable target_frog_image;
        private Canvas canvas;
        private List<Gamete> sperm = new ArrayList<Gamete>();
        private List<Gamete> eggs = new ArrayList<Gamete>();
        private PunnetListener punnet_listener;
        private ImageView ig;
        private ImageView tube;

        private ThreadJuego thread = new ThreadJuego();
        private int selected_gamete= -1;
        private int sperm_in_tube = -1;
        private int egg_in_tube = -1;
        public static final int MALE = 0;
        public static final int FEMALE = 1;
        private int type_gamete_selected = -1;
        private Animation on_tube_start, on_tube_drop, on_tube_enter;
        private int height, width;

        private PunnetProblemInstance problemInstance;

        public static final int EVENT_SET_SPERM = 0;
        public static final int EVENT_SET_EGG = 1;


        // Cada cuanto queremos procesar cambios (ms)
        private static int PERIODO_PROCESO = 50;

        private long ultimoProceso = 0;

        private myDragEventListener dragListen;
        private Context context;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PunnetView(Context contexto, AttributeSet attrs) {
        super(contexto, attrs);
        context = contexto;
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        target_frog_image = AppCompatResources.getDrawable(contexto, R.drawable.ic_bodycolor_red);

        dragListen = new myDragEventListener();
        on_tube_start = AnimationUtils.loadAnimation(context, R.anim.on_tube_start);
        on_tube_drop = AnimationUtils.loadAnimation(context, R.anim.on_tube_drop);
        on_tube_enter = AnimationUtils.loadAnimation(context, R.anim.on_tube_enter);
    }

    public myDragEventListener getDragListen() {
        return dragListen;
    }

    public void setGametes(){
        setSperm();
        setEggs();
    }
    @Override protected void onSizeChanged(int width, int height,
                                           int ancho_anter, int alto_anter) {
        super.onSizeChanged(width, height, ancho_anter, alto_anter);
        Log.d("PunnetView", "onSizeChanged");
        this.width = width;
        this.height = height;
        for (Gamete g: sperm) {
                g.setCenX((int) (Math.random()*height));
                g.setCenY((int) (Math.random()*width));
        }
        for (Gamete g: eggs) {
            g.setCenX((int) (Math.random()*height));
            g.setCenY((int) (Math.random()*width));
        }
        ultimoProceso = System.currentTimeMillis();
        thread.start();
    }
    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("PunnetView", "onDraw");
        this.canvas = canvas;
        target_frog_image.draw(canvas);
        synchronized(sperm) {
            for (Gamete gam : sperm) {
                gam.draw(canvas);
            }
            for (Gamete gam : eggs) {
                gam.draw(canvas);
            }
        }
        //unpauseGametes();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d("PunnetView", "onDetachedFromWindow");
        stopGametes();
        thread.pausar();
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d("PunnetView", "onAttachedToWindow");
        if(thread != null){
            thread.reanudar();
        }

        unpauseGametes();
        super.onAttachedToWindow();
    }

    public void setIg(ImageView imgv){
        this.ig = imgv;
    }

    public void setProblemInstance(PunnetProblemInstance problemInstance) {
        this.problemInstance = problemInstance;
    }

    public void setVistaTubo(ImageView imgv){
        this.tube = imgv;
        tube.setOnDragListener(dragListen);
        tube.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorIndividualCard, null));
        tube.setAnimation(on_tube_start);
        Log.d("PunnetView", "setVistaTubo");
    }
    public void setPunnetListener(PunnetListener listener){
        this.punnet_listener = listener;
    }
    public int getSelectedGamete(){
        return selected_gamete;
    }
    public int getSelectedGameteSex(){
        return type_gamete_selected;
    }
    public Drawable getSelectedDrawable(){
        Drawable dr;
        if (type_gamete_selected == PunnetView.MALE) {
            dr = sperm.get(selected_gamete).getDrawable();
        } else {
            dr = eggs.get(selected_gamete).getDrawable();
        }
        return dr;
    }
    protected void updateMovements() {
        long ahora = System.currentTimeMillis();
        if (ultimoProceso + PERIODO_PROCESO > ahora) {
            return;
        }

        double factorMov = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora;
        //double num = 0;
        Log.d("PunnetView", "updateMovements");
        for (Gamete sp : sperm) {
            /*num = Math.random();
            if(num < 0.005){
                sp.setRotacion(sp.getRotacion()*-1);
            }*/
            sp.setIncX(Math.cos(Math.toRadians(sp.getAngulo() + sp.ANGULO_INICIAL)) * SPERM_VELOCITY);
            sp.setIncY(Math.sin(Math.toRadians(sp.getAngulo() + sp.ANGULO_INICIAL)) * SPERM_VELOCITY);
            sp.incrementaPos(factorMov);
        }
        for (Gamete sp : eggs) {
            sp.setIncX(Math.random() * EGG_VELOCITY);
            sp.setIncY(Math.random() * EGG_VELOCITY);
            /*num = Math.random();
            if(num < 0.001){
                sp.setRotacion(sp.getRotacion()*-1);
            }else if(num < 0.002){
                sp.setIncY(sp.getIncY()*-1);
            }else if(num < 0.003){
                sp.setIncX(sp.getIncX()*-1);
            }*/
            sp.incrementaPos(factorMov);
        }
    }

    public ThreadJuego getThread() {
        return thread;
    }
    class ThreadJuego extends Thread {
        private boolean pausa, corriendo;

        public synchronized void pausar() {
            pausa = true;
        }

        public synchronized void reanudar() {
            pausa = false;
            notify();
        }

        public void detener() {
            corriendo = false;
            if (pausa) reanudar();
        }

        @Override
        public void run() {
            corriendo = true;
            while (corriendo) {
                updateMovements();
                synchronized (this) {
                    while (pausa) {
                        try {
                            wait();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    public boolean setClickedItem(int x, int y){
        selected_gamete = -1;
        for(int i = 0; i < sperm.size(); i++){
            if(sperm.get(i).isTouched(x, y)){
                selected_gamete =i;
                type_gamete_selected = MALE;
                Log.d("PunnetView", "Clicked Sperm " + Integer.toString(selected_gamete));
                return true;
            }
        }
        for(int i = 0; i < eggs.size(); i++){
            if(eggs.get(i).isTouched(x, y)){
                selected_gamete =i;
                type_gamete_selected = FEMALE;
                Log.d("PunnetView", "Clicked Egg: " + Integer.toString(selected_gamete));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int positionX = (int) event.getX();
        int positionY = (int) event.getY();
        if (!setClickedItem(positionX, positionY)) {
            Log.d("PunnetView", "Item not touched");
            return false;
        }
            ClipData.Item item = new ClipData.Item(Integer.toString(selected_gamete));
            ClipData dragData = new ClipData(
                    Integer.toString(selected_gamete),
                    new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                    item);
            if (ig == null) {
                Log.d("PunnetView", "Ig is null");
            }
            Drawable dr = null;
            if(type_gamete_selected == FEMALE){
                dr = AppCompatResources.getDrawable(context, R.drawable.egg1);
                //dr = (AnimationDrawable) AppCompatResources.getDrawable(getContext(), R.drawable.egganimation);
                eggs.get(selected_gamete).setInvisible();
            }else{
                dr = AppCompatResources.getDrawable(context, R.drawable.sperm1b);
                //dr = (AnimationDrawable) AppCompatResources.getDrawable(getContext(), R.drawable.spermanimation);
                sperm.get(selected_gamete).setInvisible();
            }
            ig.setImageDrawable(dr);
            //((AnimationDrawable)dr).start();


            View.DragShadowBuilder myShadow = new DragShadowBuilder(ig);
            startDrag(dragData, myShadow, null, 0);

            return false;
    }


    private void setSperm(){
        sperm = new ArrayList<Gamete>();
        List<String> wrongsperm = problemInstance.getSperm_genotypes();
        Log.d("PunnetView", "SetSperm: 1");
        AnimationDrawable spdraw;
        String genotype;
        for (int i = 0; i < problemInstance.getNumSperms(); i++) {
            //spdraw = AppCompatResources.getDrawable(context, R.drawable.sperm1);
            spdraw = (AnimationDrawable) AppCompatResources.getDrawable(getContext(), R.drawable.spermanimation);
            final View v = this;
            spdraw.setCallback(new Drawable.Callback() {
                @Override
                public void unscheduleDrawable(Drawable who, Runnable what) {
                    Log.d("PunnetView", "SetSperm: unscheduleDrawable");
                    v.removeCallbacks(what);
                }
                @Override
                public void scheduleDrawable(Drawable who, Runnable what, long when) {
                    Log.d("PunnetView", "SetSperm: scheduleDrawable");
                    v.postDelayed(what, when - SystemClock.uptimeMillis());
                }
                @Override
                public void invalidateDrawable(Drawable who) {
                    Log.d("PunnetView", "SetSperm: invalidateDrawable");
                    v.postInvalidate();
                }
            });
            /*if(i == 0){
                genotype = this.problemInstance.getRight_sperm();
            }else{
                genotype = wrongsperm.get(new Random().nextInt(wrongsperm.size()));
            }*/
            genotype = wrongsperm.get(i);
            Gamete sp = new Gamete(this, spdraw, genotype);
            sp.setAncho(SPERM_SIZE);
            sp.setAlto(SPERM_SIZE);
            sp.setRadioColision(SPERM_SIZE/2);
            //sp.setIncY(Math.random() * 40 - 2);
            //sp.setIncX(Math.random() * 40 - 2);
            sp.setAngulo((int) (Math.random() * 360));
            //sp.setAngulo((int) (0));
            sp.setRotacion((int) (Math.random() * 8 - 4));
            //sp.setRotacion(0);
            sp.setIncX(Math.cos(Math.toRadians(sp.getAngulo() + sp.ANGULO_INICIAL)) *
                    SPERM_VELOCITY);
            sp.setIncY(Math.sin(Math.toRadians(sp.getAngulo() + sp.ANGULO_INICIAL)) *
                    SPERM_VELOCITY);
            sp.setCenX((int) (Math.random()*width));
            sp.setCenY((int) (Math.random()*height));
            Log.d("PunnetView", "SetSperm 2: before start");
            spdraw.start();
            sperm.add(sp);
        }
    }

    private void setEggs(){
        List<String> wrongeggs = problemInstance.getEgg_genotypes();
        eggs = new ArrayList<Gamete>();
        AnimationDrawable spdraw;
        String genotype;
        for (int i = 0; i < problemInstance.getNumEggs(); i++) {
            //spdraw = (Drawable)AppCompatResources.getDrawable(context, R.drawable.sperm1);
            spdraw = (AnimationDrawable) AppCompatResources.getDrawable(getContext(), R.drawable.egganimation);
            //spdraw = (Drawable) AppCompatResources.getDrawable(getContext(), R.drawable.egg1);

            final View v = this;
            spdraw.setCallback(new Drawable.Callback() {
                @Override
                public void unscheduleDrawable(Drawable who, Runnable what) {
                    v.removeCallbacks(what);
                }
                @Override
                public void scheduleDrawable(Drawable who, Runnable what, long when) {
                    v.postDelayed(what, when - SystemClock.uptimeMillis());
                }
                @Override
                public void invalidateDrawable(Drawable who) {
                    v.postInvalidate();
                }
            });
            /*if(i == 0){
                genotype = this.problemInstance.getRight_egg();
            }else{
                genotype = wrongeggs.get(new Random().nextInt(wrongeggs.size()));
            }*/
            genotype = wrongeggs.get(i);
            Gamete sp = new Gamete(this, spdraw, genotype);
            sp.setAncho(EGG_SIZE);
            sp.setAlto(EGG_SIZE);
            sp.setRadioColision(EGG_SIZE/2);
            sp.setAngulo((int) (Math.random() * 360));
            //sp.setAngulo((int) (0));
            sp.setRotacion((int) (Math.random() * 8 - 4));
            //sp.setRotacion(0);
            sp.setIncX(Math.cos(Math.toRadians(sp.getAngulo() + sp.ANGULO_INICIAL)) *
                    EGG_VELOCITY);
            sp.setIncY(Math.sin(Math.toRadians(sp.getAngulo() + sp.ANGULO_INICIAL)) *
                    EGG_VELOCITY);
            sp.setCenX((int) (Math.random()*width));
            sp.setCenY((int) (Math.random()*height));
            spdraw.start();

            eggs.add(sp);
        }
    }
    public void stopGametes(){
        Log.d("PunnetView", "stopGametes");
        for(int i = 0; i < eggs.size(); i++){
            eggs.get(i).getDrawable().stop();
        }
        for(int i = 0; i < sperm.size(); i++){
            sperm.get(i).getDrawable().stop();
        }
    }
    public void unpauseGametes(){
        Log.d("PunnetView", "stopGametes");
        for(int i = 0; i < eggs.size(); i++){
            eggs.get(i).getDrawable().start();
        }
        for(int i = 0; i < sperm.size(); i++){
            sperm.get(i).getDrawable().start();
        }
    }

    protected class myDragEventListener implements View.OnDragListener {

        public boolean onDrag(final View v, DragEvent event) {
            final int action = event.getAction();
            switch(action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        v.invalidate();
                        return true;
                    }
                    return false;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.startAnimation(on_tube_enter);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d("DragDrop","ACTION_DRAG_EXITED");
                    //v.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorIndividualCard, null));
                    v.startAnimation(on_tube_start);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    //v.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorIndividualCard, null));
                    on_tube_drop.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            v.startAnimation(on_tube_start);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                    v.startAnimation(on_tube_drop);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.invalidate();
                    if (event.getResult()) {
                        Log.d("DragDrop","ACTION_DRAG_ENDED: Dropped inside of tube");
                        v.startAnimation(on_tube_drop);
                        dropInTube();
                    } else {
                        Log.d("DragDrop","ACTION_DRAG_ENDED: Dropped out of tube");
                        dropOutOfTube(event.getX(), event.getY());
                    }
                    return true;
                default:
                    Log.e("DragDrop","ACTION_DRAG_ENDED: Unknown action type received by OnDragListener.");
                    break;
            }
            return false;
        }
    };

    public void dropInTube(){
        //Can't mix two gametes of the same type!
        if(type_gamete_selected == MALE && sperm_in_tube != -1 || type_gamete_selected == FEMALE && egg_in_tube != -1){
            wrongAnswer("You can't mix gametes of the same type!");
        }else{
            if(type_gamete_selected == MALE){
                sperm_in_tube = selected_gamete;
                punnet_listener.onEvent(EVENT_SET_SPERM, sperm.get(sperm_in_tube).getGenotype());
            }else{
                egg_in_tube = selected_gamete;
                punnet_listener.onEvent(EVENT_SET_EGG, eggs.get(egg_in_tube).getGenotype());
            }
            if(sperm_in_tube != -1 && egg_in_tube != -1){
                boolean ans = problemInstance.checkAnswer(sperm.get(sperm_in_tube).getGenotype(), eggs.get(egg_in_tube).getGenotype());
                if(ans){
                    rightAnswer();
                }else{
                    wrongAnswer("You picked the wrong genotypes!");
                }
            }
        }


    }
    public void dropOutOfTube(double x, double y){
        //int xb = (int)x*width;
        //int yb = (int)y*height;
        if(type_gamete_selected == FEMALE){
            //eggs.get(selected_gamete).setCenX(xb);
            //eggs.get(selected_gamete).setCenY(yb);
            eggs.get(selected_gamete).setVisible();
        }else{
            //sperm.get(selected_gamete).setCenX(xb);
            //sperm.get(selected_gamete).setCenY(yb);
            sperm.get(selected_gamete).setVisible();
        }
        selected_gamete = -1;
        type_gamete_selected = -1;
    }
    public void rightAnswer(){
        selected_gamete = -1;
        type_gamete_selected = -1;
        sperm_in_tube = -1;
        egg_in_tube = -1;
        Toast.makeText(context, "Right Answer!", Toast.LENGTH_SHORT).show();
        punnet_listener.onAnswer(true);

    }
    public void wrongAnswer(String message){
        selected_gamete = -1;
        type_gamete_selected = -1;
        sperm_in_tube = -1;
        egg_in_tube = -1;
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        punnet_listener.onAnswer(false);
    }
}
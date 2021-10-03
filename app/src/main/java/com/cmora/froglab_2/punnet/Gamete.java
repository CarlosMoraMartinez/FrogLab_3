package com.cmora.froglab_2.punnet;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;

import androidx.core.content.ContextCompat;

import com.cmora.froglab_2.R;

class Gamete {
    private AnimationDrawable drawable;
    //Imagen que dibujaremos
    private int cenX, cenY;
    //Posición del centro del gráfico
    private int ancho, alto;
    //Dimensiones de la imagen
    private double incX, incY;
    //Velocidad desplazamiento
    private double angulo, rotacion;//Ángulo y velocidad rotación
    public final double ANGULO_INICIAL = -45;
    private int radioColision;
    //Para determinar colisión
    private int xAnterior, yAnterior; // Posición anterior
    private int radioInval;
    // Radio usado en invalidate()
    private View view;
    private PunnetView.myDragEventListener dragListener;
    private boolean visible = true;
    private String genotype = "_";
    private TextPaint textPaint;
    // Usada en view.invalidate()
    public Gamete(View view, AnimationDrawable drawable, String genotype){
        this.view = view;
        this.drawable = drawable;
        ancho = drawable.getIntrinsicWidth();
        alto = drawable.getIntrinsicHeight();
        angulo = ANGULO_INICIAL;

        Log.d("GAMETE", "Alto-Ancho: " + Double.toString(alto) + ", " + Double.toString(ancho));
        radioColision = (alto+ancho)/4;
        radioInval = (int) Math.hypot(ancho/2, alto/2);

        textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG | TextPaint.LINEAR_TEXT_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(56f);
        textPaint.setColor(ContextCompat.getColor(view.getContext(), R.color.colorWrong));
        this.genotype = genotype;
        //this.canvas.drawText("A/a",300,300, textPaint);

    }
    public void draw(Canvas canvas){
        if(visible) {
            Log.d("Gamete", "Is visible");
            int x = cenX - ancho / 2;
            int y = cenY - alto / 2;
            drawable.setBounds(x, y, x + ancho, y + alto);
            canvas.save();
            canvas.rotate((float) angulo, cenX, cenY);
            drawable.draw(canvas);
            canvas.restore();
            xAnterior = cenX;
            yAnterior = cenY;
            canvas.drawText(this.genotype, cenX, cenY, textPaint);
        }else{
            Log.d("Gamete", "Is invisible");
        }
    }
    public String getGenotype(){
        return this.genotype;
    }
    public void setVisible(){
        this.visible = true;
        drawable.setVisible(true, true);
    }
    public void setInvisible(){
        Log.d("Gamete", "setInvisible");
        this.visible = false;
        drawable.setVisible(false, true);
    }
    public void incrementaPos(double factor){
        cenX += incX * factor;
        cenY += incY * factor;
        angulo += rotacion * factor;
        // Si salimos de la pantalla, corregimos posición
        if(cenX<0)
            cenX=view.getWidth();
        if(cenX>view.getWidth()) cenX=0;
        if(cenY<0)
            cenY=view.getHeight();
        if(cenY>view.getHeight()) cenY=0;
        view.postInvalidate (cenX-radioInval, cenY-radioInval,
                cenX+radioInval, cenY+radioInval);
        view.postInvalidate(xAnterior-radioInval, yAnterior-radioInval,
                xAnterior+radioInval, yAnterior+radioInval);
    }
    public double distancia(Gamete g) {
        return Math.hypot(cenX-g.cenX, cenY-g.cenY);
    }
    public boolean verificaColision(Gamete g) {
        return (distancia(g) < (radioColision + g.radioColision));
    }

    public AnimationDrawable getDrawable() {
        return drawable;
    }

    public void setDrawable(AnimationDrawable drawable) {
        this.drawable = drawable;
    }

    public int getCenX() {
        return cenX;
    }

    public boolean isTouched(int x, int y){
        if(! visible)
            return false;
        return (this.cenX - this.radioColision < x && this.cenX + this.radioColision > x && this.cenY - this.radioColision < y && this.cenY + this.radioColision > y);
    }

    public void setCenX(int cenX) {
        this.cenX = cenX;
    }

    public int getCenY() {
        return cenY;
    }

    public void setCenY(int cenY) {
        this.cenY = cenY;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public double getIncX() {
        return incX;
    }

    public void setIncX(double incX) {
        this.incX = incX;
    }

    public double getIncY() {
        return incY;
    }

    public void setIncY(double incY) {
        this.incY = incY;
    }

    public double getAngulo() {
        return angulo;
    }

    public void setAngulo(double angulo) {
        this.angulo = angulo;
    }

    public double getRotacion() {
        return rotacion;
    }

    public void setRotacion(double rotacion) {
        this.rotacion = rotacion;
    }

    public int getRadioColision() {
        return radioColision;
    }

    public void setRadioColision(int radioColision) {
        this.radioColision = radioColision;
    }

    public int getxAnterior() {
        return xAnterior;
    }

    public void setxAnterior(int xAnterior) {
        this.xAnterior = xAnterior;
    }

    public int getyAnterior() {
        return yAnterior;
    }

    public void setyAnterior(int yAnterior) {
        this.yAnterior = yAnterior;
    }

    public int getRadioInval() {
        return radioInval;
    }

    public void setRadioInval(int radioInval) {
        this.radioInval = radioInval;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

}
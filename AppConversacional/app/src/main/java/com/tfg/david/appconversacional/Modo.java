package com.tfg.david.appconversacional;

/**
 * Created by david on 28/04/2018.
 */

public class Modo {
    private static int tam_elementos;
    private static boolean tocar_hablar;
    private static boolean input_texto;
    private static boolean output_voz;
    private static boolean output_texto;

    public Modo (int tam_elementos, boolean tocar_hablar, boolean input_texto, boolean output_voz, boolean output_texto){
        this.setTam_elementos(tam_elementos);
        this.setTocar_hablar(tocar_hablar);
        this.setInput_texto(input_texto);
        this.setOutput_voz(output_voz);
        this.setOutput_texto(output_texto);
    }

    public static int getTam_elementos() {
        return tam_elementos;
    }

    public static void setTam_elementos(int tam_elementos) {
        Modo.tam_elementos = tam_elementos;
    }

    public static boolean isTocar_hablar() {
        return tocar_hablar;
    }

    public static void setTocar_hablar(boolean tocar_hablar) {
        Modo.tocar_hablar = tocar_hablar;
    }

    public static boolean isInput_texto() {
        return input_texto;
    }

    public static void setInput_texto(boolean input_texto) {
        Modo.input_texto = input_texto;
    }

    public static boolean isOutput_voz() {
        return output_voz;
    }

    public static void setOutput_voz(boolean output_voz) {
        Modo.output_voz = output_voz;
    }

    public static boolean isOutput_texto() {
        return output_texto;
    }

    public static void setOutput_texto(boolean output_texto) {
        Modo.output_texto = output_texto;
    }

    public static Modo AUDITIVA(){
        return new Modo(1, false, true, false, true);
    }

    public static Modo VISUAL_LEVE(){
        return new Modo(2, false, true, true, true);
    }

    public static Modo VISUAL_GRAVE(){
        return new Modo(3, true, false, true, false);
    }
}

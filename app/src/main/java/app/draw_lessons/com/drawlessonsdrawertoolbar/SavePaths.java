package app.draw_lessons.com.drawlessonsdrawertoolbar;

import android.graphics.Path;

import java.util.ArrayList;

/**
 * Created by Theidel on 31/01/2015.
 */

/**
 * Created by Adrian on 27/01/2015.
 *
 * Clase destinada al manejo de la lista
 * de Paths cuando se destruye el activity de dibujo
 *
 * clase utilizada a modo de Singleton.
 */
public class SavePaths {

    private static SavePaths handler;
    private ArrayList<Path> list;
    private ArrayList<Integer> list2;

    private SavePaths() {
    }


    /**
     * Obtiene una instancia del Objeto
     * @return
     */
    public static SavePaths getInstance() {
        if (handler == null)
            handler = new SavePaths();
        return handler;
    }

    /**
     * Obtiene la Lista de Objetos
     * @return
     */
    public ArrayList<Path> getList() {
        return this.list;
    }

    public ArrayList<Integer> getList2(){ return this.list2; }


    /**
     * Lista de Trazos de todas las herramientas
     * @param list
     */
    public void setList(ArrayList<Path> list) {
        this.list = list;
    }


    /**
     * Lista de Trazos de la goma de borrar
     */
    public void setListB(ArrayList<Integer> list) {

        this.list2 = list;
    }



}

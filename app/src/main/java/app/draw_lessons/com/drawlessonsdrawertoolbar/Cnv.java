package app.draw_lessons.com.drawlessonsdrawertoolbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Clase que actuá como un Objeto View personalizado
 * esta clase hace de Canvas.
 * Se utiliza el Canvas para dibujar, y el dibujado se representa
 * en un Bitmap
 * @author Theidel
 *
 */
public class Cnv extends View{

    /*
        Constantes de tamaño de la brocha
     */
    public static final int SIZE_SMALL = 5;
    public static final int SIZE_MEDIUM = 8;
    public static final int SIZE_MAX = 12;
    private int rubIcon; // id de referencia al icono de la papelera


    // Herramientas del canvas
    public static boolean Ruler = false;
    public static boolean Compass = false;
    public static boolean HandMade = false;
    public static boolean Eraser = false;


    private Canvas cnv; //Objeto Canvas para re-renderizar el View
    private Bitmap bmp; //Imagen que queda dibujada sobre nuestro Canvas
    public static Paint p; //Brocha de dibujado principal
    private Path mPa; //Trazo principal de dibujo


    private int brushSize; // Variable para guardar el tamaño actual del Paint
    private int resX,resY; // Variables para indicar la resolución de nuestro Bitmap


    private int doBack = 1;
    private boolean isUnDone = false;

    //Arrays y listas
    public ArrayList<Path> Trazos = new ArrayList<Path>(); //Array para almacenar los pasos hechos en el dibujo
    private ArrayList<Integer> earserPaths = new ArrayList<Integer>(); //Almacena los paths hechos con le goma



    /*
     * Variables de la herrmaienta
     * Regla recta
     */
    public static Bitmap rulerBmp;
    public static boolean rulerLayer = false;
    public static boolean rulerT1=false, rulerT2=false;

    private float rulerX1=0, rulerX2=0, rulerY1=0, rulerY2=0;


    /*
     * Variables de la herramienta
     * Compas
     */
    public static Bitmap compassBmp;
    public static boolean compasLayer = false;
    public static boolean compassT1=false,compassT2=false;

    private float compassX1=0.0F, compassX2=0.0F, compassY1=0.0F, compassY2=0.0F;



    /**
     * Constructores heredados de View
     * @param context
     */

    public Cnv(Context context) {
        super(context);
    }




    /**
     * Metodo para preparar el Cnavas, el bitmap y el Objeto Paint
     * para poder dibujar sobre el View
     */
    public void prepareCancas(){

        //crea el bitmap
        Config  bcfg = Config.RGB_565 ;
        this.bmp = Bitmap.createBitmap(this.resX, this.resY, bcfg);

        //crea la brocha
        this.p = new Paint();
        this.p.setStrokeWidth(this.brushSize);

        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeCap(Paint.Cap.ROUND);
        this.p.setColor(this.getResources().getColor(R.color.stroke_color));

        this.cnv = new Canvas(this.bmp);
        this.cnv.drawColor(this.getResources().getColor(R.color.back_color));

        this.mPa = new Path();
        this.mPa.moveTo(0, 0);

        this.Trazos.add(mPa);
        this.mPa = new Path();

        this.HandMade=true; //por defecto pintura a mano alzada


    }




    /**
     * Metodo heradado que se encarga
     * de repintar la pantalla al crear el View, o al recibir una orden "Invalidate()"
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(this.bmp, 0, 0, this.p);

        if (this.rulerLayer==true){
            canvas.drawBitmap(this.rulerBmp, 0, 0,this.p);
        }else if (this.compasLayer==true){
            canvas.drawBitmap(this.compassBmp, 0, 0, this.p);
        }
        else {
            canvas.drawBitmap(this.bmp, 0, 0, this.p);

        }



    }





    /**
     * Método que sobre-escribe lo que ocurre al generar un evento de tipo Touch
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        activity_draw da = (activity_draw)this.getContext();

        if (da.toolClicked==false) {
            da.hide(da.ClickedID);
        }

        if(this.isUnDone==true){
            this.isUnDone=false;
            this.cleanPaths();
        }

        if (this.HandMade==true){
            this.onHandMade(event);
        }
        else if (this.Compass==true){
            this.onCompassTouch(event);
        }else if(this.Ruler==true){
            this.onRulerTouch(event);
        }else if (this.Eraser==true){
            this.onEraserTouch(event);
        }

        return true;
    }





    /**
     * Método de movimiento con la regla recta
     */
    public void onRulerTouch(MotionEvent event){

		/*
		 * Crea un canvas y un objeto paint solo para pintar sobre un bitmap dedicado
		 * a la regla recta
		 *
		 */

        this.rulerBmp = Bitmap.createBitmap(this.resX,this.resY, Config.ARGB_4444);

        Canvas tmpCNV = new Canvas(this.rulerBmp);
        tmpCNV.drawColor(Color.TRANSPARENT);

        Paint tmpP = new Paint();
        tmpP.setStyle(Paint.Style.STROKE);
        tmpP.setStrokeWidth(5);

        tmpP.setColor(this.getResources().getColor(R.color.primary));


        switch(event.getAction()){

            case MotionEvent.ACTION_MOVE:


                if (rulerT1==false) {
                    tmpCNV.drawCircle(event.getX(), event.getY(), 60, tmpP);
                    this.rulerLayer = true;
                    this.invalidate();
                }

                if(rulerT1==true){
                    this.rulerX2 = event.getX(); this.rulerY2 = event.getY();

                    tmpCNV.drawCircle(event.getX(), event.getY(), 60, tmpP);
                    tmpCNV.drawCircle(this.rulerX1, this.rulerY1, 60, tmpP);

                    this.mPa.moveTo(this.rulerX1, this.rulerY1);
                    this.mPa.lineTo(event.getX(), event.getY());

                    tmpCNV.drawPath(this.mPa, this.p);

                    this.invalidate();

                    this.mPa = new Path();
                    this.rulerT2=true;
                    this.rulerLayer = true;

                }
                break;

            case MotionEvent.ACTION_UP:

                if (this.rulerT1==false) {

                    this.mPa.moveTo(event.getX()-1, event.getY()-1);
                    this.mPa.lineTo(event.getX()+1, event.getY()+1);
                    this.cnv.drawPath(this.mPa, this.p);

                    this.rulerX1 = event.getX();
                    this.rulerY1 = event.getY();

                    this.invalidate();
                    this.rulerT1=true;

                }


                tmpCNV.drawCircle(event.getX(), event.getY(), 60, tmpP);

                if (rulerT1==true && rulerT2==true) {

                    this.mPa = new Path();
                    this.mPa.moveTo(this.rulerX1, this.rulerY1);
                    this.mPa.lineTo(this.rulerX2, this.rulerY2);
                    this.cnv.drawPath(this.mPa, this.p);

                    this.Trazos.add(this.mPa);
                    this.mPa = new Path();

                    this.rulerLayer=false;
                    this.rulerT1=false;
                    this.rulerT2=false;
                    this.rulerBmp = null;

                    this.invalidate();
                }
                break;


        }



    }






    /**
     * Método de movimiento con el dibujado a mano alzada
     */
    public void onHandMade(MotionEvent event){

        int e = event.getAction();
        switch(e){
            case MotionEvent.ACTION_DOWN:

                this.mPa.moveTo(event.getX()-1, event.getY()-1);
                this.mPa.lineTo(event.getX()+1, event.getY()+1);
                this.cnv.drawPath(this.mPa, this.p);

                this.invalidate();

                break;

            case MotionEvent.ACTION_MOVE:

                this.mPa.lineTo(event.getX(), event.getY());
                this.cnv.drawPath(this.mPa, this.p);

                this.invalidate();

                break;

            case MotionEvent.ACTION_UP:

                this.Trazos.add(this.mPa);
                this.mPa = new Path();

                this.invalidate();

                break;

            case MotionEvent.ACTION_CANCEL:
                this.Trazos.add(this.mPa);
                this.mPa = new Path();

                this.invalidate();
                break;
        }

    }




    /**
     * Método de movimiento de compass
     */
    public void onCompassTouch(MotionEvent event){

        this.compassBmp = Bitmap.createBitmap(this.resX,this.resY, Config.ARGB_4444);

        Canvas tmpCNV = new Canvas(this.compassBmp);
        tmpCNV.drawColor(Color.TRANSPARENT);

        Paint tmpP = new Paint();
        tmpP.setStyle(Paint.Style.STROKE);
        tmpP.setStrokeWidth(5);

        tmpP.setColor(this.getResources().getColor(R.color.primary));

        double c=0.0d;

        if (this.compassT1 == false) {

            switch(event.getAction()){

                case MotionEvent.ACTION_DOWN:
                    event.setAction(MotionEvent.ACTION_MOVE);
                    break;

                case MotionEvent.ACTION_MOVE:
                    this.compasLayer=true;
                    tmpCNV.drawCircle(event.getX(), event.getY(), 50, tmpP);
                    this.invalidate();
                    break;

                case MotionEvent.ACTION_UP:
                    this.compassT1=true;
                    tmpCNV.drawCircle(event.getX(), event.getY(), 50, tmpP);
                    tmpCNV.drawPoint(event.getX(), event.getY(), tmpP);


                    this.compassX1 = event.getX();
                    this.compassY1 = event.getY();
                    this.mPa.moveTo(event.getX(), event.getY());

                    this.invalidate();

                    break;
            }

        } else if (this.compassT1==true){



            switch (event.getAction()){

                case MotionEvent.ACTION_DOWN:
                    this.compassX2 = event.getX();
                    this.compassY2 = event.getY();

                    tmpCNV.drawCircle(event.getX(), event.getY(), 50, tmpP);
                    this.invalidate();

                    break;

                case MotionEvent.ACTION_MOVE:
                    this.compassX2 = event.getX();
                    this.compassY2 = event.getY();

                    c = this.getRadius();



                    this.mPa.moveTo(this.compassX1, this.compassY1);
                    this.mPa.lineTo(event.getX(), event.getY());

                    this.mPa.moveTo(event.getX(), event.getY());
                    this.mPa.addCircle(this.compassX1, this.compassY1, (float)c, Direction.CCW);
                    tmpCNV.drawPath(this.mPa, this.p);

                    this.invalidate();
                    this.mPa = new Path();

                    break;

                case MotionEvent.ACTION_UP:
                    this.compassX2 = event.getX();
                    this.compassY2 = event.getY();

                    c = this.getRadius();


                    this.mPa.moveTo(event.getX(), event.getY());
                    this.mPa.addCircle(this.compassX1, this.compassY1, (float)c, Direction.CCW);

                    this.cnv.drawPath(this.mPa, this.p);
                    this.invalidate();

                    this.Trazos.add(this.mPa);
                    this.mPa = new Path();

                    this.compasLayer=false;
                    this.compassT1 = false;
                    this.compassT2 = false;
                    this.compassBmp = null;

                    this.invalidate();

                    break;

            }

        }

    }


    /**
     * Metodo que representa
     * a una goma de borrar mediante
     * Objetos de la clase Path
     * @param event
     */
    public void onEraserTouch(MotionEvent event){

        this.p.setStrokeWidth(this.SIZE_MAX);
        this.p.setColor(this.getResources().getColor(R.color.back_color));

        int e = event.getAction();
        switch(e){
            case MotionEvent.ACTION_DOWN:

                this.mPa.moveTo(event.getX()-1, event.getY()-1);
                this.mPa.lineTo(event.getX()+1, event.getY()+1);
                this.cnv.drawPath(this.mPa, this.p);

                this.invalidate();

                break;

            case MotionEvent.ACTION_MOVE:

                this.mPa.lineTo(event.getX(), event.getY());
                this.cnv.drawPath(this.mPa, this.p);

                this.invalidate();

                break;

            case MotionEvent.ACTION_UP:

                this.Trazos.add(this.mPa);
                this.earserPaths.add(this.mPa.hashCode());
                this.mPa = new Path();
                this.invalidate();

                this.p.setStrokeWidth(this.SIZE_SMALL);
                this.p.setColor(this.getResources().getColor(R.color.stroke_color));


                break;

            case MotionEvent.ACTION_CANCEL:
                this.Trazos.add(this.mPa);
                this.earserPaths.add(this.mPa.hashCode());
                this.mPa = new Path();
                this.invalidate();

                this.p.setStrokeWidth(this.SIZE_SMALL);
                this.p.setColor(this.getResources().getColor(R.color.stroke_color));
                break;
        }

    }




    /**
     * devuelve la distancia entre 2 puntos.
     * Lo que seria el radio de un circulo
     * desde su centro
     * @return
     */
    public double getRadius(){


        float a = (this.compassX1 - this.compassX2);
        float b = (this.compassY1 - this.compassY2);

        double c = Math.sqrt((a*a)+(b*b));

        return c;

    }


    public void savePaths(){
        PathHandler p = PathHandler.getInstance();
        p.setList(this.Trazos);
        p.setList2(this.earserPaths);
    }


    /**
     * restore de Paths
     */
    public void restorePaths(){

        PathHandler p = PathHandler.getInstance();
        this.Trazos = p.getList();
        this.earserPaths = p.getList2();


        this.bmp = Bitmap.createBitmap(this.resX, this.resY, Config.ARGB_4444);
        this.cnv = new Canvas(this.bmp);
        this.cnv.drawColor(this.getResources().getColor(R.color.back_color));


        this.p = new Paint();
        this.p.setStrokeWidth(this.brushSize);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeCap(Paint.Cap.ROUND);
        this.p.setColor(this.getResources().getColor(R.color.stroke_color));


        for (int i=0; i<this.Trazos.size() ; i++){
            Path pa = this.Trazos.get(i);
            if(isDoneWithEraser(pa)){
                this.p.setColor(this.getResources().getColor(R.color.back_color));
                this.p.setStrokeWidth(this.SIZE_MAX);
                this.cnv.drawPath(this.Trazos.get(i),this.p);
            }
            else {
                this.p.setColor(this.getResources().getColor(R.color.stroke_color));
                this.p.setStrokeWidth(this.SIZE_SMALL);
                this.cnv.drawPath(this.Trazos.get(i), this.p);
            }
        }

        this.p.setColor(this.getResources().getColor(R.color.stroke_color));
        this.p.setStrokeWidth(this.SIZE_SMALL);

        this.invalidate();
    }



    /**
     * Repinta el canvas de color blanco para
     * re-establecer la imagen a su estado por defecto
     */
    public void Clean() {
            this.cnv.drawColor(0xFFFFFFFF);
            this.Trazos = new ArrayList<Path>();
            this.invalidate();
        Toast.makeText(this.getContext(),"Nuevo Lienzo",Toast.LENGTH_SHORT).show();
    }


    /**
     * Metodo para des-hacer los ultimos movimientos
     * realizados por el usuario
     */
    public void Undo(){

        this.cnv.drawColor(this.getResources().getColor(R.color.back_color));
        int c = (this.Trazos.size()-doBack)-1;
        int c2 = 0;

        while(c>-1){
            try {
                Path p = this.Trazos.get(c2);

                if (this.isDoneWithEraser(p)){

                    this.p.setColor(this.getResources().getColor(R.color.back_color));
                    this.p.setStrokeWidth(this.SIZE_MAX);

                    this.cnv.drawPath(this.Trazos.get(c2), this.p);
                }else {
                    this.p.setColor(this.getResources().getColor(R.color.stroke_color));
                    this.p.setStrokeWidth(this.SIZE_SMALL);

                    this.cnv.drawPath(this.Trazos.get(c2), this.p);

                }

            }
            catch (ArrayIndexOutOfBoundsException e){
                Toast.makeText(this.getContext(), "No hay más acciones para retroceder", Toast.LENGTH_SHORT).show();
            }
            c2++;
            c--;
        }

        this.invalidate();
        this.isUnDone=true;
        doBack++;
    }




    /**
     * Compreuab si un Path ha sido
     * dibujado con la goma de borrar
     * @param Ptmp
     * @return
     */
    public boolean isDoneWithEraser(Path Ptmp){
        boolean done=false;
        int c=0;

        while(c < (this.earserPaths.size()) ){

            if (this.earserPaths.get(c).hashCode() == Ptmp.hashCode() ){
                done=true;
            }
            c++;
        }
        return done;

    }


    /**
     * Limpia los movimientos realizados por el usuario,
     * ya des-hechos de el Array que los almacena
     */
    public void cleanPaths(){
        Cleaner c= new Cleaner(this.doBack,this.Trazos);
        this.Trazos = c.getTrazos();
        this.doBack = 1;
        this.isUnDone=false;
    }


	/* 
	 * -------------------
	 * -----------------
	 * Getters y Settrs 
	 * -----------------
	 * -------------------
	 */



    /**
     * Método para establecer la resolucion del eje X
     * @param n
     */
    public void setResX(int n){
        this.resX = n;
    }

    /**
     * Método para establecer la resolucion del eje Y
     * @param n
     */
    public void setResY(int n){
        this.resY = n;
    }


    /**
     * Método para establecer el tamaño de la brocha
     * @param n
     */
    public void setStrokeSize(int n){
        this.brushSize = n;
        this.p.setStrokeWidth(this.brushSize);
    }



    public void setRubishIcon(int rubish){
        this.rubIcon = rubish;
    }


    /**
     * get the Bitmap of canvas
     */
   public Bitmap getBitmapt(){
       return this.bmp;
   }


   public Canvas getCnv(){
       return this.cnv;
   }


    public ArrayList<Path> getTrazos() {
        return Trazos;
    }

    public void setTrazos(ArrayList<Path> trazos) {
        Trazos = trazos;
    }

    /**
     * Método para aumenar la calidad del dibujo
     */
    public void ImproveQuality(){

        this.p.setStrokeJoin(Paint.Join.ROUND);
        this.p.setAntiAlias(true);

    }

}
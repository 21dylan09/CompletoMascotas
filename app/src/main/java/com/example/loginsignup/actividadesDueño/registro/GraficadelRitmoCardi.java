package com.example.loginsignup.actividadesDueño.registro;




import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GraficadelRitmoCardi extends View {

    private Paint paint = new Paint();
    private int width, height;
    private float phase = 0;
    private boolean animando = false;

    public GraficadelRitmoCardi(Context context) {
        super(context);
        init();
    }

    public GraficadelRitmoCardi(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setColor(Color.parseColor("#20c75c")); //

        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!animando) return;

        int centerY = height / 2;
        float amplitude = height / 4f; // altura de la onda
        float frequency = 0.05f; // frecuencia de la onda

        float prevX = 0;
        float prevY = centerY;

        for (int x = 1; x < width; x++) {
            float y = (float) (centerY + amplitude * Math.sin(frequency * (x + phase)));
            canvas.drawLine(prevX, prevY, x, y, paint);
            prevX = x;
            prevY = y;
        }

        // Cambiar fase para animar (desplazamiento onda)
        phase += 10;
        if (phase > Integer.MAX_VALUE - 1000) {
            phase = 0; // reset para evitar overflow
        }

        // Pide redibujar para animar
        postInvalidateDelayed(30);
    }

    public void iniciarAnimacion() {
        animando = true;
        invalidate();
    }

    public void detenerAnimacion() {
        animando = false;
        invalidate();
    }
}

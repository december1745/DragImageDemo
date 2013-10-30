package com.example.dragimagedemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;


@SuppressLint("FloatMath")
public class ImageViewHelper {
	//�o��u�ݭn����sdcard���Ϥ��}�C �N�i�H��@�Ӭݺ��e��app
	private int[] images = {R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,R.drawable.pic4,
							R.drawable.pic5,R.drawable.pic6,R.drawable.pic7,R.drawable.pic8,
							R.drawable.pic9,R.drawable.pic10};
	private int imageIndex = 0;
	private ImageView imageView;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();

	private Bitmap bitmap;

	private float minScaleR;// �̤p�Y����

	private static final int NONE = 0;// ��l���A
	private static final int DRAG = 1;// �즲���A
	private static final int ZOOM = 2;// �Y�񪬺A
	@SuppressWarnings("unused")
	private int mode = NONE;
	float bigScale = 3f;  
	Boolean isBig = false; 
	long lastClickTime = 0;  
	private PointF start = new PointF();
	private PointF mid = new PointF();
	private DisplayMetrics dm;
	private Context mContext;
	private float dist = 1f;
	public ImageViewHelper(Context mContext,DisplayMetrics dm,ImageView imageView,Bitmap bitmap, ImageButton zoomInButton, ImageButton zoomOutButton){
		this.dm = dm;
		this.imageView = imageView;
		this.bitmap = bitmap;
		this.mContext = mContext;
		setImageSize();
		minZoom();
		center();
		
		imageView.setImageMatrix(matrix);
		
	}
	public Matrix getMatrix(){
		return matrix;
	}


    //���o�̤p�����, ���]�Ϥ���ù��j
    //�h�ù�(�e/��)/�Ϥ�(�e/��)�|�p��1 ����]�N�O�N�Ϥ��i���Y�p
    //�Ϥ� �h�i���j �ӹϤ��V�p ��j���ƫh�|�V�j
    //�p�G�ù���Ϥ��j�p�ۦP �h���Ʒ|��1 �Y����
	public void minZoom() {
        minScaleR = Math.min(
                (float) dm.widthPixels / (float) bitmap.getWidth(),
                (float) dm.heightPixels / (float) bitmap.getHeight());
        if (minScaleR <= 1.0) {
            matrix.postScale(minScaleR, minScaleR);
        }
        else{
        	matrix.postScale(1.5f, 1.5f);
        }
    }

    public void center() {
        center(true, true);
    }
    //��V�B�a�V�m��
    public void center(boolean horizontal, boolean vertical) {

        Matrix m = new Matrix();
        m.set(matrix);
        RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        m.mapRect(rect);

        float height = rect.height();
        float width = rect.width();

        float deltaX = 0, deltaY = 0;

        if (vertical) {
        	// �Ϥ��p��ù��j�p�A�h�m����ܡC
        	//�j��ù��A�W��h�d�ťիh���W���A�U��d�ťիh���U��
            int screenHeight = dm.heightPixels;
            if (height < screenHeight) {
                deltaY = (screenHeight - height) / 2 - rect.top;
            } else if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < screenHeight) {
                deltaY = imageView.getHeight() - rect.bottom;
            }
        }

        if (horizontal) {
            int screenWidth = dm.widthPixels;
            if (width < screenWidth) {
                deltaX = (screenWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < screenWidth) {
                deltaX = screenWidth - rect.right;
            }
        }
        matrix.postTranslate(deltaX, deltaY);
    }
   

    //���I���Z��
    public float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    //���I�����I
    public void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
    //�إ߹Ϥ������Y��ƥ� 
    public void setImageSize(){
	
		imageView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				switch (event.getAction() & MotionEvent.ACTION_MASK) {

		        case MotionEvent.ACTION_DOWN:
		            savedMatrix.set(matrix);
		            start.set(event.getX(), event.getY());
		            mode = DRAG;
		            
		            break;

		        case MotionEvent.ACTION_POINTER_DOWN:
		            dist = spacing(event);
		            // �p�G���I�Z���W�L10, �N�P�_���h�IĲ���Ҧ� �Y���Y��Ҧ�
		            if (spacing(event) > 10f) {
		                savedMatrix.set(matrix);
		                midPoint(mid, event);
		                mode = ZOOM;
		            }
		            break;

		        case MotionEvent.ACTION_UP:
		        	 
		        	if(start.x - event.getX() > 90){
		        		imageIndex++;
		        		if(imageIndex<0) imageIndex = images.length-1;
		        		else if(imageIndex>9) imageIndex = 0;
		        		Log.e("imageIndex left",imageIndex+"");
		        		
	                	imageView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_right_out));
	                	imageView.setImageResource(images[imageIndex]);
	                	imageView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_left_in));
	                	center();
	                	
	                }
		        	else if(event.getX()>start.x +  90 ){
		        		imageIndex--;
		        		if(imageIndex<0) imageIndex = images.length-1;
		        		else if(imageIndex>9) imageIndex = 0;
		        		Log.e("imageIndex right",imageIndex+"");
		        		
		        		imageView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_left_out));
	                	imageView.setImageResource(images[imageIndex]);
	                	imageView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_right_in));
	                	center();
	                	
		        	}
		            mode = NONE;
		            break;
		         
		        case MotionEvent.ACTION_POINTER_UP:
		           if (mode == ZOOM) {
		                float newDist = spacing(event);//������ڤ�����ʪ��Z��
		                if (newDist > 10f) {
		                    matrix.set(savedMatrix);
		                    float tScale = newDist / dist;
		                    matrix.postScale(tScale, tScale, mid.x, mid.y);
		                    
		                }
		               
		            }
		            
		            break;

		          
		                
		                
		 


		        }
		        imageView.setImageMatrix(matrix);
		        //center();
				return true;
			

			}
			
		});
	}
}




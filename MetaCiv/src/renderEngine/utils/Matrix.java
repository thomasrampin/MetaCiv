package renderEngine.utils;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.entities.Camera;

public class Matrix {

	// 2D part
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	// 3D Part
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scaleX,float scaleY,float scaleZ){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scaleX,scaleY,scaleZ), matrix, matrix);
		return matrix;
	}
    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix,
                viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYangle()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
        return viewMatrix;
    }

	public static Matrix4f createTransformationMatrix(float angle) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();

		Matrix4f.rotate((float) Math.toRadians(angle), new Vector3f(0,1,0), matrix, matrix);


		return matrix;
	}

	public static Matrix4f createMatrix(float m00,float m01,float m02,float m03,
									  float m10,float m11,float m12,float m13,
									  float m20,float m21,float m22,float m23,
									  float m30,float m31,float m32,float m33){
		Matrix4f matrix = new Matrix4f();
		matrix.m00 = m00;matrix.m01 = m01;matrix.m02 = m02;matrix.m03 = m03;
		matrix.m10 = m10;matrix.m11 = m11;matrix.m12 = m12;matrix.m13 = m13;
		matrix.m20 = m20;matrix.m21 = m21;matrix.m22 = m22;matrix.m23 = m23;
		matrix.m30 = m30;matrix.m31 = m31;matrix.m32 = m32;matrix.m33 = m33;
		return matrix;
	}
	
	public static Matrix4f identity(){
		Matrix4f matrix = new Matrix4f();
		matrix.m00 = 1;matrix.m01 = 0;matrix.m02 = 0;matrix.m03 = 0;
		matrix.m10 = 0;matrix.m11 = 1;matrix.m12 = 0;matrix.m13 = 0;
		matrix.m20 = 0;matrix.m21 = 0;matrix.m22 = 1;matrix.m23 = 0;
		matrix.m30 = 0;matrix.m31 = 0;matrix.m32 = 0;matrix.m33 = 1;		
		return matrix;
	}
	
	public static Matrix4f frustum(float left, float right, float bottom, float top, float n, float f){
		Matrix4f matrix = identity();
		
		
	    if ((right == left) ||
	            (top == bottom) ||
	            (n == f) ||
	            (n < 0.0) ||
	            (f < 0.0))
	           return matrix;

		    matrix.m00 = (2.0f * n) / (right - left);
		    matrix.m11 = (2.0f * n) / (top - bottom);
	
		    matrix.m20 = (right + left) / (right - left);
		    matrix.m21 = (top + bottom) / (top - bottom);
	        matrix.m22 = -(f + n) / (f - n);
	        matrix.m23= -1.0f;

	        matrix.m32 = -(2.0f * f * n) / (f - n);
	        matrix.m33 =  0.0f;

	    return matrix;
	}
	
	public static Matrix4f translate(float x, float y, float z)
	{
	    return createMatrix(1.0f, 0.0f, 0.0f, 0.0f,
	                        0.0f, 1.0f, 0.0f, 0.0f,
	                        0.0f, 0.0f, 1.0f, 0.0f,
	                        x, y, z, 1.0f);
	}

	
	public static Matrix4f translate(Vector3f v)
	{
	    return translate(v.x, v.y, v.z);
	}
	
	public static Matrix4f lookAt(Vector3f eye, Vector3f center, Vector3f up) {
        Vector3f forward = new Vector3f(0, 0, 0);
             Vector3f.sub(center, eye, forward);
             forward.normalise();
      
        Vector3f side = new Vector3f(0, 0, 0);
        Vector3f.cross(forward, up, side);
        side.normalise();
       
        Vector3f.cross(side, forward, up);

        Matrix4f matrix = new Matrix4f();
        matrix.m00 = side.x;
        matrix.m01 = side.y;
        matrix.m02 = side.z;
        matrix.m10 = up.x;
        matrix.m11 = up.y;
        matrix.m12 = up.z;
        matrix.m20 = -forward.x;
        matrix.m21 = -forward.y;
        matrix.m22 = -forward.z;
        matrix.invert();
       
        return matrix;
     }
}

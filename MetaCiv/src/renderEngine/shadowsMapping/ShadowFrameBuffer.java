package renderEngine.shadowsMapping;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import renderEngine.postProcessing.FrameBufferObject;

public class ShadowFrameBuffer {
	
	private static final int shadowResolution = 512;
	
	private int depthTexture;
	private int shadowFbo;
	
	public ShadowFrameBuffer(){
		initFrameBuffer();
	}

	public void cleanUp() {
        GL30.glDeleteFramebuffers(shadowFbo);
        GL11.glDeleteTextures(depthTexture);
	}
	
	private void initFrameBuffer() {
		shadowFbo = GL30.glGenFramebuffers();
        
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, shadowFbo);
        
        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
   
        depthTexture = createDepthTextureAttachment(shadowResolution,shadowResolution);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
        
	}
	
	public void resolveToFbo(int readBuffer,FrameBufferObject fbo){
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fbo.frameBuffer);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.shadowFbo);
		GL11.glReadBuffer(readBuffer);
		GL30.glBlitFramebuffer(0, 0, shadowResolution, shadowResolution, 0, 0, fbo.width, fbo.height, GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
		this.unbindCurrentFrameBuffer();
	}
	
	public void resolveToScreen(){
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.shadowFbo);
		GL11.glDrawBuffer(GL11.GL_BACK);
		GL30.glBlitFramebuffer(0, 0, shadowResolution, shadowResolution, 0, 0, Display.getWidth(), Display.getHeight(), GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
		this.unbindCurrentFrameBuffer();		
	}
	
    public void bindFrameBuffer(){
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);//To make sure the texture isn't bound
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, shadowFbo);
        GL11.glViewport(0, 0, shadowResolution, shadowResolution);
    }
    
    public int getDepthTexture(){
    	return depthTexture;
    }
    
    public void unbindCurrentFrameBuffer() {//call to switch to default frame buffer
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }
	
    private int createDepthTextureAttachment(int width, int height){
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, width, height, 0, GL11.GL_DEPTH_COMPONENT,
				GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_MODE, GL30.GL_COMPARE_REF_TO_TEXTURE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_FUNC, GL11.GL_LEQUAL);
		
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, texture, 0);
		return texture;

    }
 
	
}

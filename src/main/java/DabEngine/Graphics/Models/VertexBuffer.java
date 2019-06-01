package DabEngine.Graphics.Models;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;

public class VertexBuffer {
	
	private VertexAttrib[] attribs;
	private int totalComponents;
	private int vertcount;
	private FloatBuffer buffer;
	
	public VertexBuffer(int vertcount, VertexAttrib... attribs) {
		this.attribs = attribs;
		for(VertexAttrib attrib : attribs) {
			totalComponents += attrib.numComponents;
		}
		this.vertcount = vertcount;
		
		buffer = BufferUtils.createFloatBuffer(vertcount * totalComponents);
	}
	
	public VertexBuffer(int vertcount, List<VertexAttrib> attribs) {
		this(vertcount, attribs.toArray(new VertexAttrib[attribs.size()]));
	}
	
	public VertexBuffer flip() {
		buffer.flip();
		return this;
	}
	
	public VertexBuffer clear() {
		buffer.clear();
		return this;
	}
	
	public VertexBuffer put(float f) {
		buffer.put(f);
		return this;
	}
	
	public FloatBuffer getBuffer() {
		return buffer;
	}
	
	public int getVertcount() {
		return vertcount;
	}
	
	public int getTotalComponents() {
		return totalComponents;
	}
	
	public void bind() {
		int offset = 0;
		
		int stride = totalComponents * 4;
		
		for(int i = 0; i < attribs.length; i++) {
			VertexAttrib attrib = attribs[i];
			buffer.position(offset);
			glEnableVertexAttribArray(attrib.location);
			glVertexAttribPointer(attrib.location, attrib.numComponents, GL_FLOAT, false, stride, buffer);
			offset += attrib.numComponents;
		}
	}
	
	public void draw(int type, int first, int count) {
		glDrawArrays(type, first, count);
	}
	
	public void unbind() {
		for(int i = 0; i < attribs.length; i++) {
			VertexAttrib attrib = attribs[i];
			glDisableVertexAttribArray(attrib.location);
		}
	}
}

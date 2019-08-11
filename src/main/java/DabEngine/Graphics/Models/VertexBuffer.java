package DabEngine.Graphics.Models;

import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import DabEngine.Core.IDisposable;

import java.util.Arrays;

import static org.lwjgl.system.MemoryUtil.*;

public class VertexBuffer implements IDisposable {
	
	VertexAttrib[] attribs;
	private int totalComponents;
	private int vertcount;
	private FloatBuffer buffer;
	private int vbo, vao;
	
	public VertexBuffer(int vertcount, VertexAttrib... attribs) {
		this.attribs = attribs;
		for(VertexAttrib attrib : attribs) {
			totalComponents += attrib.numComponents;
		}
		this.vertcount = vertcount;
		
		buffer = memCallocFloat(vertcount * totalComponents);

		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vao = glGenVertexArrays();

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
		glBindVertexArray(vao);

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferSubData(GL_ARRAY_BUFFER, 0, buffer.flip());

		int offset = 0;
		
		int stride = totalComponents * 4;
		
		for(int i = 0; i < attribs.length; i++) {
			VertexAttrib attrib = attribs[i];
			glVertexAttribPointer(attrib.location, attrib.numComponents, GL_FLOAT, false, stride, offset);
			glEnableVertexAttribArray(attrib.location);
			offset += attrib.numComponents * 4;
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

		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindVertexArray(0);
	}

	@Override
	public void dispose() {
		memFree(buffer);
	}
}

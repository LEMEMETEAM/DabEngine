package DabEngine.Graphics.Batch;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import DabEngine.Core.IDisposable;
import DabEngine.Graphics.Models.VertexAttrib;
import DabEngine.Graphics.Models.VertexBuffer;

/**
 * Batch Renderer
 */
public class Batch implements IDisposable
{
	
	private VertexBuffer buffer;
	private int idx, size;
	private boolean begun;
	protected static final List<VertexAttrib> ATTRIBUTES = 
								Arrays.asList(new VertexAttrib(0, "position", 3),
								new VertexAttrib(1, "color", 4),
								new VertexAttrib(2, "texCoords", 2),
								new VertexAttrib(3, "normals", 3));

	public Batch(int size)
	{
		buffer = new VertexBuffer(size * 6, ATTRIBUTES);

		this.size = size;
		this.idx = 0;
		this.begun = false;
	}

	public void begin()
	{
		if(begun) return;

		begun = true;
		buffer.bind();
	}

	public void add(float[] data)
	{
		for(int i = 0; i < data.length/12; i++)
		{
			buffer.put(data[i*12+0]);
			buffer.put(data[i*12+1]);
			buffer.put(data[i*12+2]);

			buffer.put(data[i*12+3]);
			buffer.put(data[i*12+4]);
			buffer.put(data[i*12+5]);
			buffer.put(data[i*12+6]);

			buffer.put(data[i*12+7]);
			buffer.put(data[i*12+8]);

			buffer.put(data[i*12+9]);
			buffer.put(data[i*12+10]);
			buffer.put(data[i*12+11]);

			idx++;
		}
	}

	public void end()
	{
		if(!begun) return;

		begun = false;
		buffer.unbind();
	}

	public void flush()
	{
		if(idx > 0)
		{
			buffer.draw(GL11.GL_TRIANGLES, 0, idx);
			buffer.clear();

			idx = 0;
		}
	}

	public int getSize() {
		return size;
	}

	/**
	 * @return the idx
	 */
	public int getIdx() {
		return idx;
	}

	/**
	 * @return the begun
	 */
	public boolean hasBegun() {
		return begun;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		buffer.dispose();
	}

    
}

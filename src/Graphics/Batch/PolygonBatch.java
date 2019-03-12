package Graphics.Batch;

import Graphics.Batch.IBatch;
import Graphics.Models.VertexAttrib;
import Graphics.Models.VertexBuffer;
import Graphics.ProjectionMatrix;
import Graphics.Shaders;

import org.joml.Vector2f;
import org.joml.Vector4f;

import DabEngineResources.DabEngineResources;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class PolygonBatch implements IBatch {
    private boolean drawing;
    private int idx;
    public static int renderCalls = 0;
    private Polygon polygon;
    private VertexBuffer data;
    private int maxsize = 1000*6;
	private Shaders shader = new Shaders(DabEngineResources.class, "Shaders/default.vs", "Shaders/single_colour.fs");
    private static final List<VertexAttrib> ATTRIBUTES =
            Arrays.asList(new VertexAttrib(0, "position", 2),
                    new VertexAttrib(1, "color", 4));

    public PolygonBatch(Shaders shader) {
        data = new VertexBuffer(maxsize, ATTRIBUTES);
        this.shader = shader;
        updateUniforms();
    }
    
    public PolygonBatch() {
    	data = new VertexBuffer(maxsize, ATTRIBUTES);
    	updateUniforms();
    }

    public void begin() {
        if(drawing) {
            throw new IllegalStateException("must not be drawing before calling begin()");
        }
        drawing = true;
        shader.bind();
        idx = 0;
        renderCalls = 0;
        polygon = null;
    }

    public void end() {
        if(!drawing) {
            throw new IllegalStateException("must be drawing before calling end()");
        }
        drawing = false;
        flush();
    }

    public void updateUniforms() {
		updateUniforms(shader);
	}
	
	public void updateUniforms(Shaders shader) {
		shader.bind();
		
		shader.setUniform("projectionMatrix", ProjectionMatrix.get());
		
		shader.setUniform("texture", 0);
	}
	
	public void setShader(Shaders shader, boolean updateUniforms) {
		if(shader == null) {
			throw new NullPointerException("shader cannot be null; use getDefaultShader instead");
		}
		if(drawing) {
			flush();
		}
		this.shader = shader;
		
		if(updateUniforms) {
			updateUniforms();
		}
		else if(drawing) {
			this.shader.bind();
		}
	}

    public Shaders getShader() {
        return shader;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    //draws a polygon with no tint, full opacity and polyture wrapped fully
    public void draw(Polygon poly, float x, float y, float width, float height) {
        draw(poly, x, y, width, height, 1, 1, 1, 1);
    }

    //simplified version of above
    public void draw(Polygon poly, Vector4f xywh) {
        draw(poly, xywh.x(), xywh.y(), xywh.z(), xywh.w(), 1, 1, 1, 1);
    }


    //simplified version of above
    public void draw(Polygon poly, Vector4f xywh, Vector4f rgba) {
        draw(poly,  xywh.x(), xywh.y(), xywh.z(), xywh.w(), rgba.x(), rgba.y(), rgba.z(), rgba.w());
    }

    //full draw method
    public void draw(Polygon poly, float x, float y, float width, float height, float r, float g, float b, float a) {
        Vector2f[] verts = poly.verts;

        checkFlush(poly);

        //0, 1, 2
        //0, 3, 2
        for(int i : poly.inds) {
        	vertex((verts[i].x * width) + x, (verts[i].y * height) + y, r, g, b, a);
        }
    }

    private VertexBuffer vertex(float x, float y, float r, float g, float b, float a) {
        data.put(x).put(y).put(r).put(g).put(b).put(a);
        idx++;
        return data;
    }

    public void checkFlush(Polygon poly) {
        if(poly == null) {
            throw new NullPointerException("null polygon");
        }
        if(poly != this.polygon || idx >= maxsize) {
            flush();
            this.polygon = poly;
        }
    }

    public void flush() {
        if(idx > 0) {
            data.flip();
            
            data.bind();
            data.draw(GL_TRIANGLES, 0, idx);
            data.unbind();
            renderCalls++;

            idx = 0;
            data.clear();
        }
    }
}

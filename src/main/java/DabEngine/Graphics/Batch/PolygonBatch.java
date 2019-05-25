package DabEngine.Graphics.Batch;

import Graphics.Batch.IBatch;
import Graphics.Models.VertexAttrib;
import Graphics.Models.VertexBuffer;
import Graphics.ProjectionMatrix;
import Graphics.Shaders;

import org.joml.Vector2f;
import org.joml.Vector4f;

import DabEngineResources.DabEngineResources;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PolygonBatch implements IBatch {
	private int TYPE;
    private boolean drawing;
    private volatile int idx;
    public volatile static int renderCalls = 0;
    private Polygon polygon;
    private VertexBuffer data;
    private int maxsize = 1000*6;
	private Shaders shader = new Shaders(getClass().getResourceAsStream("/resources/Shaders/default.vs"), getClass().getResourceAsStream("/resources/Shaders/single_colour.fs"));
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

    public void begin(int type) {
        if(drawing) {
            throw new IllegalStateException("must not be drawing before calling begin()");
        }
        TYPE = type;
        drawing = true;
        shader.bind();
        idx = 0;
        renderCalls = 0;
        polygon = null;
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
        TYPE = 0;
        drawing = false;
        flush();
    }

    public void updateUniforms() {
		updateUniforms(shader);
	}
	
	public void updateUniforms(Shaders shader) {
		shader.bind();
		
		shader.setUniform("projectionMatrix", ProjectionMatrix.get());
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
    
    public void setPrimitiveType(int type) {
    	if(type == 0) {
    		throw new IllegalStateException("type cannot be 0");
    	}
    	if(drawing) {
    		flush();
    	}
    	TYPE = type;
    }

    //draws a polygon with no tint, full opacity and polyture wrapped fully
    public void draw(Polygon poly, float x, float y, float width, float height) {
        draw(poly, x, y, width, height, x, y, 0, 1, 1, 1, 1);
    }
    
    public void draw(Polygon poly, float x, float y, float width, float height, float rotation) {
        draw(poly, x, y, width, height, x, y, rotation, 1, 1, 1, 1);
    }
    
    public void draw(Polygon poly, float x, float y, float width, float height, float ox, float oy, float rotation) {
        draw(poly, x, y, width, height, ox, oy, rotation, 1, 1, 1, 1);
    }
    
    public void draw(Polygon poly, float x, float y, float width, float height, Vector4f color) {
        draw(poly, x, y, width, height, x, y, 0, color.x, color.y, color.z, color.w);
    }
    
    public void draw(Polygon poly, float x, float y, float width, float height, Vector4f color, float rotation) {
        draw(poly, x, y, width, height, x, y, rotation, color.x, color.y, color.z, color.w);
    }

    //full draw method
    public void draw(Polygon poly, float x, float y, float width, float height, float originX, float originY, float rotation, float r, float g, float b, float a) {
        Vector2f[] verts = poly.verts;

        checkFlush(poly);

        //0, 1, 2
        //0, 3, 2
        for(int i : poly.inds) {
        	float x1,y1,x2,y2,x3,y3,x4,y4;
        	
        	final float cx = originX;
        	final float cy = originY;
        	
        	float px,py,px2,py2;
        	
        	px = verts[i].x*width-cx;
        	py = verts[i].y*height-cy;
        	
        	final float cos = (float)Math.cos(Math.toRadians(rotation));
        	final float sin = (float)Math.sin(Math.toRadians(rotation));
        	
        	vertex(x + (cos * px - sin * py) + cx, y + (sin * px + cos * py) + cy, r, g, b, a);
        	//vertex((verts[i].x * (width/2)) + x, (verts[i].y * (height/2)) + y, r, g, b, a);
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
            data.draw(TYPE, 0, idx);
            data.unbind();
            renderCalls++;

            idx = 0;
            data.clear();
        }
    }
}

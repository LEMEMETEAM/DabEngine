package DabEngine.Graphics.Batch;

import java.util.Arrays;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector4f;

import DabEngine.Graphics.ProjectionMatrix;
import DabEngine.Graphics.Shaders;
import DabEngine.Graphics.Models.VertexAttrib;
import DabEngine.Graphics.Models.VertexBuffer;

public class PolygonBatch implements IBatch {
	private int TYPE;
    private boolean drawing;
    private volatile int idx;
    public volatile static int renderCalls = 0;
    private Polygon polygon;
    private VertexBuffer data;
    private int maxsize = 1000*6;
    public final Shaders defShader = new Shaders(getClass().getResourceAsStream("/Shaders/default.vs"), getClass().getResourceAsStream("/Shaders/single_colour.fs"));
    private Shaders shader;
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
        this.shader = defShader;
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
    
    public void setShader(Shaders shader){
        this.setShader(shader, true);
    }

    public Shaders getShader() {
        return shader;
    }

    public Polygon getPolygon() {
        return polygon;
    }
    
    public void setPrimitiveType(int type) {
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
        	
        	final float cx = originX;
        	final float cy = originY;
        	
        	float px,py;
        	
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

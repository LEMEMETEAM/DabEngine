package DabEngine.Graphics.Models;

import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.assimp.AIScene.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AITexture;
import org.lwjgl.assimp.AIVector3D;

import DabEngine.Cache.ResourceManager;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Graphics.OpenGL.Textures.Texture.Parameters;

public class MeshLoader {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public ArrayList<Mesh> meshes = new ArrayList<>();
    private String directory;
    private int vCount;

    public MeshLoader(String path){
        AIScene scene = aiImportFile(path, aiProcess_Triangulate | aiProcess_FlipUVs | aiProcess_GenNormals);
        if(scene == null || (scene.mFlags() & AI_SCENE_FLAGS_INCOMPLETE) == 1 || scene.mRootNode() == null){
            throw new Error("Error loading model");
        }

        directory = path.substring(0, path.lastIndexOf("/"));
        processNode(scene.mRootNode(), scene);
    }

    private void processNode(AINode node, AIScene scene){
        for (int i = 0; i < node.mNumMeshes(); i++) {
            AIMesh mesh = AIMesh.create(scene.mMeshes().get(node.mMeshes().get(i)));
            meshes.add(processMesh(mesh, scene));
        }
        for(int i = 0; i < node.mNumChildren(); i++){
            processNode(AINode.create(node.mChildren().get(i)), scene);
        }
    }

    private Mesh processMesh(AIMesh mesh, AIScene scene){
        Mesh dMesh = new Mesh(new VertexAttrib(0, "position", 3),
        new VertexAttrib(1, "color", 4),
        new VertexAttrib(2, "texCoords", 2),
        new VertexAttrib(3, "normals", 3));

        float[] data = new float[12 * mesh.mNumVertices()];

        int stride = 12;
        vCount = mesh.mNumVertices();
        for(int i = 0; i < vCount; i++){
            AIVector3D verts = mesh.mVertices().get(i);
            data[i * 12 + 0] = verts.x();
            data[i * 12 + 1] = verts.y();
            data[i * 12 + 2] = verts.z();

            AIColor4D.Buffer color = mesh.mColors(0);
            if(color != null){
                data[i * 12 + 3] = color.get(i).r();
                data[i * 12 + 4] = color.get(i).g();
                data[i * 12 + 5] = color.get(i).b();
                data[i * 12 + 6] = color.get(i).a();
            }
            else{
                data[i * 12 + 3] = 1;
                data[i * 12 + 4] = 1;
                data[i * 12 + 5] = 1;
                data[i * 12 + 6] = 1;
            }

            AIVector3D.Buffer uv = mesh.mTextureCoords(0);
            if(uv != null){
                data[i * 12 + 7] = uv.get(i).x();
                data[i * 12 + 8] = uv.get(i).y();
            }
            else{
                data[i * 12 + 7] = 0;
                data[i * 12 + 8] = 0;
            }

            AIVector3D norms = mesh.mNormals().get(i);
            data[i * 12 + 9] = norms.x();
            data[i * 12  + 10] = norms.y();
            data[i * 12 + 11] = norms.z();
        }

        Texture[] diffuse = null;
        Texture[] specular = null;
        if(mesh.mMaterialIndex() >= 0){
            AIMaterial mat = AIMaterial.create(scene.mMaterials().get(mesh.mMaterialIndex()));
            diffuse = loadMaterials(mat, aiTextureType_DIFFUSE);
            specular = loadMaterials(mat, aiTextureType_SPECULAR);
        }
        dMesh.build(data, diffuse, specular);

        return dMesh;
    }

    private Texture[] loadMaterials(AIMaterial mat, int type){
        int count = aiGetMaterialTextureCount(mat, type);
        Texture[] tex = new Texture[count];
        for(int i = 0; i < count; i++){
            AIString path = AIString.calloc();
            aiGetMaterialTexture(mat, type, i, path, (IntBuffer) null, null, null, null, null, null);
            String fPath = path.dataString();
            if(fPath != null && fPath.length() > 0){
                tex[i] = ResourceManager.INSTANCE.getTexture(directory + "/" + fPath, Parameters.LINEAR);
            }
        }
        return tex;
    }

    public Model toModel(){
        Model m = new Model();
        m.meshes = meshes;
        m.vCount = vCount;
        return m;
    }
}

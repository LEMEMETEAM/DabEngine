package DabEngine.Graphics.Models;

import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.assimp.AIScene.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joml.Vector3f;
import org.lwjgl.assimp.AIColor3D;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AILight;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AITexture;
import org.lwjgl.assimp.AIVector3D;

import DabEngine.Resources.*;
import DabEngine.Resources.Shaders.*;
import DabEngine.Resources.Textures.*;

public class MeshLoader {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public ArrayList<Mesh> meshes = new ArrayList<>();
    private String directory;
    private int vCount;

    public MeshLoader(String path){

        AIScene scene = aiImportFile(path, aiProcess_Triangulate | aiProcess_FlipUVs | aiProcess_GenSmoothNormals | aiProcess_PreTransformVertices | aiProcess_ValidateDataStructure);
        if(scene == null || (scene.mFlags() & AI_SCENE_FLAGS_INCOMPLETE) == 1 || scene.mRootNode() == null){
            throw new Error("Error loading model");
        }

        directory = path.substring(0, path.lastIndexOf("/"));
        processNode(scene.mRootNode(), scene, scene.mRootNode().mName().dataString());
    }

    private void processNode(AINode node, AIScene scene, String name){
        for (int i = 0; i < node.mNumMeshes(); i++) {
            AIMesh mesh = AIMesh.create(scene.mMeshes().get(node.mMeshes().get(i)));
            meshes.add(processMesh(mesh, scene, name));
        }
        for(int i = 0; i < node.mNumChildren(); i++){
            AINode n = AINode.create(node.mChildren().get(i));
            processNode(n, scene, n.mName().dataString());
        }
    }

    private ArrayList<Integer> genIndicies(AIMesh mesh){
        ArrayList<Integer> idx = new ArrayList<>();
        int numFaces = mesh.mNumFaces();
        AIFace.Buffer faces = mesh.mFaces();
        for(int i = 0; i < numFaces; i++){
            AIFace face = faces.get(i);
            IntBuffer idxbuffer = face.mIndices();
            while(idxbuffer.remaining() > 0){
                idx.add(idxbuffer.get());
            }
        }
        return idx;
    }

    private Mesh processMesh(AIMesh mesh, AIScene scene, String name){

        vCount = mesh.mNumVertices();
        ArrayList<Integer> idx = genIndicies(mesh);

        float[] data = new float[12 * idx.size()];
        for(int i = 0; i < idx.size(); i++){
            AIVector3D verts = mesh.mVertices().get(idx.get(i));
            data[i * 12 + 0] = verts.x();
            data[i * 12 + 1] = verts.y();
            data[i * 12 + 2] = verts.z();

            AIColor4D.Buffer color = mesh.mColors(0);
            if(color != null){
                data[i * 12 + 3] = color.get(idx.get(i)).r();
                data[i * 12 + 4] = color.get(idx.get(i)).g();
                data[i * 12 + 5] = color.get(idx.get(i)).b();
                data[i * 12 + 6] = color.get(idx.get(i)).a();
            }
            else{
                data[i * 12 + 3] = 1;
                data[i * 12 + 4] = 1;
                data[i * 12 + 5] = 1;
                data[i * 12 + 6] = 1;
            }

            AIVector3D.Buffer uv = mesh.mTextureCoords(0);
            if(uv != null){
                data[i * 12 + 7] = uv.get(idx.get(i)).x();
                data[i * 12 + 8] = uv.get(idx.get(i)).y();
            }
            else{
                data[i * 12 + 7] = 0;
                data[i * 12 + 8] = 0;
            }

            AIVector3D norms = mesh.mNormals().get(idx.get(i));
            data[i * 12 + 9] = norms.x();
            data[i * 12  + 10] = norms.y();
            data[i * 12 + 11] = norms.z();
        }

        Texture[] diffuse = null;
        Texture[] specular = null;
        Texture[] normals = null;
        if(mesh.mMaterialIndex() >= 0){
            AIMaterial mat = AIMaterial.create(scene.mMaterials().get(mesh.mMaterialIndex()));
            diffuse = loadMaterials(mat, aiTextureType_DIFFUSE);
            specular = loadMaterials(mat, aiTextureType_SPECULAR);
            normals = loadMaterials(mat, aiTextureType_NORMALS);
        }
        Mesh dMesh = new Mesh(data, diffuse, specular, normals);
        dMesh.name = name;

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
                tex[i] = ResourceManager.INSTANCE.getTexture(directory + "/" + fPath, true, false);
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

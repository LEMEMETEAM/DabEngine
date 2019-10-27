package DabEngine.Resources.Mesh;

import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AIVector3D;

import DabEngine.Resources.Resource;
import DabEngine.Resources.ResourceManager;
import DabEngine.Resources.Textures.Texture;

import static org.lwjgl.assimp.Assimp.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;

public class Model extends Resource{

    private class Mesh
    {
        public float[] vData;
        public ArrayList<Texture> diffuse;
        public ArrayList<Texture> specular;
        public ArrayList<Texture> normal;
        public String name;
    }
    private String directory;
    private int vCount;
    private ArrayList<Mesh> meshes;

    public Model(String path)
    {
        super(path);

        meshes = new ArrayList<>();
        vCount = 0;
        directory = "";
    }

    @Override
    protected void create() {
        // TODO Auto-generated method stub
        AIScene scene = aiImportFile(filename, aiProcess_Triangulate | aiProcess_FlipUVs | aiProcess_GenSmoothNormals | aiProcess_PreTransformVertices | aiProcess_ValidateDataStructure);
        if(scene == null || (scene.mFlags() & AI_SCENE_FLAGS_INCOMPLETE) == 1 || scene.mRootNode() == null){
            throw new Error("Error loading model");
        }

        directory = filename.substring(0, filename.lastIndexOf("/"));
        ready = processNode(scene.mRootNode(), scene, scene.mRootNode().mName().dataString());
    }

    private boolean processNode(AINode node, AIScene scene, String name){
        for (int i = 0; i < node.mNumMeshes(); i++) {
            AIMesh mesh = AIMesh.create(scene.mMeshes().get(node.mMeshes().get(i)));
            try
            {
                meshes.add(processMesh(mesh, scene, name));
            } catch(Error e)
            {
                e.printStackTrace();
                return false;
            }
        }
        for(int i = 0; i < node.mNumChildren(); i++){
            AINode n = AINode.create(node.mChildren().get(i));
            return processNode(n, scene, n.mName().dataString());
        }

        return true;
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

    private Mesh processMesh(AIMesh mesh, AIScene scene, String name) throws Error {

        vCount = mesh.mNumVertices();
        ArrayList<Integer> idx = genIndicies(mesh);

        float[] data = new float[12 * idx.size()];
        for(int i = 0; i < idx.size(); i++){
            AIVector3D verts = mesh.mVertices().get(idx.get(i));
            if(verts == null)
            {
                throw new Error("no verticies in mesh");
            }
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
        Mesh dMesh = new Mesh();
        dMesh.diffuse = new ArrayList<>(Arrays.asList(diffuse));
        dMesh.specular = new ArrayList<>(Arrays.asList(specular));
        dMesh.normal = new ArrayList<>(Arrays.asList(normals));
        dMesh.vData = data;
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

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
package DabEngine.Resources;

public class ResourceHandle<T extends Resource> 
{

    private T resource;

    public ResourceHandle(T r)
    {
        resource = r;
    }

    /**
     * @return the resource
     */
    public T getResource() 
    {
        return resource;
    }


}
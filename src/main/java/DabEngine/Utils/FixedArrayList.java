package DabEngine.Utils;

import java.util.ArrayList;

public class FixedArrayList<E> extends ArrayList<E>{

    private int maxsize;

    public FixedArrayList(int maxsize){
        this.maxsize = maxsize;
    }

    @Override
    public boolean add(E e) {
        if(this.size() > maxsize)
            return super.add(e);
        else
            throw new IllegalStateException();
    }
}
package DabEngine.UI;

import java.util.ArrayList;

public abstract class UIContainer extends UIElement
{

    private ArrayList<UIElement> elements = new ArrayList<>();

    public UIContainer() {
        super(0,0,0,0);
        // TODO Auto-generated constructor stub
    }

    public void addElement(UIElement e)
    {
        elements.add(e);

    }

    public void removeElement(UIElement e)
    {
        elements.remove(e);
    }

}
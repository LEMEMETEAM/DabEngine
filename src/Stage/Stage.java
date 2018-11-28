package Stage;

import Graphics.Models.Model;

import java.util.HashSet;
import java.util.Set;

public class Stage {

    private Set<Model> stageItems;
    private Model background;

    public Stage(){
        stageItems = new HashSet<>();
    }

    public void addToStage(Model e){
        stageItems.add(e);
        System.out.println("stageItems = " + stageItems);
    }

    public void setBackground(Model background) {
        this.background = background;
    }

    public void render(){
        if (background != null) {
            background.render();
        }
        for(Model m : stageItems){
            if(m.getPosition().x > 12f || m .getPosition().x < -12f || m.getPosition().y > 12f || m .getPosition().y < -12f){
                continue;
            }
            else {
                m.render();
            }
        }
    }

    public void update(){
        if(background != null){
            background.update();
        }
        for(Model m : stageItems){
            if(m.getPosition().x > 12f || m .getPosition().x < -12f || m.getPosition().y > 12f || m .getPosition().y < -12f){
                continue;
            }
            else {
                m.update();
            }
        }
    }
}

package DabEngine.Script;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import DabEngine.Resources.ResourceManager;
import DabEngine.UI.UIButton;
import DabEngine.UI.UIContainer;
import DabEngine.UI.UILabel;

public class dabengine extends TwoArgFunction {

    public dabengine() {}

    @Override
    public LuaValue call(LuaValue modname, LuaValue env) 
    {
        // TODO Auto-generated method stub
        LuaValue library = tableOf();
        library.set("resources", new resources());
        library.set("ui", new ui());
        env.set("dabengine", library);
        return library;
    }

    static class resources extends LuaTable
    {

        public resources()
        {
            set("get_texture", new get_texture());
            set("get_shader", new get_shader());
            set("get_font", new get_font());
        }

        static class get_texture extends ThreeArgFunction
        {

            @Override
            public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) 
            {
                // TODO Auto-generated method stub
                return CoerceJavaToLua.coerce(ResourceManager.INSTANCE.getTexture(arg1.checkjstring(), arg2.checkboolean(), arg3.checkboolean()));
            }
            
        }

        static class get_shader extends TwoArgFunction
        {

            @Override
            public LuaValue call(LuaValue arg1, LuaValue arg2) 
            {
                // TODO Auto-generated method stub
                return CoerceJavaToLua.coerce(ResourceManager.INSTANCE.getShader(arg1.checkjstring(), arg2.checkjstring()));
            }
            
        }

        static class get_font extends ThreeArgFunction
        {

            @Override
            public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) 
            {
                // TODO Auto-generated method stub
                return CoerceJavaToLua.coerce(ResourceManager.INSTANCE.getFont(arg1.checkjstring(), (float)arg2.checkdouble(), arg3.checkint()));
            }
            
        }

    }

    static class ui extends LuaTable
    {

        public ui()
        {
            set("new_button", new new_button());
            set("new_label", new new_label());
            set("new_container", new new_container());
            set("add_callback", new add_callback());
        }

        static class new_button extends VarArgFunction
        {
            @Override
            public Varargs invoke(Varargs args) 
            {
                // TODO Auto-generated method stub
                return CoerceJavaToLua.coerce(new UIButton((float)args.checkdouble(1), (float)args.checkdouble(2), (float)args.checkdouble(3), (float)args.checkdouble(4), args.checkjstring(5)));
            }
        }

        static class new_label extends VarArgFunction
        {
            @Override
            public Varargs invoke(Varargs args) 
            {
                // TODO Auto-generated method stub
                return CoerceJavaToLua.coerce(new UILabel((float)args.checkdouble(1), (float)args.checkdouble(2), (float)args.checkdouble(3), (float)args.checkdouble(4), args.checkjstring(5)));
            }
        }

        static class new_container extends VarArgFunction
        {
            @Override
            public Varargs invoke(Varargs args) {
                // TODO Auto-generated method stub
                return CoerceJavaToLua.coerce(new UIContainer((float)args.checkdouble(1), (float)args.checkdouble(2), (float)args.checkdouble(3), (float)args.checkdouble(4)));
            }
        }

        static class add_callback extends TwoArgFunction
        {

            @Override
            public LuaValue call(LuaValue arg1, LuaValue arg2) 
            {
                // TODO Auto-generated method stub
                Object obj = null;
                if((obj = arg1.checkuserdata(UIButton.class)) != null)
                {
                    UIButton button = (UIButton)obj;
                    if(arg2.isfunction())
                    {
                        button.setCallback(() -> arg2.call());
                    }
                }

                return LuaValue.NIL;
            }
            
        }

    }

}
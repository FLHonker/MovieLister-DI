
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyContainer implements Container {

    private Map<Class, Object> compMap = new HashMap<Class, Object>(0);

    @Override
    public void registerComponent(Class compKey, Class compImplementation, Object[] parameters) 	{
        if (compMap.get(compKey) != null) {
            return;
        }

        Constructor[] constructors = compImplementation.getConstructors();
        try {
            // 这里只支持一个构造方法
            Constructor constructor = constructors[0];
            Class[] params = null;
            if (compKey == compImplementation) {
                params = constructor.getParameterTypes();
                // 这里只支持基于构造方法的DI
                // TODO 递归构造？
                Object[] args = new Object[params.length];
                for (int i = 0; i < params.length; i++) {
                    Class param = params[i];
                    args[i] = getComponentForParam(param);
                }

                if (!hasNullArgs(args)) {
                    compMap.put(compKey, constructor.newInstance(args));
                }
            } else {
                compMap.put(compKey, constructor.newInstance(parameters));
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(MyContainer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MyContainer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MyContainer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(MyContainer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void registerComponent(Class clazz) {
        registerComponent(clazz, clazz, null);
    }

    @Override
    public Object getComponent(Class clazz) {
        // TODO Auto-generated method stub
        return compMap.get(clazz);
    }

    private boolean hasNullArgs(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                return true;
            }
        }
        return false;
    }

    private Object getComponentForParam(Class param) {
        for (Iterator iterator = compMap.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Class clazz = (Class) entry.getKey();
            if (param.isAssignableFrom(clazz)) {
                return entry.getValue();
            }

        }
        return param;
    }
}

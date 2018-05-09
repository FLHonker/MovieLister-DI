import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 为简化问题起见，这里的Container仅支持setter DI
 *
 * @author Frank
 */
public class MyContainer implements Container {

    private Map<Class, Object> compMap = new HashMap<Class, Object>(0);

    @Override
    public void registerComponent(Class compKey, Class compImplementation, Object[] parameters) {
        if (compMap.get(compKey) != null) {
            return;
        }

        Constructor[] constructors = compImplementation.getConstructors();
        try {
            Constructor constructor = constructors[0];
            Object comp = constructor.newInstance(null);

            Method[] methods = compImplementation.getDeclaredMethods();
            if (methods != null && methods.length != 0) {
                for (Method method : methods) {
                    if (method.getName().startsWith("set")) {
                        Object result = verifyAndInvoke(comp, method);
                        if (result != null) {
                            comp = result;
                        }
                    }
                }
            }
            compMap.put(compKey, comp);
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
        return null;
    }

    private Object verifyAndInvoke(Object comp, Method method) {
        // 如果此方法是在配置文件中已经配置过了的属性，则采用属性文件beans.xml中的设置调用setter方法
        String methodName = method.getName();
        String propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
        String propertyValue = parseBeansConfig(comp.getClass().getName(), propertyName);

        Object arg = null;
        if (propertyValue != null) {
            arg = propertyValue;
        } else {
            Class[] params = method.getParameterTypes();
            if (params == null || params.length != 1) {
                return null;
            }
            arg = getComponentForParam(params[0]);
        }

        if (arg == null) {
            return null;
        }

        try {
            method.invoke(comp, arg);
            return comp;
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MyContainer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MyContainer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(MyContainer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private String parseBeansConfig(String className, String propertyName) {
        try {
            InputStream is = getClass().getResourceAsStream("beans.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("bean");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nNode;
                    // 只需要检查指定的class属性
                    String clazzName = element.getAttribute("class");
                    if(clazzName.equals(className)) {
                        NodeList inList = nNode.getChildNodes();
                        for(int i = 0; i < inList.getLength(); i++){
                            Node inNode = inList.item(i);
                            if(inNode.getNodeType() == Node.ELEMENT_NODE){
                                Element inElement = (Element)inNode;
                                String pName = inElement.getAttribute("name");
                                // 只检查指定的propertyName
                                if(pName.equalsIgnoreCase(propertyName))
                                    return inElement.getElementsByTagName("value").item(0).getTextContent();
                            }

                        }
                    }                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}


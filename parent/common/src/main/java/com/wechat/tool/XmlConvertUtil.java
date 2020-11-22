package com.wechat.tool;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author dai
 * @Date 2020/5/28
 * JAXB提供了一种把object转成XML，或者把XML转成object的机制。
 */
public class XmlConvertUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlConvertUtil.class);
    private static String MARK_PREFIX="<![CDATA[";
    private static String MARK_SUFFIX="]]>";

    // ---------------------------------------------------------
    // ---------------  xml 与 object 之间  --------------------
    // ---------------------------------------------------------

    /**
     * 将对象直接转换成String类型的 XML输出
     */
/*    public static String convertToXml(Object obj) {
        // 创建输出流
        StringWriter sw = new StringWriter();
        try {
            // 利用jdk中自带的转换类实现
            JAXBContext context = JAXBContext.newInstance(obj.getClass());

            Marshaller marshaller = context.createMarshaller();
            // 格式化xml输出的格式
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            // 去掉xml版本号报文
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, SystemConstant.UTF8);
            // 将对象转换成输出流形式的xml
            marshaller.marshal(obj, sw);
        } catch (JAXBException e) {
            logger.error(ExceptionUtils.getFullStackTrace(e));
        }
        return sw.toString();
    }*/


    /**
     * 将String类型的xml转换成对象
     */
/*    public static Object convertToObject(Class clazz, String xmlStr) {
        Object xmlObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            // 进行将Xml转成对象的核心接口
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(xmlStr);
            xmlObject = unmarshaller.unmarshal(sr);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlObject;
    }*/

    // ------------------------------------------------------
    // ---------------  xml 与 map 之间  --------------------
    // ------------------------------------------------------

    /**
     *  map 转换成 xml（使用默认别名xml）
     */
    public static String mapToXml(Map<String, Object> map){
        return covertToXml(map,"");
    }
    /**
     * map 转换成 xml
     * @param map
     * @param alias 别名，默认xml
     * @return
     */
    public static String mapToXml(Map<String, Object> map, String alias){
        return covertToXml(map,alias);
    }

    /**
     * map 转换成 xml,并给集合中的每个属性添加自定义内容： <![CDATA[ 属性 ]]>
     * @param map
     * @param alias 别名，默认xml
     * @param markPrefix 前缀：默认 <![CDATA[
     * @param markSuffix 后缀：默认 ]]>
     * @return
     */
    public static String mapToXml(Map<String, Object> map, String alias, String markPrefix, String markSuffix){
        if(StrUtil.isBlank(markPrefix) || StrUtil.isBlank(markSuffix)){
            markPrefix = MARK_PREFIX;
            markSuffix = MARK_SUFFIX;
        }
        return covertToXML(map,alias,markPrefix,markSuffix);
    }


    /**
     * xml 转换成 map
     * @param strxml
     *   <xml>
     * 	   <return_code><![CDATA[FAIL]]></return_code>
     * 	   <return_msg>失败</return_code>
     *   </xml>
     * @return
     *   Map<String, String> result = xmlToMap(xmlStr);
     *   {return_code=FAIL , return_msg=失败}
     */
    public static Map<String, String> xmlToMap(String strxml){
        return doXMLParse(strxml);
    }



    // ---------------------------   封装  ---------------------------------

    private static Map<String, String> doXMLParse(String xmlStr) {
        Map<String, String> m = new HashMap<String, String>();
        try {
            if (null == xmlStr || "".equals(xmlStr)) {
                return null;
            }
            InputStream in = inputstream(xmlStr);
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(in);
            Element root = doc.getRootElement();
            @SuppressWarnings("unchecked")
            List<String> list = root.getChildren();
            @SuppressWarnings("rawtypes")
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String k = e.getName();
                String v = "";
                @SuppressWarnings("unchecked")
                List<String> children = e.getChildren();
                if (children.isEmpty()) {
                    v = e.getTextNormalize();
                } else {
                    v = getChildrenText(children);
                }
                m.put(k, v);
            }
            // 关闭流
            in.close();
        }catch (Exception e){
            System.out.print("xml转map时失败");
        }
        return m;
    }
    private static InputStream inputstream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    private static String getChildrenText(@SuppressWarnings("rawtypes") List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            @SuppressWarnings("rawtypes")
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                @SuppressWarnings("rawtypes")
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }


    //将map集合转换成xml
    protected static String covertToXml(Map<String, Object> map, String alias) {
        if(StrUtil.isBlank(alias)){
            alias = "xml";
        }
        StringBuilder sb = new StringBuilder("<");
        sb.append(alias);
        sb.append(">");
        for (Map.Entry<String, Object> e : map.entrySet()) {
            sb.append("<");
            sb.append(e.getKey());
            sb.append(">");
            sb.append(e.getValue());
            sb.append("</");
            sb.append(e.getKey());
            sb.append(">");
        }
        sb.append("</");
        sb.append(alias);
        sb.append(">");
        return sb.toString();
    }
    //将map集合转换成xml,并给集合中的每个属性添加自定义内容： <![CDATA[ 属性 ]]>
    protected static String covertToXML(Map<String, Object> map, String alias, String markPrefix, String markSuffix) {
        try {
            if(StrUtil.isBlank(alias)){
                alias = "xml";
            }
            StringBuilder sb = new StringBuilder("<");
            sb.append(alias);
            sb.append(">");
            for (Map.Entry<String, Object> e : map.entrySet()) {
                String value = ConvertUtil.toStr(e.getValue());
                if(StrUtil.isBlank(value)){
                    continue;
                }
                // 前标签
                sb.append("<");
                sb.append(e.getKey());
                sb.append(">");
                // 自定义属性，只有字符串才加
                if(! (e.getValue() instanceof Number)){
                    if(! value.startsWith("<") && ! value.endsWith(">")){
                        sb.append(markPrefix);
                    }
                    sb.append(e.getValue());
                    if(! value.startsWith("<") && ! value.endsWith(">")){
                        sb.append(markSuffix);
                    }
                }else{
                    sb.append(e.getValue());
                }

                // 后标签
                sb.append("</");
                sb.append(e.getKey());
                sb.append(">");
            }
            sb.append("</");
            sb.append(alias);
            sb.append(">");
            return sb.toString();
        }catch (Exception e){
            LOGGER.error("将map集合转换成xml,并给集合中的每个属性添加自定义内容异常：：：{}", ExceptionUtils.getStackTrace(e));
        }
        return "";
    }

}

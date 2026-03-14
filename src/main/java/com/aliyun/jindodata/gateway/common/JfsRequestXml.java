package com.aliyun.jindodata.gateway.common;

import org.apache.hadoop.fs.XAttr;
import org.apache.hadoop.fs.permission.AclEntry;
import org.apache.hadoop.hdfs.protocol.ExtendedBlock;
import org.jetbrains.annotations.TestOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.aliyun.jindodata.gateway.common.JfsConstant.JFS_BACKEND_TYPE_CLOUD;

public class JfsRequestXml {

    private static final Logger LOG = LoggerFactory.getLogger(JfsRequestXml.class);
    
    private static final String TRUE_STR = "true";
    private static final String FALSE_STR = "false";

    /** XML doc */
    private Document document;
    
    /** parameters node */
    private Element parametersNode;

    public JfsRequestXml() {
        initializeDocument();
    }

    private void initializeDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.newDocument();
        } catch (Exception e) {
            LOG.error("Failed to create XML document", e);
        }
    }

    public Element getParametersNode() {
        return parametersNode;
    }

    public int initRequest(String requestType, String requestUser, String instanceName,
                          String callerContext, String callerContextSignature) {
        if (document == null) {
            return -1;
        }

        try {
            return doInitRequest(requestType, requestUser, instanceName, callerContext, callerContextSignature);
        } catch (Exception e) {
            LOG.error("Failed to init request", e);
            return -1;
        }
    }

    private int doInitRequest(String requestType, String requestUser, String instanceName,
                              String callerContext, String callerContextSignature) {
        // root: <request>
        Element requestNode = document.createElement("request");
        document.appendChild(requestNode);

        // add clientVersion
        Element clientVersionNode = document.createElement("clientVersion");
        clientVersionNode.setTextContent("1.0.0"); // Version number can be configured
        requestNode.appendChild(clientVersionNode);

        // add instanceName
        Element instanceNameNode = document.createElement("instanceName");
        instanceNameNode.setTextContent(instanceName != null ? instanceName : "");
        requestNode.appendChild(instanceNameNode);

        // add requestType
        Element requestTypeNode = document.createElement("requestType");
        requestTypeNode.setTextContent(requestType != null ? requestType : "");
        requestNode.appendChild(requestTypeNode);

        // add user (optional)
        if (requestUser != null && !requestUser.isEmpty()) {
            Element userNode = document.createElement("user");
            userNode.setTextContent(requestUser);
            requestNode.appendChild(userNode);
        }

        // add callerContext (optional)
        if ((callerContext != null && !callerContext.isEmpty()) || 
            (callerContextSignature != null && !callerContextSignature.isEmpty())) {
            Element callerContextNode = document.createElement("callerContext");
            
            if (callerContext != null && !callerContext.isEmpty()) {
                String encodedContext = urlEncode(callerContext, true);
                Element contextNode = document.createElement("context");
                contextNode.setTextContent(encodedContext);
                callerContextNode.appendChild(contextNode);
            }
            
            if (callerContextSignature != null && !callerContextSignature.isEmpty()) {
                String encodedSignature = urlEncode(callerContextSignature, true);
                Element signatureNode = document.createElement("signature");
                signatureNode.setTextContent(encodedSignature);
                callerContextNode.appendChild(signatureNode);
            }
            
            requestNode.appendChild(callerContextNode);
        }

        // add parameters node
        parametersNode = document.createElement("parameters");
        requestNode.appendChild(parametersNode);

        return 0;
    }

    /**
     * add String node
     *
     * @return 0 means success
     */
    public int addRequestNode(Element parentNode, String key, String value, boolean allowEmpty) {
        if (!allowEmpty && (value == null || value.isEmpty())) {
            return 1;
        }

        try {
            Element node = document.createElement(key);
            if (value != null) {
                node.setTextContent(value);
            }
            parentNode.appendChild(node);
            return 0;
        } catch (Exception e) {
            LOG.warn("Failed to add Request Node {}:{}", key, value);
            return -1;
        }
    }

    /**
     * add byte node
     *
     * @return 0 means success
     */
    public int addRequestNode(Element parentNode, String key, byte value) {
        if (parentNode == null) {
            return -1;
        }

        try {
            Element node = document.createElement(key);
            node.setTextContent(String.valueOf(value));
            parentNode.appendChild(node);
            return 0;
        } catch (Exception e) {
            LOG.warn("Failed to add Request Node {}:{}", key, value);
            return -1;
        }
    }

    /**
     * add int node
     *
     * @return 0 means success
     */
    public int addRequestNode(Element parentNode, String key, int value) {
        if (parentNode == null) {
            return -1;
        }

        try {
            Element node = document.createElement(key);
            node.setTextContent(String.valueOf(value));
            parentNode.appendChild(node);
            return 0;
        } catch (Exception e) {
            LOG.warn("Failed to add Request Node {}:{}", key, value);
            return -1;
        }
    }

    /**
     * add long node
     *
     * @return 0 means success
     */
    public int addRequestNode(Element parentNode, String key, long value) {
        if (parentNode == null) {
            return -1;
        }

        try {
            Element node = document.createElement(key);
            node.setTextContent(String.valueOf(value));
            parentNode.appendChild(node);
            return 0;
        } catch (Exception e) {
            LOG.warn("Failed to add Request Node {}:{}", key, value);
            return -1;
        }
    }

    /**
     * add bool node
     *
     * @return 0 means success
     */
    public int addRequestNode(Element parentNode, String key, boolean value) {
        if (parentNode == null) {
            return -1;
        }

        try {
            Element node = document.createElement(key);
            node.setTextContent(value ? TRUE_STR : FALSE_STR);
            parentNode.appendChild(node);
            return 0;
        } catch (Exception e) {
            LOG.warn("Failed to add Request Node {}:{}", key, value);
            return -1;
        }
    }

    /**
     * add String parameter
     *
     * @return 0 means success
     */
    public int addRequestParameter(String key, String value) {
        if (parametersNode == null) {
            LOG.warn("Request Haven't Initiated Parameter.");
            return -1;
        }
        return addRequestNode(parametersNode, key, value, false);
    }

    /**
     * add byte parameter
     *
     * @return 0 means success
     */
    public int addRequestParameter(String key, byte value) {
        if (parametersNode == null) {
            LOG.warn("Request Haven't Initiated Parameter.");
            return -1;
        }
        return addRequestNode(parametersNode, key, value);
    }

    /**
     * add int parameter
     *
     * @return 0 means success
     */
    public int addRequestParameter(String key, int value) {
        if (parametersNode == null) {
            LOG.warn("Request Haven't Initiated Parameter.");
            return -1;
        }
        return addRequestNode(parametersNode, key, value);
    }

    /**
     * add long parameter
     *
     * @return 0 means success
     */
    public int addRequestParameter(String key, long value) {
        if (parametersNode == null) {
            LOG.warn("Request Haven't Initiated Parameter.");
            return -1;
        }
        return addRequestNode(parametersNode, key, value);
    }

    /**
     * add bool parameter
     *
     * @return 0 means success
     */
    public int addRequestParameter(String key, boolean value) {
        if (parametersNode == null) {
            LOG.warn("Request Haven't Initiated Parameter.");
            return -1;
        }
        return addRequestNode(parametersNode, key, value);
    }

    /**
     * add ExtendedBlock parameter
     *
     * @return 0 means success
     */
    public int addRequestParameter(String key, ExtendedBlock block) {
        if (parametersNode == null) {
            LOG.warn("Request Haven't Initiated Parameter.");
            return -1;
        }
        if (null == block) {
            return 0;
        }
        Element block_node = document.createElement(key);
        if (addRequestNode(block_node, "poolId", block.getBlockPoolId(), false) == -1) {
            return -1;
        }
        if (addRequestNode(block_node, "blockId", block.getBlockId()) == -1) {
            return -1;
        }
        if (addRequestNode(block_node, "generationStamp", block.getGenerationStamp()) == -1) {
            return -1;
        }
        if (addRequestNode(block_node, "numBytes", block.getNumBytes()) == -1) {
            return -1;
        }
        // OSS-HDFS fields. DO NOT MODIFY!!!
        if (addRequestNode(block_node, "backendType", JFS_BACKEND_TYPE_CLOUD) == -1) {
            return -1;
        }
        if (addRequestNode(block_node, "backendLocation", ".dlsdata", false) == -1) {
            return -1;
        }

        parametersNode.appendChild(block_node);
        return 0;
    }

    /**
     * add List<AclEntry> parameter
     *
     * @return 0 means success
     */
    public int addRequestParameter(String key, List<AclEntry> aclSpec) {
        if (parametersNode == null) {
            LOG.warn("Request Haven't Initiated Parameter.");
            return -1;
        }
        if (aclSpec == null || aclSpec.isEmpty()) {
            return 0;
        }
        try {
            Element aclSpecNode = document.createElement(key);
            parametersNode.appendChild(aclSpecNode);
            
            for (AclEntry acl : aclSpec) {
                if (acl == null) {
                    continue;
                }
                Element aclEntryNode = document.createElement("aclEntry");
                addRequestNode(aclEntryNode, "scope", acl.getScope().ordinal());
                addRequestNode(aclEntryNode, "name", acl.getName() != null ? acl.getName() : "", true);
                addRequestNode(aclEntryNode, "type", acl.getType().ordinal());
                addRequestNode(aclEntryNode, "permission", acl.getPermission().ordinal());
                aclSpecNode.appendChild(aclEntryNode);
            }
            return 0;
        } catch (Exception e) {
            LOG.warn("Failed to add AclEntry Request Parameter.", e);
            return -1;
        }
    }

    /**
     * add XAttr parameter
     * 参考 JfsRequestXml.cpp 第 300-322 行
     * XML format:
     * <xAttr>
     *   <namespace>0</namespace>
     *   <name>myattr</name>
     *   <value>base64_value</value>
     * </xAttr>
     *
     * @return 0 means success
     */
    public int addRequestParameter(String key, XAttr xAttr) {
        if (parametersNode == null) {
            LOG.warn("Request Haven't Initiated Parameter.");
            return -1;
        }
        if (xAttr == null) {
            return 0;
        }
        try {
            Element xAttrNode = document.createElement(key);
            // namespace 使用整数值 (USER=0, TRUSTED=1, SYSTEM=2, SECURITY=3, RAW=4)
            addRequestNode(xAttrNode, "namespace", xAttr.getNameSpace().ordinal());
            addRequestNode(xAttrNode, "name", xAttr.getName(), false);
            if (xAttr.getValue() != null) {
                addRequestNode(xAttrNode, "value", 
                        java.util.Base64.getEncoder().encodeToString(xAttr.getValue()), false);
            }
            parametersNode.appendChild(xAttrNode);
            return 0;
        } catch (Exception e) {
            LOG.warn("Failed to add XAttr Request Parameter.", e);
            return -1;
        }
    }

    /**
     * add List<XAttr> parameter
     * 参考 JfsRequestXml.cpp 第 324-344 行
     * XML format:
     * <xAttrs>
     *   <xAttr>
     *     <namespace>0</namespace>
     *     <name>myattr</name>
     *   </xAttr>
     * </xAttrs>
     *
     * @return 0 means success
     */
    public int addRequestParameterXAttrs(String key, List<XAttr> xAttrs) {
        if (parametersNode == null) {
            LOG.warn("Request Haven't Initiated Parameter.");
            return -1;
        }
        if (xAttrs == null || xAttrs.isEmpty()) {
            return 0;
        }
        try {
            Element xAttrsNode = document.createElement(key);
            parametersNode.appendChild(xAttrsNode);
            
            for (XAttr xAttr : xAttrs) {
                if (xAttr == null) {
                    continue;
                }
                Element xAttrNode = document.createElement("xAttr");
                // namespace 使用整数值 (USER=0, TRUSTED=1, SYSTEM=2, SECURITY=3, RAW=4)
                addRequestNode(xAttrNode, "namespace", xAttr.getNameSpace().ordinal());
                addRequestNode(xAttrNode, "name", xAttr.getName(), false);
                if (xAttr.getValue() != null) {
                    addRequestNode(xAttrNode, "value", 
                            java.util.Base64.getEncoder().encodeToString(xAttr.getValue()), false);
                }
                xAttrsNode.appendChild(xAttrNode);
            }
            return 0;
        } catch (Exception e) {
            LOG.warn("Failed to add XAttrs Request Parameter.", e);
            return -1;
        }
    }

    /**
     * add sources parameter for concat
     * XML format:
     * <sources>
     *   <source>path1</source>
     *   <source>path2</source>
     * </sources>
     *
     * @return 0 means success
     */
    public int addRequestParameterSources(String key, String[] sources) {
        if (parametersNode == null) {
            LOG.warn("Request Haven't Initiated Parameter.");
            return -1;
        }
        if (sources == null || sources.length == 0) {
            return 0;
        }
        try {
            Element sourcesNode = document.createElement(key);
            parametersNode.appendChild(sourcesNode);
            
            for (String source : sources) {
                if (source == null) {
                    continue;
                }
                Element sourceNode = document.createElement("source");
                sourceNode.setTextContent(encodePath(source));
                sourcesNode.appendChild(sourceNode);
            }
            return 0;
        } catch (Exception e) {
            LOG.warn("Failed to add sources Request Parameter.", e);
            return -1;
        }
    }

    /**
     * get XML String
     *
     * @return XML String
     */
    public String getXmlString() {
        try {
            if (document == null) {
                return null;
            }

            // use Transformer serialize XML
            javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = tFactory.newTransformer();
//            transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "UTF-8");
//            transformer.setOutputProperty(javax.xml.transform.OutputKeys.METHOD, "xml");
            
            StringWriter writer = new StringWriter();
            javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(document);
            javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(writer);
            transformer.transform(source, result);
            
            String xmlString = writer.toString();
            LOG.debug("Generated request XML: {}", xmlString);
            return xmlString;
        } catch (Exception e) {
            LOG.warn("Failed to get Request XML", e);
            return null;
        }
    }

    /**
     * URL encode
     *
     * @param path to encode
     * @param encodingPath if encode '/'
     * @return encoded path
     */
    public static String urlEncode(String path, boolean encodingPath) {
        if (path == null || path.isEmpty()) {
            return path;
        }

        try {
            StringBuilder sb = new StringBuilder();
            byte[] pathBytes = path.getBytes(StandardCharsets.UTF_8);
            
            for (byte b : pathBytes) {
                char c = (char) (b & 0xFF);
                
                // (RFC 3986 unreserved characters)
                if ((c >= 'a' && c <= 'z') || 
                    (c >= 'A' && c <= 'Z') || 
                    (c >= '0' && c <= '9') ||
                    c == '-' || c == '_' || c == '.' || c == '~') {
                    sb.append(c);
                } else if (c == '/' && !encodingPath) {
                    // not encode '/'
                    sb.append(c);
                } else {
                    sb.append(String.format("%%%02X", b & 0xFF));
                }
            }
            
            return sb.toString();
        } catch (Exception e) {
            LOG.warn("Failed to encode path: {}", path);
            return path;
        }
    }

    /**
     * encode path
     *
     * @param path to encode
     * @return encoded path
     */
    public static String encodePath(String path) {
        return urlEncode(path, true);
    }

    @TestOnly
    public Document getDocument() {
        return document;
    }
}

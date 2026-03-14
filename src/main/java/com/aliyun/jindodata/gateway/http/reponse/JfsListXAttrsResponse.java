package com.aliyun.jindodata.gateway.http.reponse;

import com.aliyun.jindodata.gateway.common.JfsResponseXml;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import org.apache.hadoop.fs.XAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class JfsListXAttrsResponse extends JfsAbstractHttpResponse {
    private static final Logger LOG = LoggerFactory.getLogger(JfsListXAttrsResponse.class);

    private List<XAttr> xAttrs;

    public JfsListXAttrsResponse() {
        super();
        xAttrs = new ArrayList<>();
    }

    public List<XAttr> getXAttrs() {
        return xAttrs;
    }

    @Override
    public JfsStatus parseXml(String xml) {
        if (xml == null || xml.isEmpty()) {
            return JfsStatus.OK();
        }

        try {
            JfsStatus status = responseXml.parseResponse(xml);
            if (!status.isOk()) {
                return status;
            }

            Element response = responseXml.getResponseNode();
            Element xAttrsNode = JfsResponseXml.getNode(response, "xAttrs");
            if (xAttrsNode == null) {
                // Empty result is valid
                return JfsStatus.OK();
            }

            NodeList xAttrNodes = xAttrsNode.getElementsByTagName("xAttr");
            for (int i = 0; i < xAttrNodes.getLength(); i++) {
                Element xAttrNode = (Element) xAttrNodes.item(i);
                
                // 参考 cpp 实现，解析独立的 namespace 和 name 字段
                // namespace 是整数值 (USER=0, TRUSTED=1, SYSTEM=2, SECURITY=3, RAW=4)
                int namespaceInt = JfsResponseXml.getNodeInt(xAttrNode, "namespace", 0, false);
                String name = JfsResponseXml.getNodeString(xAttrNode, "name", "", true);
                
                XAttr.NameSpace nameSpace = parseNameSpace(namespaceInt);
                
                // listXAttrs only returns namespace and name, no value
                XAttr xAttr = new XAttr.Builder()
                        .setNameSpace(nameSpace)
                        .setName(name)
                        .build();
                xAttrs.add(xAttr);
            }

        } catch (Exception e) {
            LOG.warn("Failed to parse listXAttrs response", e);
            return JfsStatus.ioError("Failed to parse listXAttrs response: " + e.getMessage());
        }

        return JfsStatus.OK();
    }

    private XAttr.NameSpace parseNameSpace(int value) {
        XAttr.NameSpace[] values = XAttr.NameSpace.values();
        if (value >= 0 && value < values.length) {
            return values[value];
        }
        return XAttr.NameSpace.USER;
    }
}

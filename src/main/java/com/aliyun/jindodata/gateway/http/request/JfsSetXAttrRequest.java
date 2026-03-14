package com.aliyun.jindodata.gateway.http.request;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import org.apache.hadoop.fs.XAttr;
import org.apache.hadoop.fs.XAttrSetFlag;

import java.util.EnumSet;

public class JfsSetXAttrRequest extends JfsAbstractHttpRequest {
    private static final String REQUEST_TYPE = "setXAttr";
    private static final String SRC_KEY = "src";
    private static final String XATTR_KEY = "xAttr";
    private static final String FLAG_KEY = "flag";

    private XAttr xAttr;
    private EnumSet<XAttrSetFlag> flag;

    public JfsSetXAttrRequest() {
        super();
        setQueryParam(NS_DFS, "");
    }

    @Override
    public void prepareRequest(JfsRequestOptions options) {
        initRequestWithOptions(options);
        initRequestXml(REQUEST_TYPE);

        requestXml.addRequestParameter(SRC_KEY, encodePath(path));
        requestXml.addRequestParameter(XATTR_KEY, xAttr);
        
        // Serialize flag
        if (flag != null) {
            int flagValue = 0;
            if (flag.contains(XAttrSetFlag.CREATE)) {
                flagValue |= 1;
            }
            if (flag.contains(XAttrSetFlag.REPLACE)) {
                flagValue |= 2;
            }
            requestXml.addRequestParameter(FLAG_KEY, flagValue);
        }

        setBody(requestXml.getXmlString());
    }

    public String getSrc() {
        return path;
    }

    public void setSrc(String src) {
        this.path = src;
    }

    public XAttr getXAttr() {
        return xAttr;
    }

    public void setXAttr(XAttr xAttr) {
        this.xAttr = xAttr;
    }

    public EnumSet<XAttrSetFlag> getFlag() {
        return flag;
    }

    public void setFlag(EnumSet<XAttrSetFlag> flag) {
        this.flag = flag;
    }
}

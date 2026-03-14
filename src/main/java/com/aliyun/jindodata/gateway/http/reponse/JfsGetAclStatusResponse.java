package com.aliyun.jindodata.gateway.http.reponse;

import com.aliyun.jindodata.gateway.common.JfsResponseXml;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import org.apache.hadoop.fs.permission.AclStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class JfsGetAclStatusResponse extends JfsAbstractHttpResponse {
    private static final Logger LOG = LoggerFactory.getLogger(JfsGetAclStatusResponse.class);

    private AclStatus result;

    public JfsGetAclStatusResponse() {
        super();
    }

    public AclStatus getResult() {
        return result;
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
            Element resultNode = JfsResponseXml.getNode(response, "result");
            if (resultNode == null) {
                LOG.warn("Missing result node in response");
                return JfsStatus.corruption("Missing result node");
            }

            result = responseXml.getAclStatus(resultNode);

        } catch (Exception e) {
            LOG.warn("Failed to parse getAclStatus response", e);
            return JfsStatus.ioError("Failed to parse getAclStatus response: " + e.getMessage());
        }

        return JfsStatus.OK();
    }
}

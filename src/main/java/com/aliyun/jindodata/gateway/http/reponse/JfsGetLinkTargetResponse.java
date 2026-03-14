package com.aliyun.jindodata.gateway.http.reponse;

import com.aliyun.jindodata.gateway.common.JfsResponseXml;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.io.IOException;

public class JfsGetLinkTargetResponse extends JfsAbstractHttpResponse {
    private static final Logger LOG = LoggerFactory.getLogger(JfsGetLinkTargetResponse.class);

    private String targetPath;

    public JfsGetLinkTargetResponse() {
        super();
    }

    @Override
    public JfsStatus parseXml(String xml) {
        Element response = responseXml.getResponseNode();

        try {
            targetPath = JfsResponseXml.getNodeString(response, "targetPath", "", true);
        } catch (IOException e) {
            LOG.warn("Failed to parse getLinkTarget response", e);
            return JfsStatus.corruption("Failed to parse getLinkTarget response: " + e.getMessage());
        }

        return JfsStatus.OK();
    }

    public String getTargetPath() {
        return targetPath;
    }
}

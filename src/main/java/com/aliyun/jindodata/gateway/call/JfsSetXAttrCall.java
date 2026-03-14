package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import com.aliyun.jindodata.gateway.http.reponse.JfsSetXAttrResponse;
import com.aliyun.jindodata.gateway.http.request.JfsSetXAttrRequest;
import org.apache.hadoop.fs.XAttr;
import org.apache.hadoop.fs.XAttrSetFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

public class JfsSetXAttrCall extends JfsBaseCall {
    private static final Logger LOG = LoggerFactory.getLogger(JfsSetXAttrCall.class);

    public JfsSetXAttrCall() {
        request = new JfsSetXAttrRequest();
        response = new JfsSetXAttrResponse();
    }

    @Override
    public JfsStatus execute(JfsRequestOptions requestOptions) {
        long startTime = System.currentTimeMillis();
        JfsSetXAttrRequest req = (JfsSetXAttrRequest) request;

        String src = req.getSrc();
        String bucket = requestOptions.getBucket();

        LOG.info("SetXAttr path {} from bucket {}", src, bucket);

        JfsStatus status = super.execute(requestOptions);

        if (status.isOk()) {
            long elapsed = System.currentTimeMillis() - startTime;
            String requestId = response.getRequestId();
            String serverTime = response.getServerTime();

            LOG.info("[RequestId: {}] Successfully SetXAttr path {} from bucket {} dur {}ms ossServerElapsed {}",
                    requestId, src, bucket, elapsed, serverTime);
        } else {
            String requestId = response.getRequestId();
            LOG.warn("[RequestId: {}] Failed to SetXAttr path {} from bucket {}, errorCode: {}, errorMessage: {}",
                    requestId, src, bucket, status.getCode(), status.getMessage());
        }

        return status;
    }

    public void setSrc(String src) {
        ((JfsSetXAttrRequest) request).setSrc(src);
    }

    public void setXAttr(XAttr xAttr) {
        ((JfsSetXAttrRequest) request).setXAttr(xAttr);
    }

    public void setFlag(EnumSet<XAttrSetFlag> flag) {
        ((JfsSetXAttrRequest) request).setFlag(flag);
    }
}

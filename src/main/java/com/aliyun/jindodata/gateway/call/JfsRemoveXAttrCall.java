package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import com.aliyun.jindodata.gateway.http.reponse.JfsRemoveXAttrResponse;
import com.aliyun.jindodata.gateway.http.request.JfsRemoveXAttrRequest;
import org.apache.hadoop.fs.XAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JfsRemoveXAttrCall extends JfsBaseCall {
    private static final Logger LOG = LoggerFactory.getLogger(JfsRemoveXAttrCall.class);

    public JfsRemoveXAttrCall() {
        request = new JfsRemoveXAttrRequest();
        response = new JfsRemoveXAttrResponse();
    }

    @Override
    public JfsStatus execute(JfsRequestOptions requestOptions) {
        long startTime = System.currentTimeMillis();
        JfsRemoveXAttrRequest req = (JfsRemoveXAttrRequest) request;

        String src = req.getSrc();
        String bucket = requestOptions.getBucket();

        LOG.info("RemoveXAttr path {} from bucket {}", src, bucket);

        JfsStatus status = super.execute(requestOptions);

        if (status.isOk()) {
            long elapsed = System.currentTimeMillis() - startTime;
            String requestId = response.getRequestId();
            String serverTime = response.getServerTime();

            LOG.info("[RequestId: {}] Successfully RemoveXAttr path {} from bucket {} dur {}ms ossServerElapsed {}",
                    requestId, src, bucket, elapsed, serverTime);
        } else {
            String requestId = response.getRequestId();
            LOG.warn("[RequestId: {}] Failed to RemoveXAttr path {} from bucket {}, errorCode: {}, errorMessage: {}",
                    requestId, src, bucket, status.getCode(), status.getMessage());
        }

        return status;
    }

    public void setSrc(String src) {
        ((JfsRemoveXAttrRequest) request).setSrc(src);
    }

    public void setXAttr(XAttr xAttr) {
        ((JfsRemoveXAttrRequest) request).setXAttr(xAttr);
    }
}

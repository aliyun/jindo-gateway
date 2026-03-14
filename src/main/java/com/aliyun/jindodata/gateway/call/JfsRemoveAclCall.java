package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import com.aliyun.jindodata.gateway.http.reponse.JfsRemoveAclResponse;
import com.aliyun.jindodata.gateway.http.request.JfsRemoveAclRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JfsRemoveAclCall extends JfsBaseCall {
    private static final Logger LOG = LoggerFactory.getLogger(JfsRemoveAclCall.class);

    public JfsRemoveAclCall() {
        request = new JfsRemoveAclRequest();
        response = new JfsRemoveAclResponse();
    }

    @Override
    public JfsStatus execute(JfsRequestOptions requestOptions) {
        long startTime = System.currentTimeMillis();
        JfsRemoveAclRequest req = (JfsRemoveAclRequest) request;

        String src = req.getSrc();
        String bucket = requestOptions.getBucket();

        LOG.info("RemoveAcl path {} from bucket {}", src, bucket);

        JfsStatus status = super.execute(requestOptions);

        if (status.isOk()) {
            long elapsed = System.currentTimeMillis() - startTime;
            String requestId = response.getRequestId();
            String serverTime = response.getServerTime();

            LOG.info("[RequestId: {}] Successfully RemoveAcl path {} from bucket {} dur {}ms ossServerElapsed {}",
                    requestId, src, bucket, elapsed, serverTime);
        } else {
            String requestId = response.getRequestId();
            LOG.warn("[RequestId: {}] Failed to RemoveAcl path {} from bucket {}, errorCode: {}, errorMessage: {}",
                    requestId, src, bucket, status.getCode(), status.getMessage());
        }

        return status;
    }

    public void setSrc(String src) {
        ((JfsRemoveAclRequest) request).setSrc(src);
    }
}

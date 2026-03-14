package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import com.aliyun.jindodata.gateway.http.reponse.JfsRemoveDefaultAclResponse;
import com.aliyun.jindodata.gateway.http.request.JfsRemoveDefaultAclRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JfsRemoveDefaultAclCall extends JfsBaseCall {
    private static final Logger LOG = LoggerFactory.getLogger(JfsRemoveDefaultAclCall.class);

    public JfsRemoveDefaultAclCall() {
        request = new JfsRemoveDefaultAclRequest();
        response = new JfsRemoveDefaultAclResponse();
    }

    @Override
    public JfsStatus execute(JfsRequestOptions requestOptions) {
        long startTime = System.currentTimeMillis();
        JfsRemoveDefaultAclRequest req = (JfsRemoveDefaultAclRequest) request;

        String src = req.getSrc();
        String bucket = requestOptions.getBucket();

        LOG.info("RemoveDefaultAcl path {} from bucket {}", src, bucket);

        JfsStatus status = super.execute(requestOptions);

        if (status.isOk()) {
            long elapsed = System.currentTimeMillis() - startTime;
            String requestId = response.getRequestId();
            String serverTime = response.getServerTime();

            LOG.info("[RequestId: {}] Successfully RemoveDefaultAcl path {} from bucket {} dur {}ms ossServerElapsed {}",
                    requestId, src, bucket, elapsed, serverTime);
        } else {
            String requestId = response.getRequestId();
            LOG.warn("[RequestId: {}] Failed to RemoveDefaultAcl path {} from bucket {}, errorCode: {}, errorMessage: {}",
                    requestId, src, bucket, status.getCode(), status.getMessage());
        }

        return status;
    }

    public void setSrc(String src) {
        ((JfsRemoveDefaultAclRequest) request).setSrc(src);
    }
}

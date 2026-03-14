package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import com.aliyun.jindodata.gateway.http.reponse.JfsCreateSymlinkResponse;
import com.aliyun.jindodata.gateway.http.request.JfsCreateSymlinkRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JfsCreateSymlinkCall extends JfsBaseCall {
    private static final Logger LOG = LoggerFactory.getLogger(JfsCreateSymlinkCall.class);

    public JfsCreateSymlinkCall() {
        request = new JfsCreateSymlinkRequest();
        response = new JfsCreateSymlinkResponse();
    }

    @Override
    public JfsStatus execute(JfsRequestOptions requestOptions) {
        long startTime = System.currentTimeMillis();
        JfsCreateSymlinkRequest req = (JfsCreateSymlinkRequest) request;

        String target = req.getTarget();
        String link = req.getLink();
        String bucket = requestOptions.getBucket();

        LOG.info("CreateSymlink {} to {} from bucket {}", link, target, bucket);

        JfsStatus status = super.execute(requestOptions);

        if (status.isOk()) {
            long elapsed = System.currentTimeMillis() - startTime;
            String requestId = response.getRequestId();
            String serverTime = response.getServerTime();

            LOG.info("[RequestId: {}] Successfully CreateSymlink {} to {} from bucket {} dur {}ms ossServerElapsed {}",
                    requestId, link, target, bucket, elapsed, serverTime);
        } else {
            String requestId = response.getRequestId();
            LOG.warn("[RequestId: {}] Failed to CreateSymlink {} to {} from bucket {}, errorCode: {}, errorMessage: {}",
                    requestId, link, target, bucket, status.getCode(), status.getMessage());
        }

        return status;
    }

    public void setTarget(String target) {
        ((JfsCreateSymlinkRequest) request).setTarget(target);
    }

    public void setLink(String link) {
        ((JfsCreateSymlinkRequest) request).setLink(link);
    }

    public void setPermission(short permission) {
        ((JfsCreateSymlinkRequest) request).setPermission(permission);
    }

    public void setCreateParent(boolean createParent) {
        ((JfsCreateSymlinkRequest) request).setCreateParent(createParent);
    }
}

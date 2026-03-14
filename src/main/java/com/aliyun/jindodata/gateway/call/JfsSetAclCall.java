package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import com.aliyun.jindodata.gateway.http.reponse.JfsSetAclResponse;
import com.aliyun.jindodata.gateway.http.request.JfsSetAclRequest;
import org.apache.hadoop.fs.permission.AclEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JfsSetAclCall extends JfsBaseCall {
    private static final Logger LOG = LoggerFactory.getLogger(JfsSetAclCall.class);

    public JfsSetAclCall() {
        request = new JfsSetAclRequest();
        response = new JfsSetAclResponse();
    }

    @Override
    public JfsStatus execute(JfsRequestOptions requestOptions) {
        long startTime = System.currentTimeMillis();
        JfsSetAclRequest req = (JfsSetAclRequest) request;

        String src = req.getSrc();
        String bucket = requestOptions.getBucket();

        LOG.info("SetAcl path {} from bucket {}", src, bucket);

        JfsStatus status = super.execute(requestOptions);

        if (status.isOk()) {
            long elapsed = System.currentTimeMillis() - startTime;
            String requestId = response.getRequestId();
            String serverTime = response.getServerTime();

            LOG.info("[RequestId: {}] Successfully SetAcl path {} from bucket {} dur {}ms ossServerElapsed {}",
                    requestId, src, bucket, elapsed, serverTime);
        } else {
            String requestId = response.getRequestId();
            LOG.warn("[RequestId: {}] Failed to SetAcl path {} from bucket {}, errorCode: {}, errorMessage: {}",
                    requestId, src, bucket, status.getCode(), status.getMessage());
        }

        return status;
    }

    public void setSrc(String src) {
        ((JfsSetAclRequest) request).setSrc(src);
    }

    public void setAclSpec(List<AclEntry> aclSpec) {
        ((JfsSetAclRequest) request).setAclSpec(aclSpec);
    }
}

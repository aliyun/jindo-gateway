package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import com.aliyun.jindodata.gateway.http.reponse.JfsRemoveAclEntriesResponse;
import com.aliyun.jindodata.gateway.http.request.JfsRemoveAclEntriesRequest;
import org.apache.hadoop.fs.permission.AclEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JfsRemoveAclEntriesCall extends JfsBaseCall {
    private static final Logger LOG = LoggerFactory.getLogger(JfsRemoveAclEntriesCall.class);

    public JfsRemoveAclEntriesCall() {
        request = new JfsRemoveAclEntriesRequest();
        response = new JfsRemoveAclEntriesResponse();
    }

    @Override
    public JfsStatus execute(JfsRequestOptions requestOptions) {
        long startTime = System.currentTimeMillis();
        JfsRemoveAclEntriesRequest req = (JfsRemoveAclEntriesRequest) request;

        String src = req.getSrc();
        String bucket = requestOptions.getBucket();

        LOG.info("RemoveAclEntries path {} from bucket {}", src, bucket);

        JfsStatus status = super.execute(requestOptions);

        if (status.isOk()) {
            long elapsed = System.currentTimeMillis() - startTime;
            String requestId = response.getRequestId();
            String serverTime = response.getServerTime();

            LOG.info("[RequestId: {}] Successfully RemoveAclEntries path {} from bucket {} dur {}ms ossServerElapsed {}",
                    requestId, src, bucket, elapsed, serverTime);
        } else {
            String requestId = response.getRequestId();
            LOG.warn("[RequestId: {}] Failed to RemoveAclEntries path {} from bucket {}, errorCode: {}, errorMessage: {}",
                    requestId, src, bucket, status.getCode(), status.getMessage());
        }

        return status;
    }

    public void setSrc(String src) {
        ((JfsRemoveAclEntriesRequest) request).setSrc(src);
    }

    public void setAclSpec(List<AclEntry> aclSpec) {
        ((JfsRemoveAclEntriesRequest) request).setAclSpec(aclSpec);
    }
}

package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import com.aliyun.jindodata.gateway.http.reponse.JfsModifyAclEntriesResponse;
import com.aliyun.jindodata.gateway.http.request.JfsModifyAclEntriesRequest;
import org.apache.hadoop.fs.permission.AclEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JfsModifyAclEntriesCall extends JfsBaseCall {
    private static final Logger LOG = LoggerFactory.getLogger(JfsModifyAclEntriesCall.class);

    public JfsModifyAclEntriesCall() {
        request = new JfsModifyAclEntriesRequest();
        response = new JfsModifyAclEntriesResponse();
    }

    @Override
    public JfsStatus execute(JfsRequestOptions requestOptions) {
        long startTime = System.currentTimeMillis();
        JfsModifyAclEntriesRequest req = (JfsModifyAclEntriesRequest) request;

        String src = req.getSrc();
        String bucket = requestOptions.getBucket();

        LOG.info("ModifyAclEntries path {} from bucket {}", src, bucket);

        JfsStatus status = super.execute(requestOptions);

        if (status.isOk()) {
            long elapsed = System.currentTimeMillis() - startTime;
            String requestId = response.getRequestId();
            String serverTime = response.getServerTime();

            LOG.info("[RequestId: {}] Successfully ModifyAclEntries path {} from bucket {} dur {}ms ossServerElapsed {}",
                    requestId, src, bucket, elapsed, serverTime);
        } else {
            String requestId = response.getRequestId();
            LOG.warn("[RequestId: {}] Failed to ModifyAclEntries path {} from bucket {}, errorCode: {}, errorMessage: {}",
                    requestId, src, bucket, status.getCode(), status.getMessage());
        }

        return status;
    }

    public void setSrc(String src) {
        ((JfsModifyAclEntriesRequest) request).setSrc(src);
    }

    public void setAclSpec(List<AclEntry> aclSpec) {
        ((JfsModifyAclEntriesRequest) request).setAclSpec(aclSpec);
    }
}

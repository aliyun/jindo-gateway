package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import com.aliyun.jindodata.gateway.http.reponse.JfsGetXAttrsResponse;
import com.aliyun.jindodata.gateway.http.request.JfsGetXAttrsRequest;
import org.apache.hadoop.fs.XAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JfsGetXAttrsCall extends JfsBaseCall {
    private static final Logger LOG = LoggerFactory.getLogger(JfsGetXAttrsCall.class);

    public JfsGetXAttrsCall() {
        request = new JfsGetXAttrsRequest();
        response = new JfsGetXAttrsResponse();
    }

    @Override
    public JfsStatus execute(JfsRequestOptions requestOptions) {
        long startTime = System.currentTimeMillis();
        JfsGetXAttrsRequest req = (JfsGetXAttrsRequest) request;

        String src = req.getSrc();
        String bucket = requestOptions.getBucket();

        LOG.info("GetXAttrs path {} from bucket {}", src, bucket);

        JfsStatus status = super.execute(requestOptions);

        if (status.isOk()) {
            long elapsed = System.currentTimeMillis() - startTime;
            String requestId = response.getRequestId();
            String serverTime = response.getServerTime();

            LOG.info("[RequestId: {}] Successfully GetXAttrs path {} from bucket {} dur {}ms ossServerElapsed {}",
                    requestId, src, bucket, elapsed, serverTime);
        } else {
            String requestId = response.getRequestId();
            LOG.warn("[RequestId: {}] Failed to GetXAttrs path {} from bucket {}, errorCode: {}, errorMessage: {}",
                    requestId, src, bucket, status.getCode(), status.getMessage());
        }

        return status;
    }

    public void setSrc(String src) {
        ((JfsGetXAttrsRequest) request).setSrc(src);
    }

    public void setXAttrs(List<XAttr> xAttrs) {
        ((JfsGetXAttrsRequest) request).setXAttrs(xAttrs);
    }

    public List<XAttr> getResult() {
        return ((JfsGetXAttrsResponse) response).getXAttrs();
    }
}

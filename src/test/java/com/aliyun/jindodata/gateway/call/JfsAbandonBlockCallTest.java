package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.JindoTestBase;
import com.aliyun.jindodata.gateway.common.JfsRequestOptions;
import com.aliyun.jindodata.gateway.common.JfsStatus;
import com.aliyun.jindodata.gateway.hdfs.namenode.JindoNameNode;
import org.apache.hadoop.hdfs.protocol.ExtendedBlock;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JfsAbandonBlockCallTest extends JindoTestBase {

    @Test
    public void testAbandonBlockCallReturnsError() throws IOException {
        // 创建 namenode 获取 requestOptions
        JindoNameNode nn = createNameNode();
        JfsRequestOptions requestOptions = nn.getJfsRequestOptions();

        // 创建 AbandonBlockCall 并设置参数
        JfsAbandonBlockCall call = new JfsAbandonBlockCall();
        call.setPath("/test/path/file.txt");
        call.setClientName("testClient");
        call.setFileId(12345L);
        
        // 创建一个 mock 的 ExtendedBlock
        ExtendedBlock block = new ExtendedBlock("test-pool", 1001L, 1024L, 1000L);
        call.setBlock(block);

        // 执行调用 - 由于路径不存在，应该会返回错误
        JfsStatus status = call.execute(requestOptions);

        // 验证返回了错误状态
        assertFalse(status.isOk(), "应该返回错误状态");
        assertNotNull(status.getMessage(), "错误消息不应为空");
        
        System.out.println("错误码: " + status.getCode());
        System.out.println("错误信息: " + status.getMessage());
    }
}

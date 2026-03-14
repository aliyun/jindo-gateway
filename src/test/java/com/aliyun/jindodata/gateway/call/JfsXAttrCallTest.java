package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.JindoSingleClusterTestBase;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * XAttr 相关 API 测试
 */
public class JfsXAttrCallTest extends JindoSingleClusterTestBase {

    // ========== setXAttr/getXAttr 测试 ==========

    /**
     * 测试 setXAttr 和 getXAttr 基本功能
     */
    @Test
    public void testSetAndGetXAttr() throws IOException {
        FileSystem fs = getDefaultFS();
        Path testFile = makeTestPath("testSetXAttr/file.txt");
        
        fs.create(testFile).close();
        assertTrue(fs.exists(testFile));
        
        String attrName = "user.myattr";
        byte[] attrValue = "myvalue".getBytes(StandardCharsets.UTF_8);
        
        fs.setXAttr(testFile, attrName, attrValue);
        
        byte[] retrieved = fs.getXAttr(testFile, attrName);
        assertNotNull(retrieved);
        assertEquals("myvalue", new String(retrieved, StandardCharsets.UTF_8));
        
        System.out.println("SetXAttr 和 GetXAttr 验证通过");
    }

    // ========== getXAttrs 测试 ==========

    /**
     * 测试 getXAttrs 获取所有扩展属性
     */
    @Test
    public void testGetXAttrs() throws IOException {
        FileSystem fs = getDefaultFS();
        Path testFile = makeTestPath("testGetXAttrs/file.txt");
        
        fs.create(testFile).close();
        
        fs.setXAttr(testFile, "user.attr1", "value1".getBytes(StandardCharsets.UTF_8));
        
        Map<String, byte[]> xAttrs = fs.getXAttrs(testFile);
        assertNotNull(xAttrs);
        assertFalse(xAttrs.isEmpty());
        
        System.out.println("GetXAttrs 验证通过，属性数: " + xAttrs.size());
    }

    // ========== listXAttrs 测试 ==========

    /**
     * 测试 listXAttrs 列出所有 XAttr 名称
     */
    @Test
    public void testListXAttrs() throws IOException {
        FileSystem fs = getDefaultFS();
        Path testFile = makeTestPath("testListXAttrs/file.txt");
        
        fs.create(testFile).close();
        
        fs.setXAttr(testFile, "user.attr1", "value1".getBytes(StandardCharsets.UTF_8));
        fs.setXAttr(testFile, "user.attr2", "value2".getBytes(StandardCharsets.UTF_8));
        
        List<String> xAttrNames = fs.listXAttrs(testFile);
        assertNotNull(xAttrNames);
        assertEquals(2, xAttrNames.size());
        
        System.out.println("ListXAttrs 验证通过，属性数: " + xAttrNames.size());
    }

    /**
     * 测试 listXAttrs 对没有 XAttr 的文件
     */
    @Test
    public void testListXAttrsEmpty() throws IOException {
        FileSystem fs = getDefaultFS();
        Path testFile = makeTestPath("testListXAttrsEmpty/file.txt");
        
        fs.create(testFile).close();
        
        List<String> xAttrNames = fs.listXAttrs(testFile);
        assertNotNull(xAttrNames);
        assertTrue(xAttrNames.isEmpty());
        
        System.out.println("ListXAttrs empty 验证通过");
    }

    // ========== removeXAttr 测试 ==========

    /**
     * 测试 removeXAttr 删除扩展属性
     */
    @Test
    public void testRemoveXAttr() throws IOException {
        FileSystem fs = getDefaultFS();
        Path testFile = makeTestPath("testRemoveXAttr/file.txt");
        
        fs.create(testFile).close();
        
        String attrName = "user.toremove";
        fs.setXAttr(testFile, attrName, "value".getBytes(StandardCharsets.UTF_8));
        
        // 删除后验证已不存在
        fs.removeXAttr(testFile, attrName);
        
        List<String> remaining = fs.listXAttrs(testFile);
        assertTrue(remaining.isEmpty());
        
        System.out.println("RemoveXAttr 验证通过");
    }

    /**
     * 测试对不存在的文件操作 XAttr
     */
    @Test
    public void testXAttrOnNonExistent() throws IOException {
        FileSystem fs = getDefaultFS();
        Path nonExistent = makeTestPath("testXAttrNonExist/not_exist.txt");
        
        assertFalse(fs.exists(nonExistent));
        
        // 对不存在的文件设置 XAttr 应抛出异常
        assertThrows(IOException.class, () -> {
            fs.setXAttr(nonExistent, "user.test", "value".getBytes(StandardCharsets.UTF_8));
        });
        
        System.out.println("XAttr on non-existent 验证通过");
    }
}

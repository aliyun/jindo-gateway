package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.JindoSingleClusterTestBase;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JfsConcatFileCallTest extends JindoSingleClusterTestBase {

    @Test
    public void testConcat() throws IOException {
        FileSystem fs = getDefaultFS();
        Path targetFile = makeTestPath("testConcat/target.txt");
        Path srcFile1 = makeTestPath("testConcat/src1.txt");
        Path srcFile2 = makeTestPath("testConcat/src2.txt");
        
        // 创建目标文件
        try (FSDataOutputStream out = fs.create(targetFile)) {
            out.writeBytes("TARGET_CONTENT");
        }
        
        // 创建源文件1
        try (FSDataOutputStream out = fs.create(srcFile1)) {
            out.writeBytes("SRC1_CONTENT");
        }
        
        // 创建源文件2
        try (FSDataOutputStream out = fs.create(srcFile2)) {
            out.writeBytes("SRC2_CONTENT");
        }
        
        // 获取原始文件大小
        FileStatus targetStatusBefore = fs.getFileStatus(targetFile);
        FileStatus src1Status = fs.getFileStatus(srcFile1);
        FileStatus src2Status = fs.getFileStatus(srcFile2);
        
        long expectedLength = targetStatusBefore.getLen() + src1Status.getLen() + src2Status.getLen();
        System.out.println("Before concat - target size: " + targetStatusBefore.getLen());
        System.out.println("src1 size: " + src1Status.getLen() + ", src2 size: " + src2Status.getLen());
        System.out.println("Expected total size: " + expectedLength);
        
        // 执行 concat 操作
        fs.concat(targetFile, new Path[]{srcFile1, srcFile2});
        
        // 验证目标文件大小
        FileStatus targetStatusAfter = fs.getFileStatus(targetFile);
        System.out.println("After concat - target size: " + targetStatusAfter.getLen());
        
        assertEquals(expectedLength, targetStatusAfter.getLen(), "合并后的文件大小应该等于各文件大小之和");
        
        // 验证源文件已被删除
        assertFalse(fs.exists(srcFile1), "源文件1应该被删除");
        assertFalse(fs.exists(srcFile2), "源文件2应该被删除");
        
        System.out.println("Concat 验证通过");
    }

    @Test
    public void testConcatSingleSource() throws IOException {
        FileSystem fs = getDefaultFS();
        Path targetFile = makeTestPath("testConcatSingle/target.txt");
        Path srcFile = makeTestPath("testConcatSingle/src.txt");
        
        // 创建目标文件
        try (FSDataOutputStream out = fs.create(targetFile)) {
            out.writeBytes("TARGET");
        }
        
        // 创建源文件
        try (FSDataOutputStream out = fs.create(srcFile)) {
            out.writeBytes("SOURCE");
        }
        
        FileStatus targetStatusBefore = fs.getFileStatus(targetFile);
        FileStatus srcStatus = fs.getFileStatus(srcFile);
        long expectedLength = targetStatusBefore.getLen() + srcStatus.getLen();
        
        // 执行 concat 操作
        fs.concat(targetFile, new Path[]{srcFile});
        
        // 验证
        FileStatus targetStatusAfter = fs.getFileStatus(targetFile);
        assertEquals(expectedLength, targetStatusAfter.getLen(), "合并后的文件大小应该正确");
        assertFalse(fs.exists(srcFile), "源文件应该被删除");
        
        System.out.println("Concat single source 验证通过");
    }
}

package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.JindoSingleClusterTestBase;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JfsSetTimesCallTest extends JindoSingleClusterTestBase {

    @Test
    public void testSetTimes() throws IOException {
        FileSystem fs = getDefaultFS();
        Path file = makeTestPath("testSetTimes/file.txt");
        
        // 创建测试文件
        fs.create(file).close();
        
        // 获取原始时间
        FileStatus statusBefore = fs.getFileStatus(file);
        System.out.println("Before setTimes - mtime: " + statusBefore.getModificationTime() + ", atime: " + statusBefore.getAccessTime());
        
        // 设置新的时间
        long newMtime = System.currentTimeMillis() - 1000000; // 1000秒前
        long newAtime = System.currentTimeMillis() - 2000000; // 2000秒前
        fs.setTimes(file, newMtime, newAtime);
        
        // 验证时间已更新
        FileStatus statusAfter = fs.getFileStatus(file);
        System.out.println("After setTimes - mtime: " + statusAfter.getModificationTime() + ", atime: " + statusAfter.getAccessTime());
        
        assertEquals(newMtime, statusAfter.getModificationTime(), "mtime 应该被更新");
        assertEquals(newAtime, statusAfter.getAccessTime(), "atime 应该被更新");
        
        System.out.println("SetTimes 验证通过");
    }

    @Test
    public void testSetTimesOnDir() throws IOException {
        FileSystem fs = getDefaultFS();
        Path dir = makeTestPath("testSetTimesOnDir/dir1");
        
        // 创建测试目录
        fs.mkdirs(dir);
        
        // 获取原始时间
        FileStatus statusBefore = fs.getFileStatus(dir);
        System.out.println("Before setTimes - mtime: " + statusBefore.getModificationTime() + ", atime: " + statusBefore.getAccessTime());
        
        // 设置新的时间
        long newMtime = System.currentTimeMillis() - 500000;
        long newAtime = System.currentTimeMillis() - 600000;
        fs.setTimes(dir, newMtime, newAtime);
        
        // 验证时间已更新
        FileStatus statusAfter = fs.getFileStatus(dir);
        System.out.println("After setTimes - mtime: " + statusAfter.getModificationTime() + ", atime: " + statusAfter.getAccessTime());
        
        assertEquals(newMtime, statusAfter.getModificationTime(), "目录 mtime 应该被更新");
        assertEquals(newAtime, statusAfter.getAccessTime(), "目录 atime 应该被更新");
        
        System.out.println("SetTimes on dir 验证通过");
    }

    @Test
    public void testSetTimesPreserve() throws IOException {
        FileSystem fs = getDefaultFS();
        Path file = makeTestPath("testSetTimesPreserve/file.txt");
        
        // 创建测试文件
        fs.create(file).close();
        
        // 获取原始时间
        FileStatus statusBefore = fs.getFileStatus(file);
        long originalMtime = statusBefore.getModificationTime();
        long originalAtime = statusBefore.getAccessTime();
        System.out.println("Original - mtime: " + originalMtime + ", atime: " + originalAtime);
        
        // 只更新 mtime，保持 atime 不变 (传 -1 表示不修改)
        long newMtime = System.currentTimeMillis() - 300000;
        fs.setTimes(file, newMtime, -1);
        
        // 验证
        FileStatus statusAfter = fs.getFileStatus(file);
        System.out.println("After setTimes(mtime only) - mtime: " + statusAfter.getModificationTime() + ", atime: " + statusAfter.getAccessTime());
        
        assertEquals(newMtime, statusAfter.getModificationTime(), "mtime 应该被更新");
        // atime 传 -1 时应该保持不变
        
        System.out.println("SetTimes preserve 验证通过");
    }
}

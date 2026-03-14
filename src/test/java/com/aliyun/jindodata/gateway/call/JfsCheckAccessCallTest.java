package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.JindoSingleClusterTestBase;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.security.AccessControlException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JfsCheckAccessCallTest extends JindoSingleClusterTestBase {

    @Test
    public void testCheckAccessRead() throws IOException {
        FileSystem fs = getDefaultFS();
        Path file = makeTestPath("testCheckAccessRead/file.txt");
        
        // 创建测试文件
        fs.create(file).close();
        
        // 设置权限 rw-r--r-- (644)
        fs.setPermission(file, new FsPermission((short) 0644));
        
        // 检查读权限 - 应该成功
        fs.access(file, FsAction.READ);
        System.out.println("CheckAccess READ 验证通过");
    }

    @Test
    public void testCheckAccessWrite() throws IOException {
        FileSystem fs = getDefaultFS();
        Path file = makeTestPath("testCheckAccessWrite/file.txt");
        
        // 创建测试文件
        fs.create(file).close();
        
        // 设置权限 rw-r--r-- (644)
        fs.setPermission(file, new FsPermission((short) 0644));
        
        // 检查写权限 - 应该成功（owner 有写权限）
        fs.access(file, FsAction.WRITE);
        System.out.println("CheckAccess WRITE 验证通过");
    }

    @Test
    public void testCheckAccessExecute() throws IOException {
        FileSystem fs = getDefaultFS();
        Path file = makeTestPath("testCheckAccessExecute/file.txt");
        
        // 创建测试文件
        fs.create(file).close();
        
        // 设置权限 rwx------ (700)
        fs.setPermission(file, new FsPermission((short) 0700));
        
        // 检查执行权限 - 应该成功
        fs.access(file, FsAction.EXECUTE);
        System.out.println("CheckAccess EXECUTE 验证通过");
    }

    @Test
    public void testCheckAccessOnDirectory() throws IOException {
        FileSystem fs = getDefaultFS();
        Path dir = makeTestPath("testCheckAccessOnDir/dir1");
        
        // 创建测试目录
        fs.mkdirs(dir);
        
        // 设置权限 rwxr-xr-x (755)
        fs.setPermission(dir, new FsPermission((short) 0755));
        
        // 检查读权限
        fs.access(dir, FsAction.READ);
        
        // 检查执行权限
        fs.access(dir, FsAction.EXECUTE);
        
        System.out.println("CheckAccess on directory 验证通过");
    }

    @Test
    public void testCheckAccessReadWrite() throws IOException {
        FileSystem fs = getDefaultFS();
        Path file = makeTestPath("testCheckAccessRW/file.txt");
        
        // 创建测试文件
        fs.create(file).close();
        
        // 设置权限 rw-r--r-- (644)
        fs.setPermission(file, new FsPermission((short) 0644));
        
        // 检查读写权限组合
        fs.access(file, FsAction.READ_WRITE);
        System.out.println("CheckAccess READ_WRITE 验证通过");
    }

    // ========== 权限检查失败的测试用例 ==========

    @Test
    public void testCheckAccessExecuteDenied() throws IOException {
        FileSystem fs = getDefaultFS();
        Path file = makeTestPath("testCheckAccessExecuteDenied/file.txt");
        
        // 创建测试文件
        fs.create(file).close();
        
        // 设置权限 rw-r--r-- (644) - 没有执行权限
        fs.setPermission(file, new FsPermission((short) 0644));
        
        // 检查执行权限 - 应该失败
        Exception exception = assertThrows(AccessControlException.class, () -> {
            fs.access(file, FsAction.EXECUTE);
        });
        
        System.out.println("CheckAccess EXECUTE denied 异常: " + exception.getMessage());
        System.out.println("CheckAccess EXECUTE denied 验证通过");
    }

    @Test
    public void testCheckAccessWriteDeniedForOthers() throws IOException {
        FileSystem fs = getDefaultFS();
        Path file = makeTestPath("testCheckAccessWriteDenied/file.txt");
        
        // 创建测试文件
        fs.create(file).close();
        
        // 设置权限 r--r--r-- (444) - 只读
        fs.setPermission(file, new FsPermission((short) 0444));
        
        // 检查写权限 - 应该失败
        Exception exception = assertThrows(AccessControlException.class, () -> {
            fs.access(file, FsAction.WRITE);
        });
        
        System.out.println("CheckAccess WRITE denied 异常: " + exception.getMessage());
        System.out.println("CheckAccess WRITE denied 验证通过");
    }

    @Test
    public void testCheckAccessOnNonExistentFile() throws IOException {
        FileSystem fs = getDefaultFS();
        Path nonExistentFile = makeTestPath("testCheckAccessNonExistent/not_exist.txt");
        
        // 确保文件不存在
        assertFalse(fs.exists(nonExistentFile));
        
        // 检查不存在文件的权限 - 应该抛出 FileNotFoundException
        Exception exception = assertThrows(FileNotFoundException.class, () -> {
            fs.access(nonExistentFile, FsAction.READ);
        });
        
        System.out.println("CheckAccess on non-existent file 异常: " + exception.getMessage());
        System.out.println("CheckAccess on non-existent file 验证通过");
    }

    @Test
    public void testCheckAccessAllDenied() throws IOException {
        FileSystem fs = getDefaultFS();
        Path file = makeTestPath("testCheckAccessAllDenied/file.txt");
        
        // 创建测试文件
        fs.create(file).close();
        
        // 设置权限 --------- (000) - 无任何权限
        fs.setPermission(file, new FsPermission((short) 0000));
        
        // 检查读权限 - 应该失败
        Exception exception = assertThrows(AccessControlException.class, () -> {
            fs.access(file, FsAction.READ);
        });
        
        System.out.println("CheckAccess ALL denied 异常: " + exception.getMessage());
        System.out.println("CheckAccess ALL denied 验证通过");
    }
}

package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.JindoSingleClusterTestBase;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 符号链接相关 API 测试
 */
public class JfsSymlinkCallTest extends JindoSingleClusterTestBase {

    /**
     * 测试 getFileLinkStatus 对不存在的文件抛出 FileNotFoundException
     * 服务端返回 null，客户端转换为 FileNotFoundException
     */
    @Test
    public void testGetFileLinkStatusOnNonExistent() throws IOException {
        FileSystem fs = getDefaultFS();
        Path nonExistent = makeTestPath("testFileLinkInfoNonExist/not_exist.txt");
        
        // 确保文件不存在
        assertFalse(fs.exists(nonExistent));
        
        // 获取不存在文件的链接信息 - 服务端返回 null，客户端抛出 FileNotFoundException
        Exception exception = assertThrows(FileNotFoundException.class, () -> {
            fs.getFileLinkStatus(nonExistent);
        });
        
        System.out.println("GetFileLinkStatus on non-existent file 异常: " + exception.getMessage());
        System.out.println("GetFileLinkStatus on non-existent file 验证通过");
    }

    /**
     * 测试 getFileLinkStatus 对普通文件返回正确信息
     */
    @Test
    public void testGetFileLinkStatusOnRegularFile() throws IOException {
        FileSystem fs = getDefaultFS();
        Path regularFile = makeTestPath("testFileLinkInfoRegular/regular.txt");
        
        // 创建普通文件
        fs.create(regularFile).close();
        
        // 获取普通文件的链接信息 - 应该返回文件信息，且不是 symlink
        FileStatus fileStatus = fs.getFileLinkStatus(regularFile);
        assertNotNull(fileStatus);
        assertFalse(fileStatus.isSymlink());
        assertTrue(fileStatus.isFile());
        
        System.out.println("GetFileLinkStatus on regular file 验证通过");
        System.out.println("  Path: " + regularFile);
        System.out.println("  IsSymlink: " + fileStatus.isSymlink());
        System.out.println("  IsFile: " + fileStatus.isFile());
    }

    /**
     * 测试 getFileLinkStatus 对目录返回正确信息
     */
    @Test
    public void testGetFileLinkStatusOnDirectory() throws IOException {
        FileSystem fs = getDefaultFS();
        Path dir = makeTestPath("testFileLinkInfoDir/subdir");
        
        // 创建目录
        fs.mkdirs(dir);
        
        // 获取目录的链接信息
        FileStatus fileStatus = fs.getFileLinkStatus(dir);
        assertNotNull(fileStatus);
        assertFalse(fileStatus.isSymlink());
        assertTrue(fileStatus.isDirectory());
        
        System.out.println("GetFileLinkStatus on directory 验证通过");
        System.out.println("  Path: " + dir);
        System.out.println("  IsSymlink: " + fileStatus.isSymlink());
        System.out.println("  IsDirectory: " + fileStatus.isDirectory());
    }

    /**
     * 测试 getLinkTarget 对不存在的文件抛出 FileNotFoundException
     * 服务端返回 null，客户端转换为 FileNotFoundException
     */
    @Test
    public void testGetLinkTargetOnNonExistent() throws IOException {
        FileSystem fs = getDefaultFS();
        Path nonExistent = makeTestPath("testGetLinkTargetNonExist/not_exist.txt");
        
        // 确保文件不存在
        assertFalse(fs.exists(nonExistent));
        
        // 获取不存在文件的链接目标 - 服务端返回 null，客户端抛出 FileNotFoundException
        Exception exception = assertThrows(FileNotFoundException.class, () -> {
            fs.getLinkTarget(nonExistent);
        });
        
        System.out.println("GetLinkTarget on non-existent file 异常: " + exception.getMessage());
        System.out.println("GetLinkTarget on non-existent file 验证通过");
    }

    /**
     * 测试 getLinkTarget 对非 symlink 文件抛出异常
     */
    @Test
    public void testGetLinkTargetOnNonSymlink() throws IOException {
        FileSystem fs = getDefaultFS();
        Path regularFile = makeTestPath("testGetLinkTargetNonSymlink/regular.txt");
        
        // 创建普通文件
        fs.create(regularFile).close();
        
        // 尝试获取非链接文件的目标 - 应该抛出 IOException
        Exception exception = assertThrows(IOException.class, () -> {
            fs.getLinkTarget(regularFile);
        });
        
        System.out.println("GetLinkTarget on non-symlink 异常: " + exception.getMessage());
        System.out.println("GetLinkTarget on non-symlink 验证通过");
    }

    // ========== createSymlink 测试 ==========

    /**
     * 测试 createSymlink 创建符号链接
     */
    @Test
    public void testCreateSymlink() throws IOException {
        FileSystem.enableSymlinks();
        FileSystem fs = getDefaultFS();
        Path targetFile = makeTestPath("testCreateSymlink/target.txt");
        Path linkPath = makeTestPath("testCreateSymlink/link.txt");
        
        // 创建目标文件
        fs.create(targetFile).close();
        assertTrue(fs.exists(targetFile));
        
        // 创建符号链接
        fs.createSymlink(targetFile, linkPath, true);
        
        // 验证链接存在且是 symlink
        FileStatus linkStatus = fs.getFileLinkStatus(linkPath);
        assertNotNull(linkStatus);
        assertTrue(linkStatus.isSymlink());
        
        // 验证链接目标正确
        Path target = fs.getLinkTarget(linkPath);
        assertEquals(targetFile.toString(), target.toString());
        
        System.out.println("CreateSymlink 验证通过");
        System.out.println("  Target: " + targetFile);
        System.out.println("  Link: " + linkPath);
        System.out.println("  Resolved: " + target);
    }

    /**
     * 测试 createSymlink 指向目录
     */
    @Test
    public void testCreateSymlinkToDirectory() throws IOException {
        FileSystem.enableSymlinks();
        FileSystem fs = getDefaultFS();
        Path targetDir = makeTestPath("testCreateSymlinkDir/targetDir");
        Path linkPath = makeTestPath("testCreateSymlinkDir/linkDir");
        
        // 创建目标目录
        fs.mkdirs(targetDir);
        assertTrue(fs.exists(targetDir));
        
        // 创建符号链接
        fs.createSymlink(targetDir, linkPath, true);
        
        // 验证链接存在且是 symlink
        FileStatus linkStatus = fs.getFileLinkStatus(linkPath);
        assertTrue(linkStatus.isSymlink());
        
        // 验证链接目标正确
        Path target = fs.getLinkTarget(linkPath);
        assertEquals(targetDir.toString(), target.toString());
        
        System.out.println("CreateSymlink to directory 验证通过");
    }

    /**
     * 测试 createSymlink 对不存在的目标 (dangling symlink)
     */
    @Test
    public void testCreateSymlinkToNonExistent() throws IOException {
        FileSystem.enableSymlinks();
        FileSystem fs = getDefaultFS();
        Path nonExistentTarget = makeTestPath("testCreateSymlinkNonExist/not_exist.txt");
        Path linkPath = makeTestPath("testCreateSymlinkNonExist/link.txt");
        
        // 确保目标不存在
        assertFalse(fs.exists(nonExistentTarget));
        
        // 创建指向不存在目标的符号链接 - 应该成功（dangling symlink）
        fs.createSymlink(nonExistentTarget, linkPath, true);
        
        // 验证链接存在且是 symlink
        FileStatus linkStatus = fs.getFileLinkStatus(linkPath);
        assertTrue(linkStatus.isSymlink());
        
        System.out.println("CreateSymlink to non-existent target 验证通过 (dangling symlink)");
    }

    // ========== resolvePath 测试 ==========

    /**
     * 测试 resolvePath 对普通文件
     */
    @Test
    public void testResolvePathOnRegularFile() throws IOException {
        FileSystem fs = getDefaultFS();
        Path regularFile = makeTestPath("testResolvePath/regular.txt");
        
        // 创建普通文件
        fs.create(regularFile).close();
        
        // resolvePath 对普通文件应该返回原路径
        Path resolved = fs.resolvePath(regularFile);
        assertNotNull(resolved);
        
        System.out.println("ResolvePath on regular file 验证通过");
        System.out.println("  Original: " + regularFile);
        System.out.println("  Resolved: " + resolved);
    }

    /**
     * 测试 resolvePath 对目录
     */
    @Test
    public void testResolvePathOnDirectory() throws IOException {
        FileSystem fs = getDefaultFS();
        Path dir = makeTestPath("testResolvePathDir/subdir");
        
        // 创建目录
        fs.mkdirs(dir);
        
        // resolvePath 对目录
        Path resolved = fs.resolvePath(dir);
        assertNotNull(resolved);
        
        System.out.println("ResolvePath on directory 验证通过");
        System.out.println("  Original: " + dir);
        System.out.println("  Resolved: " + resolved);
    }

    /**
     * 测试 resolvePath 对不存在的文件抛出 FileNotFoundException
     */
    @Test
    public void testResolvePathOnNonExistent() throws IOException {
        FileSystem fs = getDefaultFS();
        Path nonExistent = makeTestPath("testResolvePathNonExist/not_exist.txt");
        
        // 确保文件不存在
        assertFalse(fs.exists(nonExistent));
        
        // resolvePath 对不存在的文件抛出 FileNotFoundException
        Exception exception = assertThrows(FileNotFoundException.class, () -> {
            fs.resolvePath(nonExistent);
        });
        
        System.out.println("ResolvePath on non-existent file 异常: " + exception.getMessage());
        System.out.println("ResolvePath on non-existent file 验证通过");
    }
}

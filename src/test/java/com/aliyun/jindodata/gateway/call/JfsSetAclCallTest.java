package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.JindoSingleClusterTestBase;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.AclEntry;
import org.apache.hadoop.fs.permission.AclEntryScope;
import org.apache.hadoop.fs.permission.AclEntryType;
import org.apache.hadoop.fs.permission.FsAction;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JfsSetAclCallTest extends JindoSingleClusterTestBase {

    @Test
    public void testSetAcl() throws IOException {
        FileSystem fs = getDefaultFS();
        Path dir = makeTestPath("testSetAcl/dir1");
        
        // 创建测试目录
        fs.mkdirs(dir);
        
        // 构造 ACL 条目列表
        List<AclEntry> aclSpec = Arrays.asList(
                aclEntry(AclEntryScope.ACCESS, AclEntryType.USER, FsAction.ALL),
                aclEntry(AclEntryScope.ACCESS, AclEntryType.USER, "foo", FsAction.ALL),
                aclEntry(AclEntryScope.ACCESS, AclEntryType.GROUP, FsAction.READ_EXECUTE),
                aclEntry(AclEntryScope.ACCESS, AclEntryType.OTHER, FsAction.NONE),
                aclEntry(AclEntryScope.DEFAULT, AclEntryType.USER, "foo", FsAction.ALL)
        );
        
        // 设置 ACL
        fs.setAcl(dir, aclSpec);
        
        System.out.println("SetAcl 成功完成");
    }

    @Test
    public void testSetAclOnFile() throws IOException {
        FileSystem fs = getDefaultFS();
        Path file = makeTestPath("testSetAclOnFile/file.txt");
        
        // 创建测试文件
        fs.create(file).close();
        
        // 构造 ACL 条目列表
        List<AclEntry> aclSpec = Arrays.asList(
                aclEntry(AclEntryScope.ACCESS, AclEntryType.USER, FsAction.ALL),
                aclEntry(AclEntryScope.ACCESS, AclEntryType.USER, "testuser", FsAction.READ),
                aclEntry(AclEntryScope.ACCESS, AclEntryType.GROUP, FsAction.READ),
                aclEntry(AclEntryScope.ACCESS, AclEntryType.OTHER, FsAction.NONE)
        );
        
        // 设置 ACL
        fs.setAcl(file, aclSpec);
        
        System.out.println("SetAcl on file 成功完成");
    }

    /**
     * 创建无名称的 ACL 条目 (用于 USER, GROUP, MASK, OTHER 的基础权限)
     */
    private AclEntry aclEntry(AclEntryScope scope, AclEntryType type, FsAction permission) {
        return new AclEntry.Builder()
                .setScope(scope)
                .setType(type)
                .setPermission(permission)
                .build();
    }

    /**
     * 创建带名称的 ACL 条目 (用于指定用户或组的权限)
     */
    private AclEntry aclEntry(AclEntryScope scope, AclEntryType type, String name, FsAction permission) {
        return new AclEntry.Builder()
                .setScope(scope)
                .setType(type)
                .setName(name)
                .setPermission(permission)
                .build();
    }
}

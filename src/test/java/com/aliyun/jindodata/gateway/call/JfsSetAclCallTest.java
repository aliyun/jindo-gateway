package com.aliyun.jindodata.gateway.call;

import com.aliyun.jindodata.gateway.JindoSingleClusterTestBase;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.AclEntry;
import org.apache.hadoop.fs.permission.AclEntryScope;
import org.apache.hadoop.fs.permission.AclEntryType;
import org.apache.hadoop.fs.permission.AclStatus;
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
        
        // 获取 AclStatus 验证
        AclStatus aclStatus = fs.getAclStatus(dir);
        assertNotNull(aclStatus, "AclStatus should not be null");
        assertNotNull(aclStatus.getOwner(), "Owner should not be null");
        assertNotNull(aclStatus.getGroup(), "Group should not be null");
        
        // 验证 set 和 get 的对比
        List<AclEntry> returnedEntries = aclStatus.getEntries();
        System.out.println("Set AclSpec: " + aclSpec);
        System.out.println("Get AclEntries: " + returnedEntries);
        
        // 验证设置的带名称的条目是否在返回结果中
        assertTrue(containsAclEntry(returnedEntries, AclEntryScope.ACCESS, AclEntryType.USER, "foo", FsAction.ALL),
                "应包含 user:foo:rwx");
        assertTrue(containsAclEntry(returnedEntries, AclEntryScope.DEFAULT, AclEntryType.USER, "foo", FsAction.ALL),
                "应包含 default:user:foo:rwx");
        // group::r-x 会出现在返回结果中
        assertTrue(containsAclEntry(returnedEntries, AclEntryScope.ACCESS, AclEntryType.GROUP, null, FsAction.READ_EXECUTE),
                "应包含 group::r-x");
        
        System.out.println("SetAcl and GetAclStatus 验证通过");
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
        
        // 获取 AclStatus 验证
        AclStatus aclStatus = fs.getAclStatus(file);
        assertNotNull(aclStatus, "AclStatus should not be null");
        assertNotNull(aclStatus.getOwner(), "Owner should not be null");
        assertNotNull(aclStatus.getGroup(), "Group should not be null");
        
        // 验证 set 和 get 的对比
        List<AclEntry> returnedEntries = aclStatus.getEntries();
        System.out.println("Set AclSpec: " + aclSpec);
        System.out.println("Get AclEntries: " + returnedEntries);
        
        // 验证设置的带名称的条目是否在返回结果中
        assertTrue(containsAclEntry(returnedEntries, AclEntryScope.ACCESS, AclEntryType.USER, "testuser", FsAction.READ),
                "应包含 user:testuser:r--");
        assertTrue(containsAclEntry(returnedEntries, AclEntryScope.ACCESS, AclEntryType.GROUP, null, FsAction.READ),
                "应包含 group::r--");
        
        System.out.println("SetAcl on file 验证通过");
    }

    /**
     * 检查 ACL 条目列表中是否包含指定的条目
     */
    private boolean containsAclEntry(List<AclEntry> entries, AclEntryScope scope, 
                                     AclEntryType type, String name, FsAction permission) {
        for (AclEntry entry : entries) {
            if (entry.getScope() == scope && entry.getType() == type && entry.getPermission() == permission) {
                // 如果 name 为 null，则匹配无名称的条目
                if (name == null && (entry.getName() == null || entry.getName().isEmpty())) {
                    return true;
                }
                // 如果 name 不为 null，则匹配带名称的条目
                if (name != null && name.equals(entry.getName())) {
                    return true;
                }
            }
        }
        return false;
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

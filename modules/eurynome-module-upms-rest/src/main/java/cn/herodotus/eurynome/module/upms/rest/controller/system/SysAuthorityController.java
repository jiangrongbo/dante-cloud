/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.eurynome.module.upms.rest.controller.system;

import cn.herodotus.engine.assistant.core.constants.BaseConstants;
import cn.herodotus.engine.assistant.core.domain.Result;
import cn.herodotus.engine.assistant.core.enums.AuthorityType;
import cn.herodotus.engine.data.core.service.WriteableService;
import cn.herodotus.engine.rest.core.controller.BaseWriteableRestController;
import cn.herodotus.eurynome.module.upms.logic.entity.system.SysAuthority;
import cn.herodotus.eurynome.module.upms.logic.service.system.SysAuthorityService;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Description: SysAuthorityController </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/4/10 13:21
 */
@RestController
@RequestMapping("/authority")
@Tag(name = "系统权限接口")
public class SysAuthorityController extends BaseWriteableRestController<SysAuthority, String> {

    private final SysAuthorityService sysAuthorityService;

    @Autowired
    public SysAuthorityController(SysAuthorityService sysAuthorityService) {
        this.sysAuthorityService = sysAuthorityService;
    }

    @Override
    public WriteableService<SysAuthority, String> getWriteableService() {
        return this.sysAuthorityService;
    }

    @Operation(summary = "获取权限树", description = "获取权限树形数据")
    @GetMapping("/tree")
    public Result<List<Tree<String>>> findTree() {
        Result<List<Tree<String>>> result = new Result<>();

        List<SysAuthority> sysAuthorities = sysAuthorityService.findAll();
        if (CollectionUtils.isNotEmpty(sysAuthorities)) {
            List<TreeNode<String>> treeNodes = sysAuthorities.stream().map(sysAuthority -> {
                TreeNode<String> treeNode = new TreeNode<>();
                treeNode.setId(sysAuthority.getAuthorityId());
                treeNode.setName(sysAuthority.getAuthorityName());
                treeNode.setParentId(sysAuthority.getParentId());
                return treeNode;
            }).collect(Collectors.toList());
            return result.data(TreeUtil.build(treeNodes, BaseConstants.DEFAULT_TREE_ROOT_ID));
        } else {
            return result.message("获取数据失败");
        }
    }

    @Operation(summary = "获取全部接口", description = "获取全部接口")
    @GetMapping("/list")
    public Result<List<SysAuthority>> findAll() {
        List<SysAuthority> sysAuthorities = sysAuthorityService.findAll();
        return result(sysAuthorities);
    }

    @Operation(summary = "获取全部API接口", description = "获取全部API接口")
    @GetMapping("/apis")
    @Deprecated
    public Result<List<SysAuthority>> findAllApis() {
        List<SysAuthority> sysAuthorities = sysAuthorityService.findAllByAuthorityType(AuthorityType.API);
        return result(sysAuthorities);
    }
}

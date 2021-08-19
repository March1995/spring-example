package com.wyb.test.java.java8;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 查询树形菜单
 *
 * @author Marcher丶
 */
public class Java8Tree {

    private static List<TreeNode> init() {
        return Lists.newArrayList(
                new TreeNode(1, 0, "Code"),
                new TreeNode(2, 1, "Java"),
                new TreeNode(3, 1, "JavaScript"),
                new TreeNode(4, 3, "Node.js"),
                new TreeNode(5, 3, "Vue"),
                new TreeNode(6, 3, "React"),
                new TreeNode(7, 1, "Golang"),
                new TreeNode(8, 1, "Python"),
                new TreeNode(9, 8, "Python27"),
                new TreeNode(10, 8, "Python3 8")
        );
    }


    public static void main(final String[] args) {

        final List<TreeNode> treeNodes = init();

        final List<TreeNode> tree = treeNodes.stream().filter(entity -> entity.getPid() == 0).
                peek(entity -> entity.setChildren(getChildNode(entity, treeNodes))).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(tree));

    }

    private static List<TreeNode> getChildNode(final TreeNode root, final List<TreeNode> treeNodes) {
        return treeNodes.stream().filter(entity -> Objects.equals(entity.getPid(), root.getId())).
                peek(entity -> entity.setChildren(getChildNode(entity, treeNodes))).collect(Collectors.toList());
    }


    @Data
    private static class TreeNode {
        private int id;
        private int pid;
        private String value;
        private List<TreeNode> children;

        public TreeNode() {

        }

        TreeNode(final int id, final int pid, final String value) {
            this.id = id;
            this.pid = pid;
            this.value = value;
        }
    }
}

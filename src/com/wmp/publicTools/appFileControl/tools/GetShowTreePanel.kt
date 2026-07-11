package com.wmp.publicTools.appFileControl.tools

import com.wmp.publicTools.UITools.CTFont
import com.wmp.publicTools.UITools.CTFontSizeStyle
import java.awt.Component
import java.awt.Font
import java.awt.Image
import javax.swing.ImageIcon
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer

object GetShowTreePanel {
    /**
     * 获取显示树形结构面板
     * @param data  数据集,每个数据中父级和子级之间用"."分隔
     * @return JTree
     */
    fun getShowTreePanel(data: Array<String?>, rootName: String?) =
        JTree(buildTreeFromData(data, rootName, null))

    /**
     * 获取显示树形结构面板
     * @param data  数据集,每个数据中父级和子级之间用"."分隔
     * @param imageIconMap 图片图标
     * @return JTree
     */
    fun getShowTreePanel(
        data: Array<String?>,
        rootName: String?,
        imageIconMap: MutableMap<String?, ImageIcon?>?
    ): JTree {
        val root = buildTreeFromData(data, rootName, imageIconMap)

        val tree = JTree(root)
        tree.setCellRenderer(object : DefaultTreeCellRenderer() {
            override fun getTreeCellRendererComponent(
                tree: JTree?,
                value: Any?,
                sel: Boolean,
                expanded: Boolean,
                leaf: Boolean,
                row: Int,
                hasFocus: Boolean
            ): Component {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus)

                val node = value as DefaultMutableTreeNode
                val userObject = node.getUserObject()

                if (userObject is IconNode) {
                    val (name, icon) = userObject


                    setFont(CTFont.getDefaultFont(Font.PLAIN, CTFontSizeStyle.SMALL))
                    setText(name)
                    if (icon != null) {
                        setIcon(
                            ImageIcon(
                                icon.getImage()
                                    .getScaledInstance(getFont().getSize(), getFont().getSize(), Image.SCALE_SMOOTH)
                            )
                        )
                    }
                }
                return this
            }
        })
        return tree
    }

    private fun buildTreeFromData(
        data: Array<String?>,
        rootName: String?,
        imageIconMap: MutableMap<String?, ImageIcon?>?
    ): DefaultMutableTreeNode {
        val root = DefaultMutableTreeNode(rootName)

        val nodeMap: MutableMap<String?, DefaultMutableTreeNode> = HashMap()

        for (path in data) {
            val parts: List<String> = path!!.split("\\.")
            var parentNode = root

            // 遍历路径的每个部分，构建树形结构
            val currentPath = StringBuilder()
            for (i in parts.indices) {
                if (i > 0) {
                    currentPath.append(".")
                }
                currentPath.append(parts[i])

                val fullPath = currentPath.toString()

                // 检查是否已存在该节点
                if (!nodeMap.containsKey(fullPath)) {
                    val node = DefaultMutableTreeNode(IconNode(parts[i], null))
                    if (imageIconMap != null && imageIconMap.containsKey(fullPath)) {
                        node.setUserObject(IconNode(parts[i], imageIconMap[fullPath]))
                    }
                    nodeMap[fullPath] = node
                    parentNode.add(node)
                }

                parentNode = nodeMap[fullPath]!!
            }
        }
        println(nodeMap)
        return root
    }
}

@JvmRecord
internal data class IconNode(val name: String, val icon: ImageIcon?) {
    override fun toString(): String {
        return name
    }
}

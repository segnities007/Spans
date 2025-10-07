import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * AppNavRouteのツリー構造を生成してコメントとして出力するGradleタスク
 */
open class NavRouteTreeGenerator : DefaultTask() {

    @TaskAction
    fun generateTree() {
        val navRouteFile = File(
            project.rootDir,
            "presentation/common/src/main/java/com/segnities007/common/route/AppNavRoute.kt"
        )

        if (!navRouteFile.exists()) {
            println("Error: AppNavRoute.kt が見つかりません")
            return
        }

        val content = navRouteFile.readText()
        val tree = buildNavRouteTree(content)
        
        println("\n" + "=".repeat(60))
        println("AppNavRoute ツリー構造")
        println("=".repeat(60))
        println(tree)
        println("=".repeat(60) + "\n")
        
        // オプション: ファイルにも出力
        val outputFile = File(project.rootDir, "navigation_tree.txt")
        outputFile.writeText(tree)
        println("ツリー構造を ${outputFile.absolutePath} に保存しました")
    }

    private fun buildNavRouteTree(content: String): String {
        val lines = content.lines()
        val result = StringBuilder()
        
        result.appendLine("AppNavRoute")
        
        var currentInterface: String? = null
        var indent = ""
        
        for (line in lines) {
            val trimmed = line.trim()
            
            // sealed interfaceを検出
            when {
                trimmed.startsWith("sealed interface AppNavRoute") -> {
                    currentInterface = "AppNavRoute"
                    indent = "├── "
                }
                trimmed.startsWith("sealed interface AuthNavRoute") -> {
                    currentInterface = "AuthNavRoute"
                    result.appendLine("│")
                    result.appendLine("├── AuthNavRoute (認証フロー)")
                    indent = "│   ├── "
                }
                trimmed.startsWith("sealed interface HubNavRoute") -> {
                    currentInterface = "HubNavRoute"
                    result.appendLine("│")
                    result.appendLine("└── HubNavRoute (メインアプリフロー)")
                    indent = "    ├── "
                }
                trimmed.startsWith("data object") && currentInterface != null -> {
                    val objectName = trimmed
                        .removePrefix("data object")
                        .substringBefore(":")
                        .trim()
                    
                    // コメントを抽出
                    val comment = extractComment(lines, lines.indexOf(line))
                    val displayName = if (comment.isNotEmpty()) {
                        "$objectName ($comment)"
                    } else {
                        objectName
                    }
                    
                    when (currentInterface) {
                        "AppNavRoute" -> {
                            result.appendLine("├── $displayName")
                        }
                        "HubNavRoute" -> {
                            // 最後の要素かチェック
                            val isLast = isLastDataObject(lines, lines.indexOf(line), currentInterface)
                            val prefix = if (isLast) "└── " else "├── "
                            result.appendLine("    $prefix$displayName")
                        }
                        else -> {
                            // AuthNavRoute等
                            val isLast = isLastDataObject(lines, lines.indexOf(line), currentInterface)
                            val prefix = if (isLast) "└── " else "├── "
                            result.appendLine("│   $prefix$displayName")
                        }
                    }
                }
            }
        }
        
        return result.toString()
    }
    
    private fun extractComment(lines: List<String>, currentIndex: Int): String {
        // 直前の行からコメントを探す
        for (i in (currentIndex - 1) downTo maxOf(0, currentIndex - 5)) {
            val line = lines[i].trim()
            if (line.startsWith("//")) {
                return line.removePrefix("//").trim()
            }
            if (line.startsWith("*") && !line.startsWith("*/")) {
                return line.removePrefix("*").trim()
            }
            // 空行以外が見つかったら終了
            if (line.isNotEmpty() && !line.startsWith("@")) {
                break
            }
        }
        return ""
    }
    
    private fun isLastDataObject(lines: List<String>, currentIndex: Int, interfaceName: String): Boolean {
        // 現在のインターフェース内で、これ以降にdata objectがあるかチェック
        var foundClosingBrace = false
        for (i in (currentIndex + 1) until lines.size) {
            val trimmed = lines[i].trim()
            if (trimmed == "}") {
                foundClosingBrace = true
                break
            }
            if (trimmed.startsWith("data object")) {
                return false
            }
        }
        return foundClosingBrace
    }
}

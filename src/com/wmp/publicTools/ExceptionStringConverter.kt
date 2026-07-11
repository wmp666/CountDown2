package com.wmp.publicTools

object ExceptionStringConverter {
    /**
     * 将嵌套异常转换为字符串
     * 
     * @param throwable         异常对象
     * @param includeStackTrace 是否包含完整的堆栈信息
     */
    @JvmStatic
    @JvmOverloads
    fun convertToString(throwable: Throwable?, includeStackTrace: Boolean = true):String {
        if (throwable == null) {
            return "null"
        }

        val sb = StringBuilder()
        convertExceptionChain(sb, throwable, 0, includeStackTrace, ArrayList())
        return sb.toString()
    }

    /**
     * 转换为简洁的异常信息（只显示关键信息）
     */
    fun convertToSimpleString(throwable: Throwable?): String {
        if (throwable == null) {
            return "null"
        }

        val sb = StringBuilder()
        var current: Throwable? = throwable
        var level = 0

        while (current != null) {
            if (level > 0) {
                sb.append("\nCaused by: ")
            }

            sb.append(current.javaClass.getSimpleName())
                .append(": ")
                .append(current.message)

            // 添加第一个堆栈位置
            if (current.stackTrace.size > 0) {
                val first = current.stackTrace[0]
                sb.append(" (at ")
                    .append(first.className)
                    .append(".")
                    .append(first.methodName)
                    .append(":")
                    .append(first.lineNumber)
                    .append(")")
            }

            current = current.cause
            level++

            // 防止无限循环
            if (level > 20) {
                sb.append("\n... (exception chain too long)")
                break
            }
        }

        return sb.toString()
    }

    private fun convertExceptionChain(
        sb: StringBuilder, throwable: Throwable?,
        depth: Int, includeStackTrace: Boolean,
        processedExceptions: MutableList<Throwable?>
    ) {
        if (throwable == null || processedExceptions.contains(throwable)) {
            return
        }

        processedExceptions.add(throwable)

        val indent = getIndent(depth)

        // 异常头信息
        if (depth == 0) {
            sb.append("Exception Chain:\n")
        } else {
            sb.append(indent).append("Caused by: ")
        }

        sb.append(throwable.javaClass.getName())
            .append(": ")
            .append(throwable.message)
            .append("\n")

        // 堆栈信息
        if (includeStackTrace) {
            val stackTrace = throwable.stackTrace
            for (element in stackTrace) {
                sb.append(indent).append("    at ")
                    .append(element.className)
                    .append(".")
                    .append(element.methodName)
                    .append("(")
                    .append(element.fileName)
                    .append(":")
                    .append(element.lineNumber)
                    .append(")\n")
            }
        } else {
            // 只显示第一个堆栈位置
            if (throwable.stackTrace.size > 0) {
                val first = throwable.stackTrace[0]
                sb.append(indent).append("    at ")
                    .append(first.className)
                    .append(".")
                    .append(first.methodName)
                    .append("(")
                    .append(first.fileName)
                    .append(":")
                    .append(first.lineNumber)
                    .append(")\n")
            }
        }

        // 处理 suppressed exceptions (try-with-resources)
        val suppressed = throwable.suppressed
        if (suppressed.size > 0) {
            sb.append(indent).append("Suppressed exceptions:\n")
            for (suppressedEx in suppressed) {
                convertExceptionChain(sb, suppressedEx, depth + 1, includeStackTrace, processedExceptions)
            }
        }

        // 处理原因异常
        val cause = throwable.cause
        if (cause != null) {
            convertExceptionChain(sb, cause, depth + 1, includeStackTrace, processedExceptions)
        }
    }

    private fun getIndent(depth: Int) = "    ".repeat(depth)

    /**
     * 获取异常链的统计信息
     */
    fun getExceptionChainSummary(throwable: Throwable?): String {
        if (throwable == null) {
            return "No exception"
        }

        val sb = StringBuilder()
        val exceptionTypes: MutableList<Class<*>?> = ArrayList()
        val exceptionMessages: MutableList<String?> = ArrayList()

        var current: Throwable? = throwable
        var chainLength = 0

        while (current != null) {
            exceptionTypes.add(current.javaClass)
            exceptionMessages.add(current.message)
            current = current.cause
            chainLength++

            if (chainLength > 50) { // 防止无限循环
                break
            }
        }

        sb.append("Exception Chain Summary:\n")
        sb.append("Total exceptions in chain: ").append(chainLength).append("\n")
        sb.append("Exception types:\n")

        for (i in exceptionTypes.indices) {
            sb.append("  ").append(i + 1).append(". ")
                .append(exceptionTypes[i]!!.getSimpleName())
                .append(": ")
                .append(exceptionMessages[i])
                .append("\n")
        }

        return sb.toString()
    }
}
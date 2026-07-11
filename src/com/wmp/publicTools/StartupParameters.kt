package com.wmp.publicTools

import java.util.*
import java.util.List

class StartupParameters(vararg parameters: String?) {
    var parameterList: ArrayList<String?> = ArrayList<String?>()

    init {
        this.parameterList.addAll(List.of<String?>(*parameters))
    }

    fun contains(parameter: String?): Boolean {
        return parameterList.contains(parameter)
    }

    fun contains(parameters: ArrayList<String?>): Boolean {
        for (parameter in parameters) {
            if (parameterList.contains(parameter)) return true
        }
        return false
    }

    override fun toString() = "StartupParameters{启动参数=$parameterList}"

    override fun equals(other: Any?): Boolean {
        if (other !is StartupParameters) return false
        if (this.parameterList == other.parameterList) return true
        return this.parameterList.contains(other.toString())
    }

    override fun hashCode(): Int {
        return Objects.hashCode(this.parameterList)
    }

    companion object {
        fun creative(vararg parameters: String?): StartupParameters {
            return StartupParameters(*parameters)
        }
    }
}

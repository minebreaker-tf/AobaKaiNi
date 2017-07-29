package rip.deadcode.aoba3.util

object Strings2 {

    fun dropFirst(target: String, prefix: String): String {
        return if (target.startsWith(prefix))
            dropFirst(target.substring(1, target.length), prefix)
        else
            target
    }

}
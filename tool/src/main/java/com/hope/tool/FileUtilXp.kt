package com.hope.tool

import android.text.TextUtils
import android.util.Log
import java.io.File

object FileUtilXp {

    /** 删除文件，可以是文件或文件夹
     * @param delFile 要删除的文件夹或文件名
     * @return 删除成功返回true，否则返回false
     */
    fun delete(delFile: String): Boolean {
        val file = File(delFile)
        return if (!file.exists()) {
            Log.i("FileUtils", "删除文件失败" + delFile + "不存在！")
            false
        } else {
            if (file.isFile) deleteSingleFile(delFile) else deleteDirectory(delFile)
        }
    }

    /** 删除单个文件
     * @param filePathName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private fun deleteSingleFile(filePathName: String): Boolean {
        val file = File(filePathName)
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        return if (file.exists() && file.isFile) {
            if (file.delete()) {
                Log.i("FileUtils", "删除单个文件" + filePathName + "成功！")
                true
            } else {
                Log.i("FileUtils", "删除单个文件" + filePathName + "失败！")
                false
            }
        } else {
            Log.i("FileUtils", "删除单个文件" + filePathName + "不存在！")
            false
        }
    }

    /** 删除目录及目录下的文件
     * @param filePath 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    private fun deleteDirectory(filePath: String): Boolean {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        var filePath = filePath
        if (!filePath.endsWith(File.separator)) filePath += File.separator
        val dirFile = File(filePath)
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory) {
            Log.i("FileUtils", "删除目录失败" + filePath + "不存在！")
            return false
        }
        var flag = true
        // 删除文件夹中的所有文件包括子目录
        val files: Array<File> = dirFile.listFiles()!!
        for (file in files) {
            // 删除子文件
            if (file.isFile) {
                flag = deleteSingleFile(file.absolutePath)
                if (!flag) break
            } else if (file.isDirectory) {
                flag = deleteDirectory(
                    file
                        .absolutePath
                )
                if (!flag) break
            }
        }
        if (!flag) {
            Log.i("FileUtils", "删除目录失败$filePath")
            return false
        }
        // 删除当前目录
        return if (dirFile.delete()) {
            Log.i("FileUtils", "删除目录失败" + filePath + "成功！")
            true
        } else {
            Log.i("FileUtils", "删除目录失败$filePath")
            false
        }
    }

    /**
     * 获取文件大小例如 0.5 MB
     */
    fun getFileSize(filePath: String): String {
        var size = 0f
        if (!TextUtils.isEmpty(filePath)) {
            val file = File(filePath)
            size = file.length().toFloat() / 1024f / 1024f
        }
        return String.format("%.1f", size)
    }

}
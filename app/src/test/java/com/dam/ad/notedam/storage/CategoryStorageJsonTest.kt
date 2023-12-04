package storage

import CategoryStorageGenericTest
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.services.storage.categories.CategoryStorageJson
import com.dam.ad.notedam.utils.UtilsTest
import java.io.File

class CategoryStorageJsonTest: CategoryStorageGenericTest() {
    override fun filePath() = "${System.getProperty("user.dir")}${File.separator}"

    override fun getStorage() = CategoryStorageJson()
    override fun getCategoryDefault(): Iterable<Category> = UtilsTest.categories
    override fun getFileNameTail(): String = "_categories.json"
}
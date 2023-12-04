package storage

import CategoryStorageGenericTest
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.services.storage.categories.CategoryStorageCsv
import com.dam.ad.notedam.utils.UtilsTest
import java.io.File

class CategoryStorageCsvTest: CategoryStorageGenericTest() {
    override fun filePath() = "${System.getProperty("user.dir")}${File.separator}"

    override fun getStorage() = CategoryStorageCsv()
    override fun getCategoryDefault(): Iterable<Category> = UtilsTest.categories
    override fun getFileNameTail(): String  = "_categories.csv"
}
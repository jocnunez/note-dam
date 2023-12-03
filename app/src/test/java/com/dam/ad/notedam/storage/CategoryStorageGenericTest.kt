import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.services.storage.categories.CategoryStorageService
import com.dam.ad.notedam.utils.Utils
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import junit.framework.TestCase.*
import org.junit.After
import org.junit.Test
import java.io.File

abstract class CategoryStorageGenericTest {

    abstract fun filePath(): String
    abstract fun getStorage(): CategoryStorageService
    abstract fun getCategoryDefault(): Iterable<Category>
    abstract fun getFileNameTail(): String

    @After
    fun tearDown() {
        // Borrar ficheros
        Utils.clearFilesFn(listOf(), filePath(), getFileNameTail())
    }

    @Test
    fun exportTest() {
        val category = getCategoryDefault().first()
        getStorage().export(category, filePath(), true)

        // Comprobar que el fichero existe
        val file = File(filePath() + category.uuid + getFileNameTail())
        assertTrue(file.exists() && file.canWrite())
    }

    @Test
    fun exportAllTest() {
        getStorage().exportAll(getCategoryDefault(), filePath(), true)

        // Comprobar que el fichero existe
        val file = File(filePath() + getCategoryDefault().first().uuid + getFileNameTail())
        val file2 = File(filePath() + getCategoryDefault().last().uuid + getFileNameTail())
        assertTrue(file.exists() && file.canWrite())
        assertTrue(file2.exists() && file2.canWrite())
    }

    @Test
    fun loadAllTest() {
        val file = File(filePath() + getCategoryDefault().first().uuid + getFileNameTail())
        if (!file.exists()) getStorage().exportAll(getCategoryDefault(), filePath(), true)

        val storageService = getStorage()
        val categoriesLoad = storageService.loadAll(filePath())

        assertTrue(file.exists() && file.canRead())
        assertNull(categoriesLoad.getError())
        assertNotNull(categoriesLoad.get())
        assertEquals(getCategoryDefault().toList().size, categoriesLoad.get()!!.toList().size)
    }
}

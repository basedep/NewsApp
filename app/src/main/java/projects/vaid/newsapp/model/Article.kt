package projects.vaid.newsapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.io.Serializable

@Entity(
    tableName = "articles"
)
@Parcelize
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: @RawValue Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
): Parcelable{

    override fun hashCode(): Int {
        var result = id.hashCode()
        if(url?.isEmpty()!!){
            result = 31 * result + url.hashCode()
        }
        return result
    }
}
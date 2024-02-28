import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.employeerecordapp.Utility.PrefHelper
import com.example.employeerecordapp.viewmodel.AppViewModel

class AppViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            val prefHelper = PrefHelper(context)
            return AppViewModel(prefHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

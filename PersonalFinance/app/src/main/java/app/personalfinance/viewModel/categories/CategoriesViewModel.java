package app.personalfinance.viewModel.categories;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.function.Consumer;

import app.personalfinance.data.categories.CategoryModel;
import app.personalfinance.repo.categories.CategoryRepository;

public class CategoriesViewModel extends AndroidViewModel {

    // Repository
    private CategoryRepository repository;

    public CategoriesViewModel(@NonNull Application application) {
        super(application);
        //set the repository
        repository = new CategoryRepository(application);
    }

    // save category
    public void saveCategory(String name, String type, Consumer<Boolean> callback) {
        repository.insert(new CategoryModel(name, type), callback);
    }

    // update category
    public void deleteCategory(CategoryModel category, Consumer<Boolean> callback) {
        repository.delete(category, callback);
    }

    // get category
    public LiveData<List<CategoryModel>> getAllCategories() {
        return repository.getAllCategories();
    }

    // get category by type
    public LiveData<List<CategoryModel>> getCategoriesByType(String type) {
        return repository.getCategoriesByType(type);
    }
}
package app.personalfinance.viewModel.categories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import app.personalfinance.data.categories.CategoryModel;
import app.personalfinance.repo.categories.CategoryRepository;

public class CategoriesViewModel extends AndroidViewModel {

    private CategoryRepository repository;

    public CategoriesViewModel(@NonNull Application application) {
        super(application);
        repository = new CategoryRepository(application);
    }

    public void saveCategory(String name, String type) {
        repository.insert(new CategoryModel(name, type));
    }

    public void deleteCategory(CategoryModel category) {
        repository.delete(category);
    }

    public LiveData<List<CategoryModel>> getAllCategories() {
        return repository.getAllCategories();
    }

    public LiveData<List<CategoryModel>> getCategoriesByType(String type) {
        return repository.getCategoriesByType(type);
    }
}
package de.sirvierl0ffel.viswiz.viewmodels;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.data.InputSaveManager;

public class MainViewModel extends ViewModel {

    public final InputSaveManager managerInputs = new InputSaveManager();

    public final MutableLiveData<Class<? extends Fragment>> _currentFragment = new MutableLiveData<>();
    public final MutableLiveData<Algorithm> selectedAlgorithm = new MutableLiveData<>();

    public void open(Class<? extends Fragment> clazz) {
        _currentFragment.setValue(clazz);
    }

}

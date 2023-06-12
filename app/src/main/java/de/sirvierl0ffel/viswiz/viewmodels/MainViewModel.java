package de.sirvierl0ffel.viswiz.viewmodels;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.net.HttpCookie;

import de.sirvierl0ffel.viswiz.data.AlgorithmManager;
import de.sirvierl0ffel.viswiz.data.InputSaveManager;
import de.sirvierl0ffel.viswiz.models.Algorithm;

public class MainViewModel extends ViewModel {

    public final InputSaveManager managerInputs = new InputSaveManager();
    public final AlgorithmManager managerAlgorithms = new AlgorithmManager();

    public final MutableLiveData<Class<? extends Fragment>> _currentFragment = new MutableLiveData<>();
    public final MutableLiveData<Algorithm> selectedAlgorithm = new MutableLiveData<>();
    public final MutableLiveData<Boolean> algorithmUpdate = new MutableLiveData<>();

    public void open(Class<? extends Fragment> clazz) {
        _currentFragment.setValue(clazz);
    }

}

package de.sirvierl0ffel.viswiz.viewmodels;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.sirvierl0ffel.viswiz.models.InputSave;
import de.sirvierl0ffel.viswiz.views.running.InputListFragment;
import de.sirvierl0ffel.viswiz.views.running.DescriptionFragment;
import de.sirvierl0ffel.viswiz.views.running.ResultFragment;

public class AlgorithmViewModel extends ViewModel {

    public static final Class<?>[] TAB_FRAGMENT_TYPES = new Class[]{
            DescriptionFragment.class,
            InputListFragment.class,
            ResultFragment.class
    };

    private final MutableLiveData<Integer> _tab = new MutableLiveData<>(0);
    public final LiveData<Integer> tab = _tab;
    public final MutableLiveData<InputSave> selectedInput = new MutableLiveData<>();

    public void selectTab(Class<? extends Fragment> fragment) {
        for (int i = 0; i < TAB_FRAGMENT_TYPES.length; i++) {
            if (fragment.equals(TAB_FRAGMENT_TYPES[i])) {
                _tab.setValue(i);
                return;
            }
        }
        throw new IllegalArgumentException(""+fragment);
    }

}

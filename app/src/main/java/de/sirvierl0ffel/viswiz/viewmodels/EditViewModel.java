package de.sirvierl0ffel.viswiz.viewmodels;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditViewModel extends ViewModel {

    public static final Class<?>[] TAB_FRAGMENT_TYPES = new Class[]{
            // TODO
    };

    private final MutableLiveData<Integer> _tab = new MutableLiveData<>(0);
    public final LiveData<Integer> tab = _tab;

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

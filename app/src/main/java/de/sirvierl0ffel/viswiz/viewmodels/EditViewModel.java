package de.sirvierl0ffel.viswiz.viewmodels;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.sirvierl0ffel.viswiz.views.editing.CodeFragment;
import de.sirvierl0ffel.viswiz.views.editing.InformationFragment;

public class EditViewModel extends ViewModel {

    public static final Class<?>[] TAB_FRAGMENT_TYPES = new Class[]{
            InformationFragment.class,
            CodeFragment.class
    };

    private final MutableLiveData<Integer> _tab = new MutableLiveData<>(0);
    public final LiveData<Integer> tab = _tab;

    public String name = "";
    public String thumbnailURL = "";
    public String description = "";
    public String pseudoCode = "";
    public String code = "";
    public String input = "";

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

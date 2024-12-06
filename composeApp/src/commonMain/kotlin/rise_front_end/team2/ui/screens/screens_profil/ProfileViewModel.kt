package rise_front_end.team2.ui.screens.screens_profil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import rise_front_end.team2.Repo.ProfileRepository
import rise_front_end.team2.data.data_profile.ProfileObject


class ProfileViewModel(profileRepository: ProfileRepository) : ViewModel() {
    val objects: StateFlow<List<ProfileObject>> =
        profileRepository.getObjects()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}